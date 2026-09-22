package com.github.jershell.shadcn.components.input

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.LocalThemeIsDark

internal data class TextFieldColors(
    val background: Color,
    val border: Color,
    val borderInvalid: Color,
    val focusRing: Color,
    val focusRingInvalid: Color,
    val content: Color,
    val placeholder: Color,
)

@Composable
internal fun resolveTextFieldColors(): TextFieldColors {
    val isDark by LocalThemeIsDark.current
    val background = Theme[ColorProps][ColorTokens.background]
    val border = Theme[ColorProps][ColorTokens.input]
    val destructive = Theme[ColorProps][ColorTokens.destructive]

    return TextFieldColors(
        // bg-transparent in light, dark:bg-input/30 in dark
        background = if (isDark) border.copy(alpha = 0.3f) else Color.Transparent,
        border = border,
        borderInvalid = destructive,
        focusRing = Theme[ColorProps][ColorTokens.ring],
        focusRingInvalid = destructive.copy(alpha = if (isDark) 0.4f else 0.2f),
        content = Theme[ColorProps][ColorTokens.foreground],
        placeholder = Theme[ColorProps][ColorTokens.mutedForeground],
    )
}
