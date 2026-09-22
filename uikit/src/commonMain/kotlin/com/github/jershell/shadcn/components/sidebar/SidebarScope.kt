package com.github.jershell.shadcn.components.sidebar

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@DslMarker
annotation class SidebarDsl

/**
 * Receiver of render-phase blocks (Header/Footer): exposes the sidebar
 * expansion state in addition to the section layout scope.
 *
 * ```
 * Header {
 *     if (isExpanded) P("Acme Inc.")
 * }
 * ```
 */
@SidebarDsl
interface SidebarSectionScope : ColumnScope {
    val isExpanded: Boolean

    val sidebarController: SidebarController

    fun toggle()

    @Composable
    fun Trigger(modifier: Modifier = Modifier)
}

internal class SidebarSectionScopeImpl(
    columnScope: ColumnScope,
    private val sidebarState: SidebarState,
) : SidebarSectionScope, ColumnScope by columnScope {
    override val isExpanded: Boolean
        get() = sidebarState.expanded

    override val sidebarController: SidebarController
        get() = sidebarState.controller

    override fun toggle() = sidebarState.toggle()

    @Composable
    override fun Trigger(modifier: Modifier) {
        SidebarTriggerButton(
            onClick = sidebarState::toggle,
            side = sidebarState.side,
            modifier = modifier,
        )
    }
}

@SidebarDsl
interface SidebarComposeScope {
    fun ComposeItem(
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit,
    )
}

@SidebarDsl
interface SidebarContentScope : SidebarComposeScope {
    fun Group(
        modifier: Modifier = Modifier,
        block: SidebarGroupScope.() -> Unit,
    )

    fun Separator(modifier: Modifier = Modifier)
}

@SidebarDsl
interface SidebarScope {
    fun Header(
        modifier: Modifier = Modifier,
        block: @Composable SidebarSectionScope.() -> Unit,
    )

    fun Footer(
        modifier: Modifier = Modifier,
        block: @Composable SidebarSectionScope.() -> Unit,
    )

    fun Content(
        modifier: Modifier = Modifier,
        block: SidebarContentScope.() -> Unit,
    )

    fun Trigger(modifier: Modifier = Modifier)
}

@SidebarDsl
interface SidebarGroupScope : SidebarComposeScope {
    fun Label(
        text: String,
        modifier: Modifier = Modifier,
    )

    fun Action(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        label: String = "+",
    )

    fun Menu(
        modifier: Modifier = Modifier,
        block: SidebarMenuScope.() -> Unit,
    )
}

@SidebarDsl
interface SidebarMenuScope {
    fun Item(
        modifier: Modifier = Modifier,
        key: Any? = null,
        block: SidebarMenuItemScope.() -> Unit,
    )

    fun Button(
        label: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        isSelected: Boolean = false,
        variant: SidebarMenuButtonVariant = SidebarMenuButtonVariant.Default,
        size: SidebarMenuButtonSize = SidebarMenuButtonSize.Default,
        icon: @Composable (() -> Unit)? = null,
    )
}

@SidebarDsl
interface SidebarMenuItemScope : SidebarComposeScope {
    fun Button(
        label: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        isSelected: Boolean = false,
        variant: SidebarMenuButtonVariant = SidebarMenuButtonVariant.Default,
        size: SidebarMenuButtonSize = SidebarMenuButtonSize.Default,
        icon: @Composable (() -> Unit)? = null,
    )

    fun Action(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        label: String = "…",
    )

    fun Badge(
        text: String,
        modifier: Modifier = Modifier,
    )

    fun Sub(
        modifier: Modifier = Modifier,
        block: SidebarMenuSubScope.() -> Unit,
    )
}

@SidebarDsl
interface SidebarMenuSubScope {
    fun Item(
        modifier: Modifier = Modifier,
        block: SidebarMenuSubItemScope.() -> Unit,
    )

    fun Button(
        label: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        isSelected: Boolean = false,
        size: SidebarMenuSubButtonSize = SidebarMenuSubButtonSize.Md,
    )
}

@SidebarDsl
interface SidebarMenuSubItemScope : SidebarComposeScope {
    fun Button(
        label: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        isSelected: Boolean = false,
        size: SidebarMenuSubButtonSize = SidebarMenuSubButtonSize.Md,
    )
}

internal abstract class SidebarNodeBuilder {
    internal val nodes = mutableListOf<SidebarNode>()
}

internal class SidebarContentScopeBuilder : SidebarNodeBuilder(), SidebarContentScope {
    override fun ComposeItem(
        modifier: Modifier,
        content: @Composable () -> Unit,
    ) {
        addComposeItemNode(modifier, content)
    }

    override fun Group(
        modifier: Modifier,
        block: SidebarGroupScope.() -> Unit,
    ) {
        nodes += SidebarNode.Group(
            modifier = modifier,
            children = SidebarGroupScopeBuilder().apply(block).nodes.toList(),
        )
    }

