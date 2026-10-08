package com.github.jershell.shadcn.components.table

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.isActive
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * What starts a row drag when [TableConfig.reorderRows] is on.
 */
enum class TableRowDragHandle {
    /**
     * A grip column (⋮⋮) is added in front of the columns; the drag starts from it
     * right away with any pointer (mouse, touch, stylus). Cell content stays fully
     * interactive and touch scrolling over the rows is untouched.
     */
    Grip,

    /**
     * The whole row is draggable: with a mouse/stylus right away, on touch after a
     * long press (a plain swipe keeps scrolling the list). No extra column.
     */
    Row,

    /** Both: the grip column and the whole row. */
    GripAndRow,
}

/** Live state of a row drag; all coordinates are in the body viewport space. */
@Stable
internal class TableRowDragState {
    /** Index of the dragged row in `rows`; `-1` when idle. */
    var fromIndex by mutableIntStateOf(-1)

    /** Index the dragged row lands at when dropped right now. */
    var targetIndex by mutableIntStateOf(-1)

    var draggedHeightPx by mutableFloatStateOf(0f)
    var pointerY by mutableFloatStateOf(0f)

    /** Distance from the top of the grabbed row to the pointer at grab time. */
    var grabOffsetPx by mutableFloatStateOf(0f)

    val isDragging: Boolean get() = fromIndex >= 0

    /** Top of the floating row, kept inside the body viewport. */
    fun ghostTop(viewportHeight: Int): Float =
        (pointerY - grabOffsetPx).coerceIn(0f, max(0f, viewportHeight - draggedHeightPx))

    fun start(from: Int, heightPx: Float, grabOffset: Float, pointer: Float) {
        draggedHeightPx = heightPx
        grabOffsetPx = grabOffset
        pointerY = pointer
        targetIndex = from
        fromIndex = from
    }

    fun reset() {
        fromIndex = -1
        targetIndex = -1
    }
}

/** Hovered body cell; [column] is `-1` over the grip column. */
internal data class TableHoveredCell(val row: Int, val column: Int)

/** Lazy items placed before the rows by the provider (its viewport probe item). */
internal fun LazyListState.leadingItems(rowCount: Int): Int =
    (layoutInfo.totalItemsCount - rowCount).coerceAtLeast(0)

internal fun LazyListState.visibleRowItems(rowCount: Int): List<LazyListItemInfo> {
    val lead = leadingItems(rowCount)
    return layoutInfo.visibleItemsInfo.filter { it.index >= lead && it.size > 0 }
}

/** Row item under [y] (body viewport space). */
internal fun LazyListState.rowItemAt(rowCount: Int, y: Float): LazyListItemInfo? =
    visibleRowItems(rowCount).firstOrNull { y >= it.offset && y < it.offset + it.size }

/** Slot (grip + columns) under [contentX] (content space, i.e. with the horizontal scroll). */
internal fun slotAt(contentX: Float, slotWidthsPx: List<Float>): Int {
    if (contentX < 0f) return -1
    var right = 0f
    slotWidthsPx.forEachIndexed { index, width ->
        right += width
        if (contentX < right) return index
    }
    return -1
}

/**
 * Where the dragged row lands: rows whose middle the center of the floating row has
 * crossed shift towards the original slot. Rows outside the viewport (the list was
 * auto-scrolled) count as crossed.
 */
