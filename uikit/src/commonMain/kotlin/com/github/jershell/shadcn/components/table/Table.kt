package com.github.jershell.shadcn.components.table

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.composeunstyled.ThumbVisibility
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.scroll.HorizontalScrollbar
import com.github.jershell.shadcn.components.scroll.VerticalScrollbar
import com.github.jershell.shadcn.components.scroll.rememberCoalescedScrollbarState
import com.github.jershell.shadcn.theme.BaseTokens
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.TwDimensions
import com.ryinex.kotlin.datatable.data.DataTable
import com.ryinex.kotlin.datatable.data.DataTableColumnConfig
import com.ryinex.kotlin.datatable.data.DataTableColumnLayout
import com.ryinex.kotlin.datatable.data.DataTableConfig
import com.ryinex.kotlin.datatable.data.composable
import com.ryinex.kotlin.datatable.data.setList
import com.ryinex.kotlin.datatable.views.EmbeddedDataTableView
import kotlin.math.roundToInt

/**
 * Visual and behavioral settings of [Table]. Create the default once and override
 * selectively with `copy`, so call sites stay short:
 *
 * ```
 * val denseTable = TableConfig(rowHeight = 36.dp, headerHeight = 32.dp)
 * Table(rows, columns, config = denseTable.copy(rowHoverBackground = accent))
 * ```
 *
 * Highlights stack, each drawn over the previous one: the per-row `rowBackground`
 * lambda of [Table], then [rowHoverBackground], then [cellHoverBackground] — so
 * translucent colors (e.g. `muted` at 50%) work as in shadcn `hover:bg-muted/50`.
 *
 * @param rowHeight Minimal body row height. Cells with taller content grow; content
 *   is never cut off vertically.
 * @param headerHeight Minimal header (and footer) row height.
 * @param caption Text rendered under the table; `null`/blank renders nothing.
 * @param cellPadding Content padding inside header, body and footer cells.
 * @param rowHoverBackground Highlight of the row under the mouse; `null` (default)
 *   renders no row highlight. Touch never hovers.
 * @param cellHoverBackground Highlight of the single cell under the mouse; `null`
 *   keeps the row-level highlight only.
 * @param reorderRows Enables row reordering by drag & drop. Rows are NOT reordered
 *   inside the table — [Table]'s `onRowMoved` reports `(fromIndex, toIndex)` and the
 *   caller reorders `rows` (single source of truth, e.g. send the event to the
 *   backend and update the list). While dragging, the row floats under the pointer,
 *   the other rows make room for it, and the body auto-scrolls near its edges.
 * @param rowDragHandle What starts the drag, see [TableRowDragHandle].
 * @param dragAutoScrollEdge Height of the zones at the top/bottom of the body that
 *   auto-scroll while dragging.
 * @param verticalScrollState External vertical (rows) scroll state; `null` remembers its own.
 * @param horizontalScrollState External horizontal scroll state; `null` remembers its own.
 * @param stretchToContainer When the resolved columns are narrower than the container,
 *   the leftover width is scaled onto all columns (proportions preserved); `true` by
 *   default so the table always fills its container. Turn off to keep column widths
 *   exact (right side stays empty).
 */
data class TableConfig(
    val rowHeight: Dp = 44.dp,
    val headerHeight: Dp = 40.dp,
    val caption: String? = null,
    val cellPadding: PaddingValues = PaddingValues(horizontal = TwDimensions.paddingPxToken4),
    val rowHoverBackground: Color? = null,
    val cellHoverBackground: Color? = null,
    val reorderRows: Boolean = false,
    val rowDragHandle: TableRowDragHandle = TableRowDragHandle.Grip,
    val dragAutoScrollEdge: Dp = BaseTokens.token48,
    val verticalScrollState: LazyListState? = null,
    val horizontalScrollState: ScrollState? = null,
    val stretchToContainer: Boolean = true,
)

/**
 * Column definition of [Table].
 *
 * @param key Stable unique id of the column.
 * @param header Header label (also measured for the minimal width).
 * @param width Minimal column width; also the fallback when the container width is
 *   unbounded. Measured text (the header, the footer and text cells when no custom
 *   content slots are used) grows the column beyond [width], so text is never truncated.
 * @param weight When set, the column shares the leftover width of the table container
 *   with the other weighted columns proportionally (like a `flex-grow`). Fixed-width
 *   columns keep [width]; a weighted column never shrinks below [width].
 * @param alignment Content alignment inside header, body and footer cells.
 * @param footer Footer text of the column (e.g. a total); the footer row is shown when
 *   any column has one or [Table]'s `footerContent` is set.
 * @param cell Text of a body cell; ignored when [Table]'s `cellContent` is set.
 */
