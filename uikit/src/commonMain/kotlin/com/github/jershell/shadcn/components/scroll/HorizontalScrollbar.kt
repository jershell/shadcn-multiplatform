package com.github.jershell.shadcn.components.scroll

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.ScrollbarState
import com.composeunstyled.Thumb
import com.composeunstyled.ThumbVisibility
import com.composeunstyled.UnstyledHorizontalScrollbar
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.BaseTokens
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens

/**
 * Horizontal scrollbar with shadcn styling.
 *
 * The thumb is revealed on hover over the scrollbar and hidden again after
 * 800ms idle (unless [thumbVisibility] is [ThumbVisibility.AlwaysVisible]).
 *
 * @param thumbVisibility Thumb visibility behavior; pass
 *   [ThumbVisibility.AlwaysVisible] to keep the thumb rendered while idle.
 * @param thumbHeight Track/thumb height; 0.625rem by default.
 * @param minThumbSize Minimal thumb length, so the thumb of wide content stays easy
 *   to grab with the mouse.
 */
@Composable
fun HorizontalScrollbar(
    scrollbarState: ScrollbarState,
    modifier: Modifier = Modifier,
    thumbVisibility: ThumbVisibility = DefaultScrollbarThumbVisibility,
    thumbHeight: Dp = BaseTokens.token10,
    minThumbSize: Dp = DefaultMinThumbSize,
) {
    val thumbColor = Theme[ColorProps][ColorTokens.border]
    val radius = Theme[DimProps][DimTokens.radiusFull]
    val hoverReveal = rememberScrollbarHoverReveal()
    val visibility = resolveEffectiveThumbVisibility(thumbVisibility, hoverReveal.revealed)

    UnstyledHorizontalScrollbar(
        scrollbarState = scrollbarState,
        modifier = modifier
            .hoverTracker(hoverReveal::onHover)
            .fillMaxWidth()
            .height(thumbHeight),
    ) {
        Thumb(
            modifier = Modifier
                .height(thumbHeight)
                .widthIn(min = minThumbSize)
                .clip(RoundedCornerShape(radius))
                .background(thumbColor)
                .padding(horizontal = 2.dp),
            thumbVisibility = visibility,
        )
    }
}
