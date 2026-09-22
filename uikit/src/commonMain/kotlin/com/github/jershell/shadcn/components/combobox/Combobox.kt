package com.github.jershell.shadcn.components.combobox

import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.X
import com.composeunstyled.AnchorAlignment
import com.composeunstyled.AnchorSide
import com.composeunstyled.TextInput
import com.composeunstyled.UnstyledButton
import com.composeunstyled.UnstyledIcon
import com.composeunstyled.UnstyledHorizontalSeparator
import com.composeunstyled.UnstyledTextField
import com.composeunstyled.focusRing
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.badge.Badge
import com.github.jershell.shadcn.components.badge.BadgeVariant
import com.github.jershell.shadcn.components.icon.toShadcnIcon
import com.github.jershell.shadcn.components.popover.Popover
import com.github.jershell.shadcn.components.popover.PopoverScope
import com.github.jershell.shadcn.models.DataItem
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.Effects
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles
import com.github.jershell.shadcn.generated.resources.Res
import com.github.jershell.shadcn.generated.resources.combobox_empty
import com.github.jershell.shadcn.generated.resources.combobox_placeholder

internal class ComboboxState<T>(
    val items: List<DataItem<T>>,
    val selected: Set<Int>,
    val multiple: Boolean,
    val enabled: Boolean,
    val onSelectedChange: (Set<Int>) -> Unit,
    val open: MutableState<Boolean>,
    val queryState: TextFieldState,
    val focusRequester: FocusRequester,
    val filter: (DataItem<T>, String) -> Boolean,
) {
    var activeIndex by mutableStateOf<Int?>(null)

    internal var suppressQueryReopen = false

    fun isSelected(item: DataItem<T>): Boolean = item.key in selected

    fun toggle(item: DataItem<T>) {
        if (!enabled || !item.enabled) return

        val new = if (multiple) {
            if (item.key in selected) selected - item.key else selected + item.key
        } else {
            setOf(item.key)
        }
        onSelectedChange(new)

        if (!multiple) {
            open.value = false
            setQuerySilently(item.title)
        } else {
            setQuerySilently("")
            runCatching { focusRequester.requestFocus() }
        }
        activeIndex = null
    }

    fun deselect(item: DataItem<T>) {
        if (!enabled || item.key !in selected) return
        onSelectedChange(selected - item.key)
    }

    fun clear() {
        onSelectedChange(emptySet())
        setQuerySilently("")
    }

    fun selectedTitle(): String? = items.firstOrNull { it.key in selected }?.title

    /**
     * Selected items in selection order (the iteration order of [selected], which [toggle]
     * maintains by appending newly selected keys). Rendering follows this order so chips
     * appear in the order the user picked them, not in the [items] list order.
     */
    fun selectedItems(): List<DataItem<T>> = selected.mapNotNull { key ->
        items.firstOrNull { it.key == key }
    }

    val query: String get() = queryState.text.toString()

    fun filteredItems(): List<DataItem<T>> {
        return items.filter { item ->
            item.enabled && filter(item, query)
        }
    }

    fun activeItemKey(): Int? = filteredItems().getOrNull(activeIndex ?: -1)?.key

    internal fun setQuerySilently(text: String) {
        suppressQueryReopen = true
        queryState.setTextAndPlaceCursorAtEnd(text)
    }
}

internal val LocalComboboxState = compositionLocalOf<ComboboxState<*>?> { null }

internal val LocalComboboxActiveKey = staticCompositionLocalOf<Int?> { null }

internal val LocalComboboxItemContentColor = compositionLocalOf { Color.Unspecified }

