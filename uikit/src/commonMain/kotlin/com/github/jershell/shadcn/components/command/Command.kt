package com.github.jershell.shadcn.components.command

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Search
import com.composeunstyled.TextInput
import com.composeunstyled.UnstyledIcon
import com.composeunstyled.UnstyledTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import org.jetbrains.compose.resources.stringResource
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.icon.ShadcnIcon
import com.github.jershell.shadcn.theme.*
import com.github.jershell.shadcn.generated.resources.Res
import com.github.jershell.shadcn.generated.resources.command_empty
import com.github.jershell.shadcn.generated.resources.command_placeholder

/**
 * A single command shown inside a [Command] panel.
 *
 * @param key Unique identifier of the command.
 * @param label Visible label.
 * @param group Group title; commands with the same title are rendered under one heading.
 * @param icon Optional leading icon (`size-4`, muted by default).
 * @param shortcut Optional keyboard hint rendered at the trailing edge.
 * @param onSelect Called when the command is activated (click or Enter).
 */
data class CommandSpec(
    val key: String,
    val label: String,
    val group: String? = null,
    val icon: ShadcnIcon? = null,
    val shortcut: String? = null,
    val onSelect: () -> Unit = {},
)

private data class FlatCommand(val spec: CommandSpec, val groupIndex: Int, val indexInGroup: Int)

/**
 * A command palette styled after the shadcn/ui Command (cmdk): a `rounded-md
 * bg-popover` panel with a search input on top, a filtered, scrollable list of
 * grouped commands and keyboard navigation (ArrowUp/ArrowDown/Enter).
 *
 * Filtering is a case-insensitive substring match on the command label.
 *
 * @param groups Commands grouped by title; a `null` title renders items without a heading.
 * @param modifier Modifier applied to the panel container.
 * @param state Text field state of the search input; create one to control the query.
 * @param placeholder Search input placeholder.
 * @param emptyText Message shown when the query matches nothing.
 * @param enabled Whether the palette is interactive.
 * @param autoFocus Whether the search input grabs focus when the panel first composes
 *   (set `true` when the panel opens on demand, e.g. inside a dialog).
 */
@Composable
fun Command(
    groups: List<CommandGroupScopeSpec>,
    modifier: Modifier = Modifier,
    state: TextFieldState = rememberTextFieldState(),
    placeholder: String = stringResource(Res.string.command_placeholder),
    emptyText: String = stringResource(Res.string.command_empty),
    enabled: Boolean = true,
    autoFocus: Boolean = false,
) {
    val colors = CommandDefaults.resolvePanelColors()
    val radius = Theme[DimProps][DimTokens.radiusMd]
    val shape = RoundedCornerShape(radius)
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]

    // Flat list of visible commands: substring filter (case-insensitive).
    val flat = remember(groups) {
        groups.flatMapIndexed { groupIndex, group ->
            group.items.mapIndexed { indexInGroup, spec ->
                FlatCommand(spec, groupIndex, indexInGroup)
            }
        }
    }
    var activeIndex by remember { mutableIntStateOf(0) }
    val visible by remember {
        derivedStateOf {
            val query = state.text.toString().trim()
            if (query.isEmpty()) {
                flat
            } else {
                flat.filter { it.spec.label.contains(query, ignoreCase = true) }
            }
        }
    }

    // Reset the active item when the filter changes.
    LaunchedEffect(state.text) {
        snapshotFlow { state.text.toString() }.collect { activeIndex = 0 }
    }

    val scroll = rememberScrollState()
    val inputFocus = remember { FocusRequester() }
    LaunchedEffect(autoFocus) {
        if (autoFocus) {
            inputFocus.requestFocus()
        }
    }

    Column(
        modifier = modifier
            .clip(shape)
            .background(colors.background)
            .border(borderWidth, colors.border, shape)
            .onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown || !enabled) {
                    return@onPreviewKeyEvent false
                }
                when (event.key) {
                    Key.DirectionDown -> {
                        if (visible.isNotEmpty()) {
                            activeIndex = (activeIndex + 1).mod(visible.size)
                        }
                        true
                    }

                    Key.DirectionUp -> {
                        if (visible.isNotEmpty()) {
                            activeIndex = (activeIndex - 1).mod(visible.size)
                        }
                        true
                    }

                    Key.Enter, Key.NumPadEnter -> {
                        visible.getOrNull(activeIndex)?.spec?.onSelect?.invoke()
                        true
                    }

                    else -> false
                }
            }
            .focusable(enabled = enabled),
    ) {
        // Input: h-9 border-b px-3 gap-2, search icon size-4 opacity-50
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(TwDimensions.heightHToken9)
                .background(Color.Transparent)
                .padding(horizontal = TwDimensions.paddingPxToken3),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken2),
        ) {
            UnstyledIcon(
                imageVector = Lucide.Search,
                contentDescription = null,
                modifier = Modifier
                    .size(TwDimensions.heightHToken4)
                    .alpha(0.5f),
                tint = colors.mutedContent,
            )
            val inputColors = CommandDefaults.resolveInputColors()
            UnstyledTextField(
                state = state,
                selectionColors = LocalTextSelectionColors.current,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(inputFocus),
                enabled = enabled,
                textStyle = TypographyStyles.textSmRegular.copy(color = inputColors.content),
            ) {
                TextInput(
                    placeholder = {
                        BasicText(
                            text = placeholder,
                            style = TypographyStyles.textSmRegular.copy(color = inputColors.placeholder),
                        )
                    },
                )
            }
        }

        // List: max-h-300 overflow-y-auto
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scroll)
                .padding(BaseTokens.token2),
        ) {
            var lastGroupIndex = -1
            visible.forEachIndexed { index, command ->
                // Separator between groups (like cmdk-group separators)
                if (command.groupIndex != lastGroupIndex) {
                    if (lastGroupIndex != -1) {
                        CommandSeparator()
                    }
                    val groupTitle = groups.getOrNull(command.groupIndex)?.title
                    if (groupTitle != null) {
                        CommandGroupHeading(text = groupTitle)
                    }
                    lastGroupIndex = command.groupIndex
                }
                CommandRow(
                    spec = command.spec,
                    selected = index == activeIndex,
                    enabled = enabled,
                    onClick = {
                        activeIndex = index
                        command.spec.onSelect()
                    },
                )
            }
            if (visible.isEmpty()) {
                CommandEmptyState(text = emptyText)
            }
        }
    }
}

