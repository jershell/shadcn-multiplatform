package com.github.jershell.theme.models

import kotlinx.serialization.json.JsonElement

data class CollectedToken(
    val path: String,
    val propertyName: String,
    val type: String,
    val rawValue: JsonElement,
    val valueString: String,
    val isReference: Boolean,
    val objectName: String,
)
