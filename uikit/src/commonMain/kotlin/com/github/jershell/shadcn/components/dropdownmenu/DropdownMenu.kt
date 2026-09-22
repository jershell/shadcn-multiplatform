package com.github.jershell.shadcn.components.dropdownmenu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.AnchorAlignment
import com.composeunstyled.AnchorSide
import com.composeunstyled.DropdownMenuPanel
import com.composeunstyled.DropdownMenuPanelScope
import com.composeunstyled.DropdownMenuScope
import com.composeunstyled.EscapeHandler
import com.composeunstyled.Portal
import com.composeunstyled.UnstyledDropdownMenu
import com.composeunstyled.UnstyledDropdownMenuItem
import com.composeunstyled.UnstyledIcon
import com.composeunstyled.theme.Theme
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.Lucide
import com.github.jershell.shadcn.anchored.FlipAnchoredFloatingContent
import com.github.jershell.shadcn.theme.BaseTokens
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.Effects
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles

/**
 * Visual variant of a [DropdownMenuItem].
 */
enum class DropdownMenuItemVariant {

    /** Regular item: `popoverForeground` text, `accent` focus background. */
    Default,

    /** Destructive item: `destructive` text/icon, translucent `destructive` focus background. */
    Destructive,
}

internal val LocalDropdownMenuItemContentColor = compositionLocalOf { Color.Unspecified }

internal val LocalDropdownMenuClose = compositionLocalOf { {} }

/**
 * Marker scope shared by all menu row composables ([DropdownMenuItem],
 * [DropdownMenuCheckboxItem], [DropdownMenuRadioGroup], [DropdownMenuLabel],
 * [DropdownMenuSeparator], [DropdownMenuSub]). It is provided by both
 * [DropdownMenuContent] (the primitive-backed panel) and custom panels, such as
 * the one of [com.github.jershell.shadcn.components.contextmenu.ContextMenu].
 */
interface DropdownMenuEntryScope

internal val DropdownMenuEntryScopeInstance: DropdownMenuEntryScope = object : DropdownMenuEntryScope {}

/**
 * A dropdown menu styled after the shadcn/ui Dropdown Menu, built on top of
 * [UnstyledDropdownMenu]. Provides menu semantics from the primitive: keyboard
 * navigation (arrows/Home/End/Enter), Escape to close, click outside to close,
 * Tab exits the menu and continues focus traversal.
 *
 * The application root must be wrapped in [com.composeunstyled.ModalHost] so the
 * menu panel can be portaled to a window-sized overlay.
 *
 * Use [DropdownMenuContent] inside the [content] slot and fill it with
 * [DropdownMenuItem], [DropdownMenuCheckboxItem], [DropdownMenuRadioGroup] +
 * `DropdownMenuRadioItem`, [DropdownMenuSub] + `DropdownMenuSubContent`,
 * [DropdownMenuLabel], [DropdownMenuSeparator] and [DropdownMenuShortcut].
 *
 * @param expanded Whether the menu is open.
 * @param onExpandedChange Called when the user asks to open or close the menu.
 * @param side Preferred side of the anchor the panel is placed on.
 * @param alignment Alignment of the panel relative to the anchor.
 * @param sideOffset Gap between the anchor and the panel (`sideOffset=4` in the reference).
 * @param alignmentOffset Additional offset along the anchor axis.
 * @param anchor The trigger content the menu is anchored to.
 * @param content The menu content; composed only while the panel is measured.
 */
@Composable
fun DropdownMenu(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    side: AnchorSide = AnchorSide.Bottom,
    alignment: AnchorAlignment = AnchorAlignment.Start,
    sideOffset: Dp = TwDimensions.gapGapToken1,
    alignmentOffset: Dp = 0.dp,
    anchor: @Composable () -> Unit,
    content: @Composable DropdownMenuScope.() -> Unit,
) {
    CompositionLocalProvider(LocalDropdownMenuClose provides { onExpandedChange(false) }) {
        UnstyledDropdownMenu(
            expanded = expanded,
            onExpandedChange = onExpandedChange,
            modifier = modifier,
            side = side,
            alignment = alignment,
            sideOffset = sideOffset,
            alignmentOffset = alignmentOffset,
            panel = content,
            anchor = anchor,
        )
    }
}

