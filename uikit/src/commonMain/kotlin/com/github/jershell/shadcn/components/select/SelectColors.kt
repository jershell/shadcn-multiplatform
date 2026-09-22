package com.github.jershell.shadcn.components.select

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens

internal data class SelectTriggerColors(
    val background: Color,
    val border: Color,
    val content: Color,
    val placeholder: Color,
)

internal data class SelectPanelColors(
    val background: Color,
    val border: Color,
)

internal data class SelectItemColors(
    val content: Color,
    val selectedContent: Color,
    val selectedBackground: Color,
    val hoverBackground: Color,
)

@Composable
internal fun resolveSelectTriggerColors(): SelectTriggerColors {
    val background = Theme[ColorProps][ColorTokens.background]
    val border = Theme[ColorProps][ColorTokens.input]
    val content = Theme[ColorProps][ColorTokens.foreground]
    val placeholder = Theme[ColorProps][ColorTokens.mutedForeground]

    return SelectTriggerColors(
        background = background,
        border = border,
        content = content,
        placeholder = placeholder,
    )
}

@Composable
internal fun resolveSelectPanelColors(): SelectPanelColors {
    val background = Theme[ColorProps][ColorTokens.popover]
    val border = Theme[ColorProps][ColorTokens.border]

    return SelectPanelColors(
        background = background,
        border = border,
    )
}

@Composable
internal fun resolveSelectItemColors(): SelectItemColors {
    val accent = Theme[ColorProps][ColorTokens.accent]
    val accentForeground = Theme[ColorProps][ColorTokens.accentForeground]
    val foreground = Theme[ColorProps][ColorTokens.foreground]

    return SelectItemColors(
        content = foreground,
        selectedContent = accentForeground,
        selectedBackground = accent,
        hoverBackground = accent,
    )
}
