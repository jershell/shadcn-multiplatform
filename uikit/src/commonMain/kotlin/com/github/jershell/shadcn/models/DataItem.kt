package com.github.jershell.shadcn.models

import androidx.compose.runtime.Stable

@Stable
data class DataItem<T>(
    val key: Int,
    val title: String,
    val data: T,
    val enabled: Boolean = true,
)