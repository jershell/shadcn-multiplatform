package com.github.jershell.theme

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

object JsonTokenValue {
    fun readString(element: JsonElement?): String? {
        if (element == null) return null
        return when (element) {
            is JsonObject -> element["value"]?.let { readString(it) }
            is JsonPrimitive -> element.content
            else -> null
        }
    }

    fun readFloat(element: JsonElement?): Float? {
        if (element == null) return null
        return when (element) {
            is JsonObject -> element["value"]?.let { readFloat(it) }
            is JsonPrimitive -> element.content.toFloatOrNull()
            else -> null
        }
    }

    fun readInt(element: JsonElement?): Int? {
        return readFloat(element)?.toInt()
    }
}
