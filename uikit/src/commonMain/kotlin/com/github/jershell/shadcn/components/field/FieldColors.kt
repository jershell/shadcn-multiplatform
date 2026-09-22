package com.github.jershell.shadcn.components.field

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens

/**
 * Propagated by [Field] to its children: labels/titles/descriptions color
 * their text `destructive` when the field is invalid, and dim when disabled.
 */
internal val LocalFieldIsInvalid = compositionLocalOf { false }

internal val LocalFieldEnabled = compositionLocalOf { true }

internal data class FieldTextColors(
    val content: Color,
    val muted: Color,
    val destructive: Color,
    val border: Color,
    val background: Color,
)

@Composable
internal fun resolveFieldTextColors(isInvalid: Boolean): FieldTextColors {
    val foreground = Theme[ColorProps][ColorTokens.foreground]
    val destructive = Theme[ColorProps][ColorTokens.destructive]
    return FieldTextColors(
        content = if (isInvalid) destructive else foreground,
        muted = Theme[ColorProps][ColorTokens.mutedForeground],
        destructive = destructive,
        border = Theme[ColorProps][ColorTokens.border],
        background = Theme[ColorProps][ColorTokens.background],
    )
}