package com.github.jershell.shadcn.components.drawer

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens

internal data class DrawerPanelColors(
    val background: Color,
    val border: Color,
    val content: Color,
    val muted: Color,
    val dragHandle: Color,
)

@Composable
internal fun resolveDrawerPanelColors(): DrawerPanelColors {
    return DrawerPanelColors(
        background = Theme[ColorProps][ColorTokens.background],
        border = Theme[ColorProps][ColorTokens.border],
        content = Theme[ColorProps][ColorTokens.foreground],
        muted = Theme[ColorProps][ColorTokens.mutedForeground],
        dragHandle = Theme[ColorProps][ColorTokens.muted],
    )
}

/** Scrim color of the reference: `bg-black/50`, shared with Dialog. */
internal val DrawerScrimColor: Color = Color.Black.copy(alpha = 0.5f)