package com.github.jershell.shadcn.components.alert

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens

internal data class AlertColors(
    val background: Color,
    val border: Color,
    val icon: Color,
    val title: Color,
    val description: Color,
)

@Composable
internal fun resolveAlertColors(variant: AlertVariant): AlertColors {
    val background = Theme[ColorProps][ColorTokens.background]
    val foreground = Theme[ColorProps][ColorTokens.foreground]
    val border = Theme[ColorProps][ColorTokens.border]
    val destructive = Theme[ColorProps][ColorTokens.destructive]

    return when (variant) {
        AlertVariant.Default -> AlertColors(
            background = background,
            border = border,
            icon = foreground,
            title = foreground,
            description = foreground,
        )
        AlertVariant.Destructive -> AlertColors(
            background = background,
            border = destructive.copy(alpha = 0.5f),
            icon = destructive,
            title = destructive,
            description = destructive,
        )
    }
}
