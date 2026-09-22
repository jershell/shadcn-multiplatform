package com.github.jershell.shadcn.components.accordion

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens

internal data class AccordionColors(
    val background: Color,
    val border: Color,
    val triggerContent: Color,
    val content: Color,
    val chevron: Color,
)

@Composable
internal fun resolveAccordionColors(): AccordionColors {
    val background = Theme[ColorProps][ColorTokens.background]
    val border = Theme[ColorProps][ColorTokens.border]
    val triggerContent = Theme[ColorProps][ColorTokens.foreground]
    val content = Theme[ColorProps][ColorTokens.mutedForeground]
    val chevron = Theme[ColorProps][ColorTokens.mutedForeground]

    return AccordionColors(
        background = background,
        border = border,
        triggerContent = triggerContent,
        content = content,
        chevron = chevron,
    )
}
