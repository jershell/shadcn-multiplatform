package com.github.jershell.shadcn.anchored

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import com.composeunstyled.AnchorAlignment
import com.composeunstyled.AnchoredLayout
import com.composeunstyled.AnchorSide
import com.composeunstyled.FloatingPlacement
import com.composeunstyled.calculateFloatingPlacement
import kotlin.math.abs

/**
 * Flip-aware variant of [com.composeunstyled.AnchoredFloatingContent].
 *
 * The unstyled placement (`calculateFloatingPlacement`) only shifts the content to keep it
 * inside the window, so a panel that does not fit on the requested side gets clamped back
 * over its anchor. This composable instead picks the side before placing:
 *
 * 1. the requested side, when the panel fits on the side axis there;
 * 2. otherwise the opposite (flipped) side, when it fits there;
 * 3. otherwise the side with the smaller overlap (clamped shift) on the side axis.
 *
 * Placement is computed during measure through the public unstyled
 * [calculateFloatingPlacement], so offsets and RTL behavior stay identical to the
 * unstyled primitive when everything fits.
 */
@Composable
internal fun FlipAnchoredFloatingContent(
    layer: @Composable (content: @Composable () -> Unit) -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    side: AnchorSide = AnchorSide.Top,
    alignment: AnchorAlignment = AnchorAlignment.Center,
    sideOffset: Dp = 0.dp,
    alignmentOffset: Dp = 0.dp,
    onPlaced: (FloatingPlacement) -> Unit = {},
    anchor: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current

    AnchoredLayout(
        modifier = modifier,
        content = { anchorBounds, windowSize ->
            layer {
                FlipAnchoredContent(
                    anchorBounds = anchorBounds,
                    windowSize = windowSize,
                    modifier = contentModifier,
                    side = side,
                    alignment = alignment,
                    sideOffset = sideOffset,
                    alignmentOffset = alignmentOffset,
                    density = density,
                    layoutDirection = layoutDirection,
                    onPlaced = onPlaced,
                    content = content,
                )
            }
        },
        anchor = anchor,
    )
}

@Composable
private fun FlipAnchoredContent(
    anchorBounds: IntRect,
    windowSize: IntSize,
    modifier: Modifier = Modifier,
    side: AnchorSide,
    alignment: AnchorAlignment,
    sideOffset: Dp,
    alignmentOffset: Dp,
    density: Density,
    layoutDirection: LayoutDirection,
    onPlaced: (FloatingPlacement) -> Unit,
    content: @Composable () -> Unit,
) {
    var containerBoundsInWindow by remember { mutableStateOf<IntRect?>(null) }

    Layout(
        content = content,
        modifier = modifier.onGloballyPositioned {
            val position = it.positionInWindow().round()
            containerBoundsInWindow = IntRect(
                left = position.x,
                top = position.y,
                right = position.x + it.size.width,
                bottom = position.y + it.size.height,
            )
        },
    ) { measurables, constraints ->
        val contentPlaceable = measurables.firstOrNull()?.measure(Constraints())

        if (contentPlaceable == null) {
            return@Layout layout(0, 0) {}
        }

        val currentContainerBounds = containerBoundsInWindow

        if (currentContainerBounds == null) {
            return@Layout layout(constraints.maxWidth, constraints.maxHeight) {}
        }
        val placementWindowSize = IntSize(currentContainerBounds.width, currentContainerBounds.height)
        val placementAnchorBounds = anchorBounds.translate(
            IntOffset(
                x = -currentContainerBounds.left,
                y = -currentContainerBounds.top,
            ),
        )
        val contentSize = IntSize(contentPlaceable.width, contentPlaceable.height)

        fun placement(side: AnchorSide): FloatingPlacement = calculateFloatingPlacement(
            density = density,
            anchorBounds = placementAnchorBounds,
            windowSize = placementWindowSize,
            layoutDirection = layoutDirection,
            contentSize = contentSize,
            side = side,
            alignment = alignment,
            sideOffset = sideOffset,
            alignmentOffset = alignmentOffset,
        )

        val requested = placement(side)
        val chosen = if (requested.fitsOnSideAxis(side)) {
            requested
        } else {
            val flippedSide = side.flipped()
            val flipped = placement(flippedSide)
            when {
                flipped.fitsOnSideAxis(flippedSide) -> flipped
                requested.overlapOnSideAxis(side) <= flipped.overlapOnSideAxis(flippedSide) -> requested
                else -> flipped
            }
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            onPlaced(chosen)
            contentPlaceable.place(
                x = chosen.position.x,
                y = chosen.position.y,
            )
        }
    }
}

/**
 * True when the placement did not need a shift on the side axis, meaning the panel
 * fits entirely in the window on that axis. Cross-axis shifts are allowed, matching
 * the unstyled shift behavior.
 */
private fun FloatingPlacement.fitsOnSideAxis(side: AnchorSide): Boolean = when (side) {
    AnchorSide.Top, AnchorSide.Bottom -> positionAdjustment.y == 0
    AnchorSide.Start, AnchorSide.End -> positionAdjustment.x == 0
}

/**
 * The distance the panel was clamped back on the side axis, i.e. how much it overlaps
 * the window edge (and the anchor) on that side.
 */
private fun FloatingPlacement.overlapOnSideAxis(side: AnchorSide): Int = when (side) {
    AnchorSide.Top, AnchorSide.Bottom -> abs(positionAdjustment.y)
    AnchorSide.Start, AnchorSide.End -> abs(positionAdjustment.x)
}

private fun AnchorSide.flipped(): AnchorSide = when (this) {
    AnchorSide.Top -> AnchorSide.Bottom
    AnchorSide.Bottom -> AnchorSide.Top
    AnchorSide.Start -> AnchorSide.End
    AnchorSide.End -> AnchorSide.Start
}
