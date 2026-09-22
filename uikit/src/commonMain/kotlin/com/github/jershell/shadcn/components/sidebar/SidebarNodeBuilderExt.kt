package com.github.jershell.shadcn.components.sidebar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

internal fun SidebarNodeBuilder.addComposeItemNode(
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    nodes += SidebarNode.ComposeItem(modifier = modifier, content = content)
}

internal fun SidebarNodeBuilder.addMenuButtonNode(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    isSelected: Boolean,
    variant: SidebarMenuButtonVariant,
    size: SidebarMenuButtonSize,
    icon: (@Composable () -> Unit)? = null,
) {
    nodes += SidebarNode.MenuButton(
        label = label,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        isSelected = isSelected,
        variant = variant,
        size = size,
        icon = icon,
    )
}

internal fun SidebarNodeBuilder.addMenuSubButtonNode(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    isSelected: Boolean,
    size: SidebarMenuSubButtonSize,
) {
    nodes += SidebarNode.MenuSubButton(
        label = label,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        isSelected = isSelected,
        size = size,
    )
}
