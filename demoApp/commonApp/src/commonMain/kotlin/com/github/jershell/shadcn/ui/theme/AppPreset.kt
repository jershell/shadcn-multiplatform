package com.github.jershell.shadcn.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import com.github.jershell.shadcn.theme.ShadcnPreset

/**
 * The theme preset state of the demo app, owned by [com.github.jershell.shadcn.App] and
 * provided to the theme builder (`CreateScreen`): the builder writes here,
 * `ShadcnTheme` re-themes the whole app from it. The state is persisted to the
 * app session as a PresetCodec share string.
 */
val LocalAppPresetState = compositionLocalOf { mutableStateOf<ShadcnPreset?>(null) }
