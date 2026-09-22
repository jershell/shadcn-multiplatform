package com.github.jershell.shadcn.components.spinner

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import com.composables.icons.lucide.LoaderCircle
import com.composables.icons.lucide.Lucide
import com.composeunstyled.theme.Theme
import org.jetbrains.compose.resources.painterResource
import com.github.jershell.shadcn.components.icon.ShadcnIcon
import com.github.jershell.shadcn.components.icon.ShadcnIconContent
import com.github.jershell.shadcn.components.icon.toShadcnIcon
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.TwDimensions
import androidx.compose.foundation.layout.size
import com.github.jershell.shadcn.generated.resources.Res
import org.jetbrains.compose.resources.stringResource
import com.github.jershell.shadcn.generated.resources.alt_spinner
import com.github.jershell.shadcn.generated.resources.spinner_loading

enum class SpinnerVariant {
    Default,
    Alt,
}

/**
 * A loading spinner styled after shadcn/ui.
 *
 * @param modifier Modifier applied to the spinner.
 * @param icon Spinner icon. Defaults to [Lucide.LoaderCircle].
 * @param size Visual size of the spinner.
 * @param tint Icon color.
 * @param contentDescription Accessibility label.
 */
@Composable
fun Spinner(
    modifier: Modifier = Modifier,
    variant: SpinnerVariant = SpinnerVariant.Default,
    icon: ShadcnIcon = Lucide.LoaderCircle.toShadcnIcon(),
    size: Dp = TwDimensions.heightHToken4,
    tint: Color = Theme[ColorProps][ColorTokens.foreground],
    contentDescription: String = stringResource(Res.string.spinner_loading),
) {
    val resolvedIcon = when (variant) {
        SpinnerVariant.Default -> icon
        SpinnerVariant.Alt -> painterResource(Res.drawable.alt_spinner).toShadcnIcon()
    }
    val rotation = rememberInfiniteTransition(label = "SpinnerRotation").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "SpinnerAngle",
    )

    ShadcnIconContent(
        icon = resolvedIcon,
        contentDescription = contentDescription,
        modifier = modifier
            .size(size)
            .rotate(rotation.value)
            .semantics { this.contentDescription = contentDescription },
        tint = tint,
    )
}

@Composable
fun Spinner(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    variant: SpinnerVariant = SpinnerVariant.Default,
    size: Dp = TwDimensions.heightHToken4,
    tint: Color = Theme[ColorProps][ColorTokens.foreground],
    contentDescription: String = stringResource(Res.string.spinner_loading),
): Unit = Spinner(
    icon = imageVector.toShadcnIcon(),
    modifier = modifier,
    variant = variant,
    size = size,
    tint = tint,
    contentDescription = contentDescription,
)

@Composable
fun Spinner(
    painter: Painter,
    modifier: Modifier = Modifier,
    variant: SpinnerVariant = SpinnerVariant.Default,
    size: Dp = TwDimensions.heightHToken4,
    tint: Color = Theme[ColorProps][ColorTokens.foreground],
    contentDescription: String = stringResource(Res.string.spinner_loading),
): Unit = Spinner(
    icon = painter.toShadcnIcon(),
    modifier = modifier,
    variant = variant,
    size = size,
    tint = tint,
    contentDescription = contentDescription,
)
