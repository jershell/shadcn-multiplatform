package com.github.jershell.shadcn.components.toggle.group

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.UnstyledButton
import com.composeunstyled.focusRing
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.toggle.LocalToggleContentColor
import com.github.jershell.shadcn.components.toggle.LocalToggleIconSize
import com.github.jershell.shadcn.components.toggle.LocalToggleTextStyle
import com.github.jershell.shadcn.components.toggle.ToggleSize
import com.github.jershell.shadcn.components.toggle.ToggleVariant
import com.github.jershell.shadcn.components.toggle.resolveToggleColors
import com.github.jershell.shadcn.components.toggle.resolveToggleFocusRingColor
import com.github.jershell.shadcn.components.toggle.spec
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.Effects
import com.github.jershell.shadcn.theme.TwDimensions

/**
 * Scope of the [ToggleGroup] content. Items are declared in order; the group manages
 * the selection and the joined styling.
 */
interface ToggleGroupScope {

    /**
     * A toggle inside the group. Use [ToggleText][com.github.jershell.shadcn.components.toggle.ToggleText]
     * and [ToggleIcon][com.github.jershell.shadcn.components.toggle.ToggleIcon] inside
     * [content] for automatic coloring.
     *
     * @param value Unique value of the item within the group.
     * @param enabled Whether the item is interactive.
     * @param content Item content, laid out horizontally.
     */
    @Composable
    fun Item(
        value: String,
        enabled: Boolean = true,
        content: @Composable () -> Unit,
    )
}

/**
 * Tracks the item order inside a toggle group: which composed item is the first one.
 */
internal class ToggleGroupItemOrder {
    private var firstSlotTaken = false

    /**
     * Returns `true` for the first item composed after the last [reset].
     */
    fun takeFirstSlot(): Boolean {
        val isFirst = !firstSlotTaken
        firstSlotTaken = true
        return isFirst
    }

    fun reset() {
        firstSlotTaken = false
    }
}

/**
 * A group of joined toggle buttons with managed selection, styled after the
 * shadcn/ui Toggle Group (Radix `type="single"`).
 *
 * Clicking an item selects it; clicking the selected item deselects it
 * ([value] becomes `null`).
 *
 * With the default [spacing] of `0.dp` the items are joined: no individual rounding
 * or shadow, adjacent outline borders collapsed, the container clipped to `rounded-md`
 * (with the `shadow-xs` for the outline variant). A positive [spacing] separates the
 * items into standalone toggles with the given gap.
 *
 * @param value Selected item value, or `null` when nothing is selected.
 * @param onValueChange Called with the newly selected value or `null` on deselect.
 * @param modifier Modifier applied to the group container.
 * @param variant Visual style shared by all items.
 * @param size Size shared by all items.
 * @param spacing Gap between items; `0.dp` joins them into a single control.
 * @param content Items declared with [ToggleGroupScope.Item].
 */
@Composable
fun ToggleGroup(
    value: String?,
    onValueChange: (String?) -> Unit,
    modifier: Modifier = Modifier,
    variant: ToggleVariant = ToggleVariant.Default,
    size: ToggleSize = ToggleSize.Default,
    spacing: Dp = 0.dp,
    content: @Composable ToggleGroupScope.() -> Unit,
) {
    ToggleGroupContainer(
        modifier = modifier,
        variant = variant,
        size = size,
        spacing = spacing,
        isChecked = { it == value },
        onItemClick = { itemValue ->
            onValueChange(if (itemValue == value) null else itemValue)
        },
        content = content,
    )
}

/**
 * A group of joined toggle buttons with managed multi-selection, styled after the
 * shadcn/ui Toggle Group (Radix `type="multiple"`).
 *
 * Clicking an item toggles it in or out of [values].
 *
 * @param values Set of selected item values.
 * @param onValuesChange Called with the updated set of selected values.
 * @param modifier Modifier applied to the group container.
 * @param variant Visual style shared by all items.
 * @param size Size shared by all items.
 * @param spacing Gap between items; `0.dp` joins them into a single control.
 * @param content Items declared with [ToggleGroupScope.Item].
 */
@Composable
fun ToggleGroupMultiple(
    values: Set<String>,
    onValuesChange: (Set<String>) -> Unit,
    modifier: Modifier = Modifier,
    variant: ToggleVariant = ToggleVariant.Default,
    size: ToggleSize = ToggleSize.Default,
    spacing: Dp = 0.dp,
    content: @Composable ToggleGroupScope.() -> Unit,
) {
    ToggleGroupContainer(
        modifier = modifier,
        variant = variant,
        size = size,
        spacing = spacing,
        isChecked = { it in values },
        onItemClick = { itemValue ->
            onValuesChange(
                if (itemValue in values) values - itemValue else values + itemValue,
            )
        },
        content = content,
    )
}