data class DataTableColumn<T>(
    val key: String,
    val header: String,
    val width: Dp = 180.dp,
    val weight: Float? = null,
    val alignment: Alignment = Alignment.CenterStart,
    val footer: String? = null,
    val cell: ((T) -> String)? = null,
)

/**
 * Data table for Compose Multiplatform backed by `com.ryinex.kotlin:compose-data-table`
 * (exposed as a public transitive dependency, so its own types stay usable), styled
 * after the shadcn/ui table.
 *
 * Geometry: the rounded frame holds the pinned header, the body and the optional
 * footer; the vertical scrollbar lives OUTSIDE the frame on the right and appears only
 * on vertical overflow; the horizontal scrollbar appears under the frame only on
 * horizontal overflow. The frame hugs its content: when all rows fit, the frame is
 * exactly as tall as the table; otherwise it takes the available height and the body
 * scrolls. In a vertically unbounded parent (e.g. a `verticalScroll` page) the body
 * is as tall as all of its rows.
 *
 * Chrome (backgrounds, separators, hover highlight, drag visuals) is drawn by this
 * component on top of the transparent provider surface. Hover and drag are tracked on
 * the body as a whole (one pointer handler, hit-tested against the lazy layout), so
 * they don't depend on the cell content and survive lazy item reuse.
 *
 * Example:
 * ```
 * val columns = listOf(
 *     DataTableColumn<Payment>("invoice", header = "Invoice", width = 120.dp) { it.invoice },
 *     DataTableColumn<Payment>("amount", header = "Amount", footer = "$2,500.00") { it.amount },
 * )
 * Table(
 *     rows = payments,
 *     columns = columns,
 *     config = TableConfig(caption = "Recent payments"),
 * )
 * ```
 * With row reordering:
 * ```
 * Table(
 *     rows = payments,
 *     columns = columns,
 *     rowKey = { _, payment -> payment.id },
 *     config = TableConfig(reorderRows = true, rowDragHandle = TableRowDragHandle.GripAndRow),
 *     onRowMoved = { from, to -> viewModel.movePayment(from, to) },
 * )
 * ```
 *
 * @param rows Row data; the list order is the display order.
 * @param columns Column definitions.
 * @param modifier Modifier of the whole table block.
 * @param rowKey Optional stable key of a row for lazy item reuse; falls back to the
 *   index. Recommended with reordering.
 * @param config Visual/behavioral settings, see [TableConfig].
 * @param onRowMoved Called when a row is dropped at a new position: `(fromIndex,
 *   toIndex)` — indexes into [rows], the caller applies `add(to, removeAt(from))`.
 *   Only called when `config.reorderRows` is enabled.
 * @param onRowHover Called when the mouse enters/leaves a row; moving between cells of
 *   the same row does not re-fire.
 * @param onCellHover Called when the mouse enters/leaves a single cell.
 * @param headerContent Custom header cell content, falls back to [DataTableColumn.header].
 * @param cellContent Custom cell content, falls back to [DataTableColumn.cell].
 * @param footerContent Custom footer cell content, falls back to [DataTableColumn.footer].
 * @param emptyContent Body content when [rows] is empty (shadcn "No results.").
 * @param rowBackground Per-row background (zebra stripes, selection etc.); hover
 *   highlights are drawn over it.
 */
