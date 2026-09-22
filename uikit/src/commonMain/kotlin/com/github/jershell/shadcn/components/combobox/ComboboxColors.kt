package com.github.jershell.shadcn.components.combobox

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens

internal data class ComboboxInputColors(
    val background: Color,
    val border: Color,
    val content: Color,
    val placeholder: Color,
)

internal data class ComboboxPanelColors(
    val background: Color,
    val border: Color,
)

internal data class ComboboxItemColors(
    val content: Color,
    val selectedContent: Color,
    val selectedBackground: Color,
    val hoverBackground: Color,
)

internal data class ComboboxEmptyColors(
    val content: Color,
)

internal data class ComboboxLabelColors(
    val content: Color,
)

@Composable
internal fun resolveComboboxInputColors(): ComboboxInputColors {
    val background = Theme[ColorProps][ColorTokens.background]
    val border = Theme[ColorProps][ColorTokens.input]
    val content = Theme[ColorProps][ColorTokens.foreground]
    val placeholder = Theme[ColorProps][ColorTokens.mutedForeground]

    return ComboboxInputColors(
        background = background,
        border = border,
        content = content,
        placeholder = placeholder,
    )
}

@Composable
internal fun resolveComboboxPanelColors(): ComboboxPanelColors {
    val background = Theme[ColorProps][ColorTokens.popover]
    val border = Theme[ColorProps][ColorTokens.border]

    return ComboboxPanelColors(
        background = background,
        border = border,
    )
}

@Composable
internal fun resolveComboboxItemColors(): ComboboxItemColors {
    val accent = Theme[ColorProps][ColorTokens.accent]
    val accentForeground = Theme[ColorProps][ColorTokens.accentForeground]
    val foreground = Theme[ColorProps][ColorTokens.foreground]

    return ComboboxItemColors(
        content = foreground,
        selectedContent = accentForeground,
        selectedBackground = accent,
        hoverBackground = accent,
    )
}

@Composable
internal fun resolveComboboxEmptyColors(): ComboboxEmptyColors {
    val content = Theme[ColorProps][ColorTokens.mutedForeground]

    return ComboboxEmptyColors(
        content = content,
    )
}

@Composable
internal fun resolveComboboxLabelColors(): ComboboxLabelColors {
    val content = Theme[ColorProps][ColorTokens.mutedForeground]

    return ComboboxLabelColors(
        content = content,
    )
}