@Composable
private fun ToggleGroupContainer(
    modifier: Modifier,
    variant: ToggleVariant,
    size: ToggleSize,
    spacing: Dp,
    isChecked: (String) -> Boolean,
    onItemClick: (String) -> Unit,
    content: @Composable ToggleGroupScope.() -> Unit,
) {
    val joined = spacing == 0.dp
    val order = remember { ToggleGroupItemOrder() }
    // Reset before composing the content: the first-item slot is pinned in the
    // items' remember, so recompositions do not break the order.
    order.reset()
    val shape = RoundedCornerShape(Theme[DimProps][DimTokens.radiusMd])
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]

    val containerModifier = modifier
        .then(
            if (joined) {
                // Container shadow-xs (outline) dropped: compose shadows render
                // on desktop as a double glow (see BACKLOG.md)
                Modifier.clip(shape)
            } else {
                Modifier
            },
        )

    // The group outline with its rounded arcs is drawn on top of the items: the
    // straight border lines of the items get clipped by the container clip and
    // the corners would otherwise stay empty.
    Box(modifier = containerModifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val scope = ToggleGroupScopeImpl(
                holder = order,
                variant = variant,
                size = size,
                joined = joined,
                isChecked = isChecked,
                onItemClick = onItemClick,
            )
            scope.content()
        }
        if (joined && variant == ToggleVariant.Outline) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .border(borderWidth, Theme[ColorProps][ColorTokens.input], shape),
            )
        }
    }
}

internal class ToggleGroupScopeImpl(
    private val holder: ToggleGroupItemOrder,
    private val variant: ToggleVariant,
    private val size: ToggleSize,
    private val joined: Boolean,
    private val isChecked: (String) -> Boolean,
    private val onItemClick: (String) -> Unit,
) : ToggleGroupScope {

    @Composable
    override fun Item(
        value: String,
        enabled: Boolean,
        content: @Composable () -> Unit,
    ) {
        // Positional remember: the first-item flag is pinned to the slot;
        // recompositions do not change it.
        val isFirst = remember { holder.takeFirstSlot() }

        ToggleGroupItem(
            checked = isChecked(value),
            enabled = enabled,
            isFirst = isFirst,
            joined = joined,
            variant = variant,
            size = size,
            onItemClick = { onItemClick(value) },
            content = content,
        )
    }
}

@Composable
private fun ToggleGroupItem(
    checked: Boolean,
    enabled: Boolean,
    isFirst: Boolean,
    joined: Boolean,
    variant: ToggleVariant,
    size: ToggleSize,
    onItemClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val sizeSpec = size.spec()
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val colors = resolveToggleColors(
        variant = variant,
        isOn = checked,
        isHovered = isHovered,
        isPressed = isPressed,
        isInvalid = false,
    )
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val focusRingColor = resolveToggleFocusRingColor(isInvalid = false)
    val focusRingWidth = Effects.boxShadowFocusRing.spread

    // Group item: px-3 (overrides the size padding), min-w-0 (no minimum width).
    val shape: Shape = if (joined) {
        RectangleShape
    } else {
        RoundedCornerShape(Theme[DimProps][DimTokens.radiusMd])
    }

    val containerModifier = Modifier
        .height(sizeSpec.height)
        .clip(shape)
        .then(
            if (joined) {
                // Shared border: top/bottom/right always; left only on the first item.
                Modifier.groupBorder(
                    color = colors.border ?: Color.Transparent,
                    width = borderWidth,
                    showLeft = isFirst,
                )
            } else {
                Modifier.then(
                    if (colors.border != null) {
                        Modifier.border(borderWidth, colors.border, shape)
                    } else {
                        Modifier
                    },
                )
            },
        )
        .background(colors.container, shape)
        .then(
            if (!joined && colors.showShadow) {
                Modifier.shadow(
                    elevation = Effects.boxShadowShadowXs.radius,
                    shape = shape,
                    clip = false,
                )
            } else {
                Modifier
            },
        )
        .focusRing(
            interactionSource = interactionSource,
            width = focusRingWidth,
            color = focusRingColor,
            shape = shape,
        )

    UnstyledButton(
        onClick = onItemClick,
        enabled = enabled,
        interactionSource = interactionSource,
        indication = null,
        role = Role.Button,
        modifier = containerModifier,
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(
            LocalToggleContentColor provides colors.content,
            LocalToggleIconSize provides sizeSpec.iconSize,
            LocalToggleTextStyle provides sizeSpec.textStyle,
        ) {
            Row(
                modifier = Modifier.padding(horizontal = TwDimensions.paddingPxToken3), // px-3
                horizontalArrangement = Arrangement.spacedBy(
                    space = sizeSpec.gap,
                    alignment = Alignment.CenterHorizontally,
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                content()
            }
        }
    }
}

private fun Modifier.groupBorder(color: Color, width: Dp, showLeft: Boolean): Modifier =
    drawBehind {
        val w = width.toPx()
        if (w <= 0f || color == Color.Transparent) {
            return@drawBehind
        }
        // top
        drawLine(color, Offset(0f, w / 2), Offset(size.width, w / 2), strokeWidth = w)
        // bottom
        drawLine(
            color,
            Offset(0f, size.height - w / 2),
            Offset(size.width, size.height - w / 2),
            strokeWidth = w,
        )
        // right
        drawLine(
            color,
            Offset(size.width - w / 2, 0f),
            Offset(size.width - w / 2, size.height),
            strokeWidth = w,
        )
        if (showLeft) {
            drawLine(color, Offset(w / 2, 0f), Offset(w / 2, size.height), strokeWidth = w)
        }
    }
