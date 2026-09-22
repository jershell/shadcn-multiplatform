package com.github.jershell.shadcn.components.typography

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.jershell.shadcn.theme.TypographyStyles

@Composable
fun UnorderedList(
    items: List<String>,
    modifier: Modifier = Modifier,
    color: Color = defaultForegroundColor(),
) {
    Column(modifier = modifier) {
        items.forEach { item ->
            Row {
                TypographyText(
                    text = "•",
                    style = TypographyStyles.textBaseRegular,
                    modifier = Modifier.padding(end = 8.dp),
                    color = color,
                )
                TypographyText(
                    text = item,
                    style = TypographyStyles.textBaseRegular,
                    color = color,
                )
            }
        }
    }
}
