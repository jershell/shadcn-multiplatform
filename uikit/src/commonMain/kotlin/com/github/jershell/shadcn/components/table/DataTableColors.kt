package com.github.jershell.shadcn.components.table

import androidx.compose.runtime.Composable
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import androidx.compose.ui.graphics.Color

internal data class DataTableColors(
    val background: Color,
    val headerBackground: Color,
    val border: Color,
    val headerForeground: Color,
    val cellForeground: Color,
    val captionForeground: Color,
)

@Composable
internal fun resolveDataTableColors(): DataTableColors =
    DataTableColors(
        background = Theme[ColorProps][ColorTokens.background],
        headerBackground = Theme[ColorProps][ColorTokens.muted],
        border = Theme[ColorProps][ColorTokens.border],
        headerForeground = Theme[ColorProps][ColorTokens.mutedForeground],
        cellForeground = Theme[ColorProps][ColorTokens.foreground],
        captionForeground = Theme[ColorProps][ColorTokens.mutedForeground],
    )
