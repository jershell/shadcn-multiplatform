package com.github.jershell.shadcn.components.separator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.generated.resources.Res
import com.github.jershell.shadcn.generated.resources.separator_description

enum class SeparatorOrientation {
    Horizontal,
    Vertical,
}

/**
 * A visual separator styled after shadcn/ui.
 *
 * @param modifier Modifier applied to the separator.
 * @param orientation Line direction.
 * @param decorative If false, exposes basic accessibility label.
 * @param thickness Line thickness.
 */
@Composable
fun Separator(
    modifier: Modifier = Modifier,
    orientation: SeparatorOrientation = SeparatorOrientation.Horizontal,
    decorative: Boolean = true,
    thickness: Dp = Theme[DimProps][DimTokens.borderWidth],
) {
    val color = Theme[ColorProps][ColorTokens.border]
    val separatorDescription = stringResource(Res.string.separator_description)
    val semanticsModifier = if (decorative) {
        Modifier
    } else {
        Modifier.semantics { contentDescription = separatorDescription }
    }

    val lineModifier = when (orientation) {
        SeparatorOrientation.Horizontal -> Modifier
            .fillMaxWidth()
            .height(thickness)
        SeparatorOrientation.Vertical -> Modifier
            .fillMaxHeight()
            .width(thickness)
    }

    Box(
        modifier = modifier
            .then(semanticsModifier)
            .then(lineModifier)
            .background(color),
    )
}
