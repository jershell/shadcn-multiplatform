package com.github.jershell.shadcn.components.sidebar

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import com.composeunstyled.UnstyledButton
import com.composeunstyled.focusRing
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens

@Composable
private fun sidebarInteractiveBackground(
    enabled: Boolean,
    isSelected: Boolean,
    isHovered: Boolean,
    isPressed: Boolean,
    accent: Color,
): Color = when {
    !enabled -> Color.Transparent
    isSelected || isHovered || isPressed -> accent
    else -> Color.Transparent
}

@Composable
internal fun SidebarActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isSelected: Boolean = false,
    shape: Shape,
    content: @Composable () -> Unit,
) {
    val accent = Theme[ColorProps][ColorTokens.sidebarAccent]
    val ring = Theme[ColorProps][ColorTokens.sidebarRing]
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val backgroundColor = sidebarInteractiveBackground(
        enabled = enabled,
        isSelected = isSelected,
        isHovered = isHovered,
        isPressed = isPressed,
        accent = accent,
    )
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val itemState = SidebarItemState(
        isSelected = isSelected,
        isHovered = isHovered,
        isPressed = isPressed,
        enabled = enabled,
    )
    val itemTint = menuLabelColor(
        enabled = enabled,
        highlighted = isSelected || isHovered || isPressed,
        foreground = Theme[ColorProps][ColorTokens.sidebarForeground],
        accentForeground = Theme[ColorProps][ColorTokens.sidebarAccentForeground],
    )

    UnstyledButton(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        indication = null,
        modifier = modifier
            .semantics {
                role = Role.Button
                selected = isSelected
            }
            .clip(shape)
            .background(backgroundColor, shape)
            .focusRing(
                interactionSource = interactionSource,
                width = borderWidth,
                color = ring,
                shape = shape,
            ),
        contentAlignment = Alignment.Center,
        content = {
            CompositionLocalProvider(
                LocalSidebarItemState provides itemState,
                LocalSidebarItemTint provides itemTint,
            ) {
                content()
            }
        },
    )
}

@Composable
internal fun SidebarInteractiveSurface(
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isSelected: Boolean = false,
    shape: Shape,
    content: @Composable () -> Unit,
) {
    val accent = Theme[ColorProps][ColorTokens.sidebarAccent]
    val ring = Theme[ColorProps][ColorTokens.sidebarRing]
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val backgroundColor = sidebarInteractiveBackground(
        enabled = enabled,
        isSelected = isSelected,
        isHovered = isHovered,
        isPressed = isPressed,
        accent = accent,
    )
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val itemState = SidebarItemState(
        isSelected = isSelected,
        isHovered = isHovered,
        isPressed = isPressed,
        enabled = enabled,
    )
    val itemTint = menuLabelColor(
        enabled = enabled,
        highlighted = isSelected || isHovered || isPressed,
        foreground = Theme[ColorProps][ColorTokens.sidebarForeground],
        accentForeground = Theme[ColorProps][ColorTokens.sidebarAccentForeground],
    )
    val surfaceModifier = modifier
        .semantics {
            role = Role.Button
            selected = isSelected
        }
        .clip(shape)
        .background(backgroundColor, shape)
        .focusRing(
            interactionSource = interactionSource,
            width = borderWidth,
            color = ring,
            shape = shape,
        )

    val wrappedContent: @Composable () -> Unit = {
        CompositionLocalProvider(
            LocalSidebarItemState provides itemState,
            LocalSidebarItemTint provides itemTint,
        ) {
            content()
        }
    }

    if (onClick != null) {
        UnstyledButton(
            onClick = onClick,
            enabled = enabled,
            interactionSource = interactionSource,
            indication = null,
            modifier = surfaceModifier,
            contentAlignment = Alignment.CenterStart,
            content = wrappedContent,
        )
    } else {
        Box(
            modifier = surfaceModifier,
            contentAlignment = Alignment.CenterStart,
        ) {
            wrappedContent()
        }
    }
}