/**
 * The panel of a [DropdownMenu], matching the shadcn/ui `DropdownMenuContent`:
 * `min-w-[8rem] rounded-md border bg-popover p-1 text-popover-foreground shadow-md`.
 *
 * The panel lays out its direct children vertically, the width grows to the widest
 * child but never below `8rem`; override via [modifier].
 *
 * @param modifier Modifier applied to the panel container.
 * @param content Panel content; direct children are stacked vertically.
 */
@Composable
fun DropdownMenuScope.DropdownMenuContent(
    modifier: Modifier = Modifier,
    content: @Composable DropdownMenuEntryScope.() -> Unit,
) {
    DropdownMenuPanel(
        modifier = modifier.menuPanelStyle(minWidth = BaseTokens.token128), // min-w-[8rem]
        content = { with(DropdownMenuEntryScopeInstance) { content() } },
    )
}

/**
 * Menu panel styled like `DropdownMenuContent` with a custom minimum width; used
 * by sibling menu components ([com.github.jershell.shadcn.components.contextmenu.ContextMenu],
 * [com.github.jershell.shadcn.components.menubar.Menubar]).
 */
@Composable
fun DropdownMenuScope.MenuPanel(
    modifier: Modifier = Modifier,
    minWidth: Dp,
    content: @Composable DropdownMenuEntryScope.() -> Unit,
) {
    DropdownMenuPanel(
        modifier = modifier.menuPanelStyle(minWidth = minWidth),
        content = { with(DropdownMenuEntryScopeInstance) { content() } },
    )
}

/**
 * An action row inside [DropdownMenuContent], matching the shadcn/ui `DropdownMenuItem`:
 * `gap-2 rounded-sm px-2 py-1.5 text-sm`, highlighted with `bg-accent` on hover or
 * keyboard focus.
 *
 * @param onClick Invoked when the item is activated.
 * @param enabled When `false`, the item is dimmed and not clickable.
 * @param variant Visual variant; [DropdownMenuItemVariant.Destructive] colors the row
 *   and its icons with `destructive` tokens.
 * @param inset Indents the content to `pl-8`, aligning it with checkbox/radio items.
 * @param closeOnClick Whether the menu closes after activation.
 * @param content Item content laid out horizontally; use [DropdownMenuItemText],
 *   [DropdownMenuItemIcon] and [DropdownMenuShortcut] for the standard pieces.
 */
@Composable
fun DropdownMenuEntryScope.DropdownMenuItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: DropdownMenuItemVariant = DropdownMenuItemVariant.Default,
    inset: Boolean = false,
    closeOnClick: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    DropdownMenuRow(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        variant = variant,
        closeOnClick = closeOnClick,
        interactionSource = interactionSource,
        startPadding = if (inset) TwDimensions.paddingPlToken8 else TwDimensions.paddingPxToken2,
        content = content,
    )
}

/**
 * A checkable row inside [DropdownMenuContent], matching the shadcn/ui
 * `DropdownMenuCheckboxItem`: a `size-4` check indicator at the trailing edge.
 *
 * @param checked Current checked state.
 * @param onCheckedChange Called with the next checked state on activation.
 * @param enabled When `false`, the item is dimmed and not clickable.
 * @param closeOnClick Whether the menu closes after activation; keep `false`
 *   to let the user toggle several items.
 * @param content Row content before the indicator; include a stretching
 *   [DropdownMenuItemText] so the indicator aligns to the row end.
 */
@Composable
fun DropdownMenuEntryScope.DropdownMenuCheckboxItem(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    closeOnClick: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    DropdownMenuRow(
        onClick = { onCheckedChange(!checked) },
        modifier = modifier,
        enabled = enabled,
        closeOnClick = closeOnClick,
        interactionSource = interactionSource,
        startPadding = TwDimensions.paddingPxToken2,
    ) {
        content()
        IndicatorSlot(checked = checked) {
            UnstyledIcon(
                imageVector = Lucide.Check,
                contentDescription = null,
                modifier = Modifier.size(TwDimensions.heightHToken4),
                tint = LocalDropdownMenuItemContentColor.current,
            )
        }
    }
}

