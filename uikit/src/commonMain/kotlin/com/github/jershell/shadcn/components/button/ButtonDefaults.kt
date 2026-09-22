package com.github.jershell.shadcn.components.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.github.jershell.shadcn.theme.BaseTokens
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles

enum class ButtonVariant {
    Default,
    Destructive,
    Outline,
    Secondary,
    Ghost,
    Link,
}

enum class ButtonSize {
    Default,
    Xs,
    Sm,
    Lg,
    Icon,
    IconXs,
    IconSm,
    IconLg,
}

internal data class ButtonSizeSpec(
    val height: Dp,
    val horizontalPadding: Dp,
    val iconSize: Dp,
    val gap: Dp,
    val textStyle: TextStyle,
    val isIconOnly: Boolean,
)

@Composable
internal fun ButtonSize.spec(): ButtonSizeSpec = when (this) {
    ButtonSize.Default -> ButtonSizeSpec(
        height = TwDimensions.heightHToken9,
        horizontalPadding = TwDimensions.paddingPxToken4,
        iconSize = TwDimensions.heightHToken4,
        gap = TwDimensions.gapGapToken2,
        textStyle = TypographyStyles.textSmMedium,
        isIconOnly = false,
    )

    ButtonSize.Xs -> ButtonSizeSpec(
        height = TwDimensions.heightHToken6,
        horizontalPadding = TwDimensions.paddingPxToken2,
        iconSize = TwDimensions.heightHToken3,
        gap = BaseTokens.token4, // gap-1
        textStyle = TypographyStyles.textXsMedium,
        isIconOnly = false,
    )

    ButtonSize.Sm -> ButtonSizeSpec(
        height = TwDimensions.heightHToken8,
        horizontalPadding = TwDimensions.paddingPxToken3,
        iconSize = TwDimensions.heightHToken4,
        gap = TwDimensions.gapGapN1_5,
        textStyle = TypographyStyles.textSmMedium,
        isIconOnly = false,
    )

    ButtonSize.Lg -> ButtonSizeSpec(
        height = TwDimensions.heightHToken10,
        horizontalPadding = TwDimensions.paddingPxToken6,
        iconSize = TwDimensions.heightHToken4,
        gap = TwDimensions.gapGapToken2,
        textStyle = TypographyStyles.textSmMedium,
        isIconOnly = false,
    )

    ButtonSize.Icon -> ButtonSizeSpec(
        height = TwDimensions.heightHToken9,
        horizontalPadding = TwDimensions.paddingPxToken0,
        iconSize = TwDimensions.heightHToken4,
        gap = TwDimensions.gapGapToken2,
        textStyle = TypographyStyles.textSmMedium,
        isIconOnly = true,
    )

    ButtonSize.IconXs -> ButtonSizeSpec(
        height = TwDimensions.heightHToken6,
        horizontalPadding = TwDimensions.paddingPxToken0,
        iconSize = TwDimensions.heightHToken3,
        gap = TwDimensions.gapGapN1_5,
        textStyle = TypographyStyles.textXsMedium,
        isIconOnly = true,
    )

    ButtonSize.IconSm -> ButtonSizeSpec(
        height = TwDimensions.heightHToken8,
        horizontalPadding = TwDimensions.paddingPxToken0,
        iconSize = TwDimensions.heightHToken4,
        gap = TwDimensions.gapGapN1_5,
        textStyle = TypographyStyles.textSmMedium,
        isIconOnly = true,
    )

    ButtonSize.IconLg -> ButtonSizeSpec(
        height = TwDimensions.heightHToken10,
        horizontalPadding = TwDimensions.paddingPxToken0,
        iconSize = TwDimensions.heightHToken4,
        gap = TwDimensions.gapGapToken2,
        textStyle = TypographyStyles.textSmMedium,
        isIconOnly = true,
    )
}
