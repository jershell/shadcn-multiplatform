package com.github.jershell.shadcn.components.switch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.LocalThemeIsDark

internal data class SwitchColors(
    val trackUnchecked: Color,
    val trackChecked: Color,
    val thumbUnchecked: Color,
    val thumbChecked: Color,
    val disabledAlpha: Float,
)

@Composable
internal fun resolveSwitchColors(): SwitchColors {
    val isDark by LocalThemeIsDark.current
    return SwitchColors(
        trackUnchecked = if (isDark) Theme[ColorProps][ColorTokens.input].copy(alpha = 0.8f) else Theme[ColorProps][ColorTokens.input],
        thumbUnchecked = if (isDark) Theme[ColorProps][ColorTokens.foreground] else Theme[ColorProps][ColorTokens.background],
        trackChecked = Theme[ColorProps][ColorTokens.primary],
        thumbChecked = Theme[ColorProps][ColorTokens.primaryForeground],
        disabledAlpha = 0.5f,
    )
}
