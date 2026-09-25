package com.github.jershell.shadcn.components.typography

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.github.jershell.shadcn.theme.TypographyStyles

@Composable
fun ExtraSmall(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = defaultForegroundColor(),
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
) {
    TypographyText(
        text = text,
        style = TypographyStyles.textXsMedium,
        modifier = modifier,
        color = color,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines
    )
}