package com.github.jershell.shadcn.components.item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.LocalThemeIsDark

internal data class ItemContainerColors(
    val background: Color,
    val border: Color,
    val hoverBackground: Color,
    val focusRing: Color,
)

internal data class ItemTextColors(
    val title: Color,
    val description: Color,
)

internal data class ItemMediaColors(
    val background: Color,
    val border: Color,
)

/**
 * Container colors of an [Item], matching the reference `itemVariants`:
 * `default` is transparent, `outline` uses `border-border`, `muted` is `bg-muted/50`.
 */
@Composable
internal fun resolveItemContainerColors(variant: ItemVariant): ItemContainerColors {
    val background = Theme[ColorProps][ColorTokens.background]
    val border = Theme[ColorProps][ColorTokens.border]
    val muted = Theme[ColorProps][ColorTokens.muted]
    val accent = Theme[ColorProps][ColorTokens.accent]
    val ring = Theme[ColorProps][ColorTokens.ring]

    return ItemContainerColors(
        background = when (variant) {
            ItemVariant.Default -> Color.Transparent
            ItemVariant.Outline -> background
            ItemVariant.Muted -> muted.copy(alpha = 0.5f)
        },
        border = when (variant) {
            ItemVariant.Outline -> border
            else -> Color.Transparent
        },
        // [a]:hover:bg-accent/50
        hoverBackground = accent.copy(alpha = 0.5f),
        // focus-visible:ring-[3px] ring-ring/50
        focusRing = ring.copy(alpha = 0.5f),
    )
}

/** Text colors of the Item pieces (`text-foreground` / `text-muted-foreground`). */
@Composable
internal fun resolveItemTextColors(): ItemTextColors = ItemTextColors(
    title = Theme[ColorProps][ColorTokens.foreground],
    description = Theme[ColorProps][ColorTokens.mutedForeground],
)

/**
 * Media colors shared by the icon/avatar variants: `bg-muted` with a `border`
 * hairline; avatar uses `bg-muted` without a border, matching the reference.
 */
@Composable
internal fun resolveItemMediaColors(variant: ItemMediaVariant): ItemMediaColors {
    val isDark by LocalThemeIsDark.current
    val muted = Theme[ColorProps][ColorTokens.muted]
    val border = Theme[ColorProps][ColorTokens.border]
    return when (variant) {
        ItemMediaVariant.Avatar -> ItemMediaColors(background = muted, border = Color.Transparent)
        else -> ItemMediaColors(background = muted, border = border)
    }
}