/**
 * A searchable combo box styled after the shadcn/ui Combobox.
 *
 * The trigger is an editable text input with a chevron-down icon: the input value is the
 * selected item title (for single selection) or an inline search field next to chips (for
 * multi selection). The popup panel contains only the filtered list of items and is rendered
 * by a non-modal [Popover], so the trigger input keeps keyboard focus and can be typed into
 * while the popup is open.
 *
 * Keyboard: typing filters the list, Down/Up highlights an item, Enter selects it or closes
 * the popup, Escape closes the popup. In multi-select mode, Backspace on an empty query
 * removes the last chip.
 *
 * The application root must be wrapped in [com.composeunstyled.ModalHost] so the popover
 * panel can be portaled to a window-sized overlay.
 *
 * @param items The selectable options.
 * @param selected Set of selected item keys.
 * @param onSelectedChange Called when the selection changes.
 * @param placeholder Placeholder shown in the trigger when nothing is selected.
 * @param emptyText Message shown when the search query matches no items.
 * @param multiple Whether multiple items can be selected. Chips are rendered in the order
 *   the items were selected.
 * @param enabled Whether the trigger is interactive.
 * @param showClear Whether to show a clear button on the trigger.
 * @param panelModifier Modifier applied to the popup panel.
 * @param itemFilter Filter used when searching items. The default matches against [DataItem.title].
 * @param chipContent Custom renderer for a selected chip in multi-select mode.
 * @param itemContent Custom renderer for a popup item. Receives the item, its selection state and
 *   a click handler that should be wired to the rendered row.
 */
@Composable
fun <T> Combobox(
    items: List<DataItem<T>>,
    selected: Set<Int>,
    onSelectedChange: (Set<Int>) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = stringResource(Res.string.combobox_placeholder),
    emptyText: String = stringResource(Res.string.combobox_empty),
    multiple: Boolean = false,
    enabled: Boolean = true,
    showClear: Boolean = false,
    panelModifier: Modifier = Modifier,
    itemFilter: (DataItem<T>, String) -> Boolean = { item, query ->
        item.title.contains(query, ignoreCase = true)
    },
    chipContent: @Composable (item: DataItem<T>, onDismiss: () -> Unit) -> Unit = { item, onDismiss ->
        ComboboxChip(item, onDismiss)
    },
    itemContent: @Composable PopoverScope.(
        item: DataItem<T>,
        selected: Boolean,
        onClick: () -> Unit,
    ) -> Unit = { item, isSelected, onClick ->
        DefaultComboboxItem(item, isSelected, onClick)
    },
) {
    val open = remember { mutableStateOf(false) }
    val queryState = rememberTextFieldState()
    val focusRequester = remember { FocusRequester() }
    val state = remember(items, selected, multiple, enabled, onSelectedChange, itemFilter, open, queryState, focusRequester) {
        ComboboxState(
            items = items,
            selected = selected,
            multiple = multiple,
            enabled = enabled,
            onSelectedChange = onSelectedChange,
            open = open,
            queryState = queryState,
            focusRequester = focusRequester,
            filter = itemFilter,
        )
    }

    LaunchedEffect(open.value) {
        if (open.value) {
            state.activeIndex = null
        }
    }

    LaunchedEffect(selected, items) {
        if (!multiple) {
            state.setQuerySilently(state.selectedTitle() ?: "")
        }
    }

    LaunchedEffect(state) {
        var first = true
        snapshotFlow { queryState.text.toString() }
            .collect { _ ->
                state.activeIndex = null
                if (state.suppressQueryReopen) {
                    state.suppressQueryReopen = false
                } else if (!first && !open.value && state.enabled) {
                    // The trigger input may still hold focus after the popup was closed
                    // (e.g. by Escape), so typing must reopen it.
                    open.value = true
                }
                first = false
            }
    }

    CompositionLocalProvider(LocalComboboxState provides state) {
        Popover(
            expanded = open.value && enabled,
            onExpandedChange = { open.value = it },
            modifier = modifier,
            side = AnchorSide.Bottom,
            alignment = AnchorAlignment.Start,
            sideOffset = TwDimensions.gapGapToken1,
            anchor = {
                if (multiple) {
                    ComboboxChips(
                        state = state,
                        placeholder = placeholder,
                        showClear = showClear,
                        chipContent = chipContent,
                    )
                } else {
                    ComboboxInput(
                        state = state,
                        placeholder = placeholder,
                        showClear = showClear,
                    )
                }
            },
        ) {
            ComboboxPanel(modifier = panelModifier) {
                CompositionLocalProvider(LocalComboboxActiveKey provides state.activeItemKey()) {
                    ComboboxList(
                        items = state.filteredItems(),
                        state = state,
                        emptyText = emptyText,
                        itemContent = itemContent,
                    )
                }
            }
        }
    }
}