@Composable
fun <T : Any> Table(
    rows: List<T>,
    columns: List<DataTableColumn<T>>,
    modifier: Modifier = Modifier,
    rowKey: ((index: Int, row: T) -> Any)? = null,
    config: TableConfig = TableConfig(),
    onRowMoved: ((fromIndex: Int, toIndex: Int) -> Unit)? = null,
    onRowHover: ((rowIndex: Int, row: T, hovered: Boolean) -> Unit)? = null,
    onCellHover: ((rowIndex: Int, column: DataTableColumn<T>, row: T, hovered: Boolean) -> Unit)? = null,
    headerContent: (@Composable (columnIndex: Int, column: DataTableColumn<T>) -> Unit)? = null,
    cellContent: (@Composable (rowIndex: Int, column: DataTableColumn<T>, row: T) -> Unit)? = null,
    footerContent: (@Composable (columnIndex: Int, column: DataTableColumn<T>) -> Unit)? = null,
    emptyContent: (@Composable () -> Unit)? = null,
    rowBackground: (@Composable (row: T, rowIndex: Int) -> Color)? = null,
) {
    if (columns.isEmpty()) return

    val scope = rememberCoroutineScope()
    val lazyState = config.verticalScrollState ?: rememberLazyListState()
    val horizontalScrollState = config.horizontalScrollState ?: rememberScrollState()
    val colors = resolveDataTableColors()
    val density = LocalDensity.current

    val showGrip = config.reorderRows && config.rowDragHandle != TableRowDragHandle.Row
    val gripSlots = if (showGrip) 1 else 0
    val showFooter = footerContent != null || columns.any { it.footer != null }

    // Everything the provider-composed cells read; updated on every composition, like
    // rememberUpdatedState, so the provider table itself is created only once per
    // column set.
    val ctx = remember { TableContext<T>() }
    ctx.rows = rows
    ctx.columns = columns
    ctx.config = config
    ctx.colors = colors
    ctx.showGrip = showGrip
    ctx.cellContent = cellContent
    ctx.rowBackground = rowBackground

    val currentOnRowMoved by rememberUpdatedState(onRowMoved)
    val currentOnRowHover by rememberUpdatedState(onRowHover)
    val currentOnCellHover by rememberUpdatedState(onCellHover)

    // Provider surface driven transparent: chrome and widths are all ours.
    val providerConfig = remember {
        DataTableConfig.default(
            color = Color.Transparent,
            backgroundColor = Color.Transparent,
            verticalSpacing = 0,
            horizontalSpacing = 0,
            isIndexed = false,
            isHeadered = false,
            isHeaderSticky = false,
            column = { column -> column.copy(isResizable = false) },
        )
    }
    val slotKeys = (if (showGrip) listOf(GripSlotKey) else emptyList()) + columns.map { it.key }
    val table = remember(slotKeys, lazyState) {
        DataTable<T>(config = providerConfig, scope = scope, lazyState = lazyState).apply {
            slotKeys.forEachIndexed { slot, key ->
                composable(
                    name = key,
                    config = { cfg: DataTableColumnConfig ->
                        cfg.copy(
                            layout = DataTableColumnLayout.ScrollableKeepLargest,
                            weight = 0f,
                            isResizable = false,
                        )
                    },
                    presentation = { _, presentation ->
                        presentation.copy(
                            backgroundColor = Color.Transparent,
                            padding = PaddingValues(0.dp),
                        )
                    },
                    content = { location, data ->
                        BodyCell(ctx = ctx, slot = slot, rowIndex = location.dataRelativeRowIndex, row = data)
                    },
                )
            }
        }
    }
    SideEffect {
        table.setList(rows, rowKey)
    }

    // Hover: one tracker on the body, hit-tested against the lazy layout.
    val hoverPointer = remember { TableHoverPointer() }
    val hovered by remember(lazyState, horizontalScrollState) {
        derivedStateOf {
            val position = hoverPointer.position ?: return@derivedStateOf null
            if (ctx.drag.isDragging) return@derivedStateOf null
            val count = ctx.rows.size
            val item = lazyState.rowItemAt(count, position.y) ?: return@derivedStateOf null
            val slot = slotAt(position.x + horizontalScrollState.value, ctx.slotWidthsPx)
            if (slot < 0) return@derivedStateOf null
            TableHoveredCell(row = item.index - lazyState.leadingItems(count), column = slot - ctx.gripSlots)
        }
    }
    ctx.hoveredState = { hovered }
    LaunchedEffect(Unit) {
        var previous: TableHoveredCell? = null
        snapshotFlow { hovered }.collect { next ->
            val currentRows = ctx.rows
            val currentColumns = ctx.columns
            val prev = previous
            if (prev != null && prev != next) {
                currentRows.getOrNull(prev.row)?.let { row ->
                    currentColumns.getOrNull(prev.column)?.let { currentOnCellHover?.invoke(prev.row, it, row, false) }
                    if (next?.row != prev.row) currentOnRowHover?.invoke(prev.row, row, false)
                }
            }
            if (next != null && next != prev) {
                currentRows.getOrNull(next.row)?.let { row ->
                    if (prev?.row != next.row) currentOnRowHover?.invoke(next.row, row, true)
                    currentColumns.getOrNull(next.column)?.let { currentOnCellHover?.invoke(next.row, it, row, true) }
                }
            }
            previous = next
        }
    }

    TableDragAutoScroll(
        drag = ctx.drag,
        lazyState = lazyState,
        edge = config.dragAutoScrollEdge,
        maxSpeedPerFrame = BaseTokens.token16,
        rowCount = { ctx.rows.size },
    )

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val boundedHeight = constraints.hasBoundedHeight
        val hasVerticalOverflow by remember(lazyState) {
            derivedStateOf { lazyState.canScrollForward || lazyState.canScrollBackward }
        }
        val scrollbarSpace = if (hasVerticalOverflow) TableScrollbarSpace else 0.dp
        val contentWidth = (maxWidth - scrollbarSpace).coerceAtLeast(0.dp)
        val gripWidth = if (showGrip) TableGripWidth else 0.dp

        val measuredText = rememberMeasuredTextWidths(
            columns = columns,
            rows = rows,
            colors = colors,
            measureCells = headerContent == null && cellContent == null,
            measureFooter = footerContent == null,
        )
        val columnWidths = resolveColumnWidths(
            columns = columns,
            config = config,
            containerWidth = (contentWidth - gripWidth).coerceAtLeast(0.dp),
            measuredTextPx = measuredText,
        )
        val slotWidths = (if (showGrip) listOf(gripWidth) else emptyList()) + columnWidths
        ctx.slotWidths = slotWidths
        ctx.slotWidthsPx = with(density) { slotWidths.map { it.toPx() } }
        ctx.gripSlots = gripSlots

        val shape = RoundedCornerShape(Theme[DimProps][DimTokens.radiusMd])
        Column(Modifier.fillMaxWidth()) {
            var frameHeightPx by remember { mutableIntStateOf(0) }
            Box(
                Modifier
                    .fillMaxWidth()
                    .then(if (boundedHeight) Modifier.weight(1f, fill = false) else Modifier),
            ) {
                Column(
                    modifier = Modifier
                        .width(contentWidth)
                        .onSizeChanged { frameHeightPx = it.height }
                        .clip(shape)
                        .border(
                            width = Theme[DimProps][DimTokens.borderWidth],
                            color = colors.border,
                            shape = shape,
                        )
                        .background(colors.background),
                ) {
                    EdgeRow(
                        slotWidths = slotWidths,
                        gripSlots = gripSlots,
                        columns = columns,
                        minHeight = config.headerHeight,
                        padding = config.cellPadding,
                        background = colors.headerBackground,
                        colors = colors,
                        horizontalScrollState = horizontalScrollState,
                        drawTopBorder = false,
                        drawBottomBorder = true,
                    ) { columnIndex, column ->
                        if (headerContent != null) {
                            headerContent(columnIndex, column)
                        } else {
                            BasicText(text = column.header, style = colors.headerTextStyle, maxLines = 1)
                        }
                    }

                    // Body: wraps its rows, never taller than the space left. In an
                    // unbounded parent its cap follows the measured average row height.
                    val averageRowPx by remember(lazyState) {
                        derivedStateOf {
                            val visible = lazyState.visibleRowItems(ctx.rows.size)
                            if (visible.isEmpty()) 0f else visible.sumOf { it.size }.toFloat() / visible.size
                        }
                    }
                    val bodyModifier = if (boundedHeight) {
                        Modifier.weight(1f, fill = false)
                    } else {
                        val rowPx = maxOf(averageRowPx, with(density) { config.rowHeight.toPx() })
                        Modifier.heightIn(max = with(density) { (rowPx * rows.size).toDp() } + 1.dp)
                    }
                    if (rows.isEmpty() && emptyContent != null) {
                        Box(
                            modifier = Modifier.fillMaxWidth().heightIn(min = TableEmptyHeight),
                            contentAlignment = Alignment.Center,
                        ) { emptyContent() }
                    }
                    Box(Modifier.fillMaxWidth().then(bodyModifier)) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .tableHoverTracker { hoverPointer.position = it }
                                .tableRowDrag(
                                    enabled = config.reorderRows,
                                    handle = config.rowDragHandle,
                                    gripWidthPx = ctx.slotWidthsPx.firstOrNull()?.takeIf { showGrip } ?: 0f,
                                    horizontalScrollPx = { horizontalScrollState.value },
                                    lazyState = lazyState,
                                    rowCount = { ctx.rows.size },
                                    drag = ctx.drag,
                                    onDrop = { from, to ->
                                        // Moving the first visible keyed row would drag the
                                        // scroll position along; keep the viewport in place.
                                        val index = lazyState.firstVisibleItemIndex
                                        val offset = lazyState.firstVisibleItemScrollOffset
                                        currentOnRowMoved?.invoke(from, to)
                                        lazyState.requestScrollToItem(index, offset)
                                    },
                                ),
                            state = lazyState,
                        ) {
                            EmbeddedDataTableView(horizontalScrollState = horizontalScrollState, table = table)
                        }
                        Box(Modifier.matchParentSize().clipToBounds()) {
                            if (ctx.drag.isDragging) {
                                DragGhost(
                                    ctx = ctx,
                                    horizontalScrollState = horizontalScrollState,
                                    viewportHeight = { lazyState.layoutInfo.viewportSize.height },
                                )
                            }
                        }
                    }

                    if (showFooter) {
                        EdgeRow(
                            slotWidths = slotWidths,
                            gripSlots = gripSlots,
                            columns = columns,
                            minHeight = config.headerHeight,
                            padding = config.cellPadding,
                            background = colors.footerBackground,
                            colors = colors,
                            horizontalScrollState = horizontalScrollState,
                            drawTopBorder = rows.isNotEmpty() || emptyContent != null,
                            drawBottomBorder = false,
                        ) { columnIndex, column ->
                            if (footerContent != null) {
                                footerContent(columnIndex, column)
                            } else {
                                column.footer?.let { BasicText(text = it, style = colors.footerTextStyle, maxLines = 1) }
                            }
                        }
                    }
                }
                if (hasVerticalOverflow) {
                    VerticalScrollbar(
                        // Thumb drags are applied once per frame: composing a full
                        // viewport of rows per mouse event made dragging lag behind.
                        scrollbarState = rememberCoalescedScrollbarState(lazyState),
                        thumbVisibility = ThumbVisibility.AlwaysVisible,
                        // A thousand rows would shrink the thumb to a sliver; keep it
                        // about a row tall so it's easy to grab.
                        minThumbSize = minOf(config.rowHeight, TableMinThumbSize),
                        modifier = Modifier
                            .padding(start = contentWidth + (TableScrollbarSpace - BaseTokens.token10) / 2)
                            .height(with(density) { frameHeightPx.toDp() }),
                    )
                }
            }

            val hasHorizontalOverflow by remember(horizontalScrollState) {
                derivedStateOf { horizontalScrollState.maxValue > 0 }
            }
            if (hasHorizontalOverflow) {
                HorizontalScrollbar(
                    scrollbarState = rememberCoalescedScrollbarState(horizontalScrollState),
                    thumbVisibility = ThumbVisibility.AlwaysVisible,
                    minThumbSize = TableMinThumbSize,
                    modifier = Modifier
                        .width(contentWidth)
                        .padding(top = TwDimensions.paddingPxToken1),
                )
            }

            val caption = config.caption
            if (!caption.isNullOrBlank()) {
                BasicText(
                    text = caption,
                    modifier = Modifier.padding(
                        top = TwDimensions.paddingPxToken2,
                        start = TwDimensions.paddingPxToken1,
                    ),
                    style = colors.captionTextStyle,
                )
            }
        }
    }
}