/**
 * Group of mutually exclusive `DropdownMenuRadioItem`s.
 *
 * @param selected Key of the currently selected item, or `null` when none is.
 * @param onSelectedChange Called with the key of the item the user activated.
 * @param content Items; use [DropdownMenuRadioGroupScope.DropdownMenuRadioItem].
 */
@Composable
fun <K> DropdownMenuEntryScope.DropdownMenuRadioGroup(
    selected: K?,
    onSelectedChange: (K) -> Unit,
    content: @Composable DropdownMenuRadioGroupScope<K>.() -> Unit,
) {
    DropdownMenuRadioGroupScope(
        selected = selected,
        onSelectedChange = onSelectedChange,
    ).content()
}

/**
 * Scope of a [DropdownMenuRadioGroup]; provides [DropdownMenuRadioItem].
 */
class DropdownMenuRadioGroupScope<K> internal constructor(
    private val selected: K?,
    private val onSelectedChange: (K) -> Unit,
) {
    /**
     * A radio row inside [DropdownMenuRadioGroup], matching the shadcn/ui
     * `DropdownMenuRadioItem`: a `size-2` dot indicator at the trailing edge.
     *
     * @param key Identity of this item inside the group.
     * @param modifier Modifier applied to the row.
     * @param onClick Extra action invoked on activation, before selection.
     * @param enabled When `false`, the item is dimmed and not clickable.
     * @param closeOnClick Whether the menu closes after activation.
     * @param content Row content before the indicator; include a stretching
     *   [DropdownMenuItemText] so the indicator aligns to the row end.
     */
    @Composable
    fun DropdownMenuRadioItem(
        key: K,
        modifier: Modifier = Modifier,
        onClick: () -> Unit = {},
        enabled: Boolean = true,
        closeOnClick: Boolean = true,
        interactionSource: MutableInteractionSource? = null,
        content: @Composable RowScope.() -> Unit,
    ) {
        val isSelected = selected == key
        DropdownMenuRow(
            onClick = {
                onClick()
                onSelectedChange(key)
            },
            modifier = modifier,
            enabled = enabled,
            closeOnClick = closeOnClick,
            interactionSource = interactionSource,
            startPadding = TwDimensions.paddingPxToken2,
        ) {
            content()
            IndicatorSlot(checked = isSelected) {
                Box(
                    modifier = Modifier
                        .size(BaseTokens.token8) // size-2
                        .background(
                            color = LocalDropdownMenuItemContentColor.current,
                            shape = CircleShape,
                        ),
                )
            }
        }
    }
}

/**
 * A submenu inside [DropdownMenuContent], matching the shadcn/ui `DropdownMenuSub`.
 * The panel opens on the end side of the trigger and closes on click outside, on
 * Escape, on ArrowLeft inside the panel or when the trigger is clicked again.
 *
 * @param expanded Whether the submenu panel is open.
 * @param onExpandedChange Called when the user asks to open or close the submenu.
 * @param modifier Modifier applied to the submenu panel container.
 * @param alignment Alignment of the submenu panel along the anchor side axis.
 * @param trigger Trigger row content, laid out before the trailing chevron;
 *   use [DropdownMenuItemText] etc. inside.
 * @param content Submenu panel content; use [DropdownMenuItem] etc. inside.
 */
