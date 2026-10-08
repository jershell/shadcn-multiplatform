package com.github.jershell.shadcn.components.scroll

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.unit.dp
import com.composeunstyled.ThumbVisibility
import com.github.jershell.shadcn.theme.ShadcnTheme
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class ScrollAreaLayoutTest {

    @Test
    fun overflowProducesVerticalScroll() = runTest {
        runComposeUiTest {
            val state = ScrollState(0)
            setContent {
                ShadcnTheme {
                    ScrollArea(
                        modifier = Modifier.height(280.dp),
                        scrollState = state,
                        thumbVisibility = ThumbVisibility.AlwaysVisible,
                    ) {
                        repeat(20) { index ->
                            BasicText("Item ${index + 1}")
                        }
                    }
                }
            }
            runOnIdle { }
            runOnIdle { }
            assertTrue(state.maxValue > 0, "expected scrollable content, maxValue=${state.maxValue}")
        }
    }

    @Test
    fun fittingContentHasNoScroll() = runTest {
        runComposeUiTest {
            val state = ScrollState(0)
            setContent {
                ShadcnTheme {
                    ScrollArea(
                        modifier = Modifier.height(280.dp),
                        scrollState = state,
                        thumbVisibility = ThumbVisibility.AlwaysVisible,
                    ) {
                        BasicText("Only item")
                    }
                }
            }
            runOnIdle { }
            runOnIdle { }
            assertEquals(state.maxValue, 0, "expected no scroll, maxValue=${state.maxValue}")
        }
    }
}
