package com.github.jershell.shadcn.components.button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.LocalThemeIsDark

internal data class ButtonColors(
    val container: Color,
    val content: Color,
    val border: Color?,
    val showShadow: Boolean,
)

@Composable
internal fun resolveButtonColors(
    variant: ButtonVariant,
    isHovered: Boolean,
    isPressed: Boolean,
): ButtonColors {
    val isDark by LocalThemeIsDark.current
    val primary = Theme[ColorProps][ColorTokens.primary]
    val primaryForeground = Theme[ColorProps][ColorTokens.primaryForeground]
    val destructive = Theme[ColorProps][ColorTokens.destructive]
    val secondary = Theme[ColorProps][ColorTokens.secondary]
    val secondaryForeground = Theme[ColorProps][ColorTokens.secondaryForeground]
    val accent = Theme[ColorProps][ColorTokens.accent]
    val accentForeground = Theme[ColorProps][ColorTokens.accentForeground]
    val background = Theme[ColorProps][ColorTokens.background]
    val border = Theme[ColorProps][ColorTokens.border]
    val input = Theme[ColorProps][ColorTokens.input]
    val foreground = Theme[ColorProps][ColorTokens.foreground]
    val isInteractive = isHovered || isPressed

    return when (variant) {
        ButtonVariant.Default -> ButtonColors(
            container = primary.withPressedOpacity(isInteractive, isPressed, normal = 1f, hovered = 0.9f, pressed = 0.85f),
            content = primaryForeground,
            border = null,
            showShadow = false,
        )

        ButtonVariant.Destructive -> {
            val base = if (isDark) destructive.copy(alpha = 0.6f) else destructive
            ButtonColors(
                container = base.withPressedOpacity(isInteractive, isPressed, normal = 1f, hovered = 0.9f, pressed = 0.85f),
                content = Color.White,
                border = null,
                showShadow = false,
            )
        }

        ButtonVariant.Outline -> {
            val baseBackground = if (isDark) input.copy(alpha = 0.3f) else background
            val hoverBackground = if (isDark) input.copy(alpha = 0.5f) else accent

            ButtonColors(
                container = when {
                    isPressed -> hoverBackground.copy(alpha = (hoverBackground.alpha * 0.9f).coerceIn(0f, 1f))
                    isHovered -> hoverBackground
                    else -> baseBackground
                },
                content = when {
                    isInteractive -> accentForeground
                    else -> foreground
                },
                border = if (isDark) input else border,
                // shadow-xs from the reference is dropped: compose shadows render
                // on desktop as a double glow (see BACKLOG.md)
                showShadow = false,
            )
        }

        ButtonVariant.Secondary -> ButtonColors(
            container = secondary.withPressedOpacity(isInteractive, isPressed, normal = 1f, hovered = 0.8f, pressed = 0.75f),
            content = secondaryForeground,
            border = null,
            showShadow = false,
        )

        ButtonVariant.Ghost -> ButtonColors(
            container = when {
                isInteractive -> accent.copy(alpha = if (isDark) 0.5f else 1f)
                else -> Color.Transparent
            },
            content = when {
                isInteractive -> accentForeground
                else -> foreground
            },
            border = null,
            showShadow = false,
        )

        ButtonVariant.Link -> ButtonColors(
            container = Color.Transparent,
            content = primary,
            border = null,
            showShadow = false,
        )
    }
}

@Composable
internal fun resolveButtonFocusRingColor(
    variant: ButtonVariant,
    isInvalid: Boolean = false,
): Color {
    val isDark by LocalThemeIsDark.current
    val ring = Theme[ColorProps][ColorTokens.ring]
    val destructive = Theme[ColorProps][ColorTokens.destructive]

    return when {
        // aria-invalid:ring-destructive/20, dark:aria-invalid:ring-destructive/40
        isInvalid -> destructive.copy(alpha = if (isDark) 0.4f else 0.2f)
        variant == ButtonVariant.Destructive -> destructive.copy(alpha = if (isDark) 0.4f else 0.2f)
        else -> ring.copy(alpha = 0.5f)
    }
}

private fun Color.withPressedOpacity(
    isInteractive: Boolean,
    isPressed: Boolean,
    normal: Float,
    hovered: Float,
    pressed: Float,
): Color = copy(
    alpha = when {
        isPressed -> alpha * pressed
        isInteractive -> alpha * hovered
        else -> alpha * normal
    }.coerceIn(0f, 1f),
)
