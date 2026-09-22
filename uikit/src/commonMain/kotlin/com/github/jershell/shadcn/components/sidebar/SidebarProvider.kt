package com.github.jershell.shadcn.components.sidebar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember

/**
 * Owns the sidebar expansion state and provides it via [LocalSidebarController]
 * to the whole subtree, so [SidebarTrigger] can toggle the sidebar from outside
 * the sidebar itself (for example from the header of the content pane when the
 * sidebar is collapsed with [SidebarCollapsible.Offcanvas]).
 *
 * A [Sidebar] placed under the provider uses the provider state; its own
 * `expanded` / `defaultExpanded` / `onExpandedChange` parameters are ignored then.
 */
@Composable
fun SidebarProvider(
    expanded: Boolean? = null,
    defaultExpanded: Boolean = true,
    onExpandedChange: (Boolean) -> Unit = {},
    content: @Composable () -> Unit,
) {
    val (currentExpanded, onChange) = rememberSidebarExpandedState(
        expanded = expanded,
        defaultExpanded = defaultExpanded,
        onExpandedChange = onExpandedChange,
    )
    val controller = remember(currentExpanded, onChange) {
        SidebarController(
            isExpanded = currentExpanded,
            side = SidebarSide.Left,
            onExpandedChange = onChange,
        )
    }
    CompositionLocalProvider(LocalSidebarController provides controller, content = content)
}
