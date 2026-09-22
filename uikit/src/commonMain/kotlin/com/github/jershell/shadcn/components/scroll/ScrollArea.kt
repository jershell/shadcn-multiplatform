package com.github.jershell.shadcn.components.scroll

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.composeunstyled.rememberScrollbarState

/**
 * Scrollable content area with shadcn-style scrollbars.
 *
 * Default: vertical scrolling only. Pass [horizontalState] to enable horizontal
 * scrolling too (content must be wider than the viewport, e.g. fixed-width table
 * layout) — a horizontal scrollbar is rendered under the content.
 */
@Composable
fun ScrollArea(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    horizontalState: ScrollState? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val verticalScrollbarState = rememberScrollbarState(scrollState)

    Column(modifier = modifier) {
        Row(modifier = Modifier.weight(1f, fill = true)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
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
            VerticalScrollbar(scrollbarState = verticalScrollbarState)
        }
        if (horizontalState != null) {
            HorizontalScrollbar(scrollbarState = rememberScrollbarState(horizontalState))
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
    content: @Composable ColumnScope.() -> Unit,
) {
    ScrollArea(
        modifier = modifier,
        scrollState = verticalState,
        horizontalState = horizontalState,
        content = content,
    )
}