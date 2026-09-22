package com.github.jershell.shadcn.components.typography

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.composeunstyled.UnstyledButton
import com.composeunstyled.UnstyledIcon
import com.composeunstyled.theme.Theme
import com.composables.icons.lucide.Copy
import com.composables.icons.lucide.Lucide
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.TypographyStyles
import com.github.jershell.shadcn.generated.resources.Res
import com.github.jershell.shadcn.generated.resources.inline_code_copy

@Composable
fun InlineCode(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = defaultForegroundColor(),
    selected: Boolean = false,
    onSelectedChange: (Boolean) -> Unit = {},
    selectEnabled: Boolean = true,
    copyEnabled: Boolean = true,
) {
    val mutedBackground = Theme[ColorProps][ColorTokens.muted]
    val ringColor = Theme[ColorProps][ColorTokens.ring]
    val radius = Theme[DimProps][DimTokens.radiusMd]
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val shape = RoundedCornerShape(radius)
    val textStyle = TypographyStyles.textSmSemiBold.copy(
        fontFamily = FontFamily.Monospace,
    )
    val clipboardManager = LocalClipboardManager.current
    val interactionSource = remember { MutableInteractionSource() }
    var isSelected by remember { mutableStateOf(selected) }
    LaunchedEffect(selected) { isSelected = selected }

    val backgroundColor = when {
        isSelected -> mutedBackground.copy(alpha = 0.8f)
        else -> mutedBackground
    }

    Row(
        modifier = modifier
            .background(backgroundColor, shape)
            .then(
                if (isSelected) {
                    Modifier.border(borderWidth * 2, ringColor, shape)
                } else {
                    Modifier
                },
            )
            .padding(horizontal = 5.dp, vertical = 3.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (selectEnabled) {
            UnstyledButton(
                onClick = {
                    val newSelected = !isSelected
                    isSelected = newSelected
                    onSelectedChange(newSelected)
                },
                interactionSource = interactionSource,
                indication = null,
            ) {
                SelectionContainer {
                    TypographyText(
                        text = text,
                        style = textStyle,
                        color = color,
                    )
                }
            }
        } else {
            TypographyText(
                text = text,
                style = textStyle,
                color = color,
            )
        }

        if (copyEnabled) {
            UnstyledButton(
                onClick = { clipboardManager.setText(AnnotatedString(text)) },
                indication = null,
                modifier = Modifier.size(16.dp),
            ) {
                UnstyledIcon(
                    imageVector = Lucide.Copy,
                    contentDescription = stringResource(Res.string.inline_code_copy),
                    modifier = Modifier.size(14.dp),
                    tint = color,
                )
            }
        }
    }
}
