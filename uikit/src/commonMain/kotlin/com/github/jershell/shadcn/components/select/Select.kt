package com.github.jershell.shadcn.components.select

import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.Lucide
import com.composeunstyled.AnchorAlignment
import com.composeunstyled.AnchorSide
import com.composeunstyled.DropdownMenuPanel
import com.composeunstyled.DropdownMenuPanelScope
import com.composeunstyled.DropdownMenuScope
import com.composeunstyled.MenuItem
import com.composeunstyled.UnstyledButton
import com.composeunstyled.UnstyledDropdownMenu
import com.composeunstyled.UnstyledHorizontalSeparator
import com.composeunstyled.UnstyledIcon
import com.composeunstyled.focusRing
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.models.DataItem
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.Effects
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles
import com.github.jershell.shadcn.generated.resources.Res
import com.github.jershell.shadcn.generated.resources.select_placeholder

internal class SelectState<T>(
    val items: List<DataItem<T>>,
    val selected: Set<Int>,
    val multiple: Boolean,
    val enabled: Boolean,
    val onSelectedChange: (Set<Int>) -> Unit,
    val open: MutableState<Boolean>,
) {
    fun isSelected(item: DataItem<T>): Boolean = item.key in selected

    fun toggle(item: DataItem<T>) {
        if (!enabled || !item.enabled) return

        val current = selected
        val new = if (multiple) {
            if (item.key in current) current - item.key else current + item.key
        } else {
            setOf(item.key)
        }
        onSelectedChange(new)

        if (!multiple) {
            open.value = false
        }
    }

    fun selectedTitle(): String? = items.firstOrNull { it.key in selected }?.title

    fun selectedTitles(): List<String> = items.filter { it.key in selected }.map { it.title }
}

internal val LocalSelectState = compositionLocalOf<SelectState<*>?> { null }

/**
 * A styled select component built on top of [UnstyledDropdownMenu].
 *
 * Items are provided as [DataItem] values. The selection is tracked by the item's [DataItem.key]
 * and exposed through [selected] / [onSelectedChange].
 *
 * @param items The selectable options.
 * @param selected Set of selected item keys.
 * @param onSelectedChange Called when the selection changes.
 * @param placeholder Text shown when nothing is selected.
 * @param multiple Whether multiple items can be selected.
 * @param enabled Whether the select trigger is interactive.
 * @param panelModifier Modifier applied to the popup panel.
 * @param itemContent Custom renderer for a popup item. Receives the item, its selection state and
 *   a click handler that should be wired to the rendered row.
 */
@Composable
fun <T> Select(
    items: List<DataItem<T>>,
    selected: Set<Int>,
    onSelectedChange: (Set<Int>) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = stringResource(Res.string.select_placeholder),
    multiple: Boolean = false,
    enabled: Boolean = true,
    panelModifier: Modifier = Modifier,
    itemContent: @Composable DropdownMenuPanelScope.(
        item: DataItem<T>,
        selected: Boolean,
        onClick: () -> Unit,
    ) -> Unit = { item, isSelected, onClick ->
        DefaultSelectItem(item, isSelected, onClick)
    },
) {
    var open by remember { mutableStateOf(false) }
    val state = remember(items, selected, multiple, enabled, onSelectedChange) {
        SelectState(
            items = items,
            selected = selected,
            multiple = multiple,
            enabled = enabled,
            onSelectedChange = onSelectedChange,
            open = mutableStateOf(open),
        )
    }
    state.open.value = open

    CompositionLocalProvider(LocalSelectState provides state) {
        UnstyledDropdownMenu(
            expanded = open,
            onExpandedChange = { open = it },
            modifier = modifier,
            side = AnchorSide.Bottom,
            alignment = AnchorAlignment.Start,
            sideOffset = TwDimensions.gapGapToken1,
            panel = {
                SelectPanel(modifier = panelModifier) {
                    items.forEach { item ->
                        val isSelected = state.isSelected(item)
                        itemContent(item, isSelected, { state.toggle(item) })
                    }
                }
            },
            anchor = {
                SelectTrigger(
                    onClick = { open = !open },
                    enabled = enabled,
                    placeholder = placeholder,
                )
            },
        )
    }
}

@Composable
private fun DropdownMenuScope.SelectPanel(
    modifier: Modifier = Modifier,
    content: @Composable DropdownMenuPanelScope.() -> Unit,
) {
    val colors = resolveSelectPanelColors()
    val radius = Theme[DimProps][DimTokens.radiusMd]
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val shape = RoundedCornerShape(radius)

    DropdownMenuPanel(
        modifier = modifier
            .width(240.dp)
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
            .padding(vertical = TwDimensions.paddingPxToken1),
        content = content,
    )
}

@Composable
private fun <T> DropdownMenuPanelScope.DefaultSelectItem(
    item: DataItem<T>,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val state = LocalSelectState.current
    SelectItem(
        selected = selected,
        onClick = onClick,
        enabled = item.enabled,
        closeOnClick = !(state?.multiple ?: false),
    ) {
        SelectItemText(item.title)
        if (selected) {
            SelectItemIndicator()
        }
    }
}

/**
 * The trigger button that opens the select popup. Shows the selected value or a placeholder.
 */
