package com.github.jershell.theme.models

data class ThemeColor(
    val name: String,
    val value: String,
    val description: String = "",
    val isRef: Boolean = value.startsWith("{")
)
