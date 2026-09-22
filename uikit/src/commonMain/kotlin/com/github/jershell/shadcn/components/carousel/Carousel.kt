package com.github.jershell.shadcn.components.carousel

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.ChevronLeft
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.ChevronUp
import com.composables.icons.lucide.Lucide
import com.github.jershell.shadcn.components.button.Button
import com.github.jershell.shadcn.components.button.ButtonIcon
import com.github.jershell.shadcn.components.button.ButtonSize
import com.github.jershell.shadcn.components.button.ButtonVariant
import com.github.jershell.shadcn.generated.resources.Res
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.generated.resources.carousel_next_slide
import com.github.jershell.shadcn.generated.resources.carousel_previous_slide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

internal val LocalCarouselState = staticCompositionLocalOf<CarouselState?> { null }

/**
 * A carousel component styled after shadcn/ui, built on the foundation Pager.
 *
 * Provides a snap-paged row (or column) of items with previous/next buttons.
 * Use [CarouselContent], [CarouselItem], [CarouselPrevious] and [CarouselNext] inside.
 *
 * Touch, wheel and keyboard interactions come from the Pager; mouse dragging is
 * implemented on top (Compose does not drag pagers with a mouse) with a snap to the
 * nearest item on release.
 *
 * @param state Carousel state, see [rememberCarouselState].
 * @param modifier Modifier applied to the carousel container.
 * @param content Carousel content; [CarouselPrevious]/[CarouselNext] use [BoxScope]
 *   alignment relative to this container.
 */
@Composable
fun Carousel(
    state: CarouselState,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val scope = rememberCoroutineScope()

    CompositionLocalProvider(LocalCarouselState provides state) {
        Box(
            modifier = modifier.onPreviewKeyEvent { event ->
                // Arrow keys page the carousel, like shadcn (onKeyDownCapture).
                if (event.type == KeyEventType.KeyDown &&
                    state.orientation == CarouselOrientation.Horizontal
                ) {
                    when (event.key) {
                        Key.DirectionLeft -> {
                            scope.launch { state.previous() }
                            true
                        }
                        Key.DirectionRight -> {
                            scope.launch { state.next() }
                            true
                        }
                        else -> false
                    }
                } else {
                    false
                }
            },
        ) {
            content()
        }
    }
}

/**
 * The scrollable content area of a [Carousel].
 *
 * @param modifier Modifier applied to the pager.
 * @param state Carousel state. Defaults to the state provided by the surrounding [Carousel].
 * @param spacing Space between items. Defaults to the theme's `gap-2` token.
 * @param contentPadding Padding around the scrolling content; reveals neighboring items.
 *   With the default (no padding) exactly `basis` pages fit the viewport.
 * @param basis Fraction of the viewport main-axis size occupied by one page
 *   (like the Tailwind `basis-1/3` utilities); `1f` shows a single item per page.
 * @param pageSize Fixed page size along the main axis; overrides [basis] when set.
 * @param content Content of the page at [page]; usually a [CarouselItem].
 */
@Composable
fun CarouselContent(
    modifier: Modifier = Modifier,
    state: CarouselState = LocalCarouselState.current
        ?: error("CarouselContent must be used inside a Carousel"),
    spacing: Dp = TwDimensions.gapGapToken2,
    contentPadding: androidx.compose.foundation.layout.PaddingValues = androidx.compose.foundation.layout.PaddingValues(),
    basis: Float = 1f,
    fixedPageSize: Dp? = null,
    content: @Composable PagerScope.(page: Int) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val mouseDragModifier = Modifier.carouselMouseDrag(state, scope)
    val pageSize = when {
        fixedPageSize != null -> androidx.compose.foundation.pager.PageSize.Fixed(fixedPageSize)
        basis >= 1f -> androidx.compose.foundation.pager.PageSize.Fill
        else -> object : androidx.compose.foundation.pager.PageSize {
            // Like embla: N pages in the viewport accounting for pageSpacing,
            // so exactly N items fit entirely.
            // page = (viewport - spacing * (N - 1)) / N
            override fun Density.calculateMainAxisPageSize(
                availableSpace: Int,
                pageSpacing: Int,
            ): Int {
                val pagesPerViewport = (1f / basis).roundToInt().coerceAtLeast(1)
                return ((availableSpace - pageSpacing * (pagesPerViewport - 1)) / pagesPerViewport)
                    .coerceAtLeast(0)
            }
        }
    }

    when (state.orientation) {
        CarouselOrientation.Horizontal -> HorizontalPager(
            state = state.pagerState,
            modifier = modifier
                .fillMaxWidth()
                .then(mouseDragModifier),
            pageSize = pageSize,
            pageSpacing = spacing,
            contentPadding = contentPadding,
            snapPosition = state.snapPosition(),
            pageContent = content,
        )

        CarouselOrientation.Vertical -> VerticalPager(
            state = state.pagerState,
            modifier = modifier
                .fillMaxHeight()
                .then(mouseDragModifier),
            pageSize = pageSize,
            pageSpacing = spacing,
            contentPadding = contentPadding,
            snapPosition = state.snapPosition(),
            pageContent = content,
        )
    }
}

