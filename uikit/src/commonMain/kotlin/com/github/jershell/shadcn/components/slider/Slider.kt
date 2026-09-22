package com.github.jershell.shadcn.components.slider

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.composeunstyled.UnstyledSlider
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.BaseTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.Effects
import com.github.jershell.shadcn.theme.TwDimensions
import kotlin.math.abs

/** Sizes per the Figma layout: 4px track height, 10px thumb diameter. */
internal val SliderTrackSize: androidx.compose.ui.unit.Dp = BaseTokens.token4
internal val SliderThumbSize: androidx.compose.ui.unit.Dp = BaseTokens.token10

/**
 * A range input styled after the shadcn/ui Slider: a `h-1.5 rounded-full bg-muted`
 * track with a `bg-primary` filled range and a `size-4` circular thumb with a
 * white background and `ring-ring/50` hover/focus ring.
 *
 * Built on [com.composeunstyled.UnstyledSlider], which provides dragging, tap to
 * jump, keyboard stepping (arrows, Home/End, PageUp/PageDown) and progress semantics.
 *
 * @param value Current value within [valueRange].
 * @param onValueChange Called while the value changes (drag, tap, keyboard).
 * @param modifier Modifier applied to the slider root.
 * @param enabled Whether the slider is interactive; disabled sliders are dimmed.
 * @param valueRange Allowed value range.
 * @param steps Number of discrete steps between the range ends; `0` for a continuous slider.
 * @param onValueChangeFinished Called when a gesture or keyboard interaction finishes.
 * @param orientation Track orientation; vertical sliders need externally bounded height.
 */
@Composable
fun Slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    orientation: Orientation = Orientation.Horizontal,
) {
    val colors = resolveSliderColors()
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]

    UnstyledSlider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .then(
                when (orientation) {
                    // w-full
                    Orientation.Horizontal -> Modifier.fillMaxWidth()
                    // data-[orientation=vertical]:min-h-44
                    Orientation.Vertical -> Modifier.heightIn(min = BaseTokens.token176)
                },
            )
            .alpha(if (enabled) 1f else 0.5f) // data-[disabled]:opacity-50
            .hoverable(interactionSource = interactionSource, enabled = enabled),
        enabled = enabled,
        interactionSource = interactionSource,
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = onValueChangeFinished,
        orientation = orientation,
        track = { state ->
            SliderTrack(
                state = state,
                orientation = orientation,
                colors = colors,
            )
        },
        thumb = { state ->
            SliderThumb(
                ringVisible = isHovered || state.isPressed || state.isFocused,
                colors = colors,
                borderWidth = borderWidth,
            )
        },
    )
}

/**
 * A two-thumb range slider styled after the shadcn/ui Slider: taps and drags are
 * routed to the nearest thumb, keyboard arrows move the active thumb.
 *
 * The [com.composeunstyled.UnstyledSlider] primitive is single-valued, so this
 * composable implements the gesture layer on top of the same styled track and thumbs.
 *
 * @param value Current selected range within [valueRange]; `value.start <= value.endInclusive`.
 * @param onValueChange Called while the range changes.
 * @param modifier Modifier applied to the slider root.
 * @param enabled Whether the slider is interactive; disabled sliders are dimmed.
 * @param valueRange Allowed value range.
 * @param steps Number of discrete steps between the range ends; `0` for a continuous slider.
 * @param onValueChangeFinished Called when a gesture or keyboard interaction finishes.
 */
