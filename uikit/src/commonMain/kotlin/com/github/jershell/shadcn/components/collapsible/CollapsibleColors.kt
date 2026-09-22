package com.github.jershell.shadcn.components.collapsible

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens

internal data class CollapsibleColors(
    val container: Color,
    val content: Color,
    val border: Color,
    val icon: Color,
    val focusRing: Color,
)

@Composable
internal fun resolveCollapsibleColors(isHovered: Boolean): CollapsibleColors {
    val foreground = Theme[ColorProps][ColorTokens.foreground]
    val mutedForeground = Theme[ColorProps][ColorTokens.mutedForeground]
    val muted = Theme[ColorProps][ColorTokens.muted]
    val border = Theme[ColorProps][ColorTokens.border]
    val ring = Theme[ColorProps][ColorTokens.ring]

    return CollapsibleColors(
        container = if (isHovered) muted else Color.Transparent,
        content = foreground,
        border = border,
        icon = mutedForeground,
        focusRing = ring.copy(alpha = 0.5f),
    )
}