@Composable
fun DropdownMenuEntryScope.DropdownMenuSub(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    alignment: AnchorAlignment = AnchorAlignment.Start,
    trigger: @Composable RowScope.() -> Unit,
    content: @Composable DropdownMenuEntryScope.() -> Unit,
) {
    if (expanded) {
        EscapeHandler { onExpandedChange(false) }
    }

    FlipAnchoredFloatingContent(
        layer = { panelContent ->
            Portal {
                if (expanded) {
                    DropdownMenuSubScrim(onDismiss = { onExpandedChange(false) })
                    panelContent()
                }
            }
        },
        content = {
            DropdownMenuSubPanel(
                modifier = modifier,
                onDismiss = { onExpandedChange(false) },
                content = content,
            )
        },
        side = AnchorSide.End,
        alignment = alignment,
        sideOffset = TwDimensions.gapGapToken1,
        anchor = {
            DropdownMenuRow(
                onClick = { onExpandedChange(!expanded) },
                enabled = true,
                variant = DropdownMenuItemVariant.Default,
                closeOnClick = false,
                interactionSource = null,
                startPadding = TwDimensions.paddingPxToken2,
                forceHighlighted = expanded,
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken2),
                ) {
                    trigger()
                }
                UnstyledIcon(
                    imageVector = Lucide.ChevronRight,
                    contentDescription = null,
                    modifier = Modifier.size(TwDimensions.heightHToken4),
                    tint = Theme[ColorProps][ColorTokens.mutedForeground],
                )
            }
        },
    )
}

/**
 * Non-clickable heading inside [DropdownMenuContent], matching the shadcn/ui
 * `DropdownMenuLabel`: `px-2 py-1.5 text-sm font-medium`.
 *
 * @param inset Indents the text to `pl-8` to align with checkbox/radio items.
 */
@Composable
fun DropdownMenuEntryScope.DropdownMenuLabel(
    text: String,
    modifier: Modifier = Modifier,
    inset: Boolean = false,
) {
    val colors = resolveDropdownMenuPanelColors()
    BasicText(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = if (inset) TwDimensions.paddingPlToken8 else TwDimensions.paddingPxToken2,
                end = TwDimensions.paddingPxToken2,
            )
            .padding(vertical = BaseTokens.token6),
        style = TypographyStyles.textSmMedium.copy(color = colors.content),
    )
}

/**
 * Horizontal rule inside [DropdownMenuContent], matching the shadcn/ui
 * `DropdownMenuSeparator`: `my-1 h-px bg-border`.
 */
@Composable
fun DropdownMenuEntryScope.DropdownMenuSeparator(
    modifier: Modifier = Modifier,
) {
    val color = Theme[ColorProps][ColorTokens.border]
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = TwDimensions.paddingPyToken1) // my-1
            .height(BaseTokens.token1) // h-px
            .background(color),
    )
}

/**
 * Keyboard hint rendered at the trailing edge of an item, matching the shadcn/ui
 * `DropdownMenuShortcut`: `text-xs tracking-widest text-muted-foreground`.
 */
@Composable
fun DropdownMenuShortcut(
    text: String,
    modifier: Modifier = Modifier,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = TypographyStyles.textXsMedium.copy(
            color = Theme[ColorProps][ColorTokens.mutedForeground],
        ),
    )
}

/**
 * Text label of an item; colored by the item state (default or focused). Stretches
 * by default so trailing pieces like [DropdownMenuShortcut] align to the row end.
 */
