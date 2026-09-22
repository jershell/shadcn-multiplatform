package com.github.jershell.shadcn.services.models

import kotlinx.serialization.Serializable

@Serializable
data class AppSession(
    val isDark: Boolean = false,
    val lastScreen: String? = null,
    val lastPreset: String? = null,
    val componentSettings: Map<String, Map<String, String>> = emptyMap()
)
