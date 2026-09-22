package com.github.jershell.shadcn.components.calendar

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens

internal data class CalendarColors(
    val selectedBackground: Color,
    val selectedForeground: Color,
    val hoverBackground: Color,
    val todayBorder: Color,
    val rangeBackground: Color,
    val rangeForeground: Color,
    val outsideForeground: Color,
    val dayForeground: Color,
    val weekdayHeaderForeground: Color,
    val headerForeground: Color,
    val navigationButtonForeground: Color,
    val disabledAlpha: Float,
)

@Composable
internal fun resolveCalendarColors(): CalendarColors {
    val primary = Theme[ColorProps][ColorTokens.primary]
    val primaryForeground = Theme[ColorProps][ColorTokens.primaryForeground]
    val accent = Theme[ColorProps][ColorTokens.accent]
    val accentForeground = Theme[ColorProps][ColorTokens.accentForeground]
    val foreground = Theme[ColorProps][ColorTokens.foreground]
    val mutedForeground = Theme[ColorProps][ColorTokens.mutedForeground]
    val border = Theme[ColorProps][ColorTokens.border]

    return CalendarColors(
        selectedBackground = primary,
        selectedForeground = primaryForeground,
        hoverBackground = accent,
        todayBorder = border,
        rangeBackground = accent,
        rangeForeground = accentForeground,
        outsideForeground = mutedForeground,
        dayForeground = foreground,
        weekdayHeaderForeground = mutedForeground,
        headerForeground = foreground,
        navigationButtonForeground = foreground,
        disabledAlpha = 0.5f,
    )
}
