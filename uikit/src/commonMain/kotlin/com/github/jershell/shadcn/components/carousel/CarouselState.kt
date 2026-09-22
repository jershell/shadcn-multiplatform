package com.github.jershell.shadcn.components.carousel

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * Alignment of the snapped item inside the carousel viewport.
 *
 * Mirrors the `align` option from Embla Carousel / shadcn/ui.
 */
enum class CarouselAlign {
    Start,
    Center,
    End,
}

/**
 * Orientation of the carousel.
 */
enum class CarouselOrientation {
    Horizontal,
    Vertical,
}

/**
 * Carousel options inspired by Embla Carousel / shadcn/ui.
 *
 * @param align Where the active item should snap inside the viewport.
 * @param loop Whether navigation buttons wrap around the ends. Drag and wheel
 *   gestures do not loop (unlike Embla's true loop).
 * @param slidesToScroll How many items [CarouselPrevious]/[CarouselNext] scroll per click.
 * @param startIndex Index of the item that should be visible initially.
 */
data class CarouselOpts(
    val align: CarouselAlign = CarouselAlign.Start,
    val loop: Boolean = false,
    val slidesToScroll: Int = 1,
    val startIndex: Int = 0,
)

/**
 * State holder for a [Carousel], backed by the foundation [PagerState].
 *
 * @param pagerState The underlying pager state.
 * @param opts Carousel options that control snapping, wrapping and step size.
 * @param orientation Whether the carousel scrolls horizontally or vertically.
 */
class CarouselState internal constructor(
    val pagerState: PagerState,
    val opts: CarouselOpts,
    val orientation: CarouselOrientation,
) {
    /** Index of the currently settled item. */
    val currentItem: Int
        get() = pagerState.currentPage

    /** Total number of items in the carousel. */
    val itemCount: Int
        get() = pagerState.pageCount

    /** Whether the carousel can scroll to a previous item. */
    fun canScrollPrevious(): Boolean = opts.loop || pagerState.currentPage > 0

    /** Whether the carousel can scroll to a next item. */
    fun canScrollNext(): Boolean = opts.loop || pagerState.currentPage < pagerState.pageCount - 1

    /** Scroll to the item at [index]; wraps around when [CarouselOpts.loop] is set. */
    suspend fun scrollToItem(index: Int) {
        val count = pagerState.pageCount
        if (count <= 0) return
        val target = if (opts.loop) index.mod(count) else index.coerceIn(0, count - 1)
        pagerState.animateScrollToPage(target)
    }

    /** Scroll to the previous slide (or [CarouselOpts.slidesToScroll] slides). */
    suspend fun previous() {
        val target = pagerState.currentPage - opts.slidesToScroll
        scrollToItem(if (opts.loop && target < 0) pagerState.pageCount - 1 else target)
    }

    /** Scroll to the next slide (or [CarouselOpts.slidesToScroll] slides). */
    suspend fun next() {
        val target = pagerState.currentPage + opts.slidesToScroll
        scrollToItem(if (opts.loop && target > pagerState.pageCount - 1) 0 else target)
    }
}

/**
 * Remember a new [CarouselState].
 *
 * @param pageCount Total number of items in the carousel.
 * @param opts Options that configure the carousel behavior.
 * @param orientation Carousel orientation.
 */
@Composable
fun rememberCarouselState(
    pageCount: Int,
    opts: CarouselOpts = CarouselOpts(),
    orientation: CarouselOrientation = CarouselOrientation.Horizontal,
): CarouselState {
    val pagerState = rememberPagerState(
        initialPage = opts.startIndex.coerceIn(0, (pageCount - 1).coerceAtLeast(0)),
        initialPageOffsetFraction = 0f,
        pageCount = { pageCount },
    )
    return remember(opts, orientation, pagerState) {
        CarouselState(pagerState, opts, orientation)
    }
}

internal fun CarouselState.snapPosition(): androidx.compose.foundation.gestures.snapping.SnapPosition =
    when (opts.align) {
        CarouselAlign.Start -> androidx.compose.foundation.gestures.snapping.SnapPosition.Start
        CarouselAlign.Center -> androidx.compose.foundation.gestures.snapping.SnapPosition.Center
        CarouselAlign.End -> androidx.compose.foundation.gestures.snapping.SnapPosition.End
    }

internal fun CarouselState.orientationInternal(): Orientation = when (orientation) {
    CarouselOrientation.Horizontal -> Orientation.Horizontal
    CarouselOrientation.Vertical -> Orientation.Vertical
}
