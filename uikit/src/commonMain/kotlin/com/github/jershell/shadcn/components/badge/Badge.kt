package com.github.jershell.shadcn.components.badge

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.composeunstyled.UnstyledButton
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.icon.ShadcnIcon
import com.github.jershell.shadcn.components.icon.ShadcnIconContent
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles

/**
 * A small status indicator styled after shadcn/ui.
 *
 * Supports the standard shadcn variants: default, secondary, destructive, and outline,
 * plus a [Chip] variant useful for removable tags such as combobox chips.
 * Additionally, a custom [color] can be provided for the background, which is useful
 * for tags or dynamic status colors.
 *
 * @param text The badge label.
 * @param modifier Modifier applied to the badge container.
 * @param variant Visual style of the badge. Ignored when a custom [color] is set.
 * @param color Optional custom background color. When set, [variant] colors are overridden.
 * @param contentColor Optional custom content color. Defaults to the variant content color.
 * @param leadingIcon Optional icon displayed before the text.
 * @param trailingIcon Optional icon displayed after the text.
 * @param onTrailingIconClick Optional click handler for the trailing icon. When set, the trailing
 *   icon is rendered as a small, focusable button.
 * @param trailingIconClickEnabled Whether the trailing icon click button is interactive.
 */
@Composable
fun Badge(
    text: String,
    modifier: Modifier = Modifier,
    variant: BadgeVariant = BadgeVariant.Default,
    color: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified,
    leadingIcon: ShadcnIcon? = null,
    trailingIcon: ShadcnIcon? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    trailingIconClickEnabled: Boolean = true,
) {
    val variantColors = resolveBadgeColors(variant)
    val background = if (color != Color.Unspecified) color else variantColors.background
    val content = if (contentColor != Color.Unspecified) contentColor else variantColors.content
    val border = if (color != Color.Unspecified) Color.Transparent else variantColors.border
    val radius = when (variant) {
        BadgeVariant.Chip -> Theme[DimProps][DimTokens.radiusSm]
        else -> Theme[DimProps][DimTokens.radiusFull]
    }
    val shape = RoundedCornerShape(radius)
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]

    Box(
        modifier = modifier
            .clip(shape)
            .border(
                width = borderWidth,
                color = border,
                shape = shape,
            )
            .background(background, shape),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = TwDimensions.paddingPxToken2,
                vertical = TwDimensions.paddingPxToken1,
            ),
            horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken1),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingIcon?.let { icon ->
                ShadcnIconContent(
                    icon = icon,
                    contentDescription = null,
                    modifier = Modifier.size(TwDimensions.heightHToken3),
                    tint = content,
                )
            }
            BasicText(
                text = text,
                style = TypographyStyles.textXsMedium.copy(color = content),
            )
            trailingIcon?.let { icon ->
                if (onTrailingIconClick != null) {
                    UnstyledButton(
                        onClick = onTrailingIconClick,
                        enabled = trailingIconClickEnabled,
                        indication = null,
                        modifier = Modifier.size(TwDimensions.heightHToken3),
                    ) {
                        ShadcnIconContent(
                            icon = icon,
                            contentDescription = null,
                            modifier = Modifier.size(TwDimensions.heightHToken3),
                            tint = content,
                        )
                    }
                } else {
                    ShadcnIconContent(
                        icon = icon,
                        contentDescription = null,
                        modifier = Modifier.size(TwDimensions.heightHToken3),
                        tint = content,
                    )
                }
            }
        }
    }
}