@Composable
fun RangeSlider(
    value: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
) {
    val colors = resolveSliderColors()
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val isFocused by interactionSource.collectIsFocusedAsState()
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    // 0 = lower (start) bound, 1 = upper (endInclusive)
    var activeThumb by remember { mutableIntStateOf(0) }
    var rootSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current
    // The gesture layer outlives a single composition: read the value through
    // State, otherwise the commit functions see "frozen" range bounds.
    val currentValue by rememberUpdatedState(value)

    val thumbRadiusPx = with(density) { (SliderThumbSize / 2).roundToPx() }
    val trackSpanPx = (rootSize.width - 2 * thumbRadiusPx).coerceAtLeast(0)
    fun fractionOf(v: Float): Float = if (valueRange.endInclusive - valueRange.start == 0f) {
        0f
    } else {
        ((v - valueRange.start) / (valueRange.endInclusive - valueRange.start)).coerceIn(0f, 1f)
    }
    fun snap(v: Float): Float {
        val coerced = v.coerceIn(valueRange.start, valueRange.endInclusive)
        if (steps <= 0) return coerced
        val tickCount = steps + 1
        val stepSize = (valueRange.endInclusive - valueRange.start) / tickCount
        val index = ((coerced - valueRange.start) / stepSize).let { kotlin.math.floor(it + 0.5f) }
        return valueRange.start + index * stepSize
    }
    fun valueAt(xPx: Float): Float {
        val clamped = xPx.coerceIn(0f, trackSpanPx.toFloat())
        val fraction = if (trackSpanPx == 0) 0f else clamped / trackSpanPx
        return valueRange.start + fraction * (valueRange.endInclusive - valueRange.start)
    }
    fun lowerFraction() = fractionOf(currentValue.start)
    fun upperFraction() = fractionOf(currentValue.endInclusive)

    fun commitLower(v: Float) {
        val snapped = snap(v).coerceAtMost(currentValue.endInclusive)
        if (snapped != currentValue.start) onValueChange(snapped..currentValue.endInclusive)
    }

    fun commitUpper(v: Float) {
        val snapped = snap(v).coerceAtLeast(currentValue.start)
        if (snapped != currentValue.endInclusive) onValueChange(currentValue.start..snapped)
    }

    val gestureModifier = if (enabled) {
        Modifier
            .pointerInput(valueRange, steps) {
                awaitEachGesture {
                    val down = awaitFirstDown()
                    val downX = down.position.x
                    val lowerX = thumbRadiusPx + trackSpanPx * lowerFraction()
                    val upperX = thumbRadiusPx + trackSpanPx * upperFraction()
                    activeThumb = if (abs(downX - lowerX) <= abs(downX - upperX)) 0 else 1

                    fun apply(x: Float) {
                        val target = valueAt(x - thumbRadiusPx)
                        if (activeThumb == 0) {
                            commitLower(target)
                        } else {
                            commitUpper(target)
                        }
                    }

                    var drag = false
                    while (true) {
                        val event = awaitPointerEvent()
                        val change = event.changes.firstOrNull() ?: break
                        if (change.changedToUp()) break
                        val delta = change.positionChange().x
                        if (delta != 0f) {
                            drag = true
                            apply(change.position.x)
                            change.consume()
                        }
                    }
                    if (drag) {
                        onValueChangeFinished?.invoke()
                    }
                }
            }
            .focusable(enabled = enabled, interactionSource = interactionSource)
    } else {
        Modifier
    }

    val keyboardModifier = if (enabled) {
        Modifier.keyboardRangeStepping(
            value = value,
            valueRange = valueRange,
            steps = steps,
            activeThumbProvider = { activeThumb },
            onValueChange = onValueChange,
            onValueChangeFinished = onValueChangeFinished,
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            // root height = thumb height: the layers center exactly on the track line
            .height(SliderThumbSize)
            .alpha(if (enabled) 1f else 0.5f) // data-[disabled]:opacity-50
            .hoverable(interactionSource = interactionSource, enabled = enabled)
            .then(gestureModifier)
            .then(keyboardModifier)
            .onSizeChanged { rootSize = it },
    ) {
        // Track with the range
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.CenterStart,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(BaseTokens.token6) // h-1.5
                    .clip(CircleShape) // rounded-full
                    .background(colors.track),
            ) {
                // Range between the thumbs
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(upperFraction())
                        .background(colors.range),
                )
                // Start segment is painted back with the track color
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(lowerFraction())
                        .background(colors.track),
                )
            }
        }
        // Lower thumb
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterStart,
        ) {
            with(density) {
                SliderThumb(
                    ringVisible = isHovered || isPressed || (isFocused && activeThumb == 0),
                    colors = colors,
                    borderWidth = borderWidth,
                    modifier = Modifier.offset(x = (trackSpanPx * lowerFraction()).toDp()),
                )
            }
        }
        // Upper thumb
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterStart,
        ) {
            with(density) {
                SliderThumb(
                    ringVisible = isHovered || isPressed || (isFocused && activeThumb == 1),
                    colors = colors,
                    borderWidth = borderWidth,
                    modifier = Modifier.offset(x = (trackSpanPx * upperFraction()).toDp()),
                )
            }
        }
    }
}

