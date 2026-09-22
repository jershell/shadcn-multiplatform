package com.github.jershell.shadcn.components.popover

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens

internal data class PopoverContentColors(
    val background: Color,
    val content: Color,
    val border: Color,
)

@Composable
internal fun resolvePopoverContentColors(): PopoverContentColors = PopoverContentColors(
    background = Theme[ColorProps][ColorTokens.popover],
    content = Theme[ColorProps][ColorTokens.popoverForeground],
    border = Theme[ColorProps][ColorTokens.border],
)
