package com.github.jershell.shadcn.components.label

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.TypographyStyles

/**
 * A label styled after shadcn/ui.
 *
 * Renders as `text-sm` / `font-medium` and dims when [enabled] is `false`.
 */
@Composable
fun Label(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = Theme[ColorProps][ColorTokens.foreground],
) {
    BasicText(
        text = text,
        modifier = modifier.alpha(if (enabled) 1f else 0.5f),
        style = TypographyStyles.textSmMedium.copy(color = color),
    )
}
