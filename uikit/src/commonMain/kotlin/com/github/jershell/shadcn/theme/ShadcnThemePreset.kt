package com.github.jershell.shadcn.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.theme.ThemeToken
import com.composeunstyled.theme.buildTheme
import com.github.jershell.shadcn.theme.indication.AppNoIndication

/**
 * Mapping of a base color scale to the semantic tokens, mirroring the shadcn/ui
 * theme editor: the whole UI is derived from one color scale.
 */
private fun ThemeScale.lightColors(): Map<ThemeToken<Color>, Color> = mapOf(
    ColorTokens.background to this[0],
    ColorTokens.foreground to this[10],
    ColorTokens.card to this[0],
    ColorTokens.cardForeground to this[10],
    ColorTokens.popover to this[0],
    ColorTokens.popoverForeground to this[10],
    ColorTokens.primary to this[9], // 900
    ColorTokens.primaryForeground to this[0], // 50
    ColorTokens.secondary to this[1], // 100
    ColorTokens.secondaryForeground to this[9],
    ColorTokens.muted to this[1],
    ColorTokens.mutedForeground to this[5], // 500
    ColorTokens.accent to this[1],
    ColorTokens.accentForeground to this[9],
    ColorTokens.border to this[2], // 200
    ColorTokens.input to this[2],
    ColorTokens.ring to this[5],
    ColorTokens.sidebar to this[0],
    ColorTokens.sidebarForeground to this[10],
    ColorTokens.sidebarPrimary to this[9],
    ColorTokens.sidebarPrimaryForeground to this[0],
    ColorTokens.sidebarAccent to this[1],
    ColorTokens.sidebarAccentForeground to this[9],
    ColorTokens.sidebarBorder to this[2],
    ColorTokens.sidebarRing to this[5],
)

private fun ThemeScale.darkColors(): Map<ThemeToken<Color>, Color> = mapOf(
    ColorTokens.background to this[10], // 950
    ColorTokens.foreground to this[0], // 50
    ColorTokens.card to this[9], // 900
    ColorTokens.cardForeground to this[0],
    ColorTokens.popover to this[9],
    ColorTokens.popoverForeground to this[0],
    ColorTokens.primary to this[0],
    ColorTokens.primaryForeground to this[9],
    ColorTokens.secondary to this[8], // 800
    ColorTokens.secondaryForeground to this[0],
    ColorTokens.muted to this[8],
    ColorTokens.mutedForeground to this[4], // 400
    ColorTokens.accent to this[8],
    ColorTokens.accentForeground to this[0],
    ColorTokens.border to this[8],
    ColorTokens.input to this[8],
    ColorTokens.ring to this[5],
    ColorTokens.sidebar to this[9],
    ColorTokens.sidebarForeground to this[0],
    ColorTokens.sidebarPrimary to this[0],
    ColorTokens.sidebarPrimaryForeground to this[9],
    ColorTokens.sidebarAccent to this[8],
    ColorTokens.sidebarAccentForeground to this[0],
    ColorTokens.sidebarBorder to this[8],
    ColorTokens.sidebarRing to this[5],
)

/**
 * `Base Color` value that maps to the generated default theme (the Figma tokens
 * are the source of truth for it).
 */
internal const val NeutralBaseColor: String = "neutral"

/**
 * Accent tokens driven by the `theme` color scale, mirroring the reference
 * `legacy-themes.css` (`.theme-amber` etc.): only primary/ring/sidebarPrimary get
 * the hue — `accent` stays neutral from the base scale (it is the hover surface).
 *
 * Light: primary=600, primaryForeground=50, ring=400;
 * Dark:  primary=500, primaryForeground=50, ring=900.
 */
private val accentTokensLight: Map<ThemeToken<Color>, Int> = mapOf(
    ColorTokens.primary to 6,
    ColorTokens.primaryForeground to 0,
    ColorTokens.ring to 4,
    ColorTokens.sidebarPrimary to 6,
    ColorTokens.sidebarPrimaryForeground to 0,
    ColorTokens.sidebarRing to 4,
)

private val accentTokensDark: Map<ThemeToken<Color>, Int> = mapOf(
    ColorTokens.primary to 5,
    ColorTokens.primaryForeground to 0,
    ColorTokens.ring to 9,
    ColorTokens.sidebarPrimary to 5,
    ColorTokens.sidebarPrimaryForeground to 0,
    ColorTokens.sidebarRing to 9,
)