@Composable
fun SelectTrigger(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String = stringResource(Res.string.select_placeholder),
    content: @Composable RowScope.() -> Unit = {
        SelectValue(
            placeholder = placeholder,
            modifier = Modifier.weight(1f),
        )
        SelectIcon()
    },
) {
    val colors = resolveSelectTriggerColors()
    val radius = Theme[DimProps][DimTokens.radiusMd]
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val shape = RoundedCornerShape(radius)
    val interactionSource = remember { MutableInteractionSource() }
    val focusRingColor = Theme[ColorProps][ColorTokens.ring]
    val focusRingWidth = Effects.boxShadowFocusRing.spread

    UnstyledButton(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        indication = null,
        role = Role.Button,
        modifier = modifier
            .alpha(if (enabled) 1f else 0.5f)
            .fillMaxWidth()
            .height(TwDimensions.heightHToken9)
            .clip(shape)
            .border(borderWidth, colors.border, shape)
            .background(colors.background)
            .focusRing(
                interactionSource = interactionSource,
                width = focusRingWidth,
                color = focusRingColor,
                shape = shape,
            ),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = TwDimensions.paddingPxToken3)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken2),
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

/**
 * Displays the current selected value or the [placeholder] when nothing is selected.
 */
@Composable
fun SelectValue(
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    val state = LocalSelectState.current
    val colors = resolveSelectTriggerColors()
    val text = when {
        state == null || state.selected.isEmpty() -> placeholder
        state.multiple -> state.selectedTitles().joinToString(", ")
        else -> state.selectedTitle() ?: placeholder
    }
    val color = if (state?.selected?.isEmpty() != false) colors.placeholder else colors.content

    BasicText(
        text = text,
        modifier = modifier,
        style = TypographyStyles.textSmRegular.copy(color = color),
        maxLines = 1,
    )
}

/**
 * The chevron icon displayed in the trigger.
 */
@Composable
fun SelectIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector = Lucide.ChevronDown,
    contentDescription: String? = null,
) {
    val colors = resolveSelectTriggerColors()
    UnstyledIcon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier.size(TwDimensions.heightHToken4),
        tint = colors.content,
    )
}

/**
 * A selectable row inside the popup. Must be rendered inside [Select] or [SelectPanel].
 */
@Composable
fun DropdownMenuPanelScope.SelectItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    closeOnClick: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = resolveSelectItemColors()
    val radius = Theme[DimProps][DimTokens.radiusSm]
    val shape = RoundedCornerShape(radius)
    val interactionSourceOrDefault = interactionSource ?: remember { MutableInteractionSource() }
    val isHovered by interactionSourceOrDefault.collectIsHoveredAsState()

    val background = when {
        selected -> colors.selectedBackground
        isHovered -> colors.hoverBackground
        else -> Color.Transparent
    }
    val contentColor = if (selected) colors.selectedContent else colors.content

    CompositionLocalProvider(LocalSelectItemContentColor provides contentColor) {
        MenuItem(
            onClick = onClick,
            enabled = enabled,
            closeOnClick = closeOnClick,
            interactionSource = interactionSourceOrDefault,
            indication = indication,
            modifier = modifier
                .alpha(if (enabled) 1f else 0.5f)
                .padding(horizontal = TwDimensions.paddingPxToken1, vertical = TwDimensions.paddingPPx)
                .fillMaxWidth()
                .clip(shape)
                .background(background)
                .padding(
                    horizontal = TwDimensions.paddingPxToken2,
                    vertical = TwDimensions.paddingPxToken1,
                ),
                //.defaultMinSize(minHeight = TwDimensions.heightHToken8),
            content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken2),
                    verticalAlignment = Alignment.CenterVertically,
                    content = content,
                )
            },
        )
    }
}

internal val LocalSelectItemContentColor = compositionLocalOf { Color.Unspecified }

/**
 * Text label of a select item.
 */
@Composable
fun RowScope.SelectItemText(
    text: String,
    modifier: Modifier = Modifier,
) {
    val color = LocalSelectItemContentColor.current
    BasicText(
        text = text,
        modifier = modifier.weight(1f),
        style = TypographyStyles.textSmRegular.copy(
            color = if (color != Color.Unspecified) color else resolveSelectItemColors().content,
        ),
    )
}

/**
 * Checkmark indicator shown next to selected items.
 */
@Composable
fun SelectItemIndicator(
    modifier: Modifier = Modifier,
) {
    val color = LocalSelectItemContentColor.current
    UnstyledIcon(
        imageVector = Lucide.Check,
        contentDescription = null,
        modifier = modifier.size(TwDimensions.heightHToken4),
        tint = if (color != Color.Unspecified) color else resolveSelectItemColors().selectedContent,
    )
}

/**
 * Horizontal separator between groups of items.
 */
@Composable
fun SelectSeparator(
    modifier: Modifier = Modifier,
) {
    val color = Theme[ColorProps][ColorTokens.border]
    UnstyledHorizontalSeparator(
        modifier = modifier.padding(vertical = TwDimensions.paddingPxToken1),
        color = color,
    )
}

/**
 * Group label rendered inside the popup.
 */
@Composable
fun SelectLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    val color = Theme[ColorProps][ColorTokens.mutedForeground]
    BasicText(
        text = text,
        modifier = modifier.padding(
            horizontal = TwDimensions.paddingPxToken2,
            vertical = TwDimensions.paddingPxToken1,
        ),
        style = TypographyStyles.textXsMedium.copy(color = color),
    )
}
