package com.github.jershell.ui.theme

import com.github.jershell.shadcn.theme.ShadcnDefaultRadiusRem
import com.github.jershell.shadcn.theme.ShadcnPreset
import com.github.jershell.shadcn.theme.encodeToBase64
import com.github.jershell.shadcn.ui.theme.PresetCodec
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PresetCodecTest {

    @Test
    fun v2RoundTrip() {
        val preset = ShadcnPreset(
            style = "nova",
            baseColor = "zinc",
            theme = "amber",
            chartColor = "emerald",
            font = "Inter",
            iconLibrary = "lucide",
            menuStyle = "default",
            menuAccent = "subtle",
            radius = 0.75,
            light = mapOf("primary" to "ff8800"),
        )
        val encoded = PresetCodec.encode(preset)
        assertTrue(encoded.isNotEmpty())
        assertTrue(encoded.none { it == '=' || it == '+' || it == '/' })
        assertEquals(preset, PresetCodec.decode(encoded))
    }

    @Test
    fun defaultPresetRoundTrip() {
        val preset = ShadcnPreset()
        assertEquals(preset, PresetCodec.decode(PresetCodec.encode(preset)))
    }

    @Test
    fun legacyV1StringIsDecoded() {
        val preset = ShadcnPreset(baseColor = "blue", radius = ShadcnDefaultRadiusRem)
        // legacy v1: plain base64 JSON from the uikit codec
        val legacy = preset.encodeToBase64()
        assertEquals(preset, PresetCodec.decode(legacy))
    }

    @Test
    fun invalidStringsReturnNull() {
        assertNull(PresetCodec.decode("not a preset"))
        assertNull(PresetCodec.decode(""))
    }
}
