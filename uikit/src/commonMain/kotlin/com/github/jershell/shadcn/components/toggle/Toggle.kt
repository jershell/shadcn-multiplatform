package com.github.jershell.shadcn.components.toggle

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.composeunstyled.UnstyledButton
import com.composeunstyled.UnstyledIcon
import com.composeunstyled.focusRing
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.icon.ShadcnIcon
import com.github.jershell.shadcn.components.icon.toShadcnIcon
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.Effects

internal val LocalToggleContentColor = staticCompositionLocalOf { Color.Unspecified }
internal val LocalToggleIconSize = staticCompositionLocalOf { Dp.Unspecified }
internal val LocalToggleTextStyle = staticCompositionLocalOf { TextStyle.Default }

/**
 * A two-state button that can be either on or off, styled after shadcn/ui.
 *
 * Supports the standard shadcn variants: default and outline, plus the default,
 * sm and lg sizes. When [checked], the toggle shows the accent background and
 * accent foreground colors.
 *
 * @param checked Whether the toggle is on.
 * @param onCheckedChange Called when the user toggles the state.
 * @param modifier Modifier applied to the toggle container.
 * @param enabled Whether the toggle is interactive. Disabled toggles are dimmed.
 * @param variant Visual style of the toggle.
 * @param size Size of the toggle.
 * @param isInvalid Marks the toggle as invalid, applying destructive border and focus ring colors.
 * @param shape Shape of the toggle container.
 * @param content Content rendered inside the toggle. Use [ToggleText] and [ToggleIcon] to get
 *   automatic coloring based on the state.
 */
@Composable
fun Toggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: ToggleVariant = ToggleVariant.Default,
    size: ToggleSize = ToggleSize.Default,
    isInvalid: Boolean = false,
    shape: Shape = RoundedCornerShape(Theme[DimProps][DimTokens.radiusMd]),
    content: @Composable RowScope.() -> Unit,
) {
    val sizeSpec = size.spec()
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val colors = resolveToggleColors(
        variant = variant,
        isOn = checked,
        isHovered = isHovered,
        isPressed = isPressed,
        isInvalid = isInvalid,
    )
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val focusRingColor = resolveToggleFocusRingColor(isInvalid)
    val focusRingWidth = Effects.boxShadowFocusRing.spread

    val containerModifier = modifier
        .alpha(if (enabled) 1f else 0.5f)
        .height(sizeSpec.height)
        .defaultMinSize(minWidth = sizeSpec.height)
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
            if (colors.border != null) {
                Modifier.border(borderWidth, colors.border, shape)
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
        .semantics {
            role = Role.Button
            selected = checked
        }

    UnstyledButton(
        onClick = { onCheckedChange(!checked) },
        enabled = enabled,
        interactionSource = interactionSource,
        indication = null,
        role = Role.Button,
        modifier = containerModifier,
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(
            LocalToggleContentColor provides colors.content,
            LocalToggleIconSize provides sizeSpec.iconSize,
            LocalToggleTextStyle provides sizeSpec.textStyle,
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

/**
 * Text inside a [Toggle], colored according to the toggle state.
 */
@Composable
fun ToggleText(
    text: String,
    modifier: Modifier = Modifier,
) {
    val contentColor = LocalToggleContentColor.current
    val textStyle = LocalToggleTextStyle.current

    BasicText(
        text = text,
        modifier = modifier,
        style = textStyle.copy(color = contentColor),
    )
}

/**
 * Icon inside a [Toggle], sized and colored according to the toggle state.
 */
@Composable
fun ToggleIcon(
    icon: ShadcnIcon,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    when (icon) {
        is ShadcnIcon.Vector -> UnstyledIcon(
            imageVector = icon.imageVector,
            contentDescription = contentDescription,
            modifier = modifier.size(LocalToggleIconSize.current),
            tint = LocalToggleContentColor.current,
        )
        is ShadcnIcon.PainterIcon -> UnstyledIcon(
            painter = icon.painter,
            contentDescription = contentDescription,
            modifier = modifier.size(LocalToggleIconSize.current),
            tint = LocalToggleContentColor.current,
        )
    }
}

/**
 * Icon inside a [Toggle], sized and colored according to the toggle state.
 */
@Composable
fun ToggleIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
): Unit = ToggleIcon(
    icon = imageVector.toShadcnIcon(),
    contentDescription = contentDescription,
    modifier = modifier,
)

/**
 * Icon inside a [Toggle], sized and colored according to the toggle state.
 */
@Composable
fun ToggleIcon(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
): Unit = ToggleIcon(
    icon = painter.toShadcnIcon(),
    contentDescription = contentDescription,
    modifier = modifier,
)
