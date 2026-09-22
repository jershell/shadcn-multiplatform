package com.github.jershell.theme

import com.github.jershell.rjpath.RJPath
import com.github.jershell.theme.models.CollectedToken
import com.github.jershell.util.JSON
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import org.gradle.api.logging.Logger

data class PrimitiveLayerConfig(
    val objectName: String,
    val selector: String,
    val pathPrefix: String,
    val kotlinType: KotlinTokenType,
)

data class ThemeModeConfig(
    val modeName: String,
    val modePathPrefix: String,
    val selector: String,
)

enum class KotlinTokenType {
    COLOR,
    DIMENSION,
    TEXT_STYLE,
    SHADOW,
    STRING,
    NUMBER,
}

class TokenCollector(private val logger: Logger) {
    fun collectPrimitives(
        json: JsonElement,
        layers: List<PrimitiveLayerConfig>,
        case: ParseColorDesignTokens.TokenCase,
    ): List<CollectedToken> {
        val result = mutableListOf<CollectedToken>()
        layers.forEach { layer ->
            try {
                val matches = RJPath.selector(layer.selector).getAllWithPath(json)
                matches.forEach { (path, element) ->
                    if (element !is JsonObject) return@forEach
                    val type = element["type"]?.jsonPrimitive?.content ?: return@forEach
                    val propertyName = TokenNaming.pathToPropertyName(path, layer.pathPrefix, case)
                    if (propertyName.isEmpty()) return@forEach

                    val valueElement = element["value"] ?: return@forEach
                    val valueString = valueElementToString(valueElement, type)
                    if (valueString == null) return@forEach

                    result += CollectedToken(
                        path = TokenNaming.normalizeJsonPath(path),
                        propertyName = propertyName,
                        type = type,
                        rawValue = valueElement,
                        valueString = valueString,
                        isReference = valueString.startsWith("{") && valueString.endsWith("}"),
                        objectName = layer.objectName,
                    )
                }
            } catch (e: Exception) {
                logger.warn("Failed primitive layer '${layer.objectName}': ${e.message}")
            }
        }
        return result
    }

    fun collectThemeMode(
        json: JsonElement,
        config: ThemeModeConfig,
        case: ParseColorDesignTokens.TokenCase,
    ): List<CollectedToken> {
        val result = mutableListOf<CollectedToken>()
        try {
            val matches = RJPath.selector(config.selector).getAllWithPath(json)
            matches.forEach { (path, element) ->
                if (element !is JsonObject) return@forEach
                val type = element["type"]?.jsonPrimitive?.content ?: return@forEach
                val kotlinType = mapTokenType(type)
                val propertyName = TokenNaming.semanticPropertyName(path, config.modePathPrefix, case)
                if (propertyName.isEmpty()) return@forEach

                val valueElement = element["value"] ?: return@forEach
                val valueString = valueElementToString(valueElement, type)
                if (valueString == null) return@forEach

                result += CollectedToken(
                    path = path,
                    propertyName = propertyName,
                    type = type,
                    rawValue = valueElement,
                    valueString = valueString,
                    isReference = valueString.startsWith("{") && valueString.endsWith("}"),
                    objectName = config.modeName,
                )
            }
        } catch (e: Exception) {
            logger.warn("Failed theme mode '${config.modeName}': ${e.message}")
        }
        return result
    }

    fun collectTypographyStyles(
        json: JsonElement,
        selector: String,
        objectName: String,
        case: ParseColorDesignTokens.TokenCase,
    ): List<CollectedToken> {
        val result = mutableListOf<CollectedToken>()
        try {
            val root = json as? JsonObject ?: return result
            val typography = root["typography"] as? JsonObject ?: return result

            typography.forEach { (sizeKey, sizeValue) ->
                if (sizeValue !is JsonObject) return@forEach
                sizeValue.forEach { (weightKey, weightValue) ->
                    if (weightValue !is JsonObject) return@forEach
                    if (!weightValue.containsKey("fontSize")) return@forEach

                    val path = "typography.$sizeKey.$weightKey"
                    val propertyName = TokenNaming.pathToPropertyName(path, "typography.", case)
                    if (propertyName.isEmpty()) return@forEach

                    result += CollectedToken(
                        path = TokenNaming.normalizeJsonPath(path),
                        propertyName = propertyName,
                        type = "typography",
                        rawValue = weightValue,
                        valueString = path,
                        isReference = false,
                        objectName = objectName,
                    )
                }
            }
        } catch (e: Exception) {
            logger.warn("Failed typography collection: ${e.message}")
        }
        return result
    }

    private fun valueElementToString(value: JsonElement, type: String): String? {
        return when (value) {
            is JsonObject -> JSON.encodeToString(JsonElement.serializer(), value)
            is JsonPrimitive -> value.content
            else -> null
        }
    }

    private fun mapTokenType(type: String): KotlinTokenType = when (type) {
        "color" -> KotlinTokenType.COLOR
        "dimension" -> KotlinTokenType.DIMENSION
        "custom-fontStyle" -> KotlinTokenType.TEXT_STYLE
        "custom-shadow" -> KotlinTokenType.SHADOW
        "string" -> KotlinTokenType.STRING
        "number" -> KotlinTokenType.NUMBER
        else -> KotlinTokenType.STRING
    }
}
