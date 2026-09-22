package com.github.jershell.shadcn.components.resizable

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens

enum class ResizableOrientation {
    Horizontal,
    Vertical,
}

interface ResizablePanelGroupScope {
    fun panel(
        minSizeFraction: Float = 0.1f,
        content: @Composable BoxScope.() -> Unit,
    )

    fun handle(withHandle: Boolean = false)
}

private sealed interface ResizableEntry
private data class ResizablePanelEntry(
    val minSizeFraction: Float,
    val content: @Composable BoxScope.() -> Unit,
) : ResizableEntry
private data class ResizableHandleEntry(
    val withHandle: Boolean,
) : ResizableEntry

private class ResizablePanelGroupScopeImpl : ResizablePanelGroupScope {
    val entries = mutableListOf<ResizableEntry>()

    override fun panel(
        minSizeFraction: Float,
        content: @Composable BoxScope.() -> Unit,
    ) {
        entries += ResizablePanelEntry(
            minSizeFraction = minSizeFraction.coerceIn(0.01f, 0.95f),
            content = content,
        )
    }

    override fun handle(withHandle: Boolean) {
        entries += ResizableHandleEntry(withHandle)
    }
}

@Composable
fun ResizablePanelGroup(
    modifier: Modifier = Modifier,
    orientation: ResizableOrientation = ResizableOrientation.Horizontal,
    content: ResizablePanelGroupScope.() -> Unit,
) {
    val scope = remember { ResizablePanelGroupScopeImpl() }
    scope.entries.clear()
    scope.content()

    val panels = scope.entries.filterIsInstance<ResizablePanelEntry>()
    if (panels.size < 2) return

    val minFractions = panels.map { it.minSizeFraction }
    val panelSizes = rememberPanelSizes(panels.size)

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val totalSizePx = when (orientation) {
            ResizableOrientation.Horizontal -> constraints.maxWidth.toFloat().coerceAtLeast(1f)
            ResizableOrientation.Vertical -> constraints.maxHeight.toFloat().coerceAtLeast(1f)
        }

        var panelIndex = 0
        when (orientation) {
            ResizableOrientation.Horizontal -> Row(modifier = Modifier.fillMaxSize()) {
                scope.entries.forEach { entry ->
                    when (entry) {
                        is ResizablePanelEntry -> {
                            Box(
                                modifier = Modifier
                                    .weight(panelSizes[panelIndex])
                                    .fillMaxHeight(),
                                content = entry.content,
                            )
                            panelIndex++
                        }
                        is ResizableHandleEntry -> {
                            val leftIndex = panelIndex - 1
                            val rightIndex = panelIndex
                            InternalResizableHandle(
                                orientation = orientation,
                                withHandle = entry.withHandle,
                                onDrag = { deltaPx ->
                                    applyResizeDelta(
                                        panelSizes = panelSizes,
                                        minFractions = minFractions,
                                        leftIndex = leftIndex,
                                        rightIndex = rightIndex,
                                        deltaFraction = deltaPx / totalSizePx,
                                    )
                                },
                            )
                        }
                    }
                }
            }

            ResizableOrientation.Vertical -> Column(modifier = Modifier.fillMaxSize()) {
                scope.entries.forEach { entry ->
                    when (entry) {
                        is ResizablePanelEntry -> {
                            Box(
                                modifier = Modifier
                                    .weight(panelSizes[panelIndex])
                                    .fillMaxWidth(),
                                content = entry.content,
                            )
                            panelIndex++
                        }
                        is ResizableHandleEntry -> {
                            val topIndex = panelIndex - 1
                            val bottomIndex = panelIndex
                            InternalResizableHandle(
                                orientation = orientation,
                                withHandle = entry.withHandle,
                                onDrag = { deltaPx ->
                                    applyResizeDelta(
                                        panelSizes = panelSizes,
                                        minFractions = minFractions,
                                        leftIndex = topIndex,
                                        rightIndex = bottomIndex,
                                        deltaFraction = deltaPx / totalSizePx,
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberPanelSizes(panelCount: Int): SnapshotStateList<Float> {
    val sizes = remember(panelCount) {
        mutableStateListOf<Float>().apply {
            repeat(panelCount) { add(1f / panelCount) }
        }
    }
    if (sizes.size != panelCount) {
        sizes.clear()
        repeat(panelCount) { sizes += 1f / panelCount }
    }
    return sizes
}

private fun applyResizeDelta(
    panelSizes: SnapshotStateList<Float>,
    minFractions: List<Float>,
    leftIndex: Int,
    rightIndex: Int,
    deltaFraction: Float,
) {
    if (leftIndex !in panelSizes.indices || rightIndex !in panelSizes.indices) return
    val left = panelSizes[leftIndex]
    val right = panelSizes[rightIndex]
    val leftMin = minFractions[leftIndex]
    val rightMin = minFractions[rightIndex]

    val maxPositiveDelta = right - rightMin
    val maxNegativeDelta = -(left - leftMin)
    val clamped = deltaFraction.coerceIn(maxNegativeDelta, maxPositiveDelta)

    panelSizes[leftIndex] = left + clamped
    panelSizes[rightIndex] = right - clamped
}

@Composable
private fun InternalResizableHandle(
    orientation: ResizableOrientation,
    withHandle: Boolean,
    onDrag: (Float) -> Unit,
) {
    val border = Theme[ColorProps][ColorTokens.border]
    val grip = Theme[ColorProps][ColorTokens.mutedForeground].copy(alpha = 0.7f)
    val radius = Theme[DimProps][DimTokens.radiusFull]
    val shape = RoundedCornerShape(radius)

    val handleModifier = when (orientation) {
        ResizableOrientation.Horizontal -> Modifier
            .width(10.dp)
            .fillMaxHeight()
        ResizableOrientation.Vertical -> Modifier
            .height(10.dp)
            .fillMaxWidth()
    }
    Box(
        modifier = handleModifier
            .resizableHoverCursor(orientation)
            .pointerInput(orientation) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    val delta = if (orientation == ResizableOrientation.Horizontal) {
                        dragAmount.x
                    } else {
                        dragAmount.y
                    }
                    onDrag(delta)
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        val lineModifier = when (orientation) {
            ResizableOrientation.Horizontal -> Modifier
                .width(1.dp)
                .fillMaxHeight()
            ResizableOrientation.Vertical -> Modifier
                .height(1.dp)
                .fillMaxWidth()
        }
        Box(modifier = lineModifier.background(border))

        if (withHandle) {
            val gripModifier = when (orientation) {
                ResizableOrientation.Horizontal -> Modifier
                    .width(6.dp)
                    .height(48.dp)
                ResizableOrientation.Vertical -> Modifier
                    .width(48.dp)
                    .height(6.dp)
            }
            Box(
                modifier = gripModifier
                    .background(grip, shape),
            )
        }
    }
}
