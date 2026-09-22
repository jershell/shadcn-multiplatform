package com.github.jershell.shadcn.components.skeleton

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens

/**
 * A loading placeholder styled after the shadcn/ui Skeleton: a `rounded-md bg-accent`
 * block with a pulsing opacity animation (tailwind `animate-pulse`).
 *
 * The size comes from the caller's modifier (fixed size, `fillMaxWidth`, etc.).
 *
 * @param modifier Modifier that determines the skeleton size and placement.
 * @param shape Shape of the block.
 */
@Composable
fun Skeleton(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(Theme[DimProps][DimTokens.radiusMd]),
) {
    // tailwind animate-pulse: opacity 1 -> 0.5 -> 1 over 2s, cubic-bezier(0.4, 0, 0.6, 1)
    val pulse = rememberInfiniteTransition(label = "skeletonPulse")
    val alpha by pulse.animateFloat(
        initialValue = 1f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = CubicBezierEasing(0.4f, 0f, 0.6f, 1f),
            ),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "skeletonPulseAlpha",
    )

    Box(
        modifier = modifier
            .alpha(alpha)
            .clip(shape)
            .background(Theme[ColorProps][ColorTokens.accent]),
    )
}
