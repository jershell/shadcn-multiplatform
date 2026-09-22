package com.github.jershell.shadcn.components.slider

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.TwColors

internal data class SliderColors(
    val track: Color,
    val range: Color,
    val thumbBorder: Color,
    val thumbBackground: Color,
    val focusRing: Color,
)

@Composable
internal fun resolveSliderColors(): SliderColors {
    val muted = Theme[ColorProps][ColorTokens.muted]
    val primary = Theme[ColorProps][ColorTokens.primary]
    val ring = Theme[ColorProps][ColorTokens.ring]

    return SliderColors(
        track = muted,
        range = primary,
        thumbBorder = primary,
        // Reference: bg-white on the thumb (not bg-background) — it stays white in the dark theme.
        thumbBackground = TwColors.white,
        focusRing = ring.copy(alpha = 0.5f),
    )
}
