package com.github.jershell.shadcn.components.typography

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens

@Composable
fun TypographyText(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    color: Color = Theme[ColorProps][ColorTokens.foreground],
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = style.copy(color = color),
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
    )
}

@Composable
internal fun defaultForegroundColor(): Color = Theme[ColorProps][ColorTokens.foreground]

@Composable
internal fun defaultMutedForegroundColor(): Color = Theme[ColorProps][ColorTokens.mutedForeground]