@Composable
fun RowScope.DropdownMenuItemText(
    text: String,
    modifier: Modifier = Modifier.weight(1f),
) {
    val color = LocalDropdownMenuItemContentColor.current
    BasicText(
        text = text,
        modifier = modifier,
        style = TypographyStyles.textSmRegular.copy(
            color = if (color != Color.Unspecified) color
            else resolveDropdownMenuItemColors(DropdownMenuItemVariant.Default).content,
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

/**
 * Leading icon of an item; muted by default, `destructive` inside a destructive item.
 */
@Composable
fun RowScope.DropdownMenuItemIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    val color = LocalDropdownMenuItemContentColor.current
    UnstyledIcon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier.size(TwDimensions.heightHToken4),
        tint = if (color != Color.Unspecified) color
        else Theme[ColorProps][ColorTokens.mutedForeground],
    )
}

/**
 * Shared row of a menu item: highlight on hover/keyboard focus, `px-2 py-1.5`
 * rhythm, `rounded-sm`, dimmed when disabled.
 */
@Composable
private fun DropdownMenuRow(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    variant: DropdownMenuItemVariant = DropdownMenuItemVariant.Default,
    closeOnClick: Boolean,
    interactionSource: MutableInteractionSource?,
    startPadding: Dp,
    endPadding: Dp = TwDimensions.paddingPxToken2,
    forceHighlighted: Boolean = false,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = resolveDropdownMenuItemColors(variant)
    val radius = Theme[DimProps][DimTokens.radiusSm]
    val shape = RoundedCornerShape(radius)
    val interactionSourceOrDefault = interactionSource ?: remember { MutableInteractionSource() }
    val isHovered by interactionSourceOrDefault.collectIsHoveredAsState()
    val isFocused by interactionSourceOrDefault.collectIsFocusedAsState()
    val highlighted = (enabled && (isHovered || isFocused)) || forceHighlighted

    val background = when {
        highlighted -> colors.focusBackground
        else -> Color.Transparent
    }
    val contentColor = if (highlighted) colors.focusContent else colors.content
    val close = LocalDropdownMenuClose.current

    CompositionLocalProvider(LocalDropdownMenuItemContentColor provides contentColor) {
        UnstyledDropdownMenuItem(
            onClick = {
                onClick()
                if (closeOnClick) {
                    close()
                }
            },
            enabled = enabled,
            closeOnClick = false,
            interactionSource = interactionSourceOrDefault,
            indication = null,
            modifier = modifier
                .fillMaxWidth()
                .alpha(if (enabled) 1f else 0.5f)
                .clip(shape)
                .background(background)
                .padding(start = startPadding, end = endPadding)
                .padding(vertical = BaseTokens.token6), // py-1.5
            content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken2),
                    content = content,
                )
            },
        )
    }
}

/**
 * The reserved indicator slot of checkbox/radio items at the trailing edge:
 * a fixed `size-4` zone keeps the row rhythm stable whether or not the indicator
 * is shown (like the absolutely positioned `ItemIndicator` in the reference).
 */
@Composable
private fun IndicatorSlot(
    checked: Boolean,
    indicator: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier.size(TwDimensions.heightHToken4), // size-4
        contentAlignment = Alignment.Center,
        content = { if (checked) indicator() },
    )
}

/**
 * Submenu panel: styled like [DropdownMenuContent], laid out vertically; focusable
 * panel with ArrowUp/ArrowDown row traversal, ArrowLeft and Escape close the panel.
 */
@Composable
private fun DropdownMenuSubPanel(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    content: @Composable DropdownMenuEntryScope.() -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = modifier
            .menuPanelStyle(minWidth = BaseTokens.token128)
            .focusRequester(focusRequester)
            .focusable()
            .onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) {
                    return@onPreviewKeyEvent false
                }
                when (event.key) {
                    Key.DirectionDown -> {
                        focusManager.moveFocus(FocusDirection.Next)
                        true
                    }

                    Key.DirectionUp -> {
                        focusManager.moveFocus(FocusDirection.Previous)
                        true
                    }

                    Key.DirectionLeft, Key.Escape -> {
                        onDismiss()
                        true
                    }

                    else -> false
                }
            },
        content = { with(DropdownMenuEntryScopeInstance) { content() } },
    )
}

/**
 * Rounded panel background of a menu: `min-w-[8rem] rounded-md border bg-popover
 * shadow-md p-1`. `width(IntrinsicSize.Max)` makes the panel as wide as its widest
 * row so every row can stretch to the panel width like radix items do.
 */
@Composable
internal fun Modifier.menuPanelStyle(minWidth: Dp): Modifier {
    val radius = Theme[DimProps][DimTokens.radiusMd]
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val shape = RoundedCornerShape(radius)
    val colors = resolveDropdownMenuPanelColors()
    return this
        .width(IntrinsicSize.Max)
        .widthIn(min = minWidth)
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
        .padding(TwDimensions.paddingPxToken1) // p-1
}

@Composable
private fun DropdownMenuSubScrim(onDismiss: () -> Unit) {
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