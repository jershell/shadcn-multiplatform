package com.github.jershell.shadcn.components.button.group

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.UnstyledButton
import com.composeunstyled.focusRing
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.button.ButtonSize
import com.github.jershell.shadcn.components.button.ButtonVariant
import com.github.jershell.shadcn.components.button.LocalButtonContentColor
import com.github.jershell.shadcn.components.button.LocalButtonIconSize
import com.github.jershell.shadcn.components.button.LocalButtonTextStyle
import com.github.jershell.shadcn.components.button.resolveButtonColors
import com.github.jershell.shadcn.components.button.resolveButtonFocusRingColor
import com.github.jershell.shadcn.components.button.spec
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.Effects
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles

/**
 * Orientation of a [ButtonGroup].
 */
enum class ButtonGroupOrientation {
    Horizontal,
    Vertical,
}

/**
 * Scope of the [ButtonGroup] content.
 */
interface ButtonGroupScope {

    /**
     * A button joined into the group: outer corners keep their rounding, inner
     * corners are squared off and adjacent borders collapse into one.
     * Use [ButtonText] and [ButtonIcon] inside [content] for automatic coloring.
     *
     * @param onClick Called when the button is clicked.
     * @param modifier Modifier applied to the button.
     * @param enabled Whether the button is interactive.
     * @param variant Visual style of the button.
     * @param size Size of the button.
     * @param content Button content.
     */
    @Composable
    fun Item(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        variant: ButtonVariant = ButtonVariant.Outline,
        size: ButtonSize = ButtonSize.Default,
        content: @Composable RowScope.() -> Unit,
    )

    /**
     * A non-interactive label block (`bg-muted border text-sm font-medium`),
     * joined into the group like [Item].
     *
     * @param text Label text.
     * @param modifier Modifier applied to the label block.
     */
    @Composable
    fun Text(
        text: String,
        modifier: Modifier = Modifier,
    )

    /**
     * A separator line between items (`bg-input`); stretches across the group's
     * cross axis.
     *
     * @param modifier Modifier applied to the separator.
     */
    @Composable
    fun Separator(modifier: Modifier = Modifier)
}

/**
 * Tracks the order of children inside a button group.
 */
internal class ButtonGroupOrder {
    // Incremented in each item's remember during composition.
    var count = 0

    // Synced by the container's SideEffect after composition: items read this
    // state for isLast. Writing count during composition is not allowed — there
    // would be no recomposition on the first frame (no previous value).
    var reportedCount by mutableIntStateOf(0)

    fun nextIndex(): Int = count++
}

/**
 * A joined group of buttons and label blocks, styled after the shadcn/ui Button Group:
 * the first/last children keep their outer rounding, inner corners are squared and
 * adjacent borders collapse into a single line.
 *
 * Unlike the reference (which styles arbitrary children via CSS selectors), items
 * must be declared through [ButtonGroupScope.Item] / [ButtonGroupScope.Text] /
 * [ButtonGroupScope.Separator] so the joining can be applied.
 *
 * @param orientation Layout direction of the group.
 * @param modifier Modifier applied to the group container.
 * @param content Items declared with the [ButtonGroupScope] helpers.
 */
@Composable
fun ButtonGroup(
    modifier: Modifier = Modifier,
    orientation: ButtonGroupOrientation = ButtonGroupOrientation.Horizontal,
    content: @Composable ButtonGroupScope.() -> Unit,
) {
    val order = remember { ButtonGroupOrder() }
    val scope = remember(orientation) { ButtonGroupScopeImpl(order, orientation) }

    // After composition, publish the final child count: on the first frame
    // reportedCount is still 0 (the last item's right corner is squared off);
    // this write schedules a recomposition with the correct corners.
    androidx.compose.runtime.SideEffect {
        if (order.reportedCount != order.count) {
            order.reportedCount = order.count
        }
    }

    when (orientation) {
        // items-stretch: items align to the tallest child
        ButtonGroupOrientation.Horizontal -> Row(
            modifier = modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            scope.content()
        }

        ButtonGroupOrientation.Vertical -> Column(
            modifier = modifier.width(IntrinsicSize.Min),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            scope.content()
        }
    }
}

