package com.github.jershell.shadcn.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.graphics.Shape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import com.composeunstyled.UnstyledButton
import com.composeunstyled.UnstyledIcon
import com.composeunstyled.focusRing
import com.github.jershell.shadcn.components.icon.ShadcnIcon
import com.github.jershell.shadcn.components.icon.toShadcnIcon
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.Effects

internal val LocalButtonContentColor = staticCompositionLocalOf { Color.Unspecified }
internal val LocalButtonIconSize = staticCompositionLocalOf { Dp.Unspecified }
internal val LocalButtonTextStyle = staticCompositionLocalOf { TextStyle.Default }
internal val LocalButtonLinkUnderlined = staticCompositionLocalOf { false }

/**
 * A button styled after shadcn/ui.
 *
 * Supports the standard shadcn variants (default, destructive, outline, secondary,
 * ghost, link) and sizes. When [isInvalid], the button shows the shadcn
 * `aria-invalid` state: destructive border and destructive focus ring.
 *
 * @param onClick Called when the button is clicked.
 * @param modifier Modifier applied to the button container.
 * @param enabled Whether the button is interactive. Disabled buttons are dimmed.
 * @param variant Visual style of the button.
 * @param size Size of the button.
 * @param isInvalid Shows the shadcn `aria-invalid` state: destructive border and focus ring.
 * @param shape Shape of the button container.
 * @param content Button content; use [ButtonText] and [ButtonIcon] for automatic
 *   coloring and sizing.
 */
@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: ButtonVariant = ButtonVariant.Default,
    size: ButtonSize = ButtonSize.Default,
    isInvalid: Boolean = false,
    shape: Shape = RoundedCornerShape(Theme[DimProps][DimTokens.radiusMd]),
    content: @Composable RowScope.() -> Unit,
) {
    val sizeSpec = size.spec()
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val isFocused by interactionSource.collectIsFocusedAsState()
    val colors = resolveButtonColors(
        variant = variant,
        isHovered = isHovered,
        isPressed = isPressed,
    )
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val focusRingColor = resolveButtonFocusRingColor(variant, isInvalid)
    val focusRingWidth = Effects.boxShadowFocusRing.spread
    // focus-visible:border-ring + aria-invalid:border-destructive:
    // the border becomes ring-colored on focus, destructive when invalid.
    val ring = Theme[ColorProps][ColorTokens.ring]
    val destructive = Theme[ColorProps][ColorTokens.destructive]
    val borderColor = when {
        isInvalid -> destructive
        isFocused && colors.border != null -> ring
        else -> colors.border
    }

    val containerModifier = modifier
        .alpha(if (enabled) 1f else 0.5f)
        .then(
            if (sizeSpec.isIconOnly) {
                Modifier.size(sizeSpec.height)
            } else {
                Modifier
                    .height(sizeSpec.height)
                    .defaultMinSize(minWidth = sizeSpec.height)
            },
        )
        .then(
            if (colors.showShadow) {
                Modifier.shadow(
                    elevation = Effects.boxShadowShadowXs.radius,
                    shape = shape,
                    clip = false,
                )
            } else {
                Modifier
            },
        )
        .clip(shape)
        .then(
            if (borderColor != null) {
                Modifier.border(borderWidth, borderColor, shape)
            } else {
                Modifier
            },
        )
        .background(colors.container, shape)
        .focusRing(
            interactionSource = interactionSource,
            width = focusRingWidth,
            color = focusRingColor,
            shape = shape,
        )

    UnstyledButton(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        indication = null,
        role = Role.Button,
        modifier = containerModifier,
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(
            LocalButtonContentColor provides colors.content,
            LocalButtonIconSize provides sizeSpec.iconSize,
            LocalButtonTextStyle provides sizeSpec.textStyle,
            LocalButtonLinkUnderlined provides (variant == ButtonVariant.Link && isHovered && enabled),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = sizeSpec.horizontalPadding),
                horizontalArrangement = Arrangement.spacedBy(
                    space = sizeSpec.gap,
                    alignment = Alignment.CenterHorizontally,
                ),
                verticalAlignment = Alignment.CenterVertically,
                content = content,
            )
        }
    }
}

@Composable
fun ButtonText(
    text: String,
    modifier: Modifier = Modifier,
) {
    val contentColor = LocalButtonContentColor.current
    val underline = LocalButtonLinkUnderlined.current
    val textStyle = LocalButtonTextStyle.current

    BasicText(
        text = text,
        modifier = modifier,
        style = textStyle.copy(
            color = contentColor,
            textDecoration = if (underline) TextDecoration.Underline else TextDecoration.None,
        ),
    )
}

@Composable
fun ButtonIcon(
    icon: ShadcnIcon,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    when (icon) {
        is ShadcnIcon.Vector -> UnstyledIcon(
            imageVector = icon.imageVector,
            contentDescription = contentDescription,
            modifier = modifier.size(LocalButtonIconSize.current),
            tint = LocalButtonContentColor.current,
        )
        is ShadcnIcon.PainterIcon -> UnstyledIcon(
            painter = icon.painter,
            contentDescription = contentDescription,
            modifier = modifier.size(LocalButtonIconSize.current),
            tint = LocalButtonContentColor.current,
        )
    }
}

@Composable
fun ButtonIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
): Unit = ButtonIcon(
    icon = imageVector.toShadcnIcon(),
    contentDescription = contentDescription,
    modifier = modifier,
)

@Composable
fun ButtonIcon(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
): Unit = ButtonIcon(
    icon = painter.toShadcnIcon(),
    contentDescription = contentDescription,
    modifier = modifier,
)
