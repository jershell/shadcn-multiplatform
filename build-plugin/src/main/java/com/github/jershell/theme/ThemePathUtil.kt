package com.github.jershell.theme

import kotlinx.serialization.json.JsonObject

/**
 * Utility for working with JSON paths
 */
object ThemePathUtil {
    /**
     * Extracts a color name from a full path and applies the transformation
     * @param path full path to the color in JSON
     * @param case naming style
     * @return formatted color name
     */
    fun extractColorName(
        path: String,
        case: ParseColorDesignTokens.TokenCase,
        transforms: List<ParseColorDesignTokens.Transform>
    ): String {
        var normalized = applyTransforms(path, transforms)
        // older Figma exports prefixed paths with the collection name
        val legacyPrefix = "${ThemeUtil.LEGACY_COLLECTION_PREFIX}."
        if (normalized.startsWith(legacyPrefix)) {
            normalized = normalized.removePrefix(legacyPrefix)
        } else if (normalized == ThemeUtil.LEGACY_COLLECTION_PREFIX) {
            normalized = ""
        }
        val parts = normalized.replace("-", ".").split(".").filter { it.isNotEmpty() }

        if (parts.isEmpty()) return ""

        // Build the name from the path parts
        return ThemeUtil.formatPropertyName(
            name = parts.joinToString("_"),
            style = case
        )
    }

    /**
     * Extracts a color name from a full path and applies the transformation
     * Only the last element is used as the name
     * @param path full path to the color in JSON
     * @param case naming style
     * @return formatted color name
     */
    fun extractThemeColorName(
        path: String,
        case: ParseColorDesignTokens.TokenCase,
        transforms: List<ParseColorDesignTokens.Transform>
    ): String {
        val parts = applyTransforms(path, transforms).split(".").filter { it.isNotEmpty() }

        if (parts.isEmpty()) return ""

        // Build the name from the last path part
        return ThemeUtil.formatPropertyName(
            name = parts.last().replace("-", ".").split(".").joinToString("_"),
            style = case
        )
    }

    fun applyTransforms(path: String, transforms: List<ParseColorDesignTokens.Transform>): String {
        var result = path
        transforms.forEach { t ->
            when(t) {
                is ParseColorDesignTokens.Transform.Replace -> {
                    result = result.replace(t.oldValue, t.newValue)
                }
            }
        }
        return result
    }

    /**
     * Extracts the path to the color referenced by the current color
     * @param refValue reference value in the "{path.to.color}" format
     * @return the path to the color, or null if this is not a reference
     */
    fun extractReferencePath(refValue: String): String? {
        if (!refValue.startsWith("{") || !refValue.endsWith("}")) return null
        var path = refValue.substring(1, refValue.length - 1)
        // older Figma exports referenced tokens through the collection name
        val legacyPrefix = "${ThemeUtil.LEGACY_COLLECTION_PREFIX}."
        if (path.startsWith(legacyPrefix)) {
            path = path.removePrefix(legacyPrefix)
        }
        return path
    }
} 