/** State shared with the provider-composed cells (they live in other compositions). */
@Stable
internal class TableContext<T : Any> {
    var rows by mutableStateOf<List<T>>(emptyList(), referentialEqualityPolicy())
    var columns by mutableStateOf<List<DataTableColumn<T>>>(emptyList())
    var config by mutableStateOf(TableConfig())
    var colors by mutableStateOf<DataTableColors?>(null)
    var showGrip by mutableStateOf(false)
    var gripSlots by mutableIntStateOf(0)
    var slotWidths by mutableStateOf<List<Dp>>(emptyList())
    var slotWidthsPx by mutableStateOf<List<Float>>(emptyList())
    var cellContent by mutableStateOf<(@Composable (Int, DataTableColumn<T>, T) -> Unit)?>(null)
    var rowBackground by mutableStateOf<(@Composable (T, Int) -> Color)?>(null)
    var hoveredState: () -> TableHoveredCell? = { null }
    val drag = TableRowDragState()
}

/**
 * One body cell (or the grip) of row [rowIndex]. [ghost] renders it inside the
 * floating copy of the dragged row.
 */
@Composable
private fun <T : Any> BodyCell(
    ctx: TableContext<T>,
    slot: Int,
    rowIndex: Int,
    row: T,
    ghost: Boolean = false,
) {
    val colors = ctx.colors ?: return
    val config = ctx.config
    val width = ctx.slotWidths.getOrNull(slot) ?: return
    val isGrip = ctx.showGrip && slot == 0
    val columnIndex = slot - ctx.gripSlots
    val column = if (isGrip) null else (ctx.columns.getOrNull(columnIndex) ?: return)
    val drag = ctx.drag

    // Drag preview: the provider clips every cell to its own bounds, so rows can't be
    // slid by translation. Instead each slot shows the row that lands there if the
    // drop happened now (the gap at the target stays empty under the floating row),
    // and the content slides in from the slot it was shown at before.
    val dragging = !ghost && drag.isDragging
    val from = drag.fromIndex
    val target = drag.targetIndex
    val sourceIndex = if (dragging) previewSource(rowIndex, from, target) else rowIndex
    val shown = if (dragging) ctx.rows.getOrNull(sourceIndex) ?: row else row
    val hidden = dragging && rowIndex == target

    // The slide must start in the very frame the content swaps, otherwise the new
    // content flashes at its final place before jumping back (a visible bounce). So
    // the start offset is computed during composition and a fresh Animatable is
    // created with it as the initial value; the effect only runs it down to zero.
    val previous = remember { IntArray(2) { -1 } } // [from, target] of the last composition
    var startOffset = 0f
    if (dragging && previous[0] == from && previous[1] >= 0 && previous[1] != target && !hidden) {
        val previousSlot = previewSlot(sourceIndex, from, previous[1])
        if (previousSlot != rowIndex) startOffset = (previousSlot - rowIndex) * drag.draggedHeightPx
    }
    previous[0] = if (dragging) from else -1
    previous[1] = if (dragging) target else -1
    // `slides` is fixed for the Animatable's lifetime, so the effect never leaves the
    // composition mid-animation; cells that don't slide launch no coroutine at all
    // (cheap composition of freshly scrolled-in rows).
    val (slide, slides) = remember(dragging, from, target) { Animatable(startOffset) to (startOffset != 0f) }
    if (slides) {
        LaunchedEffect(slide) { slide.animateTo(0f, tween(TableShiftMillis)) }
    }

    val hovered = if (ghost) null else ctx.hoveredState()
    val rowHovered = hovered?.row == rowIndex
    val cellHovered = rowHovered && !isGrip && hovered.column == columnIndex

    TableBox(
        width = width,
        minHeight = config.rowHeight,
        alignment = column?.alignment ?: Alignment.Center,
        padding = if (isGrip) PaddingValues(0.dp) else config.cellPadding,
        background = ctx.rowBackground?.invoke(shown, sourceIndex) ?: colors.background,
        rowOverlay = if (rowHovered) config.rowHoverBackground else null,
        cellOverlay = if (cellHovered) config.cellHoverBackground else null,
        borderColor = colors.border,
        borderWidth = colors.borderWidthPx,
        drawRightBorder = false,
        drawTopBorder = false,
        drawBottomBorder = !ghost && rowIndex < ctx.rows.lastIndex,
        fillHeight = ghost,
        contentModifier = Modifier.graphicsLayer {
            // Read in the draw layer: no recomposition per animation frame, and the
            // drop snaps back in the very frame it lands.
            translationY = if (drag.isDragging) slide.value else 0f
            alpha = if (hidden && drag.isDragging) 0f else 1f
        },
    ) {
        when {
            isGrip -> GripDots(color = colors.gripForeground)
            column == null -> Unit
            else -> {
                val content = ctx.cellContent
                if (content != null) {
                    content(sourceIndex, column, shown)
                } else {
                    val text = column.cell?.invoke(shown)
                    if (!text.isNullOrBlank()) {
                        BasicText(text = text, style = colors.cellTextStyle, maxLines = 1)
                    }
                }
            }
        }
    }
}

