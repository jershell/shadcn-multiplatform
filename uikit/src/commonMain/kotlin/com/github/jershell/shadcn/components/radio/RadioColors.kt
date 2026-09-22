package com.github.jershell.shadcn.components.radio

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens

internal data class RadioColors(
    val background: Color,
    val border: Color,
    val content: Color,
    val selectedBackground: Color,
    val selectedContent: Color,
)

@Composable
internal fun resolveRadioColors(): RadioColors {
    val background = Theme[ColorProps][ColorTokens.background]
    val border = Theme[ColorProps][ColorTokens.input]
    val content = Theme[ColorProps][ColorTokens.foreground]
    val selectedBackground = Theme[ColorProps][ColorTokens.primary]
    val selectedContent = Theme[ColorProps][ColorTokens.primaryForeground]

    return RadioColors(
        background = background,
        border = border,
        content = content,
        selectedBackground = selectedBackground,
        selectedContent = selectedContent,
    )
}
