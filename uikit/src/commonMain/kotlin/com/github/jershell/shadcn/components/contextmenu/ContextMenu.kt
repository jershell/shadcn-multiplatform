package com.github.jershell.shadcn.components.contextmenu

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.composeunstyled.AnchorAlignment
import com.composeunstyled.AnchorSide
import com.composeunstyled.EscapeHandler
import com.composeunstyled.Portal
import com.github.jershell.shadcn.anchored.FlipAnchoredFloatingContent
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuEntryScope
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuEntryScopeInstance
import com.github.jershell.shadcn.components.dropdownmenu.LocalDropdownMenuClose
import com.github.jershell.shadcn.components.dropdownmenu.menuPanelStyle
import com.github.jershell.shadcn.theme.BaseTokens
import androidx.compose.ui.unit.round
import kotlin.math.roundToInt

/**
 * A context menu styled after the shadcn/ui Context Menu. The menu opens at the
 * pointer position on a right click (secondary button release). A right click while
 * the menu is open re-opens it at the new pointer position, dismissing the
 * previously shown menu. A primary click outside the panel or Escape closes the menu.
 *
 * The menu rows of the Dropdown Menu component are reused inside the panel:
 * [com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuItem],
 * [com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuCheckboxItem],
 * [com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuRadioGroup] etc.
 *
 * Long-press on touch devices is not handled in this version (BACKLOG).
 *
 * @param enabled Whether the area reacts to right clicks.
 * @param modifier Modifier applied to the trigger area container.
 * @param menu Panel content; use the Dropdown Menu row composables.
 * @param content The area the context menu opens for.
 */
@OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun ContextMenu(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    menu: @Composable DropdownMenuEntryScope.() -> Unit,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    var open by remember { mutableStateOf(false) }
    var cursorInAnchor by remember { mutableStateOf(Offset.Zero) }
    var anchorOrigin by remember { mutableStateOf(IntOffset.Zero) }
    var anchorSize by remember { mutableStateOf(IntSize.Zero) }
    val close: () -> Unit = remember { { open = false } }

    if (open) {
        EscapeHandler { open = false }
    }

    CompositionLocalProvider(LocalDropdownMenuClose provides close) {
        FlipAnchoredFloatingContent(
            layer = { panelContent ->
                Portal {
                    if (open) {
                        ContextMenuScrim(
                            onSecondaryClick = { positionInWindow ->
                                val anchor = anchorOrigin
                                cursorInAnchor = Offset(
                                    x = positionInWindow.x - anchor.x,
                                    y = positionInWindow.y - anchor.y,
                                )
                            },
                            onPrimaryClick = close,
                        )
                        panelContent()
                    }
                }
            },
            content = {
                ContextMenuPanel(
                    onDismiss = close,
                    content = menu,
                )
            },
            side = AnchorSide.Bottom,
            alignment = AnchorAlignment.Start,
            sideOffset = with(density) { (cursorInAnchor.y - anchorSize.height).toDp() },
            alignmentOffset = with(density) { cursorInAnchor.x.toDp() },
            anchor = {
                Box(
                    modifier = modifier
                        .onGloballyPositioned {
                            val position = it.positionInWindow().round()
                            anchorOrigin = IntOffset(position.x, position.y)
                            anchorSize = it.size
                        }
                        .pointerInput(enabled) {
                            if (!enabled) return@pointerInput
                            awaitEachGesture {
                                // Wait for a press; the first event of a gesture may be a move.
                                var press: androidx.compose.ui.input.pointer.PointerEvent? = null
                                while (true) {
                                    val candidate = awaitPointerEvent(PointerEventPass.Initial)
                                    if (candidate.changes.any { it.pressed }) {
                                        press = candidate
                                        break
                                    }
                                }
                                val down = press ?: return@awaitEachGesture
                                if (down.buttons.isSecondaryPressed.not()) {
                                    return@awaitEachGesture
                                }
                                val position = down.changes.firstOrNull()?.position ?: return@awaitEachGesture
                                while (true) {
                                    val release = awaitPointerEvent(PointerEventPass.Initial)
                                    if (release.changes.all { !it.pressed }) break
                                }
                                cursorInAnchor = position
                                open = true
                            }
                        },
                ) {
                    content()
                }
            },
        )
    }
}

/**
 * The context menu panel: styled like [com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuContent],
 * focusable, with ArrowUp/ArrowDown row traversal; Escape closes the menu.
 */
@Composable
private fun ContextMenuPanel(
    onDismiss: () -> Unit,
    content: @Composable DropdownMenuEntryScope.() -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
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

                    Key.Escape -> {
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
 * Full-window gesture layer of an open context menu: a secondary click re-opens the
 * menu at the new pointer position, a primary click closes it. Only complete
 * press→release gestures count; hover moves are ignored.
 */
@OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
private fun ContextMenuScrim(
    onSecondaryClick: (Offset) -> Unit,
    onPrimaryClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitEachGesture {
                    // Wait for a press; ignore hover/scroll events.
                    var isSecondaryPress = false
                    while (true) {
                        val down = awaitPointerEvent(PointerEventPass.Initial)
                        if (down.changes.any { it.pressed }) {
                            isSecondaryPress = down.buttons.isSecondaryPressed
                            break
                        }
                    }
                    // Wait for the release, then resolve the gesture.
                    while (true) {
                        val release = awaitPointerEvent(PointerEventPass.Initial)
                        if (release.changes.any { it.pressed }) continue
                        val isSecondary = isSecondaryPress
                        val position = release.changes.firstOrNull()?.position ?: Offset.Zero
                        if (isSecondary) {
                            onSecondaryClick(position)
                        } else {
                            onPrimaryClick()
                        }
                        break
                    }
                }
            },
    )
}