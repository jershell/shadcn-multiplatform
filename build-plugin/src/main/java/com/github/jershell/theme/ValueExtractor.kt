package com.github.jershell.theme

import com.github.jershell.theme.models.dto.ThemeColorDto
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Utility for extracting values from JSON
 */
object ValueExtractor {
    /**
     * Extracts a value from a JSON object
     * @param jsonObject JSON object holding a color
     * @return ThemeColorDto with the extracted color value
     */
    fun extractColor(jsonObject: JsonObject): ThemeColorDto? {
        val value = jsonObject["value"]?.jsonPrimitive?.content ?: return null
        return ThemeColorDto(value = value)
    }
}