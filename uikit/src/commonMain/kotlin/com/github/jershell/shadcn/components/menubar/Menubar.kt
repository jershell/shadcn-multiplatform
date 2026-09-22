package com.github.jershell.shadcn.components.menubar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.indexOfFirstPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.round
import com.composeunstyled.AnchorAlignment
import com.composeunstyled.AnchorSide
import com.composeunstyled.EscapeHandler
import com.composeunstyled.Portal
import com.composeunstyled.UnstyledButton
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.anchored.FlipAnchoredFloatingContent
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuEntryScope
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuEntryScopeInstance
import com.github.jershell.shadcn.components.dropdownmenu.LocalDropdownMenuClose
import com.github.jershell.shadcn.components.dropdownmenu.menuPanelStyle
import com.github.jershell.shadcn.theme.BaseTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles

/**
 * Shared mutable state of one [Menubar].
 */
internal class MenubarState {
    val openIndex = mutableStateOf<Int?>(null)
    val hoveredTriggerIndex = mutableStateOf<Int?>(null)
    val triggerEnabled = mutableStateMapOf<Int, Boolean>()
    val triggerBounds = mutableStateMapOf<Int, IntRect>()
    val panelBounds = mutableStateOf<IntRect?>(null)
    val barOrigin = mutableStateOf(IntOffset.Zero)
    val panelContents = mutableStateMapOf<Int, @Composable () -> Unit>()

    /** Whether the given window position hits an enabled trigger or the open panel. */
    fun hitsMenuArea(position: IntOffset): Boolean {
        val onEnabledTrigger = triggerBounds.any { (index, bounds) ->
            triggerEnabled[index] == true && bounds.contains(position)
        }
        val onPanel = panelBounds.value?.contains(position) == true
        return onEnabledTrigger || onPanel
    }
}

/**
 * A menubar styled after the shadcn/ui Menubar: a horizontal `h-9` bordered bar whose
 * menus open below their triggers.
 *
 * The first menu opens on trigger click; while a menu is open, hovering another
 * trigger immediately switches to it (radix menubar behavior). The menu stays open
 * until the user clicks outside of it (outside the triggers and the panel), presses
 * Escape or activates an item.
 *
 * Built on a custom anchored layer rather than the modal menu primitive: its scrim
 * swallows pointer events, so the triggers could not react to hover while open.
 *
 * The `shadow-xs` of the reference is not rendered, following the project-wide
 * decision on compose shadows (BACKLOG.md).
 *
 * @param modifier Modifier applied to the bar container.
 * @param content Menus; use [MenubarScope.MenubarMenu].
 */
@Composable
fun Menubar(
    modifier: Modifier = Modifier,
    content: @Composable MenubarScope.() -> Unit,
) {
    val state = remember { MenubarState() }
    val scope = remember(state) { MenubarScope(state) }
    val density = LocalDensity.current

    if (state.openIndex.value != null) {
        EscapeHandler { state.openIndex.value = null }
    }

    CompositionLocalProvider(LocalDropdownMenuClose provides { state.openIndex.value = null }) {
        FlipAnchoredFloatingContent(
            layer = { panelContent ->
                Portal {
                    if (state.openIndex.value != null) {
                        MenubarScrim(state = state)
                        panelContent()
                    }
                }
            },
            content = {
                val open = state.openIndex.value
                if (open != null) {
                    val bounds = state.triggerBounds[open]
                    if (bounds != null) {
                        val origin = state.barOrigin.value
                        MenubarPanel(
                            state = state,
                            index = open,
                            alignmentOffset = with(density) {
                                (bounds.left - origin.x).toDp() - BaseTokens.token4 // alignOffset=-4
                            },
                        )
                    }
                }
            },
            side = AnchorSide.Bottom,
            alignment = AnchorAlignment.Start,
            sideOffset = TwDimensions.gapGapToken2, // sideOffset=8
            anchor = {
                Row(
                    modifier = modifier
                        .menubarBarStyle()
                        .onGloballyPositioned { coordinates ->
                            val position = coordinates.positionInWindow().round()
                            state.barOrigin.value = IntOffset(position.x, position.y)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken1), // gap-1
                    content = { scope.content() },
                )
            },
        )
    }
}

/**
 * Scope of a [Menubar]; provides [MenubarMenu].
 */
class MenubarScope internal constructor(
    private val state: MenubarState,
) {
    private var menuCount = 0

    /**
     * One menu of the [Menubar]. The panel opens on trigger click; while a menu is
     * open, hovering another trigger switches to it (radix menubar behavior).
     *
     * @param title Trigger label.
     * @param enabled When `false`, the trigger is dimmed and does not open the menu.
     * @param content Panel content; use the Dropdown Menu pieces.
     */
    @Composable
    fun MenubarMenu(
        title: String,
        enabled: Boolean = true,
        content: @Composable DropdownMenuEntryScope.() -> Unit,
    ) {
        val index = remember { menuCount++ }
        state.panelContents[index] = {
            with(DropdownMenuEntryScopeInstance) { content() }
        }
        state.triggerEnabled[index] = enabled
        val interactionSource = remember { MutableInteractionSource() }
        val isHovered by interactionSource.collectIsHoveredAsState()
        val isFocused by interactionSource.collectIsFocusedAsState()
        val isOpen = state.openIndex.value == index

        MenubarTrigger(
            title = title,
            enabled = enabled,
            isOpen = isOpen,
            isHovered = isHovered,
            isFocused = isFocused,
            interactionSource = interactionSource,
            onClick = {
                state.hoveredTriggerIndex.value = if (isOpen) null else index
                state.openIndex.value = if (isOpen) null else index
            },
            onKeyOpen = {
                state.hoveredTriggerIndex.value = index
                state.openIndex.value = index
            },
            onPlaced = { bounds -> state.triggerBounds[index] = bounds },
        )
    }
}

