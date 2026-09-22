package com.github.jershell.shadcn.components.progress

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.TwDimensions

/**
 * A progress indicator styled after shadcn/ui.
 *
 * @param value Progress value in range [0f, 1f].
 * @param modifier Modifier applied to the root container.
 * @param thickness Progress bar thickness.
 * @param animate Whether value changes should animate.
 */
@Composable
fun Progress(
    value: Float,
    modifier: Modifier = Modifier,
    thickness: Dp = TwDimensions.heightHToken2,
    animate: Boolean = true,
) {
    val clamped = value.coerceIn(0f, 1f)
    val colors = resolveProgressColors()
    val radius = Theme[DimProps][DimTokens.radiusFull]
    val shape = RoundedCornerShape(radius)
    val animatedValue = if (animate) {
        animateFloatAsState(
            targetValue = clamped,
            animationSpec = tween(durationMillis = 250),
            label = "ProgressValue",
        ).value
    } else {
        clamped
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness)
            .clip(shape)
            .background(colors.track),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedValue)
                .height(thickness)
                .background(colors.indicator),
        )
    }
}