/**
 * A removable chip used for multi-select combobox values.
 */
@Composable
fun <T> ComboboxChip(
    item: DataItem<T>,
    onDismiss: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    text: String = item.title,
    enabled: Boolean = item.enabled,
) {
    @Suppress("UNCHECKED_CAST")
    val state = LocalComboboxState.current as? ComboboxState<T>

    val onDismissClick: () -> Unit = onDismiss ?: {
        if (state != null) {
            state.deselect(item)
        }
    }

    Badge(
        text = text,
        modifier = modifier,
        variant = BadgeVariant.Chip,
        trailingIcon = Lucide.X.toShadcnIcon(),
        onTrailingIconClick = onDismissClick,
        trailingIconClickEnabled = enabled,
    )
}

private fun <T> comboboxInputKeyHandler(state: ComboboxState<T>): (KeyEvent) -> Boolean {
    return { event ->
        if (event.type != KeyEventType.KeyDown) {
            false
        } else {
            when (event.key) {
                Key.Escape -> {
                    if (state.open.value) {
                        state.open.value = false
                        true
                    } else {
                        false
                    }
                }

                Key.Enter, Key.NumPadEnter -> {
                    if (!state.open.value) {
                        false
                    } else {
                        val activeItem = state.filteredItems().getOrNull(state.activeIndex ?: -1)
                        if (activeItem != null) {
                            state.toggle(activeItem)
                            state.activeIndex = null
                        } else {
                            state.open.value = false
                        }
                        true
                    }
                }

                Key.DirectionDown -> {
                    if (!state.open.value) {
                        state.open.value = true
                    }
                    val items = state.filteredItems()
                    if (items.isNotEmpty()) {
                        state.activeIndex = ((state.activeIndex ?: -1) + 1).coerceAtMost(items.lastIndex)
                    }
                    true
                }

                Key.DirectionUp -> {
                    if (state.open.value) {
                        val items = state.filteredItems()
                        if (items.isNotEmpty()) {
                            state.activeIndex = ((state.activeIndex ?: items.size) - 1).coerceAtLeast(0)
                        }
                        true
                    } else {
                        false
                    }
                }

                Key.Backspace -> {
                    // In multi-select mode, Backspace on an empty query removes the last chip.
                    if (!state.multiple || state.selected.isEmpty() || state.query.isNotEmpty()) {
                        false
                    } else {
                        val lastItem = state.selectedItems().lastOrNull()
                        if (lastItem != null) {
                            state.deselect(lastItem)
                        } else {
                            state.onSelectedChange(state.selected - state.selected.last())
                        }
                        true
                    }
                }

                else -> false
            }
        }
    }
}