/** Row shown at [slot] while the row [from] hovers over [target]. */
private fun previewSource(slot: Int, from: Int, target: Int): Int = when {
    slot == target -> from
    from < target && slot in from until target -> slot + 1
    target < from && slot in (target + 1)..from -> slot - 1
    else -> slot
}

/** Slot where row [source] is shown while the row [from] hovers over [target]. */
private fun previewSlot(source: Int, from: Int, target: Int): Int = when {
    source == from -> target
    from < target && source in (from + 1)..target -> source - 1
    target < from && source in target until from -> source + 1
    else -> source
}

/** Floating copy of the dragged row, following the pointer over the body. */
@Composable
private fun <T : Any> DragGhost(
    ctx: TableContext<T>,
    horizontalScrollState: ScrollState,
    viewportHeight: () -> Int,
) {
    val drag = ctx.drag
    val row = ctx.rows.getOrNull(drag.fromIndex) ?: return
    val colors = ctx.colors ?: return
    val density = LocalDensity.current
    Row(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart, unbounded = true)
            .offset {
                IntOffset(-horizontalScrollState.value, drag.ghostTop(viewportHeight()).roundToInt())
            }
            .height(with(density) { drag.draggedHeightPx.toDp() })
            .shadow(TableGhostElevation)
            .graphicsLayer { alpha = TableGhostAlpha }
            .border(Theme[DimProps][DimTokens.borderWidth], colors.border),
    ) {
        ctx.slotWidths.indices.forEach { slot ->
            BodyCell(ctx = ctx, slot = slot, rowIndex = drag.fromIndex, row = row, ghost = true)
        }
    }
}

