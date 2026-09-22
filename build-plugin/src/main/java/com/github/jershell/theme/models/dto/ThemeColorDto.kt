package com.github.jershell.theme.models.dto

import kotlinx.serialization.Serializable


@Serializable
data class ThemeColorDto(
    val type: String = "color",
    val value: String,
    val blendMode: String = "normal",
    val description: String = ""
)

val ThemeColorDto.isRef: Boolean
    get() {
        if(!this.value.isNotBlank()) return false
        return !(this.value[0] == '#' || this.value[0] == 'R' || this.value[0] == 'r')
    }
