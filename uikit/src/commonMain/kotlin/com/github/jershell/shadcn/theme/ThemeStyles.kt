package com.github.jershell.shadcn.theme

/**
 * A style family of the theme builder, mirroring the `Menu Style` picker of the
 * shadcn/ui create page. In v1 a style carries only the defaults of the resolved
 * preset — the radius family, the heading/body fonts and the chart color — and does
 * not re-skin individual components (decision: styles = data, no component overrides).
 */
public data class ThemeStyle(
    val name: String,
    val radius: Double = ShadcnDefaultRadiusRem,
    val headingFont: String? = null,
    val bodyFont: String? = null,
)

/**
 * Style families of the reference create page. Per-style defaults are provisional
 * (to be tuned against the reference); all of them share the reference radius so far.
 */
public val themeStyles: List<ThemeStyle> = listOf(
    ThemeStyle("luma"),
    ThemeStyle("lyra"),
    ThemeStyle("maia"),
    ThemeStyle("mira"),
    ThemeStyle("nova"),
    ThemeStyle("rhea"),
    ThemeStyle("sera"),
    ThemeStyle("vega"),
)

internal fun themeStyleOrNull(name: String): ThemeStyle? =
    themeStyles.firstOrNull { it.name == name }

/**
 * The effective radius of the preset in `rem`: the explicit preset radius wins,
 * then the style default, then the reference default.
 */
internal fun ShadcnPreset.resolvedRadiusRem(): Double =
    radius ?: style?.let { styleName -> themeStyleOrNull(styleName)?.radius } ?: ShadcnDefaultRadiusRem
