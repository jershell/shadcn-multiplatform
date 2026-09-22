package com.github.jershell.shadcn.theme

import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * A shareable theme preset of the theme builder, mirroring the parameters of the
 * shadcn/ui create page:
 *
 * - [style] — the component style family (`luma`, `lyra`, `maia`, `mira`, `nova`,
 *   `rhea`, `sera`, `vega`); `null` = the default generated look.
 * - [baseColor] — the base (background/gray) color scale; `"neutral"` maps to the
 *   generated default theme.
 * - [theme] — the accent color scale driving `primary`/`ring`/`sidebarPrimary`;
 *   `null` = inherit from [baseColor].
 * - [chartColor] — the color scale of `chart1..chart5`; `null` = follow [theme]
 *   (or the generated default charts when [theme] is unset too).
 * - [font] — the app font family name; `null` = inherit.
 * - [iconLibrary] — icon set (`lucide`, `tabler`); informational for now.
 * - [menuStyle] / [menuAccent] — sidebar look options (`default|solid`, `subtle|...`);
 *   informational for now.
 * - [radius] — the corner radius in `rem` (like `--radius`); `null` = the default
 *   radius of the resolved style.
 * - [light]/[dark] — advanced per-token overrides (`tokenName -> #rrggbb`) applied last.
 *
 * Serializable to a shareable string: the demo `PresetCodec` writes the v2 format
 * (JSON -> ZLIB deflate -> Base64url); [decodeShadcnPreset] reads the legacy v1
 * format (plain Base64 of JSON).
 */
@Serializable
public data class ShadcnPreset(
    val style: String? = null,
    val baseColor: String = "neutral",
    val theme: String? = null,
    val chartColor: String? = null,
    val font: String? = null,
    val iconLibrary: String? = null,
    val menuStyle: String? = null,
    val menuAccent: String? = null,
    val radius: Double? = null,
    val light: Map<String, String> = emptyMap(),
    val dark: Map<String, String> = emptyMap(),
)

/**
 * Encodes the preset to a string in the **legacy v1** format (plain Base64url of
 * JSON, no padding). Kept for backward compatibility; the app uses the v2 codec
 * (ZLIB deflate + Base64url).
 */
@OptIn(ExperimentalEncodingApi::class)
public fun ShadcnPreset.encodeToBase64(): String {
    val json = presetJson.encodeToString(ShadcnPreset.serializer(), this)
    return Base64.UrlSafe.encode(json.encodeToByteArray()).trimEnd('=')
}

/**
 * Decodes a preset from the legacy v1 format (plain Base64 JSON); returns `null`
 * when the string is not a valid preset.
 */
@OptIn(ExperimentalEncodingApi::class)
public fun decodeShadcnPreset(base64: String): ShadcnPreset? = try {
    val normalized = base64.trim().replace(" ", "").padEnd(
        length = base64.length + ((4 - base64.length % 4) % 4),
        padChar = '=',
    )
    val json = Base64.UrlSafe.decode(normalized).decodeToString()
    presetJson.decodeFromString(ShadcnPreset.serializer(), json)
} catch (_: IllegalArgumentException) {
    null
} catch (_: SerializationException) {
    null
}

private val presetJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = false
}

/**
 * The default radius of the reference: `--radius: 0.625rem`.
 */
public const val ShadcnDefaultRadiusRem: Double = 0.625