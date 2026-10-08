package com.github.jershell.shadcn.components.scroll

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
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

/**
 * Default thumb behavior used by the scrollbars: the thumb fades out 800ms
 * after the last scroll gesture or leaving the scrollbar bounds.
 */
internal val DefaultScrollbarThumbVisibility = ThumbVisibility.HideWhileIdle(
    enter = fadeIn(),
    exit = fadeOut(),
    hideDelay = 800.milliseconds,
)

/** Default minimal thumb length of the scrollbars. */
internal val DefaultMinThumbSize: Dp = BaseTokens.token32

/**
 * Reports pointer enter/exit on the decorated node.
 */
internal fun Modifier.hoverTracker(onHover: (Boolean) -> Unit): Modifier =
    pointerInput(Unit) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent()
                when (event.type) {
                    PointerEventType.Enter -> onHover(true)
                    PointerEventType.Exit -> onHover(false)
                }
            }
        }
    }

/**
 * Resolves the effective thumb visibility: hovering reveals the thumb even when
 * the requested behavior hides it while idle (the library itself does not reveal
 * a hidden thumb on hover), otherwise the requested behavior wins.
 */
@Composable
internal fun resolveEffectiveThumbVisibility(
    requested: ThumbVisibility,
    hovered: Boolean,
): ThumbVisibility = when {
    requested is ThumbVisibility.AlwaysVisible || hovered -> ThumbVisibility.AlwaysVisible
    else -> requested
}

/**
 * Vertical scrollbar with shadcn styling.
 *
 * The thumb is revealed on hover over the scrollbar and hidden again after
 * 800ms idle (unless [thumbVisibility] is [ThumbVisibility.AlwaysVisible]).
 *
 * @param thumbVisibility Thumb visibility behavior; pass
 *   [ThumbVisibility.AlwaysVisible] to keep the thumb rendered while idle.
 * @param thumbWidth Track/thumb width; 0.625rem by default.
 * @param minThumbSize Minimal thumb length, so the thumb of a long list stays easy to
 *   grab with the mouse.
 */
@Composable
fun VerticalScrollbar(
    scrollbarState: ScrollbarState,
    modifier: Modifier = Modifier,
    thumbVisibility: ThumbVisibility = DefaultScrollbarThumbVisibility,
    thumbWidth: Dp = BaseTokens.token10,
    minThumbSize: Dp = DefaultMinThumbSize,
) {
    val thumbColor = Theme[ColorProps][ColorTokens.border]
    val radius = Theme[DimProps][DimTokens.radiusFull]
    val hoverReveal = rememberScrollbarHoverReveal()
    val visibility = resolveEffectiveThumbVisibility(thumbVisibility, hoverReveal.revealed)

    UnstyledVerticalScrollbar(
        scrollbarState = scrollbarState,
        modifier = modifier
            .hoverTracker(hoverReveal::onHover)
            .fillMaxHeight()
            .width(thumbWidth),
    ) {
        Thumb(
            modifier = Modifier
                .width(thumbWidth)
                // The unstyled scrollbar takes the thumb's min intrinsic size as its
                // minimal length.
                .heightIn(min = minThumbSize)
                .clip(RoundedCornerShape(radius))
                .background(thumbColor)
                .padding(vertical = 2.dp),
            thumbVisibility = visibility,
        )
    }
}
