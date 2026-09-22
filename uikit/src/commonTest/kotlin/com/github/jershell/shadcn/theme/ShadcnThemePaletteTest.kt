package com.github.jershell.shadcn.theme

import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.ThemeToken
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Palette resolve rules of the theme builder, mirroring the reference
 * `legacy-themes.css` (`.theme-amber` etc.):
 * - the generated default palette provides the tokens the scales do not define
 *   (destructive, ...);
 * - the base color scale overrides background/foreground/muted/border/...;
 * - the accent (`theme`) scale overrides ONLY primary/ring/sidebarPrimary
 *   (light: 600/400, dark: 500/900) — `accent` stays neutral from the base;
 * - chart1..5 follow the theme scale unless an explicit chart color is set
 *   (shades 300/500/600/700/800).
 */
class ShadcnThemePaletteTest {

    private fun defaultLight(token: ThemeToken<Color>) = themeLightSemanticColors.getValue(token)
    private fun defaultDark(token: ThemeToken<Color>) = themeDarkSemanticColors.getValue(token)

    @Test
    fun neutralWithoutThemeKeepsGeneratedDefault() {
        val palette = ShadcnPreset(baseColor = "neutral").lightPalette()
        assertEquals(defaultLight(ColorTokens.primary), palette?.getValue(ColorTokens.primary))
        assertEquals(defaultLight(ColorTokens.destructive), palette?.getValue(ColorTokens.destructive))
        assertEquals(defaultLight(ColorTokens.chartToken1), palette?.getValue(ColorTokens.chartToken1))
    }

    @Test
    fun accentScaleOverridesPrimaryRingLight() {
        val palette = ShadcnPreset(baseColor = "neutral", theme = "amber").lightPalette()
        // amber-600
        assertEquals(Color(0xFFD97706), palette?.getValue(ColorTokens.primary))
        // amber-50
        assertEquals(Color(0xFFFFFBEB), palette?.getValue(ColorTokens.primaryForeground))
        // amber-400
        assertEquals(Color(0xFFFBBF24), palette?.getValue(ColorTokens.ring))
        // accent stays neutral from the base scale (hover surface, not themed)
        assertEquals(defaultLight(ColorTokens.accent), palette?.getValue(ColorTokens.accent))
        // background stays from the base (neutral = generated default)
        assertEquals(defaultLight(ColorTokens.background), palette?.getValue(ColorTokens.background))
    }

    @Test
    fun accentScaleOverridesPrimaryRingDark() {
        val palette = ShadcnPreset(baseColor = "neutral", theme = "amber").darkPalette()
        // amber-500
        assertEquals(Color(0xFFF59E0B), palette?.getValue(ColorTokens.primary))
        // amber-50
        assertEquals(Color(0xFFFFFBEB), palette?.getValue(ColorTokens.primaryForeground))
        // amber-900
        assertEquals(Color(0xFF78350F), palette?.getValue(ColorTokens.ring))
    }

    @Test
    fun chartsFollowThemeScaleWhenChartColorIsNull() {
        val palette = ShadcnPreset(baseColor = "neutral", theme = "amber").lightPalette()
        // amber-300 / 500 / 800
        assertEquals(Color(0xFFFCD34D), palette?.getValue(ColorTokens.chartToken1))
        assertEquals(Color(0xFFF59E0B), palette?.getValue(ColorTokens.chartToken2))
        assertEquals(Color(0xFF92400E), palette?.getValue(ColorTokens.chartToken5))
    }

    @Test
    fun explicitChartColorOverridesThemeCharts() {
        val palette = ShadcnPreset(baseColor = "neutral", theme = "amber", chartColor = "emerald").lightPalette()
        // emerald-300
        assertEquals(Color(0xFF6EE7B7), palette?.getValue(ColorTokens.chartToken1))
    }

    @Test
    fun baseScaleDrivesPrimaryWhenThemeIsNull() {
        val palette = ShadcnPreset(baseColor = "blue").lightPalette()
        assertEquals(Color(0xFF1E3A8A), palette?.getValue(ColorTokens.primary))
    }

    @Test
    fun unknownBaseColorInvalidatesPalette() {
        assertNull(ShadcnPreset(baseColor = "not-a-scale").lightPalette())
    }
}
