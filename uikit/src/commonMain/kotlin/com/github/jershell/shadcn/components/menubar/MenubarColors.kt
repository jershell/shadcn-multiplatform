package com.github.jershell.shadcn.components.menubar

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens

internal data class MenubarBarColors(
    val background: Color,
    val border: Color,
)

internal data class MenubarTriggerColors(
    val background: Color,
    val content: Color,
)

@Composable
internal fun resolveMenubarBarColors(): MenubarBarColors {
    return MenubarBarColors(
        background = Theme[ColorProps][ColorTokens.background],
        border = Theme[ColorProps][ColorTokens.border],
    )
}

@Composable
internal fun resolveMenubarTriggerColors(
    isOpen: Boolean,
    isHovered: Boolean,
    isFocused: Boolean,
): MenubarTriggerColors {
    val highlighted = isOpen || isHovered || isFocused
    val accent = Theme[ColorProps][ColorTokens.accent]

    return if (highlighted) {
        MenubarTriggerColors(
            background = accent,
            content = Theme[ColorProps][ColorTokens.accentForeground],
        )
    } else {
        MenubarTriggerColors(
            background = Color.Transparent,
            content = Theme[ColorProps][ColorTokens.foreground],
        )
    }
}