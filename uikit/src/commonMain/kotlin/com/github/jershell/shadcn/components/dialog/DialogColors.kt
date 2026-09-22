package com.github.jershell.shadcn.components.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens

internal data class DialogColors(
    val background: Color,
    val content: Color,
    val border: Color,
    val close: Color,
)

@Composable
internal fun resolveDialogColors(): DialogColors {
    val background = Theme[ColorProps][ColorTokens.background]
    val foreground = Theme[ColorProps][ColorTokens.foreground]
    val border = Theme[ColorProps][ColorTokens.border]

    return DialogColors(
        background = background,
        content = foreground,
        border = border,
        close = foreground.copy(alpha = 0.7f),
    )
}

/**
 * Dialog scrim color, matching the shadcn/ui `bg-black/50` overlay.
 *
 * The Figma token export has no scrim/overlay token yet; replace this constant
 * with a token once one is added (see BACKLOG.md).
 */
internal val DialogScrimColor: Color = Color.Black.copy(alpha = 0.5f)
