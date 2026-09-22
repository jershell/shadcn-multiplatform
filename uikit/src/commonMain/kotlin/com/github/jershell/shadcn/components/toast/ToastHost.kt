package com.github.jershell.shadcn.components.toast

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.UnstyledButton
import com.composeunstyled.UnstyledIcon
import com.composeunstyled.theme.Theme
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.X
import com.github.jershell.shadcn.components.button.Button
import com.github.jershell.shadcn.components.button.ButtonSize
import com.github.jershell.shadcn.components.button.ButtonText
import com.github.jershell.shadcn.components.button.ButtonVariant
import com.github.jershell.shadcn.theme.BaseTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.Effects
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.runtime.snapshotFlow
import org.jetbrains.compose.resources.stringResource
import com.github.jershell.shadcn.generated.resources.Res
import com.github.jershell.shadcn.generated.resources.toast_dismiss_notification

/**
 * Matches the Base UI toast transition curve `cubic-bezier(0.22, 1, 0.36, 1)`.
 */
internal val ToastEasing = CubicBezierEasing(0.22f, 1f, 0.36f, 1f)

private const val STACK_SCALE_STEP = 0.1f
private const val STACK_ANIM_MILLIS = 400
private const val EXIT_ANIM_MILLIS = 200
private const val TIMER_TICK_MILLIS = 100L

/**
 * Window-level overlay rendering the toast queue of [manager], styled after the
 * shadcn/ui Sonner toaster and positioned like the Base UI toast viewport.
 *
 * Compose this host inside a window-sized parent (the [com.composeunstyled.ModalHost]
 * of the application root); it fills all available space as a non-interactive overlay.
 * Pointer events outside the toast stack pass through to the content below.
 *
 * Stacked mode ([ToasterConfig.stacked]) renders a Sonner-style deck: collapsed it shows
 * the frontmost toast with the others peeking above (bottom positions) or below (top
 * positions), expanding on hover. Column mode renders a plain list.
 *
 * While the pointer is over the toast stack, auto-dismiss timers are paused.
 *
 * @param manager Queue to render and mutate.
 * @param config Viewport placement and stacking mode.
 * @param modifier Modifier for the overlay container; fills all available space by default.
 */
@Composable
fun ToastHost(
    manager: ToastManager = Toast.manager,
    config: ToasterConfig = ToasterConfig(),
    modifier: Modifier = Modifier,
) {
    val entries = manager.entries
    val isTop = config.position == ToastPosition.TopStart ||
        config.position == ToastPosition.TopCenter ||
        config.position == ToastPosition.TopEnd
    val alignment = when (config.position) {
        ToastPosition.TopStart -> Alignment.TopStart
        ToastPosition.TopCenter -> Alignment.TopCenter
        ToastPosition.TopEnd -> Alignment.TopEnd
        ToastPosition.BottomStart -> Alignment.BottomStart
        ToastPosition.BottomCenter -> Alignment.BottomCenter
        ToastPosition.BottomEnd -> Alignment.BottomEnd
    }

    var hovered by remember { mutableStateOf(false) }
    val heights = remember { mutableStateMapOf<Long, Int>() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(BaseTokens.token32), // 2rem viewport margin
        contentAlignment = alignment,
    ) {
        if (config.stacked) {
            StackedToastViewport(
                manager = manager,
                entries = entries,
                isTop = isTop,
                expanded = hovered,
                onHover = { hovered = it },
                heights = heights,
            )
        } else {
            ColumnToastViewport(
                manager = manager,
                entries = entries,
                isTop = isTop,
                onHover = { hovered = it },
                heights = heights,
            )
        }
    }
}