@Composable
private fun <T> ComboboxInput(
    state: ComboboxState<T>,
    placeholder: String,
    showClear: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = resolveComboboxInputColors()
    val interactionSource = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(Theme[DimProps][DimTokens.radiusMd])
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val focusRingColor = Theme[ColorProps][ColorTokens.ring]
    val focusRingWidth = Effects.boxShadowFocusRing.spread

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            if (interaction is PressInteraction.Press && !state.open.value) {
                state.open.value = true
            }
        }
    }

    UnstyledTextField(
        state = state.queryState,
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged {
                if (it.isFocused) state.open.value = true
            }
            .onPreviewKeyEvent(comboboxInputKeyHandler(state)),
        enabled = state.enabled,
        textStyle = TypographyStyles.textSmRegular,
        textColor = colors.content,
        cursorBrush = SolidColor(colors.content),
        lineLimits = TextFieldLineLimits.SingleLine,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        onKeyboardAction = {
            val activeItem = state.filteredItems().getOrNull(state.activeIndex ?: -1)
            if (activeItem != null) {
                state.toggle(activeItem)
                state.activeIndex = null
            } else {
                state.open.value = false
            }
        },
        interactionSource = interactionSource,
    ) {
        Row(
            modifier = Modifier
                .alpha(if (state.enabled) 1f else 0.5f)
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
                )
                .padding(horizontal = TwDimensions.paddingPxToken3),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken2),
        ) {
            TextInput(
                modifier = Modifier.weight(1f),
                placeholder = {
                    BasicText(
                        text = placeholder,
                        style = TypographyStyles.textSmRegular.copy(color = colors.placeholder),
                    )
                },
            )
            if (showClear && (state.selected.isNotEmpty() || state.query.isNotEmpty())) {
                UnstyledButton(
                    onClick = { state.clear() },
                    enabled = state.enabled,
                    modifier = Modifier.size(TwDimensions.heightHToken4),
                    indication = null,
                ) {
                    UnstyledIcon(
                        imageVector = Lucide.X,
                        contentDescription = null,
                        tint = colors.content,
                    )
                }
            }
            UnstyledButton(
                onClick = { state.open.value = !state.open.value },
                enabled = state.enabled,
                modifier = Modifier.size(TwDimensions.heightHToken4),
                indication = null,
            ) {
                UnstyledIcon(
                    imageVector = Lucide.ChevronDown,
                    contentDescription = null,
                    tint = colors.content,
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun <T> ComboboxChips(
    state: ComboboxState<T>,
    placeholder: String,
    showClear: Boolean,
    chipContent: @Composable (item: DataItem<T>, onDismiss: () -> Unit) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = resolveComboboxInputColors()
    val interactionSource = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(Theme[DimProps][DimTokens.radiusMd])
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val focusRingColor = Theme[ColorProps][ColorTokens.ring]
    val focusRingWidth = Effects.boxShadowFocusRing.spread
    val selectedItems = state.selectedItems()
    val showPlaceholder = selectedItems.isEmpty()

    Row(
        modifier = modifier
            .alpha(if (state.enabled) 1f else 0.5f)
            .fillMaxWidth()
            .defaultMinSize(minHeight = TwDimensions.heightHToken9)
            .clip(shape)
            .border(borderWidth, colors.border, shape)
            .background(colors.background)
            .focusRing(
                interactionSource = interactionSource,
                width = focusRingWidth,
                color = focusRingColor,
                shape = shape,
            )
            .clickable(
                enabled = state.enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    runCatching { state.focusRequester.requestFocus() }
                },
            )
            .padding(
                horizontal = TwDimensions.paddingPxToken3,
                vertical = TwDimensions.paddingPxToken1,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken2),
    ) {
        FlowRow(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken2),
            verticalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken1),
        ) {
            selectedItems.forEach { item ->
                chipContent(item, { state.deselect(item) })
            }
            ComboboxChipsInput(
                state = state,
                placeholder = if (showPlaceholder) placeholder else null,
                fillAvailableWidth = showPlaceholder,
                interactionSource = interactionSource,
                modifier = Modifier.widthIn(min = 64.dp),
            )
        }
        if (showClear && state.selected.isNotEmpty()) {
            UnstyledButton(
                onClick = { state.clear() },
                enabled = state.enabled,
                modifier = Modifier.size(TwDimensions.heightHToken4),
                indication = null,
            ) {
                UnstyledIcon(
                    imageVector = Lucide.X,
                    contentDescription = null,
                    tint = colors.content,
                )
            }
        }
        UnstyledButton(
            onClick = { state.open.value = !state.open.value },
            enabled = state.enabled,
            modifier = Modifier.size(TwDimensions.heightHToken4),
            indication = null,
        ) {
            UnstyledIcon(
                imageVector = Lucide.ChevronDown,
                contentDescription = null,
                tint = colors.content,
            )
        }
    }
}

