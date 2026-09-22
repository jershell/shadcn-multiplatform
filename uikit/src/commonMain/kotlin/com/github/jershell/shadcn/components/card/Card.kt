package com.github.jershell.shadcn.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.Effects
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles

/**
 * A card component styled after shadcn/ui.
 *
 * Cards are flexible containers for grouping related content and actions.
 *
 * @param modifier Modifier applied to the card container.
 * @param content The card content, usually [CardHeader], [CardContent], and [CardFooter].
 */
@Composable
fun Card(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = resolveCardColors()
    val radius = Theme[DimProps][DimTokens.radiusLg]
    val shape = RoundedCornerShape(radius)
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .shadow(
                elevation = Effects.boxShadowShadowSm.radius,
                shape = shape,
                clip = false,
            )
            .border(borderWidth, colors.border, shape)
            .background(colors.background, shape),
        content = content,
    )
}

/**
 * The header section of a [Card].
 *
 * @param modifier Modifier applied to the header container.
 * @param content Header content, usually [CardTitle] and [CardDescription].
 */
@Composable
fun CardHeader(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = TwDimensions.paddingPxToken6,
                end = TwDimensions.paddingPxToken6,
                top = TwDimensions.paddingPxToken6,
                bottom = 0.dp
            ),
        verticalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken2),
        content = content,
    )
}

/**
 * The title of a [Card].
 *
 * @param text The title text.
 * @param modifier Modifier applied to the title.
 */
@Composable
fun CardTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    val colors = resolveCardColors()
    BasicText(
        text = text,
        modifier = modifier,
        style = TypographyStyles.textXlSemiBold.copy(color = colors.foreground),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

/**
 * The description of a [Card].
 *
 * @param text The description text.
 * @param modifier Modifier applied to the description.
 */
@Composable
fun CardDescription(
    text: String,
    modifier: Modifier = Modifier,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = TypographyStyles.textSmRegular.copy(
            color = Theme[ColorProps][ColorTokens.mutedForeground],
        ),
    )
}

/**
 * The main content section of a [Card].
 *
 * @param modifier Modifier applied to the content container.
 * @param content The card content.
 */
@Composable
fun CardContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = TwDimensions.paddingPxToken6,
                end = TwDimensions.paddingPxToken6,
                bottom = TwDimensions.paddingPxToken6,
                top = TwDimensions.paddingPxToken6,
            ),
        content = content,
    )
}

/**
 * The footer section of a [Card], typically used for actions.
 *
 * @param modifier Modifier applied to the footer container.
 * @param content Footer content, usually a row of buttons.
 */
@Composable
fun CardFooter(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = TwDimensions.paddingPxToken6,
                end = TwDimensions.paddingPxToken6,
                bottom = TwDimensions.paddingPxToken6,
            ),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}
