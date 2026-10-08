package com.github.jershell.shadcn.components.scroll

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.composeunstyled.ThumbVisibility
import com.github.jershell.shadcn.theme.BaseTokens

/**
 * Scrollable content area with shadcn-style scrollbars.
 *
 * Default: vertical scrolling only. Pass [horizontalState] to enable horizontal
 * scrolling too (content must be wider than the viewport, e.g. fixed-width table
 * layout) — a horizontal scrollbar is rendered under the content.
 *
 * @param thumbVisibility Thumb visibility behavior applied to both scrollbars; pass
 *   [ThumbVisibility.AlwaysVisible] to keep thumbs rendered while idle. Hovering the
 *   content or a scrollbar reveals the thumb; after the pointer leaves, both thumbs
 *   stay visible for another 800ms before hiding.
 * @param verticalThumbWidth Width of the vertical scrollbar track.
 * @param horizontalThumbHeight Height of the horizontal scrollbar track.
 */
@Composable
fun ScrollArea(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    horizontalState: ScrollState? = null,
    thumbVisibility: ThumbVisibility = DefaultScrollbarThumbVisibility,
    verticalThumbWidth: Dp = BaseTokens.token10,
    horizontalThumbHeight: Dp = BaseTokens.token10,
    content: @Composable ColumnScope.() -> Unit,
) {
    val verticalScrollbarState = rememberCoalescedScrollbarState(scrollState)
    val contentHoverReveal = rememberScrollbarHoverReveal()

    Column(modifier = modifier) {
        Row(modifier = Modifier.weight(1f, fill = true)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .hoverTracker(contentHoverReveal::onHover)
                    .verticalScroll(scrollState)
                    .then(
                        if (horizontalState != null) {
                            Modifier.horizontalScroll(horizontalState)
                        } else {
                            Modifier
                        },
                    ),
                content = content,
            )
            VerticalScrollbar(
                scrollbarState = verticalScrollbarState,
                thumbVisibility = resolveEffectiveThumbVisibility(
                    thumbVisibility,
                    contentHoverReveal.revealed,
                ),
                thumbWidth = verticalThumbWidth,
            )
        }
        if (horizontalState != null) {
            HorizontalScrollbar(
                scrollbarState = rememberCoalescedScrollbarState(horizontalState),
                thumbVisibility = resolveEffectiveThumbVisibility(
                    thumbVisibility,
                    contentHoverReveal.revealed,
                ),
                thumbHeight = horizontalThumbHeight,
            )
        }
    }
}

/**
 * Two-dimensional scroll area (vertical + horizontal scrollbars) for content that
 * has its own intrinsic size (e.g. a fixed-width dashboard block inside a BoxScope).
 */
@Composable
fun ScrollArea2D(
    modifier: Modifier = Modifier,
    verticalState: ScrollState = rememberScrollState(),
    horizontalState: ScrollState = rememberScrollState(),
    thumbVisibility: ThumbVisibility = DefaultScrollbarThumbVisibility,
    verticalThumbWidth: Dp = BaseTokens.token10,
    horizontalThumbHeight: Dp = BaseTokens.token10,
    content: @Composable ColumnScope.() -> Unit,
) {
    ScrollArea(
        modifier = modifier,
        scrollState = verticalState,
        horizontalState = horizontalState,
        thumbVisibility = thumbVisibility,
        verticalThumbWidth = verticalThumbWidth,
        horizontalThumbHeight = horizontalThumbHeight,
        content = content,
    )
}