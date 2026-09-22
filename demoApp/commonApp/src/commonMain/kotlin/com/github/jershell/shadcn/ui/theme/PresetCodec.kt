package com.github.jershell.shadcn.ui.theme

import com.github.jershell.shadcn.theme.ShadcnPreset
import com.github.jershell.shadcn.theme.decodeShadcnPreset
import com.rafambn.kflate.KFlate
import com.rafambn.kflate.ZLIB
import com.rafambn.kflate.Zlib
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.serialization.json.Json

private val codecJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = false
}

/**
 * The share-string codec of the theme builder:
 *
 * - v2: `JSON -> ZLIB deflate (KFlate) -> Base64url` (no padding) — the current format;
 * - v1 (legacy): plain Base64url of JSON — decoded via
 *   `com.github.jershell.shadcn.theme.decodeShadcnPreset`.
 */
public object PresetCodec {

    /** Encodes the preset to the v2 share string. */
    @OptIn(ExperimentalEncodingApi::class)
    public fun encode(preset: ShadcnPreset): String {
        val json = codecJson.encodeToString(ShadcnPreset.serializer(), preset)
        val compressed = KFlate.compress(json.encodeToByteArray(), ZLIB())
        return Base64.UrlSafe.encode(compressed).trimEnd('=')
    }

    /** Decodes a v2 or legacy v1 share string; `null` when it is not a valid preset. */
    @OptIn(ExperimentalEncodingApi::class)
    public fun decode(value: String): ShadcnPreset? =
        decodeV2(value) ?: decodeShadcnPreset(value)

    private fun decodeV2(value: String): ShadcnPreset? = try {
        val normalized = value.trim().replace(" ", "")
        val padded = normalized.padEnd(normalized.length + ((4 - normalized.length % 4) % 4), '=')
        val json = KFlate.decompress(Base64.UrlSafe.decode(padded), Zlib()).decodeToString()
        codecJson.decodeFromString(ShadcnPreset.serializer(), json)
    } catch (_: IllegalArgumentException) {
        null
    } catch (_: Exception) {
        null
    }
}
