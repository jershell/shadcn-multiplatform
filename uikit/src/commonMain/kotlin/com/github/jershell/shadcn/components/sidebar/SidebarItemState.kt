package com.github.jershell.shadcn.components.sidebar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf

@Stable
data class SidebarItemState(
    val isSelected: Boolean = false,
    val isHovered: Boolean = false,
    val isPressed: Boolean = false,
    val enabled: Boolean = true,
)

internal val LocalSidebarItemState = compositionLocalOf { SidebarItemState() }

@Composable
fun currentSidebarItemState(): SidebarItemState = LocalSidebarItemState.current
