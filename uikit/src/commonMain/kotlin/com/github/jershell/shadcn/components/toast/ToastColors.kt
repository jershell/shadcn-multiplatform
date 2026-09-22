package com.github.jershell.shadcn.components.toast

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens

internal data class ToastColors(
    val background: Color,
    val border: Color,
    val title: Color,
    val description: Color,
    val icon: Color,
    val close: Color,
)

@Composable
internal fun resolveToastColors(variant: ToastVariant): ToastColors {
    val popover = Theme[ColorProps][ColorTokens.popover]
    val popoverForeground = Theme[ColorProps][ColorTokens.popoverForeground]
    val mutedForeground = Theme[ColorProps][ColorTokens.mutedForeground]
    val border = Theme[ColorProps][ColorTokens.border]
    val destructive = Theme[ColorProps][ColorTokens.destructive]

    return when (variant) {
        ToastVariant.Default -> ToastColors(
            background = popover,
            border = border,
            title = popoverForeground,
            description = mutedForeground,
            icon = popoverForeground,
            close = mutedForeground,
        )

        ToastVariant.Destructive -> ToastColors(
            background = popover,
            border = border,
            title = popoverForeground,
            description = mutedForeground,
            icon = destructive,
            close = mutedForeground,
        )
    }
}