private fun Modifier.keyboardRangeStepping(
    value: ClosedFloatingPointRange<Float>,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    activeThumbProvider: () -> Int,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onValueChangeFinished: (() -> Unit)?,
): Modifier = onKeyEvent { event ->
    if (event.type != KeyEventType.KeyDown) {
        return@onKeyEvent false
    }
    val span = valueRange.endInclusive - valueRange.start
    val stepSize = if (steps > 0) span / (steps + 1) else span * 0.01f
    val isLower = activeThumbProvider() == 0
    val current = if (isLower) value.start else value.endInclusive
    val next = when (event.key) {
        Key.DirectionRight, Key.DirectionUp -> current + stepSize
        Key.DirectionLeft, Key.DirectionDown -> current - stepSize
        Key.MoveHome -> if (isLower) valueRange.start else value.start
        Key.MoveEnd -> if (isLower) value.endInclusive else valueRange.endInclusive
        else -> return@onKeyEvent false
    }
    val coerced = next.coerceIn(
        if (isLower) valueRange.start else value.start,
        if (isLower) value.endInclusive else valueRange.endInclusive,
    )
    if (isLower) {
        onValueChange(coerced..value.endInclusive)
    } else {
        onValueChange(value.start..coerced)
    }
    onValueChangeFinished?.invoke()
    true
}

@Composable
private fun SliderTrack(
    state: com.composeunstyled.SliderState,
    orientation: Orientation,
    colors: SliderColors,
) {
    val trackModifier = when (orientation) {
        Orientation.Horizontal -> Modifier
            .fillMaxWidth()
            .height(SliderTrackSize)
        Orientation.Vertical -> Modifier
            .fillMaxHeight()
            .width(SliderTrackSize)
    }

    Box(
        modifier = trackModifier
            .clip(CircleShape) // rounded-full
            .background(colors.track),
        contentAlignment = when (orientation) {
            Orientation.Horizontal -> Alignment.CenterStart
            Orientation.Vertical -> Alignment.BottomCenter
        },
    ) {
        // Range: the track part filled from the start up to the thumb.
        Box(
            modifier = when (orientation) {
                Orientation.Horizontal -> Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(state.fraction)
                Orientation.Vertical -> Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(state.fraction)
            }
                .background(colors.range),
        )
    }
}

@Composable
private fun SliderThumb(
    ringVisible: Boolean,
    colors: SliderColors,
    borderWidth: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier,
) {
    val ringColor by animateColorAsState(
        targetValue = if (ringVisible) colors.focusRing else Color.Transparent,
        animationSpec = tween(150),
        label = "sliderThumbRing",
    )

    // The slot is exactly size-4: the primitive positions it edge to edge, and
    // ring-4 is drawn outside the slot bounds via drawBehind.
    Box(
        modifier = modifier
            .size(SliderThumbSize)
            .drawBehind {
                if (ringColor != Color.Transparent) {
                    // ring-4 around size-4: diameter 16 + 2 * 4
                    drawCircle(
                        color = ringColor,
                        radius = size.minDimension / 2 + BaseTokens.token4.toPx(),
                    )
                }
            }
            .shadow(
                elevation = Effects.boxShadowShadowSm.radius, // shadow-sm
                shape = CircleShape,
                clip = false,
                ambientColor = Effects.boxShadowShadowSm.color,
                spotColor = Effects.boxShadowShadowSm.color,
            )
            .clip(CircleShape)
            .background(colors.thumbBackground)
            .border(borderWidth, colors.thumbBorder, CircleShape),
    )
}
