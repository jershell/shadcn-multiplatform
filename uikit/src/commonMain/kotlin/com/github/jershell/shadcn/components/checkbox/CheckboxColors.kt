package com.github.jershell.shadcn.components.checkbox

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens

internal data class CheckboxColors(
    val background: Color,
    val border: Color,
    val content: Color,
    val checkedBackground: Color,
    val checkedContent: Color,
)

@Composable
internal fun resolveCheckboxColors(): CheckboxColors {
    val background = Theme[ColorProps][ColorTokens.background]
    val border = Theme[ColorProps][ColorTokens.border]
    val content = Theme[ColorProps][ColorTokens.foreground]
    val checkedBackground = Theme[ColorProps][ColorTokens.primary]
    val checkedContent = Theme[ColorProps][ColorTokens.primaryForeground]

    return CheckboxColors(
        background = background,
        border = border,
        content = content,
        checkedBackground = checkedBackground,
        checkedContent = checkedContent,
    )
}