internal class ButtonGroupScopeImpl(
    private val order: ButtonGroupOrder,
    private val orientation: ButtonGroupOrientation,
) : ButtonGroupScope {

    @Composable
    override fun Item(
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        variant: ButtonVariant,
        size: ButtonSize,
        content: @Composable RowScope.() -> Unit,
    ) {
        // Positional remember: the index is pinned to the slot; recompositions
        // do not shift it.
        val index = remember { order.nextIndex() }
        val sizeSpec = size.spec()
        val interactionSource = remember { MutableInteractionSource() }
        val isHovered by interactionSource.collectIsHoveredAsState()
        val isPressed by interactionSource.collectIsPressedAsState()
        val colors = resolveButtonColors(
            variant = variant,
            isHovered = isHovered,
            isPressed = isPressed,
        )
        val focusRingColor = resolveButtonFocusRingColor(variant)
        val focusRingWidth = Effects.boxShadowFocusRing.spread
        val borderWidth = Theme[DimProps][DimTokens.borderWidth]
        val radius = Theme[DimProps][DimTokens.radiusMd]
        val vertical = orientation == ButtonGroupOrientation.Vertical
        val shape = joinedShape(index == 0, index == order.reportedCount - 1, radius, vertical)

        val containerModifier = modifier
            .then(
                when {
                    vertical -> Modifier.fillMaxWidth().height(sizeSpec.height)
                    // icon-only: square (same as the standalone Button)
                    sizeSpec.isIconOnly -> Modifier.size(sizeSpec.height)
                    else -> Modifier.height(sizeSpec.height)
                },
            )
            .clip(shape)
            .then(
                if (colors.border != null) {
                    // Border collapse: the left (horizontal) / top (vertical)
                    // edge is suppressed on every item except the first.
                    Modifier.groupBorder(
                        color = colors.border,
                        width = borderWidth,
                        shape = shape,
                        suppressLeft = !vertical && index != 0,
                        suppressTop = vertical && index != 0,
                    )
                } else {
                    Modifier
                },
            )
            .background(colors.container, shape)
            .focusRing(
                interactionSource = interactionSource,
                width = focusRingWidth,
                color = focusRingColor,
                shape = shape,
            )

        UnstyledButton(
            onClick = onClick,
            enabled = enabled,
            interactionSource = interactionSource,
            indication = null,
            role = Role.Button,
            modifier = containerModifier,
            contentAlignment = Alignment.Center,
        ) {
            CompositionLocalProvider(
                LocalButtonContentColor provides colors.content,
                LocalButtonIconSize provides sizeSpec.iconSize,
                LocalButtonTextStyle provides sizeSpec.textStyle,
            ) {
                Row(
                    modifier = Modifier.then(
                        if (sizeSpec.isIconOnly) {
                            Modifier
                        } else {
                            Modifier.padding(horizontal = sizeSpec.horizontalPadding)
                        },
                    ),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = sizeSpec.gap,
                        alignment = Alignment.CenterHorizontally,
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    content = content,
                )
            }
        }
    }

    @Composable
    override fun Text(
        text: String,
        modifier: Modifier,
    ) {
        val index = remember { order.nextIndex() }
        val vertical = orientation == ButtonGroupOrientation.Vertical
        val radius = Theme[DimProps][DimTokens.radiusMd]
        val borderWidth = Theme[DimProps][DimTokens.borderWidth]
        val shape = joinedShape(index == 0, index == order.reportedCount - 1, radius, vertical)

        val blockModifier = when (orientation) {
            ButtonGroupOrientation.Horizontal -> modifier
                .fillMaxHeight()
            ButtonGroupOrientation.Vertical -> modifier
                .fillMaxWidth()
                .height(TwDimensions.heightHToken9)
        }

        Box(
            modifier = blockModifier
                .clip(shape)
                .groupBorder(
                    color = Theme[ColorProps][ColorTokens.border],
                    width = borderWidth,
                    shape = shape,
                    suppressLeft = !vertical && index != 0,
                    suppressTop = vertical && index != 0,
                )
                .background(Theme[ColorProps][ColorTokens.muted]),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                modifier = Modifier.padding(horizontal = TwDimensions.paddingPxToken4), // px-4
                horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken2),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BasicText(
                    text = text,
                    style = TypographyStyles.textSmMedium.copy(
                        color = Theme[ColorProps][ColorTokens.foreground],
                    ),
                )
            }
        }
    }

    @Composable
    override fun Separator(modifier: Modifier) {
        // The separator is also a group child: it takes an order slot.
        remember { order.nextIndex() }
        val lineColor = Theme[ColorProps][ColorTokens.input]
        val borderWidth = Theme[DimProps][DimTokens.borderWidth]

        Box(
            modifier = modifier.then(
                if (orientation == ButtonGroupOrientation.Vertical) {
                    // Vertical group: a horizontal line spanning the full width.
                    Modifier
                        .fillMaxWidth()
                        .height(borderWidth)
                } else {
                    // Horizontal group: a vertical line spanning the full height
                    // (IntrinsicSize.Min on the container gives bounded height).
                    Modifier
                        .fillMaxHeight()
                        .width(borderWidth)
                },
            )
                .background(lineColor),
        )
    }
}

/**
 * Shape of a group item: outer corners (left on the first item / right on the
 * last) keep their rounding, inner corners are squared off.
 */
private fun joinedShape(
    isFirst: Boolean,
    isLast: Boolean,
    radius: Dp,
    vertical: Boolean,
): Shape {
    if (isFirst && isLast) {
        return RoundedCornerShape(radius)
    }
    return if (!vertical) {
        RoundedCornerShape(
            topStart = if (isFirst) radius else 0.dp,
            topEnd = if (isLast) radius else 0.dp,
            bottomEnd = if (isLast) radius else 0.dp,
            bottomStart = if (isFirst) radius else 0.dp,
        )
    } else {
        RoundedCornerShape(
            topStart = if (isFirst) radius else 0.dp,
            topEnd = if (isFirst) radius else 0.dp,
            bottomEnd = if (isLast) radius else 0.dp,
            bottomStart = if (isLast) radius else 0.dp,
        )
    }
}

/**
 * Border of a group item: the full outline of [shape] (including the rounded
 * arcs), with the suppressed side (left/top) clipped away by a strip of [width]
 * thickness — so neighboring borders collapse into a single line, like
 * `border-l-0` / `border-t-0` in the reference.
 */
private fun Modifier.groupBorder(
    color: Color,
    width: Dp,
    shape: Shape,
    suppressLeft: Boolean,
    suppressTop: Boolean,
): Modifier = drawBehind {
    val w = width.toPx()
    if (w <= 0f || color == Color.Transparent) {
        return@drawBehind
    }
    val outline = shape.createOutline(
        size = size,
        layoutDirection = layoutDirection,
        density = this,
    )
    val stroke = Stroke(width = w)
    when {
        suppressLeft -> clipRect(left = w) {
            drawOutline(outline = outline, color = color, style = stroke)
        }
        suppressTop -> clipRect(top = w) {
            drawOutline(outline = outline, color = color, style = stroke)
        }
        else -> drawOutline(outline = outline, color = color, style = stroke)
    }
}