/**
 * Chart tokens and their shades within a scale (reference: `chart-1..5 =
 * scale-300/500/600/700/800`, i.e. `.theme-amber -> --chart-1: var(--color-amber-300)`).
 */
private val chartTokens: List<ThemeToken<Color>> = listOf(
    ColorTokens.chartToken1,
    ColorTokens.chartToken2,
    ColorTokens.chartToken3,
    ColorTokens.chartToken4,
    ColorTokens.chartToken5,
)

private val chartShades: List<Int> = listOf(3, 5, 6, 7, 8)

private fun Map<ThemeToken<Color>, Color>.withAccentScale(
    scale: ThemeScale,
    isDark: Boolean,
): Map<ThemeToken<Color>, Color> {
    val accents = if (isDark) accentTokensDark else accentTokensLight
    return this + accents.entries.associate { (token, shade) -> token to scale[shade] }
}

private fun Map<ThemeToken<Color>, Color>.withChartScale(
    scale: ThemeScale,
): Map<ThemeToken<Color>, Color> =
    this + chartTokens.zip(chartShades) { token, shade -> token to scale[shade] }

/**
 * The light palette of the preset: the generated default palette (source of
 * `destructive`/`destructiveForeground`/`chart1..5`, which the scales do not
 * define) overlaid with the base scale (`neutral` = keep the generated mapping)
 * + accent/chart scale overwrites + explicit per-token overrides of the preset.
 */
/**
 * The fully resolved light palette of the preset (base scale + accent/chart scales
 * + overrides). `null` when [ShadcnPreset.baseColor] does not name a known scale.
 */
public fun ShadcnPreset.lightPalette(): Map<ThemeToken<Color>, Color>? {
    var palette = themeLightSemanticColors + when (baseColor) {
        NeutralBaseColor -> emptyMap()
        else -> themeScaleOrNull(baseColor)?.lightColors() ?: return null
    }
    val accentScale = theme?.let { themeScaleOrNull(it) }
    // charts follow the theme scale when no explicit Chart Color is set (reference:
    // .theme-amber -> --chart-1..5: amber-300..800)
    val chartScale = chartColor?.let { themeScaleOrNull(it) } ?: accentScale
    accentScale?.let { scale -> palette = palette.withAccentScale(scale, isDark = false) }
    chartScale?.let { scale -> palette = palette.withChartScale(scale) }
    light.forEach { (tokenName, hex) ->
        tokenByName(tokenName)?.let { token -> palette = palette + (token to Color(hex.toLong(16) or 0xFF000000)) }
    }
    return palette
}

/**
 * The dark palette of the preset; same resolve order as [lightPalette].
 */
/**
 * The fully resolved dark palette of the preset; same resolve order as [lightPalette].
 */
public fun ShadcnPreset.darkPalette(): Map<ThemeToken<Color>, Color>? {
    var palette = themeDarkSemanticColors + when (baseColor) {
        NeutralBaseColor -> emptyMap()
        else -> themeScaleOrNull(baseColor)?.darkColors() ?: return null
    }
    val accentScale = theme?.let { themeScaleOrNull(it) }
    val chartScale = chartColor?.let { themeScaleOrNull(it) } ?: accentScale
    accentScale?.let { scale -> palette = palette.withAccentScale(scale, isDark = true) }
    chartScale?.let { scale -> palette = palette.withChartScale(scale) }
    dark.forEach { (tokenName, hex) ->
        tokenByName(tokenName)?.let { token -> palette = palette + (token to Color(hex.toLong(16) or 0xFF000000)) }
    }
    return palette
}

internal fun tokenByName(name: String): ThemeToken<Color>? = when (name) {
    "accent" -> ColorTokens.accent
    "accentForeground" -> ColorTokens.accentForeground
    "background" -> ColorTokens.background
    "border" -> ColorTokens.border
    "card" -> ColorTokens.card
    "cardForeground" -> ColorTokens.cardForeground
    "chart1" -> ColorTokens.chartToken1
    "chart2" -> ColorTokens.chartToken2
    "chart3" -> ColorTokens.chartToken3
    "chart4" -> ColorTokens.chartToken4
    "chart5" -> ColorTokens.chartToken5
    "destructive" -> ColorTokens.destructive
    "foreground" -> ColorTokens.foreground
    "input" -> ColorTokens.input
    "muted" -> ColorTokens.muted
    "mutedForeground" -> ColorTokens.mutedForeground
    "popover" -> ColorTokens.popover
    "popoverForeground" -> ColorTokens.popoverForeground
    "primary" -> ColorTokens.primary
    "primaryForeground" -> ColorTokens.primaryForeground
    "ring" -> ColorTokens.ring
    "secondary" -> ColorTokens.secondary
    "secondaryForeground" -> ColorTokens.secondaryForeground
    "sidebar" -> ColorTokens.sidebar
    "sidebarAccent" -> ColorTokens.sidebarAccent
    "sidebarAccentForeground" -> ColorTokens.sidebarAccentForeground
    "sidebarBorder" -> ColorTokens.sidebarBorder
    "sidebarForeground" -> ColorTokens.sidebarForeground
    "sidebarPrimary" -> ColorTokens.sidebarPrimary
    "sidebarPrimaryForeground" -> ColorTokens.sidebarPrimaryForeground
    "sidebarRing" -> ColorTokens.sidebarRing
    else -> null
}

