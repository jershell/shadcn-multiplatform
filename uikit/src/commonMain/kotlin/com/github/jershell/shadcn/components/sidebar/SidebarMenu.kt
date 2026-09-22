package com.github.jershell.shadcn.components.sidebar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.composeunstyled.UnstyledIcon
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.utils.leftBorder

enum class SidebarMenuButtonVariant {
    Default,
    Outline,
}

enum class SidebarMenuButtonSize {
    Default,
    Sm,
    Lg,
}

enum class SidebarMenuSubButtonSize {
    Sm,
    Md,
}

/**
 * An icon colored like the sidebar item text: `sidebarForeground` normally,
 * `sidebarAccentForeground` when the item is selected/hovered/pressed, dimmed when
 * disabled. Use it inside the `icon` slot of [SidebarMenuScope.Button]:
 * `icon = { SidebarIcon(Lucide.House) }`.
 */
@Composable
fun SidebarIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    val localTint = LocalSidebarItemTint.current
    UnstyledIcon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier,
        tint = if (localTint != Color.Unspecified) localTint
        else Theme[ColorProps][ColorTokens.sidebarForeground],
    )
}

@Composable
internal fun SidebarMenu(
    modifier: Modifier = Modifier,
    block: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SidebarDefaults.MenuGap),
        content = { block() },
    )
}

@Composable
internal fun SidebarMenuItem(
    modifier: Modifier = Modifier,
    block: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        content = { block() },
    )
}

@Composable
internal fun SidebarMenuButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isSelected: Boolean = false,
    variant: SidebarMenuButtonVariant = SidebarMenuButtonVariant.Default,
    size: SidebarMenuButtonSize = SidebarMenuButtonSize.Default,
    icon: (@Composable () -> Unit)? = null,
) {
    val sidebarState = requireSidebarState()
    val sidebarBorder = Theme[ColorProps][ColorTokens.sidebarBorder]
    val radiusMd = Theme[DimProps][DimTokens.radiusMd]
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]

    val height = when (size) {
        SidebarMenuButtonSize.Default -> SidebarDefaults.MenuButtonHeight
        SidebarMenuButtonSize.Sm -> SidebarDefaults.MenuButtonHeightSm
        SidebarMenuButtonSize.Lg -> SidebarDefaults.MenuButtonHeightLg
    }

    val shape = RoundedCornerShape(radiusMd)
    val textStyle = if (size == SidebarMenuButtonSize.Sm) {
        SidebarTextStyle.MenuSubItem
    } else {
        SidebarTextStyle.MenuItem
    }

    val buttonModifier = modifier
        .then(
            if (sidebarState.isIconCollapsed) {
                Modifier.width(height)
            } else {
                Modifier.fillMaxWidth()
            },
        )
        .height(height)
        .then(
            if (variant == SidebarMenuButtonVariant.Outline) {
                Modifier.border(borderWidth, sidebarBorder, shape)
            } else {
                Modifier
            },
        )

    SidebarInteractiveSurface(
        onClick = onClick,
        modifier = buttonModifier,
        enabled = enabled,
        isSelected = isSelected,
        shape = shape,
        content = {
            if (sidebarState.isIconCollapsed) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height),
                    contentAlignment = Alignment.Center,
                ) {
                    SidebarMenuButtonIcon(
                        label = label,
                        icon = icon,
                        textStyle = textStyle,
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SidebarDefaults.ContentPadding),
                    horizontalArrangement = Arrangement.spacedBy(SidebarDefaults.ContentPadding),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (icon != null) {
                        Box(
                            modifier = Modifier.size(SidebarDefaults.IconSize),
                            contentAlignment = Alignment.Center,
                        ) {
                            icon()
                        }
                    }
                    SidebarText(
                        text = label,
                        style = textStyle,
                    )
                }
            }
        },
    )
}

@Composable
private fun SidebarMenuButtonIcon(
    label: String,
    icon: (@Composable () -> Unit)?,
    textStyle: SidebarTextStyle,
) {
    if (icon != null) {
        Box(
            modifier = Modifier.size(SidebarDefaults.IconSize),
            contentAlignment = Alignment.Center,
        ) {
            icon()
        }
    } else {
        SidebarText(
            text = label.take(1),
            style = textStyle,
        )
    }
}

@Composable
internal fun SidebarMenuAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String = "…",
    inline: Boolean = false,
) {
    val sidebarState = requireSidebarState()
    if (sidebarState.isIconCollapsed) return

    val radiusMd = Theme[DimProps][DimTokens.radiusMd]

    val actionButton: @Composable () -> Unit = {
        SidebarActionButton(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier.size(20.dp),
            shape = RoundedCornerShape(radiusMd),
            content = {
                SidebarText(text = label, style = SidebarTextStyle.Action)
            },
        )
    }

    if (inline) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            actionButton()
        }
    } else {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd,
        ) {
            actionButton()
        }
    }
}

@Composable
internal fun SidebarMenuBadge(
    text: String,
    modifier: Modifier = Modifier,
) {
    val sidebarState = requireSidebarState()
    if (sidebarState.isIconCollapsed) return

    val sidebarForeground = Theme[ColorProps][ColorTokens.sidebarForeground]
    val radiusMd = Theme[DimProps][DimTokens.radiusMd]

    Box(
        modifier = modifier
            .height(20.dp)
            .background(sidebarForeground.copy(alpha = 0.08f), RoundedCornerShape(radiusMd))
            .padding(horizontal = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        SidebarText(text = text, style = SidebarTextStyle.Badge)
    }
}

@Composable
internal fun SidebarMenuSub(
    modifier: Modifier = Modifier,
    block: @Composable ColumnScope.() -> Unit,
) {
    val sidebarState = requireSidebarState()
    if (sidebarState.isIconCollapsed) return

    val sidebarBorder = Theme[ColorProps][ColorTokens.sidebarBorder]
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = SidebarDefaults.SubMenuIndent)
            .leftBorder(sidebarBorder, borderWidth)
            .padding(start = SidebarDefaults.ContentPadding, top = 2.dp, bottom = 2.dp),
        verticalArrangement = Arrangement.spacedBy(SidebarDefaults.MenuGap),
        content = block,
    )
}

@Composable
internal fun SidebarMenuSubItem(
    modifier: Modifier = Modifier,
    block: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        content = { block() },
    )
}

@Composable
internal fun SidebarMenuSubButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isSelected: Boolean = false,
    size: SidebarMenuSubButtonSize = SidebarMenuSubButtonSize.Md,
) {
    val sidebarState = requireSidebarState()
    if (sidebarState.isIconCollapsed) return

    val radiusMd = Theme[DimProps][DimTokens.radiusMd]
    val textStyle = when (size) {
        SidebarMenuSubButtonSize.Sm -> SidebarTextStyle.MenuSubItem
        SidebarMenuSubButtonSize.Md -> SidebarTextStyle.MenuItem
    }

    SidebarInteractiveSurface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(SidebarDefaults.MenuButtonHeightSm),
        enabled = enabled,
        isSelected = isSelected,
        shape = RoundedCornerShape(radiusMd),
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SidebarDefaults.ContentPadding),
                horizontalArrangement = Arrangement.spacedBy(SidebarDefaults.ContentPadding),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SidebarText(text = label, style = textStyle)
            }
        },
    )
}
