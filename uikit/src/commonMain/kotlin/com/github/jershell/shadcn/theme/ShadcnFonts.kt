package com.github.jershell.shadcn.theme

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.font.FontFamily

/**
 * The app font family of the theme builder (`Font` picker of the shadcn/ui create
 * page), resolved by the host app. `null` means the generated theme default
 * (`FontFamily.Default`).
 *
 * The uikit stays asset-free: the app builds a [FontFamily] from its bundled
 * fonts and provides it via [LocalShadcnFonts]; the generated
 * `TypographyStyles` read it live at composition time, so every text style
 * follows the picked font.
 */
public val LocalShadcnFonts: ProvidableCompositionLocal<FontFamily?> =
    staticCompositionLocalOf { null }
