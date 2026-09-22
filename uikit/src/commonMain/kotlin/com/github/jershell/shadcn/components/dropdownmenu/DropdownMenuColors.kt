package com.github.jershell.shadcn.components.dropdownmenu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.LocalThemeIsDark

internal data class DropdownMenuPanelColors(
    val background: Color,
    val content: Color,
    val border: Color,
)

internal data class DropdownMenuItemColors(
    val content: Color,
    val icon: Color,
    val focusBackground: Color,
    val focusContent: Color,
)

@Composable
internal fun resolveDropdownMenuPanelColors(): DropdownMenuPanelColors {
    val popover = Theme[ColorProps][ColorTokens.popover]
    val popoverForeground = Theme[ColorProps][ColorTokens.popoverForeground]
    val border = Theme[ColorProps][ColorTokens.border]

    return DropdownMenuPanelColors(
        background = popover,
        content = popoverForeground,
        border = border,
    )
}

@Composable
internal fun resolveDropdownMenuItemColors(
    variant: DropdownMenuItemVariant,
): DropdownMenuItemColors {
    val isDark by LocalThemeIsDark.current
    val destructive = Theme[ColorProps][ColorTokens.destructive]
    val accent = Theme[ColorProps][ColorTokens.accent]
    val accentForeground = Theme[ColorProps][ColorTokens.accentForeground]
    val mutedForeground = Theme[ColorProps][ColorTokens.mutedForeground]
    val popoverForeground = Theme[ColorProps][ColorTokens.popoverForeground]

    return when (variant) {
        DropdownMenuItemVariant.Default -> DropdownMenuItemColors(
            content = popoverForeground,
            icon = mutedForeground,
            focusBackground = accent,
            focusContent = accentForeground,
        )

        DropdownMenuItemVariant.Destructive -> DropdownMenuItemColors(
            content = destructive,
            icon = destructive,
            focusBackground = destructive.copy(alpha = if (isDark) 0.2f else 0.1f),
            focusContent = destructive,
        )
    }
}