/**
 * Group of commands with a shared title.
 *
 * @param title Optional group heading; `null` renders items without a heading.
 * @param items Commands in the group.
 */
data class CommandGroupScopeSpec(
    val title: String? = null,
    val items: List<CommandSpec>,
)

@Composable
private fun CommandGroupHeading(text: String) {
    val color = Theme[ColorProps][ColorTokens.mutedForeground]
    BasicText(
        text = text,
        modifier = Modifier.padding(
            horizontal = TwDimensions.paddingPxToken2,
            vertical = BaseTokens.token6, // py-1.5
        ),
        style = TypographyStyles.textXsMedium.copy(color = color),
    )
}

@Composable
private fun CommandSeparator() {
    val color = Theme[ColorProps][ColorTokens.border]
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = BaseTokens.token1)
            .height(1.dp)
            .background(color),
    )
}

@Composable
private fun CommandEmptyState(text: String) {
    val color = Theme[ColorProps][ColorTokens.mutedForeground]
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = BaseTokens.token24), // py-6
        contentAlignment = Alignment.Center,
    ) {
        BasicText(
            text = text,
            style = TypographyStyles.textSmRegular.copy(color = color),
        )
    }
}

@Composable
private fun CommandRow(
    spec: CommandSpec,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val itemColors = CommandDefaults.resolveItemColors(selected)
    val radius = Theme[DimProps][DimTokens.radiusSm]
    val shape = RoundedCornerShape(radius)
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.5f)
            .clip(shape)
            .background(itemColors.background)
            .clickable(
                enabled = enabled,
                onClick = onClick,
                interactionSource = interactionSource,
                indication = null,
            )
            .padding(
                horizontal = TwDimensions.paddingPxToken2,
                vertical = BaseTokens.token6, // py-1.5
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken2),
    ) {
        spec.icon?.let { icon ->
            UnstyledIcon(
                imageVector = (icon as? ShadcnIcon.Vector)?.imageVector ?: return@let,
                contentDescription = null,
                modifier = Modifier.size(TwDimensions.heightHToken4),
                tint = itemColors.icon,
            )
        }
        BasicText(
            text = spec.label,
            modifier = Modifier.weight(1f),
            style = TypographyStyles.textSmRegular.copy(color = itemColors.content),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        spec.shortcut?.let { shortcut ->
            BasicText(
                text = shortcut,
                style = TypographyStyles.textXsMedium.copy(color = itemColors.icon),
            )
        }
    }
}

/**
 * Colors and defaults of the [Command] panel.
 */
object CommandDefaults {

    internal class PanelColors(
        val background: Color,
        val border: Color,
        val mutedContent: Color,
    )

    @Composable
    internal fun resolvePanelColors(): PanelColors = PanelColors(
        background = Theme[ColorProps][ColorTokens.popover],
        border = Theme[ColorProps][ColorTokens.border],
        mutedContent = Theme[ColorProps][ColorTokens.mutedForeground],
    )

    internal class InputColors(
        val content: Color,
        val placeholder: Color,
    )

    @Composable
    internal fun resolveInputColors(): InputColors = InputColors(
        content = Theme[ColorProps][ColorTokens.popoverForeground],
        placeholder = Theme[ColorProps][ColorTokens.mutedForeground],
    )

    internal class ItemColors(
        val background: Color,
        val content: Color,
        val icon: Color,
    )

    @Composable
    internal fun resolveItemColors(selected: Boolean): ItemColors {
        val accent = Theme[ColorProps][ColorTokens.accent]
        val accentForeground = Theme[ColorProps][ColorTokens.accentForeground]
        val foreground = Theme[ColorProps][ColorTokens.popoverForeground]
        val mutedForeground = Theme[ColorProps][ColorTokens.mutedForeground]

        return if (selected) {
            // data-[selected=true]:bg-accent data-[selected=true]:text-accent-foreground
            ItemColors(
                background = accent,
                content = accentForeground,
                icon = accentForeground,
            )
        } else {
            // [&_svg:not([class*='text-'])]:text-muted-foreground
            ItemColors(
                background = Color.Transparent,
                content = foreground,
                icon = mutedForeground,
            )
        }
    }
}
