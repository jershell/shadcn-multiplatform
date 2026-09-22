package com.github.jershell.shadcn.components.sidebar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

internal sealed interface SidebarNode {
    data class Header(
        val modifier: Modifier,
        val block: @Composable SidebarSectionScope.() -> Unit,
    ) : SidebarNode

    data class Footer(
        val modifier: Modifier,
        val block: @Composable SidebarSectionScope.() -> Unit,
    ) : SidebarNode

    data class Content(
        val modifier: Modifier,
        val children: List<SidebarNode>,
    ) : SidebarNode

    data class Group(
        val modifier: Modifier,
        val children: List<SidebarNode>,
    ) : SidebarNode

    data class Separator(
        val modifier: Modifier,
    ) : SidebarNode

    data class Trigger(
        val modifier: Modifier,
    ) : SidebarNode

    data class ComposeItem(
        val modifier: Modifier,
        val content: @Composable () -> Unit,
    ) : SidebarNode

    data class GroupLabel(
        val text: String,
        val modifier: Modifier,
    ) : SidebarNode

    data class GroupAction(
        val onClick: () -> Unit,
        val modifier: Modifier,
        val enabled: Boolean,
        val label: String,
    ) : SidebarNode

    data class Menu(
        val modifier: Modifier,
        val children: List<SidebarNode>,
    ) : SidebarNode

    data class MenuItem(
        val modifier: Modifier,
        val key: Any?,
        val children: List<SidebarNode>,
    ) : SidebarNode

    data class MenuButton(
        val label: String,
        val onClick: () -> Unit,
        val modifier: Modifier,
        val enabled: Boolean,
        val isSelected: Boolean,
        val variant: SidebarMenuButtonVariant,
        val size: SidebarMenuButtonSize,
        val icon: (@Composable () -> Unit)?,
    ) : SidebarNode

    data class MenuAction(
        val onClick: () -> Unit,
        val modifier: Modifier,
        val enabled: Boolean,
        val label: String,
    ) : SidebarNode

    data class MenuBadge(
        val text: String,
        val modifier: Modifier,
    ) : SidebarNode

    data class MenuSub(
        val modifier: Modifier,
        val children: List<SidebarNode>,
    ) : SidebarNode

    data class MenuSubItem(
        val modifier: Modifier,
        val children: List<SidebarNode>,
    ) : SidebarNode

    data class MenuSubButton(
        val label: String,
        val onClick: () -> Unit,
        val modifier: Modifier,
        val enabled: Boolean,
        val isSelected: Boolean,
        val size: SidebarMenuSubButtonSize,
    ) : SidebarNode
}

@Composable
internal fun RenderSidebarNodes(
    nodes: List<SidebarNode>,
    columnScope: ColumnScope? = null,
) {
    nodes.forEachIndexed { index, node ->
        key(sidebarNodeKey(node, index)) {
            RenderSidebarNode(node, columnScope)
        }
    }
}

@Composable
private fun RenderSidebarNode(
    node: SidebarNode,
    columnScope: ColumnScope?,
) {
    when (node) {
        is SidebarNode.Header -> SidebarHeader(
            modifier = node.modifier,
            block = node.block,
        )

        is SidebarNode.Footer -> SidebarFooter(
            modifier = node.modifier,
            block = node.block,
        )

        is SidebarNode.Content -> {
            requireNotNull(columnScope) { "Sidebar Content must be rendered inside Sidebar Column" }
            columnScope.SidebarContent(modifier = node.modifier) {
                RenderSidebarNodes(node.children)
            }
        }

        is SidebarNode.Group -> SidebarGroup(modifier = node.modifier) {
            RenderSidebarNodes(node.children)
        }

        is SidebarNode.Separator -> SidebarSeparator(modifier = node.modifier)

        is SidebarNode.Trigger -> {
            val sidebarState = requireSidebarState()
            SidebarTriggerButton(
                onClick = sidebarState::toggle,
                side = sidebarState.side,
                modifier = node.modifier,
            )
        }

        is SidebarNode.ComposeItem -> SidebarComposeItem(modifier = node.modifier) {
            node.content()
        }

        is SidebarNode.GroupLabel -> SidebarGroupLabel(text = node.text, modifier = node.modifier)

        is SidebarNode.GroupAction -> SidebarGroupAction(
            onClick = node.onClick,
            modifier = node.modifier,
            enabled = node.enabled,
            label = node.label,
        )

        is SidebarNode.Menu -> SidebarMenu(modifier = node.modifier) {
            RenderSidebarNodes(node.children)
        }

        is SidebarNode.MenuItem -> SidebarMenuItem(modifier = node.modifier) {
            RenderMenuItemChildren(node.children)
        }

        is SidebarNode.MenuButton -> SidebarMenuButton(
            label = node.label,
            onClick = node.onClick,
            modifier = node.modifier,
            enabled = node.enabled,
            isSelected = node.isSelected,
            variant = node.variant,
            size = node.size,
            icon = node.icon,
        )

        is SidebarNode.MenuAction -> SidebarMenuAction(
            onClick = node.onClick,
            modifier = node.modifier,
            enabled = node.enabled,
            label = node.label,
        )

        is SidebarNode.MenuBadge -> SidebarMenuBadge(text = node.text, modifier = node.modifier)

        is SidebarNode.MenuSub -> SidebarMenuSub(modifier = node.modifier) {
            RenderSidebarNodes(node.children)
        }

        is SidebarNode.MenuSubItem -> SidebarMenuSubItem(modifier = node.modifier) {
            RenderSidebarNodes(node.children)
        }

        is SidebarNode.MenuSubButton -> SidebarMenuSubButton(
            label = node.label,
            onClick = node.onClick,
            modifier = node.modifier,
            enabled = node.enabled,
            isSelected = node.isSelected,
            size = node.size,
        )
    }
}

@Composable
private fun RenderMenuItemChildren(children: List<SidebarNode>) {
    val buttons = children.filterIsInstance<SidebarNode.MenuButton>()
    val badges = children.filterIsInstance<SidebarNode.MenuBadge>()
    val actions = children.filterIsInstance<SidebarNode.MenuAction>()
    val trailing = badges + actions
    val below = children.filter {
        it !is SidebarNode.MenuButton &&
            it !is SidebarNode.MenuBadge &&
            it !is SidebarNode.MenuAction
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            buttons.forEach { button ->
                RenderSidebarNode(button, columnScope = null)
            }

            if (trailing.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = SidebarDefaults.ContentPadding),
                    horizontalArrangement = Arrangement.spacedBy(SidebarDefaults.MenuGap),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    trailing.forEachIndexed { index, node ->
                        key(sidebarNodeKey(node, index)) {
                            when (node) {
                                is SidebarNode.MenuBadge -> SidebarMenuBadge(
                                    text = node.text,
                                    modifier = node.modifier,
                                )

                                is SidebarNode.MenuAction -> SidebarMenuAction(
                                    onClick = node.onClick,
                                    modifier = node.modifier,
                                    enabled = node.enabled,
                                    label = node.label,
                                    inline = true,
                                )

                                else -> RenderSidebarNode(node, columnScope = null)
                            }
                        }
                    }
                }
            }
        }

        below.forEachIndexed { index, node ->
            key(sidebarNodeKey(node, index)) {
                RenderSidebarNode(node, columnScope = null)
            }
        }
    }
}
