package com.github.jershell.shadcn.components.kbd

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens

internal data class KbdColors(
    val container: Color,
    val content: Color,
)

@Composable
internal fun resolveKbdColors() = KbdColors(
    container = Theme[ColorProps][ColorTokens.muted],
    content = Theme[ColorProps][ColorTokens.mutedForeground],
)