/** Header/footer row: pinned, shares the horizontal scroll with the body. */
@Composable
private fun <T : Any> EdgeRow(
    slotWidths: List<Dp>,
    gripSlots: Int,
    columns: List<DataTableColumn<T>>,
    minHeight: Dp,
    padding: PaddingValues,
    background: Color,
    colors: DataTableColors,
    horizontalScrollState: ScrollState,
    drawTopBorder: Boolean,
    drawBottomBorder: Boolean,
    content: @Composable (columnIndex: Int, column: DataTableColumn<T>) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(horizontalScrollState),
    ) {
        Row(Modifier.height(IntrinsicSize.Min)) {
            slotWidths.forEachIndexed { slot, width ->
                val column = columns.getOrNull(slot - gripSlots)
                TableBox(
                    width = width,
                    minHeight = minHeight,
                    alignment = column?.alignment ?: Alignment.Center,
                    padding = if (column == null) PaddingValues(0.dp) else padding,
                    background = background,
                    rowOverlay = null,
                    cellOverlay = null,
                    borderColor = colors.border,
                    borderWidth = colors.borderWidthPx,
                    drawRightBorder = false,
                    drawTopBorder = drawTopBorder,
                    drawBottomBorder = drawBottomBorder,
                    fillHeight = true,
                ) {
                    if (column != null) content(slot - gripSlots, column)
                }
            }
        }
    }
}

