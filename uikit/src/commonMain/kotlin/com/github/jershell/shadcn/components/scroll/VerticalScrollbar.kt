package com.github.jershell.shadcn.components.scroll

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.composeunstyled.ScrollbarState
import com.composeunstyled.Thumb
import com.composeunstyled.ThumbVisibility
import com.composeunstyled.UnstyledVerticalScrollbar
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.BaseTokens
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun VerticalScrollbar(
    scrollbarState: ScrollbarState,
    modifier: Modifier = Modifier,
) {
    val thumbColor = Theme[ColorProps][ColorTokens.border]
    val radius = Theme[DimProps][DimTokens.radiusFull]
    val trackWidth = BaseTokens.token8

    UnstyledVerticalScrollbar(
        scrollbarState = scrollbarState,
        modifier = modifier
            .fillMaxHeight()
            .width(trackWidth)
            .padding(start = 4.dp),
    ) {
        Thumb(
            modifier = Modifier
                .width(trackWidth)
                .clip(RoundedCornerShape(radius))
                .background(thumbColor)
                .padding(vertical = 2.dp),
            thumbVisibility = ThumbVisibility.HideWhileIdle(
                enter = fadeIn(),
                exit = fadeOut(),
                hideDelay = 800.milliseconds,
            ),
        )
    }
}
