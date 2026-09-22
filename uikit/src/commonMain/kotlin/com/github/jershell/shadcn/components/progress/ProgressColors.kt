package com.github.jershell.shadcn.components.progress

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens

internal data class ProgressColors(
    val track: Color,
    val indicator: Color,
)

@Composable
internal fun resolveProgressColors(): ProgressColors =
    ProgressColors(
        track = Theme[ColorProps][ColorTokens.secondary],
        indicator = Theme[ColorProps][ColorTokens.primary],
    )
