package com.github.jershell.shadcn.components.empty

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.unit.Dp
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.icon.ShadcnIcon
import com.github.jershell.shadcn.components.icon.ShadcnIconContent
import com.github.jershell.shadcn.theme.BaseTokens
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.TypographyStyles

/**
 * Variants of the [EmptyMedia] block.
 */
enum class EmptyMediaVariant {
    /** Transparent container for arbitrary media. */
    Default,

    /** `size-10 rounded-lg bg-muted` container for an icon. */
    Icon,
}

/**
 * The dashed-border placeholder container styled after the shadcn/ui Empty:
 * a centered column with `gap-6`, `rounded-lg border-dashed` and `p-6`.
 *
 * Assemble the placeholder with [EmptyHeader] (usually with [EmptyMedia],
 * [EmptyTitle] and [EmptyDescription]) and [EmptyContent].
 *
 * @param modifier Modifier applied to the container.
 * @param content Header and action blocks, laid out vertically and centered.
 */
@Composable
fun Empty(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val border = Theme[ColorProps][ColorTokens.border]
    val radius = Theme[DimProps][DimTokens.radiusLg]
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val shape = RoundedCornerShape(radius)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .dashedBorder(
                width = borderWidth,
                color = border,
                shape = shape,
            )
            .padding(BaseTokens.token24), // p-6
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BaseTokens.token24), // gap-6
        content = content,
    )
}

/**
 * Header block of an [Empty]: a centered column with `gap-2`, capped at `max-w-sm`.
 *
 * @param modifier Modifier applied to the header container.
 * @param content Media, title and description.
 */
@Composable
fun EmptyHeader(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = BaseTokens.token384), // max-w-sm
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BaseTokens.token8), // gap-2
        content = content,
    )
}

/**
 * Media block of an [EmptyHeader].
 *
 * @param variant [EmptyMediaVariant.Icon] wraps the content into a
 *   `size-10 rounded-lg bg-muted` square; [EmptyMediaVariant.Default] is transparent.
 * @param icon Icon rendered inside the block (colored `foreground` for the icon variant).
 * @param modifier Modifier applied to the block.
 * @param content Custom media content; ignored when [icon] is provided.
 */
@Composable
fun EmptyMedia(
    variant: EmptyMediaVariant = EmptyMediaVariant.Default,
    icon: ShadcnIcon? = null,
    modifier: Modifier = Modifier,
    content: (@Composable () -> Unit)? = null,
) {
    val isIconVariant = variant == EmptyMediaVariant.Icon
    val iconColor = Theme[ColorProps][ColorTokens.foreground]

    val containerModifier = when {
        isIconVariant -> Modifier
            .size(BaseTokens.token40) // size-10
            .clip(RoundedCornerShape(Theme[DimProps][DimTokens.radiusLg])) // rounded-lg
            .background(Theme[ColorProps][ColorTokens.muted])
        else -> Modifier
    }

    Column(
        modifier = modifier
            .padding(bottom = BaseTokens.token8) // mb-2
            .then(containerModifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        icon?.let {
            ShadcnIconContent(
                icon = it,
                contentDescription = null,
                modifier = Modifier.size(BaseTokens.token24), // size-6
                tint = if (isIconVariant) iconColor else Color.Unspecified,
            )
        } ?: content?.invoke()
    }
}

/**
 * Title of an [Empty]: `text-lg font-medium`.
 */
@Composable
fun EmptyTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = TypographyStyles.textLgMedium.copy(
            color = Theme[ColorProps][ColorTokens.foreground],
        ),
    )
}

/**
 * Description of an [Empty]: `text-sm text-muted-foreground`.
 */
@Composable
fun EmptyDescription(
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
 * Actions block of an [Empty]: a centered column capped at `max-w-sm` with `gap-4`.
 *
 * @param modifier Modifier applied to the actions container.
 * @param content Buttons, inputs and other actions.
 */
@Composable
fun EmptyContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = BaseTokens.token384), // max-w-sm
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BaseTokens.token16), // gap-4
        content = content,
    )
}

/** CSS `border-style: dashed`: a dash pattern along the [shape] outline. */
private fun Modifier.dashedBorder(
    width: Dp,
    color: Color,
    shape: Shape,
): Modifier = this.then(
    Modifier.drawBehind {
        val outline = shape.createOutline(
            size = size,
            layoutDirection = layoutDirection,
            density = this,
        )
        val stroke = Stroke(
            width = width.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f)),
        )
        drawOutline(outline = outline, color = color, style = stroke)
    },
)
