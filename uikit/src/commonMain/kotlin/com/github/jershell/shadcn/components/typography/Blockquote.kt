package com.github.jershell.shadcn.components.typography

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.TypographyStyles

@Composable
fun Blockquote(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = defaultForegroundColor(),
) {
    val borderColor = Theme[ColorProps][ColorTokens.border]
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]

    Box(
        modifier = modifier
            .drawBehind {
                val strokeWidth = borderWidth * 2
                drawLine(
                    color = borderColor,
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = strokeWidth.toPx(),
                )
            }
            .padding(start = 24.dp),
    ) {
        TypographyText(
            text = text,
            style = TypographyStyles.textBaseRegular.copy(fontStyle = FontStyle.Italic),
            color = color,
        )
    }
}