/**
 * Corner radius overrides of the preset: the base radius drives the whole radius
 * family like in Tailwind v4 (`rounded-sm = base - 4`, `md = base - 2`, `lg = base`,
 * `xl = base + 4`, `2xl = base + 8`). Small values are clamped to 0 — corner sizes
 * must never be negative.
 */
private fun radiusDimensions(basePx: Double): Map<ThemeToken<Dp>, Dp> {
    val base = basePx.dp
    val zero = 0.dp
    return mapOf(
        DimTokens.radiusXs to (base - 8.dp).coerceAtLeast(zero),
        DimTokens.radiusSm to (base - 4.dp).coerceAtLeast(zero),
        DimTokens.radiusMd to (base - 2.dp).coerceAtLeast(zero),
        DimTokens.radiusLg to base.coerceAtLeast(zero),
        DimTokens.radiusXl to base + 4.dp,
        DimTokens.radiusN2xl to base + 8.dp,
    )
}

/**
 * Theme of the whole app built from a shareable [ShadcnPreset]: the semantic palette
 * is derived from the base color scale, the radius family from the preset radius,
 * and per-token overrides are applied last. `preset = null` renders the generated
 * default theme.
 *
 * @param preset Shareable theme preset; `null` uses the default generated theme.
 * @param mode Light/dark mode of the theme.
 * @param onModeChanged Invoked when the mode changes.
 * @param content Application content.
 */
@Composable
public fun ShadcnTheme(
    preset: ShadcnPreset?,
    mode: Mode = if (isSystemInDarkTheme()) Mode.Dark else Mode.Light,
    onModeChanged: @Composable (mode: Mode) -> Unit = {},
    content: @Composable () -> Unit,
) {
    val isDark = mode == Mode.Dark
    val isDarkState = remember { mutableStateOf(isDark) }
    LaunchedEffect(isDark) {
        isDarkState.value = isDark
    }
    CompositionLocalProvider(LocalThemeIsDark provides isDarkState) {
        val currentIsDark by isDarkState
        onModeChanged(if (currentIsDark) Mode.Dark else Mode.Light)

        val presetPalette = if (isDark) preset?.darkPalette() else preset?.lightPalette()
        val ShadcnPresetTheme = remember(preset, isDark) {
            buildTheme {
                val dark by LocalThemeIsDark.current
                val basePalette = if (dark) themeDarkSemanticColors else themeLightSemanticColors
                val palette = presetPalette ?: basePalette
                defaultIndication = AppNoIndication
                defaultContentColor = palette.getValue(ColorTokens.foreground)
                // theme font: implicit Text (no explicit style) inherits LocalTextStyle
                defaultTextStyle = TextStyle(fontFamily = LocalShadcnFonts.current ?: FontFamily.Default)
                defaultTextSelectionColors = TextSelectionColors(
                    backgroundColor = palette.getValue(ColorTokens.primary),
                    handleColor = palette.getValue(ColorTokens.primaryForeground).copy(alpha = 0.3f),
                )
                properties[ColorProps] = palette
                properties[DimProps] = mergedDimensions(preset, dark)
            }
        }
        ShadcnPresetTheme(content)
    }
}

private fun mergedDimensions(
    preset: ShadcnPreset?,
    isDark: Boolean,
): Map<ThemeToken<Dp>, Dp> {
    val base = if (isDark) themeDarkSemanticDimensions else themeLightSemanticDimensions
    if (preset == null) {
        return base
    }
    val radiusOverrides = radiusDimensions(preset.resolvedRadiusRem() * 16.0)
    return base + radiusOverrides
}