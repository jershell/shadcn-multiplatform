package com.github.jershell.shadcn.components.sidebar

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.LocalShadcnFonts

internal enum class SidebarTextStyle {
    GroupLabel,
    MenuItem,
    MenuSubItem,
    Badge,
    Action,
}

@Composable
internal fun SidebarText(
    text: String,
    style: SidebarTextStyle,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight? = null,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = textStyle(style, fontWeight),
    )
}

@Composable
private fun textStyle(
    style: SidebarTextStyle,
    fontWeight: FontWeight?,
): TextStyle {
    val sidebarForeground = Theme[ColorProps][ColorTokens.sidebarForeground]
    val sidebarAccentForeground = Theme[ColorProps][ColorTokens.sidebarAccentForeground]
    val fontFamily = LocalShadcnFonts.current ?: FontFamily.Default

    return when (style) {
        SidebarTextStyle.GroupLabel -> TextStyle(
            fontSize = 12.sp,
            fontWeight = fontWeight ?: FontWeight.Medium,
            color = sidebarForeground.copy(alpha = 0.7f),
            fontFamily = fontFamily,
        )

        SidebarTextStyle.MenuItem -> {
            val itemState = currentSidebarItemState()
            val highlighted = itemState.isSelected || itemState.isHovered || itemState.isPressed
            TextStyle(
                fontSize = 14.sp,
                fontWeight = fontWeight ?: if (itemState.isSelected) FontWeight.Medium else FontWeight.Normal,
                color = menuLabelColor(
                    enabled = itemState.enabled,
                    highlighted = highlighted,
                    foreground = sidebarForeground,
                    accentForeground = sidebarAccentForeground,
                ),
                fontFamily = fontFamily,
            )
        }

        SidebarTextStyle.MenuSubItem -> {
            val itemState = currentSidebarItemState()
            val highlighted = itemState.isSelected || itemState.isHovered || itemState.isPressed
            TextStyle(
                fontSize = 12.sp,
                fontWeight = fontWeight ?: FontWeight.Normal,
                color = menuLabelColor(
                    enabled = itemState.enabled,
                    highlighted = highlighted,
                    foreground = sidebarForeground,
                    accentForeground = sidebarAccentForeground,
                ),
                fontFamily = fontFamily,
            )
        }

        SidebarTextStyle.Badge -> TextStyle(
            fontSize = 12.sp,
            fontWeight = fontWeight ?: FontWeight.Medium,
            color = sidebarForeground,
            fontFamily = fontFamily,
        )

        SidebarTextStyle.Action -> {
            val itemState = currentSidebarItemState()
            val highlighted = itemState.isSelected || itemState.isHovered || itemState.isPressed
            TextStyle(
                fontSize = 12.sp,
                fontWeight = fontWeight ?: FontWeight.Medium,
                color = menuLabelColor(
                    enabled = itemState.enabled,
                    highlighted = highlighted,
                    foreground = sidebarForeground,
                    accentForeground = sidebarAccentForeground,
                ),
                fontFamily = fontFamily,
            )
        }
    }
}

internal fun menuLabelColor(
    enabled: Boolean,
    highlighted: Boolean,
    foreground: Color,
    accentForeground: Color,
): Color = when {
    !enabled -> foreground.copy(alpha = 0.5f)
    highlighted -> accentForeground
    else -> foreground
}
