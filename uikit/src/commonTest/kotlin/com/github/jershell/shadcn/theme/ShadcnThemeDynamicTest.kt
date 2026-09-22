package com.github.jershell.shadcn.theme

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.composeunstyled.theme.Theme
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

/**
 * Live re-theming: changing the preset (accent scale / base scale / radius) while
 * the composition is running must be reflected by `Theme[ColorProps][...]` reads
 * inside the already-composed content.
 */
@OptIn(ExperimentalTestApi::class)
class ShadcnThemeDynamicTest {

    @Test
    fun changingAccentThemeUpdatesPrimaryLive() = runComposeUiTest {
        val primary = mutableStateOf<Color?>(null)
        val preset = mutableStateOf<ShadcnPreset?>(null)

        setContent {
            ShadcnTheme(preset = preset.value, mode = Mode.Light) {
                primary.value = Theme[ColorProps][ColorTokens.primary]
            }
        }
        waitForIdle()
        val before = primary.value
        assertEquals(themeLightSemanticColors.getValue(ColorTokens.primary), before)

        runOnIdle {
            preset.value = ShadcnPreset(baseColor = "neutral", theme = "amber")
        }
        waitForIdle()

        // amber-600: the accent scale's light primary (reference mapping)
        assertEquals(Color(0xFFD97706), primary.value, "primary must follow the accent scale live")
        assertNotEquals(before, primary.value)
    }

    @Test
    fun changingBaseColorUpdatesBackgroundLive() = runComposeUiTest {
        val background = mutableStateOf<Color?>(null)
        val preset = mutableStateOf<ShadcnPreset?>(null)

        setContent {
            ShadcnTheme(preset = preset.value, mode = Mode.Light) {
                background.value = Theme[ColorProps][ColorTokens.background]
            }
        }
        waitForIdle()
        val before = background.value

        runOnIdle {
            preset.value = ShadcnPreset(baseColor = "blue")
        }
        waitForIdle()

        assertEquals(Color(0xFFEFF6FF), background.value, "background must follow the base scale live")
        assertNotEquals(before, background.value)
    }
}
