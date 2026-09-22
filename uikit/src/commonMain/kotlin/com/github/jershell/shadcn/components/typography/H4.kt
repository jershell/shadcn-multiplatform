package com.github.jershell.shadcn.components.typography

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.jershell.shadcn.theme.TypographyStyles

@Composable
fun H4(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = defaultForegroundColor(),
) {
    TypographyText(
        text = text,
        style = TypographyStyles.textXlSemiBold,
        modifier = modifier,
        color = color
    )
}
