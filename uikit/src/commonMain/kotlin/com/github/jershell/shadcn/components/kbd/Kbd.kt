package com.github.jershell.shadcn.components.kbd

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles

/**
 * A keyboard key styled after shadcn/ui [Kbd](https://ui.shadcn.com/docs/components/kbd).
 * Displays a keyboard shortcut, usually inside input addons or tooltips.
 *
 * @param text Key label, e.g. "Enter".
 */
@Composable
fun Kbd(
    text: String,
    modifier: Modifier = Modifier,
) {
    Kbd(modifier = modifier) {
        BasicText(
            text = text,
            style = kbdTextStyle(),
        )
    }
}

/**
 * A keyboard key styled after shadcn/ui [Kbd](https://ui.shadcn.com/docs/components/kbd)
 * with custom content such as icons.
 */
@Composable
fun Kbd(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = resolveKbdColors()
    val shape = RoundedCornerShape(Theme[DimProps][DimTokens.radiusSm])
    Box(
        modifier = modifier
            .height(TwDimensions.heightHToken5)
            .defaultMinSize(minWidth = TwDimensions.heightHToken5)
            .clip(shape)
            .background(colors.container)
            .padding(horizontal = TwDimensions.paddingPxToken1),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken1),
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

/**
 * A group of [Kbd] keys displayed inline with a small gap, per shadcn/ui KbdGroup.
 */
@Composable
fun KbdGroup(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken1),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

/** Recommended icon size inside Kbd (size-3). */
internal val KbdIconSize = TwDimensions.heightHToken3

@Composable
private fun kbdTextStyle(): TextStyle =
    TypographyStyles.textXsMedium.copy(color = resolveKbdColors().content)
