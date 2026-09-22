package com.github.jershell.shadcn.components.datepicker

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.github.jershell.shadcn.components.button.ButtonColors
import com.github.jershell.shadcn.components.button.ButtonVariant
import com.github.jershell.shadcn.components.button.resolveButtonColors
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.composeunstyled.theme.Theme

internal data class DatePickerTriggerColors(
    val background: Color,
    val content: Color,
    val border: Color,
    val placeholder: Color,
)

@Composable
internal fun resolveDatePickerTriggerColors(
    isHovered: Boolean,
    isPressed: Boolean,
): DatePickerTriggerColors {
    val outline = resolveButtonColors(
        variant = ButtonVariant.Outline,
        isHovered = isHovered,
        isPressed = isPressed,
    )
    return DatePickerTriggerColors(
        background = outline.container,
        content = outline.content,
        border = outline.border ?: Theme[ColorProps][ColorTokens.input],
        placeholder = Theme[ColorProps][ColorTokens.mutedForeground],
    )
}