/**
 * Cell box: fixed width, minimal height (taller content grows), padding and content
 * alignment. Background, highlights and separators are drawn here (inside the bounds,
 * so neighbours never paint over half of a line).
 */
@Composable
private fun TableBox(
    width: Dp,
    minHeight: Dp,
    alignment: Alignment,
    padding: PaddingValues,
    background: Color,
    rowOverlay: Color?,
    cellOverlay: Color?,
    borderColor: Color,
    borderWidth: Float,
    drawRightBorder: Boolean,
    drawTopBorder: Boolean,
    drawBottomBorder: Boolean,
    fillHeight: Boolean,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .width(width)
            .then(if (fillHeight) Modifier.fillMaxHeight() else Modifier)
            .heightIn(min = minHeight)
            .drawBehind {
                drawRect(color = background)
                if (rowOverlay != null) drawRect(color = rowOverlay)
                if (cellOverlay != null) drawRect(color = cellOverlay)
                val half = borderWidth / 2f
                if (drawRightBorder) {
                    drawLine(borderColor, Offset(size.width - half, 0f), Offset(size.width - half, size.height), borderWidth)
                }
                if (drawTopBorder) {
                    drawLine(borderColor, Offset(0f, half), Offset(size.width, half), borderWidth)
                }
                if (drawBottomBorder) {
                    drawLine(borderColor, Offset(0f, size.height - half), Offset(size.width, size.height - half), borderWidth)
                }
            }
            .padding(padding),
        contentAlignment = alignment,
    ) {
        Box(modifier = contentModifier, contentAlignment = alignment) { content() }
    }
}

/** Drag grip (lucide `grip-vertical`): two columns of three dots. */
@Composable
private fun GripDots(color: Color) {
    Canvas(
        modifier = Modifier
            .size(BaseTokens.token16)
            .pointerHoverIcon(PointerIcon.Hand),
    ) {
        val radius = size.minDimension / 10f
        val xs = listOf(size.width * 0.35f, size.width * 0.65f)
        val ys = listOf(size.height * 0.2f, size.height * 0.5f, size.height * 0.8f)
        xs.forEach { x -> ys.forEach { y -> drawCircle(color, radius, Offset(x, y)) } }
    }
}

/**
 * Measured text widths per column (px): the header always (custom header slots
 * typically render the same label), the footer text when no footer slot is used, and
 * text cells when no custom content slots are used. Cached until the inputs change.
 */
