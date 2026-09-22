package com.github.jershell.shadcn.components.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import com.composeunstyled.UnstyledIcon

/**
 * Unified icon source for shadcn components.
 *
 * Supports both vector icons ([ImageVector]) and arbitrary painters ([Painter]),
 * which covers drawable resources, bitmaps, and custom painters.
 */
sealed class ShadcnIcon private constructor() {
    data class Vector(val imageVector: ImageVector) : ShadcnIcon()
    data class PainterIcon(val painter: Painter) : ShadcnIcon()
}

/** Wraps an [ImageVector] into a [ShadcnIcon]. */
fun ImageVector.toShadcnIcon(): ShadcnIcon = ShadcnIcon.Vector(this)

/** Wraps a [Painter] into a [ShadcnIcon]. */
fun Painter.toShadcnIcon(): ShadcnIcon = ShadcnIcon.PainterIcon(this)

/**
 * Renders a [ShadcnIcon] with a single API regardless of the underlying source.
 */
@Composable
fun ShadcnIconContent(
    icon: ShadcnIcon,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
) {
    when (icon) {
        is ShadcnIcon.Vector -> UnstyledIcon(
            imageVector = icon.imageVector,
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint,
        )
        is ShadcnIcon.PainterIcon -> UnstyledIcon(
            painter = icon.painter,
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint,
        )
    }
}
