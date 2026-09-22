package com.github.jershell.shadcn.components.sidebar

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Icon tint of the current sidebar item: provided by the interactive surfaces with
 * the same logic as the text color (foreground normally, accent-foreground when the
 * item is selected/hovered/pressed, dimmed when disabled). Read with [SidebarIcon].
 */
internal val LocalSidebarItemTint = compositionLocalOf { Color.Unspecified }