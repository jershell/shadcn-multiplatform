package com.github.jershell.util

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

val JSON = Json {
    encodeDefaults = true
    coerceInputValues = true
    ignoreUnknownKeys = true
    serializersModule = SerializersModule {
    }
}