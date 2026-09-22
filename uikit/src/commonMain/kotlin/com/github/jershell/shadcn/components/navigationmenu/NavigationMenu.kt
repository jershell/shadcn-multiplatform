package com.github.jershell.shadcn.components.navigationmenu

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
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
import androidx.compose.ui.unit.dp
import com.composeunstyled.AnchorAlignment
import com.composeunstyled.AnchorSide
import com.composeunstyled.EscapeHandler
import com.composeunstyled.Portal
import com.composeunstyled.UnstyledButton
import com.composeunstyled.UnstyledIcon
import com.composeunstyled.theme.Theme
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.Lucide
import com.github.jershell.shadcn.anchored.FlipAnchoredFloatingContent
import com.github.jershell.shadcn.components.dropdownmenu.LocalDropdownMenuClose
import com.github.jershell.shadcn.theme.BaseTokens
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.Effects
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles
import androidx.compose.ui.unit.round
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

/** Delay before a hover opens a panel, mirroring the radix `delayDuration`. */
private const val HoverOpenDelayMs = 100L

/** Grace period before a panel closes after the pointer left it. */
private const val HoverCloseGraceMs = 300L

/**
 * Shared mutable state of one [NavigationMenu].
 */
internal class NavigationMenuState {
    val openIndex = mutableStateOf<Int?>(null)
    val hoveredTriggerIndex = mutableStateOf<Int?>(null)
    val panelHovered = mutableStateOf(false)
    val hoverGeneration = mutableStateOf(0)
    val hoverOpenAllowed = mutableStateOf(true)
    val triggerBounds = mutableStateMapOf<Int, IntRect>()
    val barOrigin = mutableStateOf(IntOffset.Zero)
    val panelContents = mutableStateMapOf<Int, @Composable () -> Unit>()
}

/**
 * A navigation menu styled after the shadcn/ui Navigation Menu (the `viewport=false`
 * variant): a horizontal row of triggers; the panel opens right below its trigger.
 *
 * Hover behavior matches radix: hovering a trigger opens its panel after a short
 * delay, moving to another trigger switches panels, leaving both the trigger and the
 * panel closes the menu after a grace period. A click toggles the panel; Escape and
 * a click outside close it.
 *
 * Built on a custom anchored layer rather than the modal menu primitive: its scrim
 * swallows pointer events, so the triggers could not react to hover while open.
 *
 * @param modifier Modifier applied to the bar container.
 * @param content Items; use [NavigationMenuScope.NavigationMenuTrigger] and
 *   [NavigationMenuScope.NavigationMenuLink].
 */
