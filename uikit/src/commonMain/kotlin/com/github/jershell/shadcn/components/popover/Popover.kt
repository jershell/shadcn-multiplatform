package com.github.jershell.shadcn.components.popover

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.AnchorAlignment
import com.composeunstyled.AnchorSide
import com.composeunstyled.EscapeHandler
import com.composeunstyled.Portal
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.anchored.FlipAnchoredFloatingContent
import com.github.jershell.shadcn.theme.BaseTokens
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.Effects
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles

interface PopoverScope

internal object PopoverScopeInstance : PopoverScope

internal val LocalPopoverScope = staticCompositionLocalOf<PopoverScope?> { null }

/**
 * A non-modal anchored floating panel styled after the shadcn/ui Popover.
 *
 * Unlike [com.composeunstyled.UnstyledDropdownMenu], the panel is rendered in the same window
 * as the anchor (through [Portal]) instead of a modal dialog, so the anchor keeps keyboard
 * focus while the popover is open. That is what makes editable triggers, such as the Combobox
 * input, able to receive typed characters while the popup is visible.
 *
 * The application root must be wrapped in [com.composeunstyled.ModalHost] (or [com.composeunstyled.PortalHost])
 * so the panel can be portaled to a window-sized overlay.
 *
 * Clicking outside the panel or pressing Escape closes the popover.
 *
 * @param expanded Whether the popover panel is visible.
 * @param onExpandedChange Called when the user asks to show or hide the panel.
 * @param side Preferred side of the anchor the panel is placed on. When the panel does not
 *   fit on this side, it flips to the opposite side; when neither side fits, the one with
 *   the smaller overlap wins.
 * @param alignment Alignment of the panel relative to the anchor.
 * @param sideOffset Gap between the anchor and the panel.
 * @param alignmentOffset Additional offset along the anchor axis.
 * @param anchor The trigger content the panel is anchored to.
 * @param content The panel content. Composed only while [expanded] is true.
 */
@Composable
fun Popover(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    side: AnchorSide = AnchorSide.Bottom,
    alignment: AnchorAlignment = AnchorAlignment.Start,
    sideOffset: Dp = 0.dp,
    alignmentOffset: Dp = 0.dp,
    anchor: @Composable () -> Unit,
    content: @Composable PopoverScope.() -> Unit,
) {
    if (expanded) {
        EscapeHandler { onExpandedChange(false) }
    }

    FlipAnchoredFloatingContent(
        modifier = modifier,
        layer = { panelContent ->
            Portal {
                if (expanded) {
                    PopoverScrim(onDismiss = { onExpandedChange(false) })
                    panelContent()
                }
            }
        },
        content = {
            CompositionLocalProvider(LocalPopoverScope provides PopoverScopeInstance) {
                PopoverScopeInstance.content()
            }
        },
        side = side,
        alignment = alignment,
        sideOffset = sideOffset,
        alignmentOffset = alignmentOffset,
        anchor = anchor,
    )
}

@Composable
private fun PopoverScrim(onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    onDismiss()
                }
            },
    )
}

/**
 * The styled panel of a [Popover], matching the shadcn/ui `PopoverContent`:
 * a `w-72 rounded-md border bg-popover p-4 shadow-md` container.
 *
 * Render inside the [Popover] content slot. The default width can be overridden
 * by applying a width modifier to [modifier].
 *
 * @param modifier Modifier applied to the panel container.
 * @param content Panel content, laid out vertically. Use [PopoverHeader],
 *   [PopoverTitle] and [PopoverDescription] for the standard header block.
 */
@Composable
fun PopoverContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = resolvePopoverContentColors()
    val radius = Theme[DimProps][DimTokens.radiusMd]
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val shape = RoundedCornerShape(radius)

    Column(
        modifier = modifier
            .width(BaseTokens.token288) // w-72
            .clip(shape)
            .shadow(
                elevation = Effects.boxShadowShadowMdToken0.radius,
                shape = shape,
                clip = false,
                ambientColor = Effects.boxShadowShadowMdToken0.color,
                spotColor = Effects.boxShadowShadowMdToken1.color,
            )
            .background(colors.background)
            .border(borderWidth, colors.border, shape)
            .padding(TwDimensions.paddingPxToken4), // p-4
        content = content,
    )
}

/**
 * Header block of a [PopoverContent] panel, matching the shadcn/ui `PopoverHeader`:
 * a vertical stack with `gap-1 text-sm` rhythm.
 *
 * @param modifier Modifier applied to the header container.
 * @param content Header content, usually [PopoverTitle] and [PopoverDescription].
 */
@Composable
fun PopoverHeader(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken1), // gap-1
        content = content,
    )
}

/**
 * Title of a [PopoverHeader] block, matching the shadcn/ui `PopoverTitle`
 * (`font-medium` on the `text-sm` inherited from the header).
 */
@Composable
fun PopoverTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = TypographyStyles.textSmMedium.copy(
            color = Theme[ColorProps][ColorTokens.popoverForeground],
        ),
    )
}

/**
 * Description of a [PopoverHeader] block, matching the shadcn/ui `PopoverDescription`
 * (`text-muted-foreground` on the `text-sm` inherited from the header).
 */
@Composable
fun PopoverDescription(
    text: String,
    modifier: Modifier = Modifier,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = TypographyStyles.textSmRegular.copy(
            color = Theme[ColorProps][ColorTokens.mutedForeground],
        ),
    )
}
