package com.github.jershell.shadcn.data

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

val JSON = Json {
    encodeDefaults = true
    coerceInputValues = true
    ignoreUnknownKeys = true
    serializersModule = SerializersModule {}
}