@Composable
private fun <T> ComboboxChipsInput(
    state: ComboboxState<T>,
    placeholder: String?,
    fillAvailableWidth: Boolean,
    interactionSource: MutableInteractionSource,
    modifier: Modifier = Modifier,
) {
    val colors = resolveComboboxInputColors()

    UnstyledTextField(
        state = state.queryState,
        modifier = modifier
            .focusRequester(state.focusRequester)
            .onFocusChanged {
                if (it.isFocused) state.open.value = true
            }
            .onPreviewKeyEvent(comboboxInputKeyHandler(state)),
        enabled = state.enabled,
        textStyle = TypographyStyles.textSmRegular,
        textColor = colors.content,
        cursorBrush = SolidColor(colors.content),
        lineLimits = TextFieldLineLimits.SingleLine,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        onKeyboardAction = {
            val activeItem = state.filteredItems().getOrNull(state.activeIndex ?: -1)
            if (activeItem != null) {
                state.toggle(activeItem)
                state.activeIndex = null
            } else {
                state.open.value = false
            }
        },
        interactionSource = interactionSource,
    ) {
        TextInput(
            // Filling the available width is only safe while the input is the only item in the
            // FlowRow: FlowRow measures children with the full line width, so a filling input
            // would always wrap to the next line and grow the container as soon as a chip is
            // added. With chips present the input is content-sized (min width set by the
            // caller), so it stays on the first line while there is room for it.
            modifier = if (fillAvailableWidth) Modifier.fillMaxWidth() else Modifier,
            placeholder = placeholder?.let { placeholderText -> {
                BasicText(
                    text = placeholderText,
                    style = TypographyStyles.textSmRegular.copy(color = colors.placeholder),
                )
            } },
        )
    }
}

@Composable
private fun PopoverScope.ComboboxPanel(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val colors = resolveComboboxPanelColors()
    val radius = Theme[DimProps][DimTokens.radiusMd]
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val shape = RoundedCornerShape(radius)

    Box(
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
    ) {
        content()
    }
}

@Composable
private fun <T> PopoverScope.ComboboxList(
    items: List<DataItem<T>>,
    state: ComboboxState<T>,
    emptyText: String,
    itemContent: @Composable PopoverScope.(
        item: DataItem<T>,
        selected: Boolean,
        onClick: () -> Unit,
    ) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 256.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken1),
    ) {
        if (items.isEmpty()) {
            ComboboxEmpty(text = emptyText)
        } else {
            items.forEach { item ->
                val isSelected = state.isSelected(item)
                itemContent(item, isSelected, { state.toggle(item) })
            }
        }
    }
}

/**
 * Message shown when the current search query does not match any item.
 */
@Composable
fun PopoverScope.ComboboxEmpty(
    text: String,
    modifier: Modifier = Modifier,
) {
    val colors = resolveComboboxEmptyColors()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = TwDimensions.paddingPxToken3,
                vertical = TwDimensions.paddingPxToken2,
            ),
        contentAlignment = Alignment.Center,
    ) {
        BasicText(
            text = text,
            style = TypographyStyles.textSmRegular.copy(color = colors.content),
        )
    }
}

/**
 * Groups related combo box items with an optional label.
 */
@Composable
fun PopoverScope.ComboboxGroup(
    modifier: Modifier = Modifier,
    label: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken1),
    ) {
        if (label != null) {
            ComboboxLabel(text = label)
        }
        content()
    }
}