internal fun computeDropTarget(lazyState: LazyListState, rowCount: Int, drag: TableRowDragState): Int {
    val from = drag.fromIndex
    if (from < 0 || rowCount <= 0) return drag.targetIndex
    val lead = lazyState.leadingItems(rowCount)
    val visible = lazyState.visibleRowItems(rowCount)
    if (visible.isEmpty()) return drag.targetIndex
    val firstRow = visible.first().index - lead
    val lastRow = visible.last().index - lead
    // Unclamped center: the floating row is kept inside the body, but the pointer may
    // go past it (over the header/footer) — that must still reach the first/last row,
    // whose middle the clamped center could only touch, never cross.
    val center = drag.pointerY - drag.grabOffsetPx + drag.draggedHeightPx / 2f
    var target = when {
        from < firstRow -> firstRow - 1
        from > lastRow -> lastRow + 1
        else -> from
    }
    for (item in visible) {
        val row = item.index - lead
        if (row == from) continue
        val middle = item.offset + item.size / 2f
        if (row > from && center > middle) {
            target = max(target, row)
        } else if (row < from && center < middle) {
            target = min(target, row)
        }
    }
    return target.coerceIn(0, rowCount - 1)
}

/**
 * Mouse/stylus hover tracking of the body: reports the pointer position in the body
 * space, `null` when it leaves (touch never hovers).
 */
internal fun Modifier.tableHoverTracker(onPosition: (Offset?) -> Unit): Modifier =
    pointerInput(Unit) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent(PointerEventPass.Main)
                val change = event.changes.firstOrNull() ?: continue
                when {
                    change.type == PointerType.Touch -> onPosition(null)
                    event.type == PointerEventType.Exit -> onPosition(null)
                    else -> onPosition(change.position)
                }
            }
        }
    }

/**
 * Row drag gesture of the whole body. It runs on the [PointerEventPass.Initial] pass,
 * so once a drag starts the events are consumed before the lazy list (scroll) and the
 * cells (clicks) see them. Living on the body (not on a cell) keeps the gesture alive
 * when the grabbed row is auto-scrolled out and disposed by the lazy list.
 */
internal fun Modifier.tableRowDrag(
    enabled: Boolean,
    handle: TableRowDragHandle,
    gripWidthPx: Float,
    horizontalScrollPx: () -> Int,
    lazyState: LazyListState,
    rowCount: () -> Int,
    drag: TableRowDragState,
    onDrop: (fromIndex: Int, toIndex: Int) -> Unit,
): Modifier {
    if (!enabled) return this
    return pointerInput(handle, gripWidthPx, lazyState) {
        awaitEachGesture {
            val down = awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
            val count = rowCount()
            val grabbed = lazyState.rowItemAt(count, down.position.y) ?: return@awaitEachGesture
            val onGrip = handle != TableRowDragHandle.Row &&
                gripWidthPx > 0f &&
                down.position.x + horizontalScrollPx() < gripWidthPx
            val rowDraggable = handle != TableRowDragHandle.Grip
            val startPosition: Offset = when {
                onGrip -> awaitVerticalSlop(down, ownsGesture = true)
                rowDraggable && down.type != PointerType.Touch -> awaitVerticalSlop(down, ownsGesture = false)
                rowDraggable -> awaitLongPress(down)
                else -> null
            } ?: return@awaitEachGesture

            // Re-read the row: the list may have moved while waiting for slop/long press.
            val item = lazyState.layoutInfo.visibleItemsInfo
                .firstOrNull { it.index == grabbed.index } ?: return@awaitEachGesture
            val from = item.index - lazyState.leadingItems(count)
            if (from !in 0 until count) return@awaitEachGesture
            drag.start(
                from = from,
                heightPx = item.size.toFloat(),
                grabOffset = down.position.y - item.offset,
                pointer = startPosition.y,
            )

            var committed = false
            try {
                while (true) {
                    val event = awaitPointerEvent(PointerEventPass.Initial)
                    val change = event.changes.firstOrNull { it.id == down.id } ?: break
                    if (change.changedToUpIgnoreConsumed() || !change.pressed) {
                        change.consume()
                        committed = true
                        break
                    }
                    if (change.position.y != drag.pointerY) {
                        drag.pointerY = change.position.y
                        drag.targetIndex = computeDropTarget(lazyState, rowCount(), drag)
                    }
                    change.consume()
                }
            } finally {
                val dropFrom = drag.fromIndex
                val dropTo = drag.targetIndex
                drag.reset()
                if (committed && dropFrom >= 0 && dropTo >= 0 && dropFrom != dropTo) {
                    onDrop(dropFrom, dropTo)
                }
            }
        }
    }
}

