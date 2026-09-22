package com.github.jershell.shadcn.components.avatar

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens

internal data class AvatarColors(
    val fallbackBackground: Color,
    val fallbackContent: Color,
)

@Composable
internal fun resolveAvatarColors(): AvatarColors {
    val fallbackBackground = Theme[ColorProps][ColorTokens.muted]
    val fallbackContent = Theme[ColorProps][ColorTokens.mutedForeground]

    return AvatarColors(
        fallbackBackground = fallbackBackground,
        fallbackContent = fallbackContent,
    )
}