@Composable
private fun StackedToastViewport(
    manager: ToastManager,
    entries: List<ToastEntry>,
    isTop: Boolean,
    expanded: Boolean,
    onHover: (Boolean) -> Unit,
    heights: MutableMap<Long, Int>,
) {
    val density = LocalDensity.current
    val gap = BaseTokens.token12 // --gap: 0.75rem
    val peek = BaseTokens.token12 // --peek: 0.75rem

    val newestId = entries.lastOrNull()?.id
    val frontHeight = newestId?.let { id -> heights[id] }?.let { with(density) { it.toDp() } }
        ?: 64.dp

    val totalExpanded = with(density) {
        (entries.sumOf { heights[it.id] ?: 0 } + gap.roundToPx() * (entries.size - 1).coerceAtLeast(0))
            .toDp()
    }
    val viewportHeight by animateDpAsState(
        targetValue = if (expanded) totalExpanded else frontHeight,
        animationSpec = tween(STACK_ANIM_MILLIS, easing = ToastEasing),
        label = "toastViewportHeight",
    )

    Box(
        modifier = Modifier
            .widthIn(max = TwDimensions.maxWidthMaxWSm) // max-w-sm (viewport ~22.5rem)
            .fillMaxWidth()
            .height(viewportHeight)
            .toastHoverTracker(onHover),
        contentAlignment = if (isTop) Alignment.TopCenter else Alignment.BottomCenter,
    ) {
        // Compose oldest-to-newest so the frontmost (newest) toast draws on top.
        // key(entry.id) keeps each toast's animation state with the toast itself
        // when siblings are removed or the list shifts.
        for (listIndex in entries.indices) {
            val entry = entries[listIndex]
            val index = entries.lastIndex - listIndex
            val cumPx = (0 until index).sumOf { i ->
                heights[entries[entries.lastIndex - i].id] ?: 0
            }
            val targetY = if (expanded) {
                with(density) { (cumPx + gap.roundToPx() * index).toDp() }
            } else {
                peek * index
            }
            val offsetY = if (isTop) targetY else -targetY
            val scale = if (expanded) 1f else (1f - STACK_SCALE_STEP * index).coerceAtLeast(0f)
            val clampedHeight = if (!expanded && index > 0) frontHeight else null

            key(entry.id) {
                ToastItem(
                    entry = entry,
                    manager = manager,
                    stacked = true,
                    expanded = expanded,
                    isTop = isTop,
                    offsetY = offsetY,
                    scale = scale,
                    clampedHeight = clampedHeight,
                    behind = clampedHeight != null,
                    heights = heights,
                )
            }
        }
    }
}

@Composable
private fun ColumnToastViewport(
    manager: ToastManager,
    entries: List<ToastEntry>,
    isTop: Boolean,
    onHover: (Boolean) -> Unit,
    heights: MutableMap<Long, Int>,
) {
    val gap = BaseTokens.token12

    Column(
        modifier = Modifier
            .widthIn(max = TwDimensions.maxWidthMaxWSm)
            .fillMaxWidth()
            .toastHoverTracker(onHover),
        verticalArrangement = Arrangement.spacedBy(gap),
    ) {
        val ordered = if (isTop) entries.asReversed() else entries
        ordered.forEach { entry ->
            key(entry.id) {
                ToastItem(
                    entry = entry,
                    manager = manager,
                    stacked = false,
                    expanded = true,
                    isTop = isTop,
                    offsetY = 0.dp,
                    scale = 1f,
                    clampedHeight = null,
                    behind = false,
                    heights = heights,
                )
            }
        }
    }
}

private fun Modifier.toastHoverTracker(onHover: (Boolean) -> Unit): Modifier =
    pointerInput(Unit) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent()
                when (event.type) {
                    PointerEventType.Enter -> onHover(true)
                    PointerEventType.Exit -> onHover(false)
                }
            }
        }
    }

