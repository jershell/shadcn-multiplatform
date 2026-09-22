package com.github.jershell.shadcn.components.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.LocalThemeIsDark

internal data class TabsColors(
    val listBackground: Color,
    val activeBackground: Color,
    val activeContent: Color,
    val inactiveContent: Color,
    val disabledContent: Color,
)

@Composable
internal fun resolveTabsColors(): TabsColors {
    val localTheme = LocalThemeIsDark.current
    val muted = Theme[ColorProps][ColorTokens.muted]
    val background = Theme[ColorProps][ColorTokens.background]
    val foreground = Theme[ColorProps][ColorTokens.foreground]
    val mutedForeground = Theme[ColorProps][ColorTokens.mutedForeground]

    return remember(localTheme.value) {
        TabsColors(
            listBackground = muted,
            activeBackground = background,
            activeContent = foreground,
            inactiveContent = mutedForeground,
            disabledContent = mutedForeground.copy(alpha = 0.5f),
        )
    }
}
