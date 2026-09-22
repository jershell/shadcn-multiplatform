package com.github.jershell.shadcn.components.tooltip

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens

internal data class TooltipColors(
    val container: Color,
    val content: Color,
)

@Composable
internal fun resolveTooltipColors() = TooltipColors(
    // bg-foreground text-background — inverted panel, like shadcn/ui
    container = Theme[ColorProps][ColorTokens.foreground],
    content = Theme[ColorProps][ColorTokens.background],
)
