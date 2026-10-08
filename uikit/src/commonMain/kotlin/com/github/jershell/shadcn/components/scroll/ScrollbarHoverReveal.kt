package com.github.jershell.shadcn.components.scroll

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Hover state with a grace period: `revealed` turns `false` only after
 * [hideDelayMillis] without hover, so the thumb is not revealed/dismissed afresh
 * on every mouse trip in and out of the scrollbar/content bounds.
 */
internal class ScrollbarHoverReveal(
    private val scope: CoroutineScope,
    private val hideDelayMillis: Long,
) {
    var revealed by mutableStateOf(false)
        private set

    private var hideJob: Job? = null

    fun onHover(hovered: Boolean) {
        hideJob?.cancel()
        hideJob = null
        if (hovered) {
            revealed = true
        } else {
            hideJob = scope.launch {
                delay(hideDelayMillis)
                revealed = false
            }
        }
    }

    fun dispose() = hideJob?.cancel()
}

/**
 * Remembers an internal [ScrollbarHoverReveal] with the default 800ms grace.
 */
@Composable
internal fun rememberScrollbarHoverReveal(hideDelayMillis: Long = 800L): ScrollbarHoverReveal {
    val scope = rememberCoroutineScope()
    val reveal = remember(scope, hideDelayMillis) {
        ScrollbarHoverReveal(scope, hideDelayMillis)
    }
    androidx.compose.runtime.DisposableEffect(reveal) {
        onDispose { reveal.dispose() }
    }
    return reveal
}