/**
 * Mouse drag support for the Pager: Compose does not drag pagers/lists with a mouse.
 * Uses `dispatchRawDelta` synchronously and snaps to the nearest item on release.
 */
private fun Modifier.carouselMouseDrag(
    state: CarouselState,
    scope: CoroutineScope,
): Modifier = pointerInput(state.pagerState, state.orientation) {
    awaitPointerEventScope {
        var dragging = false
        while (true) {
            val event = awaitPointerEvent(PointerEventPass.Initial)
            val mouseChange = event.changes.firstOrNull { it.type == PointerType.Mouse } ?: continue
            if (mouseChange.pressed) {
                val delta = mouseChange.positionChange()
                if (delta != Offset.Zero) {
                    mouseChange.consume()
                    dragging = true
                    when (state.orientation) {
                        CarouselOrientation.Horizontal -> state.pagerState.dispatchRawDelta(-delta.x)
                        CarouselOrientation.Vertical -> state.pagerState.dispatchRawDelta(-delta.y)
                    }
                }
            } else if (dragging) {
                dragging = false
                val pager = state.pagerState
                val nearest = (pager.currentPage + pager.currentPageOffsetFraction)
                    .roundToInt()
                    .coerceIn(0, pager.pageCount - 1)
                scope.launch { pager.animateScrollToPage(nearest) }
            }
        }
    }
}

/**
 * A single item in a [Carousel].
 *
 * Must be called inside [CarouselContent]. The page size is controlled by the
 * [CarouselContent.basis] parameter; the item usually stretches to fill its page
 * (e.g. `Modifier.fillMaxWidth()` for a horizontal carousel).
 *
 * @param modifier Modifier applied to the item.
 * @param content Item content.
 */
@Composable
fun CarouselItem(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    LocalCarouselState.current
        ?: error("CarouselItem must be used inside a CarouselContent")
    Box(
        modifier = modifier,
        content = content,
    )
}

/**
 * Button to scroll the carousel to the previous item.
 *
 * Must be used inside a [Carousel] (positioned relative to its container).
 *
 * @param modifier Modifier applied to the button.
 */
@Composable
fun BoxScope.CarouselPrevious(
    modifier: Modifier = Modifier,
) {
    val state = LocalCarouselState.current ?: error("CarouselPrevious must be used inside a Carousel")
    val scope = rememberCoroutineScope()

    Button(
        onClick = { scope.launch { state.previous() } },
        modifier = carouselNavigationModifier(
            orientation = state.orientation,
            isPrevious = true,
        ).then(modifier),
        variant = ButtonVariant.Outline,
        size = ButtonSize.Icon,
        shape = CircleShape,
        enabled = state.canScrollPrevious(),
    ) {
        ButtonIcon(
            if (state.orientation == CarouselOrientation.Horizontal) Lucide.ChevronLeft else Lucide.ChevronUp,
            contentDescription = stringResource(Res.string.carousel_previous_slide),
        )
    }
}

/**
 * Button to scroll the carousel to the next item.
 *
 * Must be used inside a [Carousel] (positioned relative to its container).
 *
 * @param modifier Modifier applied to the button.
 */
@Composable
fun BoxScope.CarouselNext(
    modifier: Modifier = Modifier,
) {
    val state = LocalCarouselState.current ?: error("CarouselNext must be used inside a Carousel")
    val scope = rememberCoroutineScope()

    Button(
        onClick = { scope.launch { state.next() } },
        modifier = carouselNavigationModifier(
            orientation = state.orientation,
            isPrevious = false,
        ).then(modifier),
        variant = ButtonVariant.Outline,
        size = ButtonSize.Icon,
        shape = CircleShape,
        enabled = state.canScrollNext(),
    ) {
        ButtonIcon(
            if (state.orientation == CarouselOrientation.Horizontal) Lucide.ChevronRight else Lucide.ChevronDown,
            contentDescription = stringResource(Res.string.carousel_next_slide),
        )
    }
}

private fun BoxScope.carouselNavigationModifier(
    orientation: CarouselOrientation,
    isPrevious: Boolean,
): Modifier {
    val offset = TwDimensions.gapGapToken12
    return when (orientation) {
        CarouselOrientation.Horizontal -> {
            val xOffset = if (isPrevious) -offset else offset
            Modifier
                .align(if (isPrevious) Alignment.CenterStart else Alignment.CenterEnd)
                .offset(x = xOffset)
        }

        CarouselOrientation.Vertical -> {
            val yOffset = if (isPrevious) -offset else offset
            Modifier
                .align(if (isPrevious) Alignment.TopCenter else Alignment.BottomCenter)
                .offset(y = yOffset)
        }
    }
}
