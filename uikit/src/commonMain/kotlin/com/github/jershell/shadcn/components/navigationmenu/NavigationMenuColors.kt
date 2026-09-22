package com.github.jershell.shadcn.components.navigationmenu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.LocalThemeIsDark

internal data class NavigationMenuTriggerColors(
    val background: Color,
    val content: Color,
)

internal data class NavigationMenuLinkColors(
    val background: Color,
    val content: Color,
    val description: Color,
)

@Composable
internal fun resolveNavigationMenuTriggerColors(
    isOpen: Boolean,
    isHovered: Boolean,
    isFocused: Boolean,
): NavigationMenuTriggerColors {
    val accent = Theme[ColorProps][ColorTokens.accent]
    val accentForeground = Theme[ColorProps][ColorTokens.accentForeground]
    val background = Theme[ColorProps][ColorTokens.background]

    return when {
        isOpen -> NavigationMenuTriggerColors(
            background = accent.copy(alpha = 0.5f), // data-[state=open]:bg-accent/50
            content = accentForeground,
        )

        isHovered || isFocused -> NavigationMenuTriggerColors(
            background = accent,
            content = accentForeground,
        )

        else -> NavigationMenuTriggerColors(
            background = background,
            content = Theme[ColorProps][ColorTokens.foreground],
        )
    }
}

@Composable
internal fun resolveNavigationMenuLinkColors(
    isActive: Boolean,
    isHovered: Boolean,
    isFocused: Boolean,
): NavigationMenuLinkColors {
    val accent = Theme[ColorProps][ColorTokens.accent]
    val accentForeground = Theme[ColorProps][ColorTokens.accentForeground]

    return when {
        isHovered || isFocused -> NavigationMenuLinkColors(
            background = accent,
            content = accentForeground,
            description = accentForeground.copy(alpha = 0.7f),
        )

        isActive -> NavigationMenuLinkColors(
            background = accent.copy(alpha = 0.5f), // data-[active=true]:bg-accent/50
            content = accentForeground,
            description = accentForeground.copy(alpha = 0.7f),
        )

        else -> NavigationMenuLinkColors(
            background = Color.Transparent,
            content = Theme[ColorProps][ColorTokens.foreground],
            description = Theme[ColorProps][ColorTokens.mutedForeground],
        )
    }
}