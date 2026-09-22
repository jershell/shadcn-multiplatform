package com.github.jershell.shadcn.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey

@Serializable
data class Component(val id: ComponentId) : Route

@Serializable
data object Overview : Route

@Serializable
data object Icons : Route
