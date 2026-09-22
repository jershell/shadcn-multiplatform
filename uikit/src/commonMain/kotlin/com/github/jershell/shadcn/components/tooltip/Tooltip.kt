package com.github.jershell.shadcn.components.tooltip

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.AnchorAlignment
import com.composeunstyled.AnchorSide
import com.composeunstyled.TooltipPanel
import com.composeunstyled.TooltipPlacement
import com.composeunstyled.TooltipScope
import com.composeunstyled.UnstyledTooltip
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.TypographyStyles

internal val LocalTooltipContentColor = staticCompositionLocalOf { Color.Unspecified }

/**
 * A floating hint shown on hover or keyboard focus, styled after shadcn/ui
 * [Tooltip](https://ui.shadcn.com/docs/components/tooltip).
 *
 * Shows on hover (shadcn uses delayDuration = 0) and while the anchor has keyboard
 * focus; hides on mouse leave or Escape.
 *
 * The application root must be wrapped in [com.composeunstyled.ModalHost]
 * (or [com.composeunstyled.PortalHost]) so the panel can be portaled.
 *
 * @param tooltip Panel content; composed only while the tooltip is visible.
 * @param enabled Whether hovering/focusing the anchor shows the tooltip.
 * @param side Side of the anchor the panel is placed on.
 * @param alignment Alignment of the panel relative to the anchor.
 * @param sideOffset Gap between the anchor and the panel.
 * @param alignmentOffset Additional offset along the anchor axis.
 * @param hoverDelayMillis Hover delay before the tooltip shows.
 * @param anchor The trigger content.
 */
@Composable
fun Tooltip(
    tooltip: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    side: AnchorSide = AnchorSide.Top,
    alignment: AnchorAlignment = AnchorAlignment.Center,
    sideOffset: Dp = DefaultSideOffset,
    alignmentOffset: Dp = 0.dp,
    hoverDelayMillis: Long = 0L,
    anchor: @Composable () -> Unit,
) {
    UnstyledTooltip(
        enabled = enabled,
        side = side,
        alignment = alignment,
        sideOffset = sideOffset,
        alignmentOffset = alignmentOffset,
        hoverDelayMillis = hoverDelayMillis,
        anchor = { Box(modifier = modifier) { anchor() } },
        panel = {
            TooltipPanel(
                enter = tooltipEnter(side),
                exit = tooltipExit(side),
            ) { placement ->
                Box {
                    TooltipPanelContainer {
                        tooltip()
                    }
                    // Arrow on the face OPPOSITE the panel side: side=Top — the panel
                    // is above the anchor, the arrow is at the bottom pointing to it
                    TooltipArrow(
                        side = placement.side.opposite(),
                        modifier = Modifier.align(arrowAlignment(placement.side.opposite())),
                    )
                }
            }
        },
    )
}

/**
 * Styled tooltip panel container: bg-foreground, rounded-md, px-3 py-1.5
 * (colors inverted relative to the page, like shadcn/ui).
 * For arbitrary content, the text color is provided by [LocalTooltipContentColor].
 */
@Composable
fun TooltipPanelContainer(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(Theme[DimProps][DimTokens.radiusMd]),
    content: @Composable () -> Unit,
) {
    val colors = resolveTooltipColors()
    // No clip: it would cut off the outer half of the arrow
    Box(
        modifier = modifier
            .background(colors.container, shape)
            .padding(horizontal = BasePaddingToken12, vertical = BasePaddingToken6),
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalTooltipContentColor provides colors.content) {
            content()
        }
    }
}

/**
 * Text in the tooltip panel color (bg-foreground/text-background inversion).
 */
@Composable
fun TooltipText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TypographyStyles.textXsRegular,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = style.copy(color = LocalTooltipContentColor.current),
    )
}

private fun arrowAlignment(side: AnchorSide): Alignment = when (side) {
    AnchorSide.Top -> Alignment.TopCenter
    AnchorSide.Bottom -> Alignment.BottomCenter
    AnchorSide.Start -> Alignment.CenterStart
    AnchorSide.End -> Alignment.CenterEnd
}

/** Diamond-shaped arrow on the panel edge, like TooltipPrimitive.Arrow in shadcn/ui.
 *  [side] is the panel side the arrow is attached to (opposite the panel side). */
@Composable
private fun TooltipArrow(side: AnchorSide, modifier: Modifier = Modifier) {
    val colors = resolveTooltipColors()
    // half of the diamond sticks out past the panel edge, toward the anchor
    val edgeOffset = when (side) {
        AnchorSide.Top -> Modifier.offset(y = -5.dp)
        AnchorSide.Bottom -> Modifier.offset(y = 5.dp)
        AnchorSide.Start -> Modifier.offset(x = -5.dp)
        AnchorSide.End -> Modifier.offset(x = 5.dp)
    }
    Box(
        modifier = modifier
            .then(edgeOffset)
            .size(ArrowSize)
            .rotate(45f)
            .clip(RoundedCornerShape(2.dp))
            .background(colors.container),
    )
}

private fun AnchorSide.opposite(): AnchorSide = when (this) {
    AnchorSide.Top -> AnchorSide.Bottom
    AnchorSide.Bottom -> AnchorSide.Top
    AnchorSide.Start -> AnchorSide.End
    AnchorSide.End -> AnchorSide.Start
}

private val DefaultSideOffset = 10.dp

private val BasePaddingToken6 = 6.dp
private val BasePaddingToken12 = 12.dp
private val ArrowSize = 10.dp

private fun tooltipEnter(side: AnchorSide): EnterTransition =
    fadeIn(tween(150)) +
        scaleIn(initialScale = 0.95f, animationSpec = tween(150)) +
        when (side) {
            AnchorSide.Top -> slideInVertically(tween(150)) { 8 } // slide-in-from-bottom-2
            AnchorSide.Bottom -> slideInVertically(tween(150)) { -8 }
            AnchorSide.Start -> slideInHorizontally(tween(150)) { 8 }
            AnchorSide.End -> slideInHorizontally(tween(150)) { -8 }
        }

private fun tooltipExit(side: AnchorSide): ExitTransition =
    fadeOut(tween(100)) +
        scaleOut(targetScale = 0.95f, animationSpec = tween(100))

@Suppress("unused")
private fun placementSide(placement: TooltipPlacement): AnchorSide = placement.side
