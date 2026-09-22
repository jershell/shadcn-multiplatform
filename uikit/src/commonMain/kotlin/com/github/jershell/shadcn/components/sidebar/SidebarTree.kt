package com.github.jershell.shadcn.components.sidebar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState

@Composable
internal fun rememberSidebarNodes(
    content: @DisallowComposableCalls SidebarScope.() -> Unit,
): List<SidebarNode> {
    val currentContent = rememberUpdatedState(content)
    val builder = remember { SidebarScopeBuilder() }
    builder.nodes.clear()
    currentContent.value.invoke(builder)
    return builder.nodes.toList()
}

internal fun sidebarNodeKey(node: SidebarNode, index: Int): Any = when (node) {
    is SidebarNode.MenuItem -> node.key ?: "menu-item-$index"
    is SidebarNode.MenuButton -> "menu-button-${node.label}-$index"
    is SidebarNode.GroupLabel -> "group-label-${node.text}"
    is SidebarNode.Group -> "group-$index"
    is SidebarNode.Menu -> "menu-$index"
    is SidebarNode.Content -> "content-$index"
    is SidebarNode.Header -> "header-$index"
    is SidebarNode.Footer -> "footer-$index"
    is SidebarNode.Separator -> "separator-$index"
    is SidebarNode.Trigger -> "trigger-$index"
    is SidebarNode.ComposeItem -> "compose-item-$index"
    is SidebarNode.GroupAction -> "group-action-${node.label}-$index"
    is SidebarNode.MenuAction -> "menu-action-${node.label}-$index"
    is SidebarNode.MenuBadge -> "menu-badge-${node.text}-$index"
    is SidebarNode.MenuSub -> "menu-sub-$index"
    is SidebarNode.MenuSubItem -> "menu-sub-item-$index"
    is SidebarNode.MenuSubButton -> "menu-sub-button-${node.label}-$index"
}