@Composable
private fun ToastItem(
    entry: ToastEntry,
    manager: ToastManager,
    stacked: Boolean,
    expanded: Boolean,
    isTop: Boolean,
    offsetY: Dp,
    scale: Float,
    clampedHeight: Dp?,
    behind: Boolean,
    heights: MutableMap<Long, Int>,
) {
    val density = LocalDensity.current
    val colors = resolveToastColors(entry.variant)
    val radius = Theme[DimProps][DimTokens.radiusMd]
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val shape = RoundedCornerShape(radius)

    var hovered by remember { mutableStateOf(false) }
    val paused = hovered || entry.exiting

    // Auto-dismiss timer with pause on hover, Base UI style.
    val remainingMillis = remember(entry.id) { mutableStateOf(entry.durationMillis) }
    LaunchedEffect(entry.id) {
        if (entry.durationMillis <= 0L) return@LaunchedEffect
        snapshotFlow { paused }.collectLatest { isPaused ->
            if (isPaused) return@collectLatest
            while (remainingMillis.value > 0) {
                delay(TIMER_TICK_MILLIS)
                remainingMillis.value -= TIMER_TICK_MILLIS
            }
            manager.close(entry.id)
        }
    }

    // Enter / exit animations.
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { entered = true }
    val enterProgress by animateFloatAsState(
        targetValue = if (entered) 1f else 0f,
        animationSpec = tween(EXIT_ANIM_MILLIS, easing = ToastEasing),
        label = "toastEnter",
    )
    val exitProgress by animateFloatAsState(
        targetValue = if (entry.exiting) 1f else 0f,
        animationSpec = tween(EXIT_ANIM_MILLIS, easing = ToastEasing),
        label = "toastExit",
    )
    LaunchedEffect(entry.exiting) {
        if (entry.exiting) {
            delay(EXIT_ANIM_MILLIS + 50L)
            manager.finalizeRemove(entry.id)
        }
    }

    val ownHeight: Dp? = heights[entry.id]?.let { with(density) { it.toDp() } }
    val heightTarget = clampedHeight ?: ownHeight
    val animatedHeight by animateDpAsState(
        targetValue = heightTarget ?: 0.dp,
        animationSpec = tween(STACK_ANIM_MILLIS, easing = ToastEasing),
        label = "toastHeight",
    )
    val animatedOffsetY by animateDpAsState(
        targetValue = offsetY,
        animationSpec = tween(STACK_ANIM_MILLIS, easing = ToastEasing),
        label = "toastOffsetY",
    )
    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = tween(STACK_ANIM_MILLIS, easing = ToastEasing),
        label = "toastScale",
    )
    val behindAlpha by animateFloatAsState(
        targetValue = if (behind) 0f else 1f,
        animationSpec = tween(250, easing = ToastEasing),
        label = "toastBehind",
    )

    val slideSign = if (isTop) -1f else 1f
    val ownHeightPx = with(density) { (ownHeight ?: 64.dp).toPx() }
    val enterSlidePx = with(density) { BaseTokens.token24.toPx() }

    Box(
        modifier = Modifier
            .widthIn(max = TwDimensions.maxWidthMaxWSm)
            .fillMaxWidth()
            .offset(y = animatedOffsetY)
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
                transformOrigin = if (isTop) {
                    TransformOrigin(0.5f, 0f)
                } else {
                    TransformOrigin(0.5f, 1f)
                }
                alpha = enterProgress * (1f - exitProgress)
                translationY = slideSign * (
                    (1f - enterProgress) * enterSlidePx +
                        exitProgress * ownHeightPx
                    )
            }
            .then(
                if (stacked) {
                    Modifier.height(animatedHeight)
                } else {
                    Modifier
                },
            )
            .toastHoverTracker { hovered = it },
        contentAlignment = Alignment.TopStart,
    ) {
        // Natural-height measuring container: measures content even while the
        // surface box is clamped to the frontmost height.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(unbounded = true)
                .onSizeChanged { size -> heights[entry.id] = size.height },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape)
                    .clipToBounds()
                    .shadow(
                        elevation = Effects.boxShadowShadowMdToken0.radius,
                        shape = shape,
                        clip = false,
                        ambientColor = Effects.boxShadowShadowMdToken0.color,
                        spotColor = Effects.boxShadowShadowMdToken1.color,
                    )
                    .background(colors.background)
                    .border(borderWidth, colors.border, shape),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(TwDimensions.paddingPxToken4) // p-4
                        .graphicsLayer { alpha = behindAlpha },
                    horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken3),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (entry.variant == ToastVariant.Destructive) {
                        UnstyledIcon(
                            imageVector = Lucide.CircleAlert,
                            contentDescription = null,
                            modifier = Modifier.size(TwDimensions.heightHToken4),
                            tint = colors.icon,
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken1), // gap-1
                    ) {
                        BasicText(
                            text = entry.title,
                            style = TypographyStyles.textSmMedium.copy(color = colors.title),
                        )
                        entry.description?.let { description ->
                            BasicText(
                                text = description,
                                style = TypographyStyles.textSmRegular.copy(color = colors.description),
                            )
                        }
                    }
                    entry.action?.let { action ->
                        Button(
                            onClick = {
                                action.onClick()
                                manager.close(entry.id)
                            },
                            size = ButtonSize.Sm,
                            variant = ButtonVariant.Outline,
                        ) {
                            ButtonText(action.label)
                        }
                    }
                    UnstyledButton(
                        onClick = { manager.close(entry.id) },
                        indication = null,
                        modifier = Modifier.size(TwDimensions.heightHToken5),
                    ) {
                        UnstyledIcon(
                            imageVector = Lucide.X,
                            contentDescription = stringResource(Res.string.toast_dismiss_notification),
                            modifier = Modifier.size(TwDimensions.heightHToken4),
                            tint = colors.close,
                        )
                    }
                }
            }
        }
    }

    DisposableEffect(entry.id) {
        onDispose { heights.remove(entry.id) }
    }
}