@Composable
private fun <T : Any> rememberMeasuredTextWidths(
    columns: List<DataTableColumn<T>>,
    rows: List<T>,
    colors: DataTableColors,
    measureCells: Boolean,
    measureFooter: Boolean,
): List<Int> {
    val measurer = rememberTextMeasurer()
    // Text → width caches survive recompositions: reordering or appending rows only
    // measures strings never seen before (a reorder of 1,000 rows measures nothing).
    val headerCache = remember(measurer, colors.headerTextStyle) { HashMap<String, Int>() }
    val cellCache = remember(measurer, colors.cellTextStyle) { HashMap<String, Int>() }
    val footerCache = remember(measurer, colors.footerTextStyle) { HashMap<String, Int>() }
    val columnsKey = columns.map { Triple(it.key, it.header, it.footer) }
    return remember(columnsKey, rows, measureCells, measureFooter, headerCache, cellCache, footerCache) {
        fun HashMap<String, Int>.width(text: String, style: TextStyle): Int = getOrPut(text) {
            measurer.measure(text = text, style = style, constraints = Constraints(), maxLines = 1).size.width
        }
        columns.map { column ->
            var max = headerCache.width(column.header, colors.headerTextStyle)
            val footer = column.footer
            if (measureFooter && footer != null) max = maxOf(max, footerCache.width(footer, colors.footerTextStyle))
            val cell = column.cell
            if (measureCells && cell != null) {
                for (row in rows) max = maxOf(max, cellCache.width(cell(row), colors.cellTextStyle))
            }
            max
        }
    }
}

/**
 * Column widths: weighted columns share the leftover of the container after the
 * fixed ones (never below their minimal width), then everything is optionally scaled
 * up to fill the container, then measured text grows a column so nothing is cut off.
 */
@Composable
private fun <T : Any> resolveColumnWidths(
    columns: List<DataTableColumn<T>>,
    config: TableConfig,
    containerWidth: Dp,
    measuredTextPx: List<Int>,
): List<Dp> {
    val weighted = columns.filter { it.weight != null }
    val totalWeight = weighted.sumOf { it.weight?.toDouble() ?: 0.0 }
    val widths: List<Dp> = if (weighted.isEmpty() || containerWidth == Dp.Infinity || totalWeight <= 0.0) {
        columns.map { it.width }
    } else {
        val fixedWidth = columns.filter { it.weight == null }.sumOf { it.width.value.toDouble() }
        val flexibleWidth = (containerWidth.value.toDouble() - fixedWidth).coerceAtLeast(0.0)
        columns.map { column ->
            val w = column.weight ?: return@map column.width
            Dp(maxOf(column.width.value, (flexibleWidth * (w / totalWeight)).toFloat()))
        }
    }

    // Stretch: scale up to fill the container (proportions stay). Columns wider than
    // the container are never squeezed — that case scrolls horizontally.
    val total = widths.sumOf { it.value.toDouble() }
    val stretched = if (
        config.stretchToContainer &&
        containerWidth != Dp.Infinity &&
        total > 0.0 &&
        total < containerWidth.value.toDouble() - 0.5
    ) {
        val factor = containerWidth.value / total.toFloat()
        widths.map { it * factor }
    } else {
        widths
    }

    val density = LocalDensity.current
    val direction = LocalLayoutDirection.current
    val horizontalPaddingPx = with(density) {
        (config.cellPadding.calculateLeftPadding(direction) + config.cellPadding.calculateRightPadding(direction)).toPx()
    }
    return columns.indices.map { index ->
        val textWidth = with(density) { (measuredTextPx[index] + horizontalPaddingPx).toDp() }
        maxOf(stretched[index], textWidth)
    }
}

private const val GripSlotKey = "__table_row_grip__"
private const val TableShiftMillis = 150
private const val TableGhostAlpha = 0.92f

/** Reserve for the always-visible vertical scrollbar: track + side padding. */
private val TableScrollbarSpace: Dp = BaseTokens.token10 * 2

/** Grip column width (shadcn drag-handle column: `size-7` button + cell padding). */
private val TableGripWidth: Dp = BaseTokens.token40

/** Height of the empty state (shadcn `h-24`). */
private val TableEmptyHeight: Dp = BaseTokens.token96

private val TableGhostElevation: Dp = BaseTokens.token8

/** Minimal scrollbar thumb length of the table (about a row). */
private val TableMinThumbSize: Dp = BaseTokens.token40