/**
 * Label rendered above a group of combo box items.
 */
@Composable
fun PopoverScope.ComboboxLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    val colors = resolveComboboxLabelColors()

    BasicText(
        text = text,
        modifier = modifier.padding(
            horizontal = TwDimensions.paddingPxToken2,
            vertical = TwDimensions.paddingPxToken1,
        ),
        style = TypographyStyles.textXsMedium.copy(color = colors.content),
    )
}

/**
 * Horizontal separator between groups of items.
 */
@Composable
fun PopoverScope.ComboboxSeparator(
    modifier: Modifier = Modifier,
) {
    val color = Theme[ColorProps][ColorTokens.border]

    UnstyledHorizontalSeparator(
        modifier = modifier.padding(vertical = TwDimensions.paddingPxToken1),
        color = color,
    )
}

/**
 * A selectable row inside the popup. Must be rendered inside [Combobox] or [ComboboxPanel].
 *
 * @param itemKey Key of the rendered item; used to highlight the keyboard-active row.
 */
@Composable
fun PopoverScope.ComboboxItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    itemKey: Int? = null,
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = resolveComboboxItemColors()
    val radius = Theme[DimProps][DimTokens.radiusSm]
    val shape = RoundedCornerShape(radius)
    val interactionSourceOrDefault = interactionSource ?: remember { MutableInteractionSource() }
    val isHovered by interactionSourceOrDefault.collectIsHoveredAsState()
    val isActive = itemKey != null && itemKey == LocalComboboxActiveKey.current
    val background = when {
        selected -> colors.selectedBackground
        isActive || isHovered -> colors.hoverBackground
        else -> Color.Transparent
    }
    val contentColor = if (selected) colors.selectedContent else colors.content

    CompositionLocalProvider(LocalComboboxItemContentColor provides contentColor) {
        Box(
            modifier = modifier
                .alpha(if (enabled) 1f else 0.5f)
                .padding(
                    horizontal = TwDimensions.paddingPxToken1,
                    vertical = TwDimensions.paddingPPx,
                )
                .fillMaxWidth()
                .clip(shape)
                .background(background)
                .clickable(
                    enabled = enabled,
                    onClick = onClick,
                    interactionSource = interactionSourceOrDefault,
                    indication = indication,
                )
                .padding(
                    horizontal = TwDimensions.paddingPxToken2,
                    vertical = TwDimensions.paddingPxToken1,
                ),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken2),
                verticalAlignment = Alignment.CenterVertically,
                content = content,
            )
        }
    }
}

/**
 * Text label of a combo box item.
 */
@Composable
fun RowScope.ComboboxItemText(
    text: String,
    modifier: Modifier = Modifier,
) {
    val color = LocalComboboxItemContentColor.current

    BasicText(
        text = text,
        modifier = modifier.weight(1f),
        style = TypographyStyles.textSmRegular.copy(
            color = if (color != Color.Unspecified) color else resolveComboboxItemColors().content,
        ),
    )
}

/**
 * Checkmark indicator shown next to selected items.
 */
@Composable
fun ComboboxItemIndicator(
    modifier: Modifier = Modifier,
) {
    val color = LocalComboboxItemContentColor.current

    UnstyledIcon(
        imageVector = Lucide.Check,
        contentDescription = null,
        modifier = modifier.size(TwDimensions.heightHToken4),
        tint = if (color != Color.Unspecified) color else resolveComboboxItemColors().selectedContent,
    )
}

@Composable
private fun <T> PopoverScope.DefaultComboboxItem(
    item: DataItem<T>,
    selected: Boolean,
    onClick: () -> Unit,
) {
    ComboboxItem(
        selected = selected,
        onClick = onClick,
        enabled = item.enabled,
        itemKey = item.key,
    ) {
        ComboboxItemText(item.title)
        if (selected) {
            ComboboxItemIndicator()
        }
    }
}
