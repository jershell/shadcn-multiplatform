package com.github.jershell.shadcn.components.scroll

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import com.composeunstyled.ScrollbarState
import com.composeunstyled.rememberScrollbarState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Scrollbar state of a lazy list whose thumb drags are applied at most once per frame.
 *
 * Why: the unstyled scrollbar scrolls on every pointer event, and every scroll of a
 * lazy list forces a synchronous remeasure that composes the newly visible items.
 * Mice report several events per frame, so with heavy items (tables, cards) a thumb
 * drag queues more work than a frame can hold: the list lags and keeps catching up
 * after the pointer stops. With this state a drag only records the target — the
 * thumb follows the pointer right away — and the list catches up to the latest
 * target once per frame. Wheel/touch scrolling is not affected.
 *
 * ```
 * val listState = rememberLazyListState()
 * Row {
 *     LazyColumn(state = listState, modifier = Modifier.weight(1f)) { ... }
 *     VerticalScrollbar(scrollbarState = rememberCoalescedScrollbarState(listState))
 * }
 * ```
 */
@Composable
fun rememberCoalescedScrollbarState(lazyListState: LazyListState): ScrollbarState =
    rememberCoalesced(rememberScrollbarState(lazyListState))

/** [rememberCoalescedScrollbarState] for a lazy grid. */
@Composable
fun rememberCoalescedScrollbarState(lazyGridState: LazyGridState): ScrollbarState =
    rememberCoalesced(rememberScrollbarState(lazyGridState))

/**
 * [rememberCoalescedScrollbarState] for a plain scroll container: scrolling it never
 * composes anything, but coalescing still saves relayouts of heavy content.
 */
@Composable
fun rememberCoalescedScrollbarState(scrollState: ScrollState): ScrollbarState =
    rememberCoalesced(rememberScrollbarState(scrollState))

/** Wraps any [ScrollbarState] (e.g. a custom one) into the per-frame coalescing one. */
@Composable
fun rememberCoalesced(scrollbarState: ScrollbarState): ScrollbarState {
    val scope = rememberCoroutineScope()
    return remember(scrollbarState, scope) { CoalescedScrollbarState(scrollbarState, scope) }
}

@Stable
private class CoalescedScrollbarState(
    private val delegate: ScrollbarState,
    private val scope: CoroutineScope,
) : ScrollbarState {
    /** Latest requested offset not yet applied; the thumb shows it meanwhile. */
    private var pending by mutableStateOf<Double?>(null)
    private var job: Job? = null

    override val scrollOffset: Double get() = pending ?: delegate.scrollOffset
    override val contentSize: Double get() = delegate.contentSize
    override val viewportSize: Double get() = delegate.viewportSize
    override val interactionSource: InteractionSource get() = delegate.interactionSource
    override val isScrollInProgress: Boolean get() = pending != null || delegate.isScrollInProgress

    override suspend fun scrollTo(scrollOffset: Double) {
        pending = scrollOffset
        if (job?.isActive == true) return
        job = scope.launch {
            while (true) {
                withFrameNanos { }
                val target = pending ?: break
                delegate.scrollTo(target)
                // Nothing newer arrived while scrolling: done.
                if (pending == target) {
                    pending = null
                    break
                }
            }
        }
    }
}
