package com.github.jershell.shadcn.components.alert

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.icon.ShadcnIcon
import com.github.jershell.shadcn.components.icon.ShadcnIconContent
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles

internal val LocalAlertVariant = staticCompositionLocalOf { AlertVariant.Default }

/**
 * A callout for displaying important messages, styled after shadcn/ui.
 *
 * Supports a leading icon, a title, and a description. The icon and title/description are
 * arranged horizontally, with the icon aligned to the top of the title.
 *
 * @param modifier Modifier applied to the alert container.
 * @param variant Visual style of the alert.
 * @param leadingIcon Optional icon displayed at the start of the alert.
 * @param content Title and description content, usually [AlertTitle] and [AlertDescription].
 */
@Composable
fun Alert(
    modifier: Modifier = Modifier,
    variant: AlertVariant = AlertVariant.Default,
    leadingIcon: ShadcnIcon? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = resolveAlertColors(variant)
    val radius = Theme[DimProps][DimTokens.radiusMd]
    val shape = RoundedCornerShape(radius)
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]

    CompositionLocalProvider(LocalAlertVariant provides variant) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(shape)
                .border(borderWidth, colors.border, shape)
                .background(colors.background, shape)
                .padding(
                    horizontal = TwDimensions.paddingPxToken4,
                    vertical = TwDimensions.paddingPxToken3,
                ),
            horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken3),
            verticalAlignment = Alignment.Top,
        ) {
            leadingIcon?.let { icon ->
                ShadcnIconContent(
                    icon = icon,
                    contentDescription = null,
                    modifier = Modifier.size(TwDimensions.heightHToken4),
                    tint = colors.icon,
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken1),
            ) {
                content()
            }
        }
    }
}

/**
 * The title of an [Alert].
 *
 * @param text The title text.
 * @param modifier Modifier applied to the title.
 */
@Composable
fun AlertTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    val colors = resolveAlertColors(LocalAlertVariant.current)
    BasicText(
        text = text,
        modifier = modifier,
        style = TypographyStyles.textSmSemiBold.copy(color = colors.title),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

/**
 * The description of an [Alert].
 *
 * @param text The description text.
 * @param modifier Modifier applied to the description.
 */
@Composable
fun AlertDescription(
    text: String,
    modifier: Modifier = Modifier,
) {
    val colors = resolveAlertColors(LocalAlertVariant.current)
    BasicText(
        text = text,
        modifier = modifier,
        style = TypographyStyles.textSmRegular.copy(color = colors.description),
    )
}