    override fun Separator(modifier: Modifier) {
        nodes += SidebarNode.Separator(modifier)
    }
}

internal class SidebarScopeBuilder : SidebarNodeBuilder(), SidebarScope {
    override fun Header(
        modifier: Modifier,
        block: @Composable SidebarSectionScope.() -> Unit,
    ) {
        nodes += SidebarNode.Header(modifier = modifier, block = block)
    }

    override fun Footer(
        modifier: Modifier,
        block: @Composable SidebarSectionScope.() -> Unit,
    ) {
        nodes += SidebarNode.Footer(modifier = modifier, block = block)
    }

    override fun Content(
        modifier: Modifier,
        block: SidebarContentScope.() -> Unit,
    ) {
        nodes += SidebarNode.Content(
            modifier = modifier,
            children = SidebarContentScopeBuilder().apply(block).nodes.toList(),
        )
    }

    override fun Trigger(modifier: Modifier) {
        nodes += SidebarNode.Trigger(modifier = modifier)
    }
}

internal class SidebarGroupScopeBuilder : SidebarNodeBuilder(), SidebarGroupScope {
    override fun ComposeItem(
        modifier: Modifier,
        content: @Composable () -> Unit,
    ) {
        addComposeItemNode(modifier, content)
    }

    override fun Label(
        text: String,
        modifier: Modifier,
    ) {
        nodes += SidebarNode.GroupLabel(text = text, modifier = modifier)
    }

    override fun Action(
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        label: String,
    ) {
        nodes += SidebarNode.GroupAction(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            label = label,
        )
    }

    override fun Menu(
        modifier: Modifier,
        block: SidebarMenuScope.() -> Unit,
    ) {
        nodes += SidebarNode.Menu(
            modifier = modifier,
            children = SidebarMenuScopeBuilder().apply(block).nodes.toList(),
        )
    }
}

internal class SidebarMenuScopeBuilder : SidebarNodeBuilder(), SidebarMenuScope {
    override fun Item(
        modifier: Modifier,
        key: Any?,
        block: SidebarMenuItemScope.() -> Unit,
    ) {
        nodes += SidebarNode.MenuItem(
            modifier = modifier,
            key = key,
            children = SidebarMenuItemScopeBuilder().apply(block).nodes.toList(),
        )
    }

    override fun Button(
        label: String,
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        isSelected: Boolean,
        variant: SidebarMenuButtonVariant,
        size: SidebarMenuButtonSize,
        icon: @Composable (() -> Unit)?,
    ) {
        addMenuButtonNode(
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
}

internal class SidebarMenuItemScopeBuilder : SidebarNodeBuilder(), SidebarMenuItemScope {
    override fun ComposeItem(
        modifier: Modifier,
        content: @Composable () -> Unit,
    ) {
        addComposeItemNode(modifier, content)
    }

    override fun Button(
        label: String,
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        isSelected: Boolean,
        variant: SidebarMenuButtonVariant,
        size: SidebarMenuButtonSize,
        icon: @Composable (() -> Unit)?,
    ) {
        addMenuButtonNode(
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

    override fun Action(
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        label: String,
    ) {
        nodes += SidebarNode.MenuAction(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            label = label,
        )
    }

    override fun Badge(
        text: String,
        modifier: Modifier,
    ) {
        nodes += SidebarNode.MenuBadge(text = text, modifier = modifier)
    }

    override fun Sub(
        modifier: Modifier,
        block: SidebarMenuSubScope.() -> Unit,
    ) {
        nodes += SidebarNode.MenuSub(
            modifier = modifier,
            children = SidebarMenuSubScopeBuilder().apply(block).nodes.toList(),
        )
    }
}

internal class SidebarMenuSubScopeBuilder : SidebarNodeBuilder(), SidebarMenuSubScope {
    override fun Item(
        modifier: Modifier,
        block: SidebarMenuSubItemScope.() -> Unit,
    ) {
        nodes += SidebarNode.MenuSubItem(
            modifier = modifier,
            children = SidebarMenuSubItemScopeBuilder().apply(block).nodes.toList(),
        )
    }

    override fun Button(
        label: String,
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        isSelected: Boolean,
        size: SidebarMenuSubButtonSize,
    ) {
        addMenuSubButtonNode(
            label = label,
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            isSelected = isSelected,
            size = size,
        )
    }
}

internal class SidebarMenuSubItemScopeBuilder : SidebarNodeBuilder(), SidebarMenuSubItemScope {
    override fun ComposeItem(
        modifier: Modifier,
        content: @Composable () -> Unit,
    ) {
        addComposeItemNode(modifier, content)
    }

    override fun Button(
        label: String,
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        isSelected: Boolean,
        size: SidebarMenuSubButtonSize,
    ) {
        addMenuSubButtonNode(
            label = label,
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            isSelected = isSelected,
            size = size,
        )
    }
}
