package com.github.jershell.shadcn.components.toggle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.BaseTokens
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.LocalThemeIsDark
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles

/**
 * Visual variants of the [Toggle] component, matching the shadcn/ui variants.
 */
enum class ToggleVariant {
    Default,
    Outline,
}

/**
 * Sizes of the [Toggle] component, matching the shadcn/ui sizes.
 */
enum class ToggleSize {
    Default,
    Sm,
    Lg,
}

internal data class ToggleColors(
    val container: Color,
    val content: Color,
    val border: Color?,
    val showShadow: Boolean,
)

@Composable
internal fun resolveToggleColors(
    variant: ToggleVariant,
    isOn: Boolean,
    isHovered: Boolean,
    isPressed: Boolean,
    isInvalid: Boolean,
): ToggleColors {
    val accent = Theme[ColorProps][ColorTokens.accent]
    val accentForeground = Theme[ColorProps][ColorTokens.accentForeground]
    val muted = Theme[ColorProps][ColorTokens.muted]
    val mutedForeground = Theme[ColorProps][ColorTokens.mutedForeground]
    val foreground = Theme[ColorProps][ColorTokens.foreground]
    val input = Theme[ColorProps][ColorTokens.input]
    val destructive = Theme[ColorProps][ColorTokens.destructive]
    val isInteractive = isHovered || isPressed

    return when (variant) {
        ToggleVariant.Default -> ToggleColors(
            container = when {
                isOn -> accent
                isInteractive -> muted
                else -> Color.Transparent
            },
            content = when {
                isOn -> accentForeground
                isInteractive -> mutedForeground
                else -> foreground
            },
            border = null,
            showShadow = false,
        )

        ToggleVariant.Outline -> ToggleColors(
            container = when {
                isOn || isInteractive -> accent
                else -> Color.Transparent
            },
            content = when {
                isOn || isInteractive -> accentForeground
                else -> foreground
            },
            border = if (isInvalid) destructive else input,
            // shadow-xs from the reference is dropped: compose shadows render
            // on desktop as a double glow (see BACKLOG.md)
            showShadow = false,
        )
    }
}

@Composable
internal fun resolveToggleFocusRingColor(isInvalid: Boolean): Color {
    val isDark by LocalThemeIsDark.current
    val ring = Theme[ColorProps][ColorTokens.ring]
    val destructive = Theme[ColorProps][ColorTokens.destructive]

    return when {
        isInvalid -> destructive.copy(alpha = if (isDark) 0.4f else 0.2f)
        else -> ring.copy(alpha = 0.5f)
    }
}

internal data class ToggleSizeSpec(
    val height: Dp,
    val horizontalPadding: Dp,
    val iconSize: Dp,
    val gap: Dp,
    val textStyle: TextStyle,
)

@Composable
internal fun ToggleSize.spec(): ToggleSizeSpec = when (this) {
    ToggleSize.Default -> ToggleSizeSpec(
        height = TwDimensions.heightHToken9,
        horizontalPadding = TwDimensions.paddingPxToken2, // px-2
        iconSize = TwDimensions.heightHToken4, // size-4
        gap = TwDimensions.gapGapToken2, // gap-2
        textStyle = TypographyStyles.textSmMedium,
    )

    ToggleSize.Sm -> ToggleSizeSpec(
        height = TwDimensions.heightHToken8,
        horizontalPadding = BaseTokens.token6, // px-1.5
        iconSize = TwDimensions.heightHToken4,
        gap = TwDimensions.gapGapToken2,
        textStyle = TypographyStyles.textSmMedium,
    )

    ToggleSize.Lg -> ToggleSizeSpec(
        height = TwDimensions.heightHToken10,
        horizontalPadding = BaseTokens.token10, // px-2.5
        iconSize = TwDimensions.heightHToken4,
        gap = TwDimensions.gapGapToken2,
        textStyle = TypographyStyles.textSmMedium,
    )
}