/**
 * Waits for a vertical drag past the touch slop; returns the pointer position then,
 * or `null` when the pointer is released, the move goes horizontal first, or (when
 * the gesture is not owned) someone else consumed it.
 */
private suspend fun AwaitPointerEventScope.awaitVerticalSlop(
    down: PointerInputChange,
    ownsGesture: Boolean,
): Offset? {
    val slop = viewConfiguration.touchSlop
    while (true) {
        val event = awaitPointerEvent(PointerEventPass.Initial)
        val change = event.changes.firstOrNull { it.id == down.id } ?: return null
        if (!change.pressed) return null
        if (!ownsGesture && change.isConsumed) return null
        val delta = change.position - down.position
        if (ownsGesture) change.consume()
        if (abs(delta.y) > slop) {
            change.consume()
            return change.position
        }
        if (!ownsGesture && abs(delta.x) > slop) return null
    }
}

/**
 * Waits for a long press without moving past the touch slop; returns the pointer
 * position, or `null` when released/moved/consumed earlier (the list scrolls then).
 */
private suspend fun AwaitPointerEventScope.awaitLongPress(down: PointerInputChange): Offset? {
    val slop = viewConfiguration.touchSlop
    var last = down.position
    val interrupted = withTimeoutOrNull(viewConfiguration.longPressTimeoutMillis) {
        while (true) {
            val event = awaitPointerEvent(PointerEventPass.Initial)
            val change = event.changes.firstOrNull { it.id == down.id } ?: return@withTimeoutOrNull true
            if (!change.pressed || change.isConsumed) return@withTimeoutOrNull true
            if ((change.position - down.position).getDistance() > slop) return@withTimeoutOrNull true
            last = change.position
        }
        @Suppress("UNREACHABLE_CODE")
        true
    }
    return if (interrupted == null) last else null
}

/**
 * Auto-scroll while dragging near the top/bottom edge of the body; the speed grows
 * towards the edge (and past it), frame-rate independent.
 */
@Composable
internal fun TableDragAutoScroll(
    drag: TableRowDragState,
    lazyState: LazyListState,
    edge: Dp,
    maxSpeedPerFrame: Dp,
    rowCount: () -> Int,
) {
    val dragging = drag.isDragging
    val density = LocalDensity.current
    val edgePx = with(density) { edge.toPx() }
    val maxStepPx = with(density) { maxSpeedPerFrame.toPx() }
    LaunchedEffect(dragging, lazyState, edgePx, maxStepPx) {
        if (!dragging) return@LaunchedEffect
        var lastFrame = withFrameNanos { it }
        while (isActive && drag.isDragging) {
            val now = withFrameNanos { it }
            val frames = ((now - lastFrame) / FrameNanos).coerceIn(0f, MaxFramesPerStep)
            lastFrame = now
            val height = lazyState.layoutInfo.viewportSize.height.toFloat()
            if (height <= 0f) continue
            val zone = min(edgePx, height / 3f)
            val y = drag.pointerY
            val speed = when {
                y < zone -> -((zone - y) / zone).coerceAtMost(1f)
                y > height - zone -> ((y - (height - zone)) / zone).coerceAtMost(1f)
                else -> 0f
            }
            if (speed != 0f) {
                val consumed = lazyState.scrollBy(speed * maxStepPx * frames)
                if (consumed != 0f) {
                    drag.targetIndex = computeDropTarget(lazyState, rowCount(), drag)
                }
            }
        }
    }
}

private const val FrameNanos = 16_666_667f
private const val MaxFramesPerStep = 4f

/** Holder for the hovered position, read by derived hover state. */
@Stable
internal class TableHoverPointer {
    var position by mutableStateOf<Offset?>(null)
}