@Composable
fun NavigationMenu(
    modifier: Modifier = Modifier,
    content: @Composable NavigationMenuScope.() -> Unit,
) {
    val state = remember { NavigationMenuState() }
    val scope = remember(state) { NavigationMenuScope(state) }
    val density = LocalDensity.current

    if (state.openIndex.value != null) {
        EscapeHandler { state.openIndex.value = null }
    }

    // Hover-open / hover-switch after a short delay. Entry-based: the generation
    // counter changes only when the pointer enters a trigger, so a click-close on
    // the same trigger is not undone while the pointer stays there.
    LaunchedEffect(state.hoverGeneration.value) {
        val hovered = state.hoveredTriggerIndex.value ?: return@LaunchedEffect
        if (hovered == state.openIndex.value || state.hoverOpenAllowed.value.not()) {
            return@LaunchedEffect
        }
        delay(HoverOpenDelayMs)
        if (state.hoveredTriggerIndex.value == hovered && state.hoverOpenAllowed.value) {
            state.openIndex.value = hovered
        }
    }

    // Grace close: a panel is open but the pointer is neither on the open trigger
    // nor on the panel.
    LaunchedEffect(
        state.openIndex.value,
        state.hoveredTriggerIndex.value,
        state.panelHovered.value,
    ) {
        val open = state.openIndex.value ?: return@LaunchedEffect
        if (state.hoveredTriggerIndex.value == open || state.panelHovered.value) {
            return@LaunchedEffect
        }
        delay(HoverCloseGraceMs)
        if (state.openIndex.value == open &&
            state.hoveredTriggerIndex.value != state.openIndex.value &&
            state.panelHovered.value.not()
        ) {
            state.openIndex.value = null
        }
    }

    CompositionLocalProvider(LocalDropdownMenuClose provides { state.openIndex.value = null }) {
        FlipAnchoredFloatingContent(
            layer = { panelContent ->
                Portal {
                    if (state.openIndex.value != null) {
                        NavigationMenuScrim(state = state)
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
                        NavigationMenuPanel(
                            state = state,
                            index = open,
                            alignmentOffset = with(density) {
                                (bounds.left - origin.x).toDp()
                            },
                        )
                    }
                }
            },
            side = AnchorSide.Bottom,
            alignment = AnchorAlignment.Start,
            sideOffset = BaseTokens.token6, // mt-1.5
            anchor = {
                Row(
                    modifier = modifier
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
 * Scope of a [NavigationMenu]; provides [NavigationMenuTrigger] and [NavigationMenuLink].
 */
class NavigationMenuScope internal constructor(
    internal val state: NavigationMenuState,
) {
    private var itemCount = 0

    /**
     * A trigger with a card panel below it, matching the shadcn/ui
     * `NavigationMenuTrigger`: `h-9 px-4 py-2 text-sm font-medium rounded-md`,
     * highlighted on hover/focus, `bg-accent/50` while the panel is open; the
     * trailing chevron rotates when open.
     *
     * @param title Trigger label.
     * @param enabled When `false`, the trigger is dimmed and does not open the panel.
     * @param content Panel content; use [NavigationMenuLink] cards.
     */
    @Composable
    fun NavigationMenuTrigger(
        title: String,
        enabled: Boolean = true,
        content: @Composable () -> Unit,
    ) {
        val index = remember { itemCount++ }
        state.panelContents[index] = content
        val interactionSource = remember { MutableInteractionSource() }
        val isHovered by interactionSource.collectIsHoveredAsState()
        val isFocused by interactionSource.collectIsFocusedAsState()
        val isOpen = state.openIndex.value == index
        var wasHovered by remember { mutableStateOf(false) }

        LaunchedEffect(isHovered) {
            if (isHovered && wasHovered.not()) {
                if (state.hoveredTriggerIndex.value != index) {
                    state.hoveredTriggerIndex.value = index
                }
                state.hoverGeneration.value += 1
                state.hoverOpenAllowed.value = true
            } else if (isHovered.not() && wasHovered && state.openIndex.value != index) {
                state.hoveredTriggerIndex.value = null
            }
            wasHovered = isHovered
        }

        NavigationMenuTriggerButton(
            title = title,
            enabled = enabled,
            isOpen = isOpen,
            isHovered = isHovered,
            isFocused = isFocused,
            interactionSource = interactionSource,
            onClick = {
                if (isOpen) {
                    state.hoverOpenAllowed.value = false
                    state.openIndex.value = null
                } else {
                    state.hoveredTriggerIndex.value = index
                    state.hoverOpenAllowed.value = true
                    state.openIndex.value = index
                }
            },
            onKeyOpen = {
                state.hoveredTriggerIndex.value = index
                state.hoverOpenAllowed.value = true
                state.openIndex.value = index
            },
            onPlaced = { bounds -> state.triggerBounds[index] = bounds },
        )
    }
}

@Composable
private fun NavigationMenuTriggerButton(
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
    val colors = resolveNavigationMenuTriggerColors(
        isOpen = isOpen,
        isHovered = isHovered,
        isFocused = isFocused,
    )
    val shape = RoundedCornerShape(Theme[DimProps][DimTokens.radiusMd])
    val chevronRotation by animateFloatAsState(
        targetValue = if (isOpen) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
    )

    UnstyledButton(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        indication = null,
        modifier = Modifier
            .alpha(if (enabled) 1f else 0.5f)
            .height(TwDimensions.heightHToken9) // h-9
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
            .padding(horizontal = TwDimensions.paddingPxToken4) // px-4
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                BasicText(
                    text = title,
                    style = TypographyStyles.textSmMedium.copy(color = colors.content),
                )
                UnstyledIcon(
                    imageVector = Lucide.ChevronDown,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = BaseTokens.token4) // ml-1
                        .size(BaseTokens.token12) // size-3
                        .rotate(chevronRotation),
                    tint = colors.content,
                )
            }
        },
    )
}

/**
 * A plain link in the trigger row, styled like the reference `NavigationMenuLink`
 * used with the trigger style in the shadcn docs example.
 *
 * @param title Link label.
 * @param onSelect Invoked when the link is activated.
 * @param enabled When `false`, the link is dimmed.
 */
@Composable
fun NavigationMenuScope.NavigationMenuLink(
    title: String,
    onSelect: () -> Unit,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isFocused by interactionSource.collectIsFocusedAsState()
    val colors = resolveNavigationMenuTriggerColors(
        isOpen = false,
        isHovered = isHovered,
        isFocused = isFocused,
    )
    val shape = RoundedCornerShape(Theme[DimProps][DimTokens.radiusMd])

    UnstyledButton(
        onClick = onSelect,
        enabled = enabled,
        interactionSource = interactionSource,
        indication = null,
        modifier = Modifier
            .alpha(if (enabled) 1f else 0.5f)
            .clip(shape)
            .background(colors.background)
            .padding(
                horizontal = TwDimensions.paddingPxToken4, // px-4
                vertical = TwDimensions.paddingPyToken2, // py-2
            ),
        content = {
            BasicText(
                text = title,
                style = TypographyStyles.textSmMedium.copy(color = colors.content),
            )
        },
    )
}

/**
 * A link card inside a [NavigationMenuScope.NavigationMenuTrigger] panel, matching the
 * shadcn/ui `NavigationMenuLink`: a vertical `p-2 rounded-sm` block with title,
 * optional description, highlighted with `bg-accent` on hover/focus, `bg-accent/50`
 * when [active].
 *
 * @param title Card title.
 * @param description Optional secondary line under the title.
 * @param active Marks the link as the current page.
 * @param onSelect Invoked when the card is activated; the menu closes afterwards.
 * @param enabled When `false`, the card is dimmed and not clickable.
 */
@Composable
fun NavigationMenuLink(
    title: String,
    description: String? = null,
    modifier: Modifier = Modifier.fillMaxWidth(),
    active: Boolean = false,
    enabled: Boolean = true,
    onSelect: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isFocused by interactionSource.collectIsFocusedAsState()
    val colors = resolveNavigationMenuLinkColors(
        isActive = active,
        isHovered = isHovered,
        isFocused = isFocused,
    )
    val shape = RoundedCornerShape(Theme[DimProps][DimTokens.radiusSm])
    val close = LocalDropdownMenuClose.current

    UnstyledButton(
        onClick = {
            onSelect()
            close()
        },
        enabled = enabled,
        interactionSource = interactionSource,
        indication = null,
        modifier = modifier
            .alpha(if (enabled) 1f else 0.5f)
            .clip(shape)
            .background(colors.background)
            .padding(TwDimensions.paddingPxToken2), // p-2
        content = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken1),
            ) {
                BasicText(
                    text = title,
                    style = TypographyStyles.textSmMedium.copy(color = colors.content),
                )
                if (description != null) {
                    BasicText(
                        text = description,
                        style = TypographyStyles.textSmRegular.copy(color = colors.description),
                    )
                }
            }
        },
    )
}

