package com.github.jershell.shadcn.components.breadcrumb

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens

internal data class BreadcrumbColors(
    val muted: Color,
    val foreground: Color,
    val separator: Color,
)

@Composable
internal fun resolveBreadcrumbColors(): BreadcrumbColors {
    val muted = Theme[ColorProps][ColorTokens.mutedForeground]
    val foreground = Theme[ColorProps][ColorTokens.foreground]
    return BreadcrumbColors(
        muted = muted,
        foreground = foreground,
        separator = muted,
    )
}