@Composable
private fun MenubarTrigger(
    title: String,
    enabled: Boolean,
    isOpen: Boolean,
    isHovered: Boolean,
    isFocused: Boolean,
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit,
    onKeyOpen: () -> Unit,
    onPlaced: (IntRect) -> Unit,
) {
    val colors = resolveMenubarTriggerColors(
        isOpen = isOpen,
        isHovered = isHovered,
        isFocused = isFocused,
    )
    val radius = Theme[DimProps][DimTokens.radiusSm]
    val shape = RoundedCornerShape(radius)

    UnstyledButton(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        indication = null,
        modifier = Modifier
            .alpha(if (enabled) 1f else 0.5f)
            .onGloballyPositioned { coordinates ->
                val position = coordinates.positionInWindow().round()
                onPlaced(
                    IntRect(
                        left = position.x,
                        top = position.y,
                        right = position.x + coordinates.size.width,
                        bottom = position.y + coordinates.size.height,
                    ),
                )
            }
            .clip(shape)
            .background(colors.background)
            .padding(
                horizontal = TwDimensions.paddingPxToken2,
                vertical = BaseTokens.token4,
            ) // px-2 py-1
            .onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) {
                    return@onPreviewKeyEvent false
                }
                when (event.key) {
                    Key.DirectionDown -> {
                        onKeyOpen()
                        true
                    }

                    else -> false
                }
            },
        content = {
            BasicText(
                text = title,
                style = TypographyStyles.textSmMedium.copy(color = colors.content),
            )
        },
    )
}

/**
 * The open menu panel: styled like `DropdownMenuContent` with `min-w-[12rem]`;
 * focusable, ArrowUp/ArrowDown traversal, Escape closes. The panel bounds are
 * reported to the scrim so clicks inside the panel do not close the menu.
 */
@Composable
private fun MenubarPanel(
    state: MenubarState,
    index: Int,
    alignmentOffset: androidx.compose.ui.unit.Dp,
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val content = state.panelContents[index]

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .offset(x = alignmentOffset)
            .onGloballyPositioned { coordinates ->
                val position = coordinates.positionInWindow().round()
                state.panelBounds.value = IntRect(
                    left = position.x,
                    top = position.y,
                    right = position.x + coordinates.size.width,
                    bottom = position.y + coordinates.size.height,
                )
            }
            .menuPanelStyle(minWidth = BaseTokens.token192) // min-w-[12rem]
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

                    Key.Escape -> {
                        state.openIndex.value = null
                        true
                    }

                    else -> false
                }
            },
        content = { with(DropdownMenuEntryScopeInstance) { content?.invoke() } },
    )
}

/**
 * Full-window gesture layer of an open menubar menu: pointer moves resolve the
 * hovered enabled trigger (immediate switching); a primary click closes the menu
 * only when it lands outside the triggers and the open panel.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun MenubarScrim(state: MenubarState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    // Index of the button pressed by the current gesture (desktop parity:
                    // 0 = primary, 1 = secondary); released with the Release event.
                    var pressedIndex = -1
                    while (true) {
                        val event = awaitPointerEvent(PointerEventPass.Initial)
                        when (event.type) {
                            PointerEventType.Press -> {
                                pressedIndex = event.buttons.indexOfFirstPressed()
                            }

                            PointerEventType.Move -> {
                                val position = event.changes.firstOrNull()?.position
                                    ?: continue
                                val rounded = position.round()
                                val hovered = state.triggerBounds.entries
                                    .firstOrNull { (index, bounds) ->
                                        state.triggerEnabled[index] == true &&
                                            bounds.contains(rounded)
                                    }
                                    ?.key
                                if (hovered != state.hoveredTriggerIndex.value) {
                                    state.hoveredTriggerIndex.value = hovered
                                    if (hovered != null) {
                                        state.openIndex.value = hovered
                                    }
                                }
                            }

                            PointerEventType.Release -> {
                                val releasedIndex = pressedIndex
                                pressedIndex = -1
                                if (releasedIndex == 0) {
                                    val position = event.changes.firstOrNull()?.position
                                        ?: continue
                                    if (state.hitsMenuArea(position.round()).not()) {
                                        state.openIndex.value = null
                                    }
                                }
                            }

                            else -> Unit
                        }
                    }
                }
            },
    )
}

@Composable
private fun Modifier.menubarBarStyle(): Modifier {
    val colors = resolveMenubarBarColors()
    val radius = Theme[DimProps][DimTokens.radiusMd]
    val shape = RoundedCornerShape(radius)
    return this
        .height(TwDimensions.heightHToken9) // h-9
        .clip(shape)
        .border(
            width = Theme[DimProps][DimTokens.borderWidth],
            color = colors.border,
            shape = shape,
        )
        .background(colors.background)
        .padding(TwDimensions.paddingPxToken1) // p-1
}