/**
 * The open panel: `rounded-md border bg-popover p-2 pr-2.5 shadow`, placed below its
 * trigger; hoverable (keeps the menu alive), focusable, ArrowUp/ArrowDown traversal,
 * Escape closes.
 */
@Composable
private fun NavigationMenuPanel(
    state: NavigationMenuState,
    index: Int,
    alignmentOffset: androidx.compose.ui.unit.Dp,
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val panelInteractionSource = remember { MutableInteractionSource() }
    val isPanelHovered by panelInteractionSource.collectIsHoveredAsState()
    val shape = RoundedCornerShape(Theme[DimProps][DimTokens.radiusMd])
    val borderColor = Theme[ColorProps][ColorTokens.border]
    val background = Theme[ColorProps][ColorTokens.popover]
    val content = state.panelContents[index]

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(isPanelHovered) {
        state.panelHovered.value = isPanelHovered
        if (isPanelHovered.not() && state.hoveredTriggerIndex.value == state.openIndex.value) {
            state.hoveredTriggerIndex.value = null
        }
    }

    Column(
        modifier = Modifier
            .offset(x = alignmentOffset)
            .width(IntrinsicSize.Max)
            .clip(shape)
            .shadow(
                elevation = Effects.boxShadowShadowMdToken0.radius,
                shape = shape,
                clip = false,
                ambientColor = Effects.boxShadowShadowMdToken0.color,
                spotColor = Effects.boxShadowShadowMdToken1.color,
            )
            .background(background)
            .border(Theme[DimProps][DimTokens.borderWidth], borderColor, shape)
            .padding(
                start = TwDimensions.paddingPxToken2, // p-2
                top = TwDimensions.paddingPxToken2,
                bottom = TwDimensions.paddingPxToken2,
                end = BaseTokens.token10, // pr-2.5
            )
            .focusRequester(focusRequester)
            .focusable()
            .hoverable(panelInteractionSource)
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
        verticalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken1), // gap-1 between cards
        content = { content?.invoke() },
    )
}

/**
 * Full-window gesture layer of an open navigation menu: pointer moves resolve the
 * hovered trigger (hover switching), a primary click closes the menu.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun NavigationMenuScrim(state: NavigationMenuState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    // Index of the button pressed by the current gesture (0 = primary);
                    // released with the Release event.
                    var pressedIndex = -1
                    while (true) {
                        val event = awaitPointerEvent(PointerEventPass.Initial)
                        when (event.type) {
                            PointerEventType.Press -> {
                                pressedIndex = event.buttons.indexOfFirstPressed()
                            }

                            PointerEventType.Move -> {
                                val position = event.changes.firstOrNull()?.position ?: continue
                                val hovered = state.triggerBounds.entries
                                    .firstOrNull { it.value.contains(position.round()) }
                                    ?.key
                                if (hovered != state.hoveredTriggerIndex.value) {
                                    if (hovered != null) {
                                        state.hoverOpenAllowed.value = true
                                        state.hoverGeneration.value += 1
                                    }
                                    state.hoveredTriggerIndex.value = hovered
                                }
                            }

                            PointerEventType.Release -> {
                                val releasedIndex = pressedIndex
                                pressedIndex = -1
                                if (releasedIndex == 0) {
                                    state.openIndex.value = null
                                }
                            }

                            else -> Unit
                        }
                    }
                }
            },
    )
}