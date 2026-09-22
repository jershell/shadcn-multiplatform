package com.github.jershell.shadcn.components.badge

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens

/**
 * Visual variants of the [Badge] component, matching the shadcn/ui variants.
 */
enum class BadgeVariant {
    Default,
    Secondary,
    Destructive,
    Outline,
    Chip,
}

internal data class BadgeColors(
    val background: Color,
    val content: Color,
    val border: Color,
)

@Composable
internal fun resolveBadgeColors(variant: BadgeVariant): BadgeColors {
    val primary = Theme[ColorProps][ColorTokens.primary]
    val primaryForeground = Theme[ColorProps][ColorTokens.primaryForeground]
    val secondary = Theme[ColorProps][ColorTokens.secondary]
    val secondaryForeground = Theme[ColorProps][ColorTokens.secondaryForeground]
    val destructive = Theme[ColorProps][ColorTokens.destructive]
    val foreground = Theme[ColorProps][ColorTokens.foreground]
    val border = Theme[ColorProps][ColorTokens.border]
    val muted = Theme[ColorProps][ColorTokens.muted]

    return when (variant) {
        BadgeVariant.Default -> BadgeColors(
            background = primary,
            content = primaryForeground,
            border = Color.Transparent,
        )
        BadgeVariant.Secondary -> BadgeColors(
            background = secondary,
            content = secondaryForeground,
            border = Color.Transparent,
        )
        BadgeVariant.Destructive -> BadgeColors(
            background = destructive,
            content = primaryForeground,
            border = Color.Transparent,
        )
        BadgeVariant.Outline -> BadgeColors(
            background = Color.Transparent,
            content = foreground,
            border = border,
        )
        BadgeVariant.Chip -> BadgeColors(
            background = muted,
            content = foreground,
            border = Color.Transparent,
        )
    }
}
