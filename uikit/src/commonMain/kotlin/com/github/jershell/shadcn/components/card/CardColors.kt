package com.github.jershell.shadcn.components.card

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens

internal data class CardColors(
    val background: Color,
    val foreground: Color,
    val border: Color,
)

@Composable
internal fun resolveCardColors(): CardColors {
    return CardColors(
        background = Theme[ColorProps][ColorTokens.card],
        foreground = Theme[ColorProps][ColorTokens.cardForeground],
        border = Theme[ColorProps][ColorTokens.border],
    )
}
