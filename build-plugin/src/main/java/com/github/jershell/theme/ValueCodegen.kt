package com.github.jershell.theme

import kotlinx.serialization.json.JsonObject

class ValueCodegen(
    private val pathIndex: Map<String, TokenRef>,
    private val case: ParseColorDesignTokens.TokenCase,
) {
    data class TokenRef(
        val objectName: String,
        val propertyName: String,
    )

    fun kotlinReference(path: String): String? {
        val normalized = TokenNaming.normalizeJsonPath(path)
        val direct = pathIndex[normalized] ?: pathIndex[path]
        if (direct != null) return "${direct.objectName}.${direct.propertyName}"

        val suffixMatch = pathIndex.entries.firstOrNull { (indexedPath, _) ->
            indexedPath == normalized ||
                indexedPath.endsWith(".$normalized") ||
                indexedPath.endsWith(normalized)
        }
        if (suffixMatch != null) {
            val ref = suffixMatch.value
            return "${ref.objectName}.${ref.propertyName}"
        }

        if (normalized.startsWith("rdx.colors.")) {
            val rdxSuffix = normalized.removePrefix("rdx.colors.")
            val rdxMatch = pathIndex.entries.firstOrNull { (indexedPath, _) ->
                indexedPath.contains(".$rdxSuffix") || indexedPath.endsWith(rdxSuffix)
            }
            if (rdxMatch != null) {
                val ref = rdxMatch.value
                return "${ref.objectName}.${ref.propertyName}"
            }
        }

        return null
    }

    fun resolveValue(value: String, isReference: Boolean): String {
        if (isReference) {
            val tokenPath = TokenNaming.referencePathToTokenPath(value)
            val ref = kotlinReference(tokenPath)
            if (ref != null) return ref
            return unresolvedReferenceLiteral(value)
        }
        return literalValue(value)
    }

    private fun unresolvedReferenceLiteral(reference: String): String {
        val tokenPath = TokenNaming.referencePathToTokenPath(reference)
        if (tokenPath.contains("colors") || reference.contains("color")) {
            return "Color.Transparent"
        }
        return "0.dp"
    }

    fun literalValue(value: String): String {
        if (value.startsWith("#")) {
            return ThemeUtil.generateColorValue(value, case, emptyList())
        }
        val asDouble = value.toDoubleOrNull()
        if (asDouble != null) {
            return if (asDouble == asDouble.toLong().toDouble()) {
                "${asDouble.toLong()}.dp"
            } else {
                "${value}.dp"
            }
        }
        return value
    }

    fun textStyleFromJson(json: JsonObject, fontFamilyExpr: String? = null): String {
        val fontSize = JsonTokenValue.readFloat(json["fontSize"]) ?: 14f
        val lineHeight = JsonTokenValue.readFloat(json["lineHeight"]) ?: fontSize
        val fontWeight = JsonTokenValue.readInt(json["fontWeight"]) ?: 400
        val letterSpacing = JsonTokenValue.readFloat(json["letterSpacing"]) ?: 0f

        return buildString {
            append("TextStyle(")
            append("fontSize = ${fontSize}.sp, ")
            append("lineHeight = ${lineHeight}.sp, ")
            append("fontWeight = FontWeight.W$fontWeight, ")
            append("letterSpacing = ${letterSpacing}.sp, ")
            append("fontFamily = ${fontFamilyExpr ?: "FontFamily.SansSerif"}")
            append(")")
        }
    }

    fun shadowFromJson(json: JsonObject): String {
        val radius = JsonTokenValue.readFloat(json["radius"]) ?: 0f
        val color = JsonTokenValue.readString(json["color"]) ?: "#00000000"
        val offsetX = JsonTokenValue.readFloat(json["offsetX"]) ?: 0f
        val offsetY = JsonTokenValue.readFloat(json["offsetY"]) ?: 0f
        val spread = JsonTokenValue.readFloat(json["spread"]) ?: 0f

        val colorExpr = literalValue(color)
        return "TokenShadow(" +
            "radius = ${radius}.dp, " +
            "color = $colorExpr, " +
            "offset = Offset(${offsetX}f, ${offsetY}f), " +
            "spread = ${spread}.dp" +
            ")"
    }

    companion object {
        fun buildPathIndex(tokens: List<com.github.jershell.theme.models.CollectedToken>): Map<String, TokenRef> {
            val index = mutableMapOf<String, TokenRef>()
            tokens.forEach { token ->
                val normalized = TokenNaming.normalizeJsonPath(token.path)
                index[normalized] = TokenRef(token.objectName, token.propertyName)
                index[token.path] = TokenRef(token.objectName, token.propertyName)
            }
            return index
        }
    }
}
