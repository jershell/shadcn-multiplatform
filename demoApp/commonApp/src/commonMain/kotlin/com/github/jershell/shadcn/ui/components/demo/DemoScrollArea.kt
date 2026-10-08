package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.composeunstyled.ThumbVisibility
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.scroll.ScrollArea
import com.github.jershell.shadcn.components.slider.Slider
import com.github.jershell.shadcn.components.switch.Switch
import com.github.jershell.shadcn.components.typography.Blockquote
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.scroll_area_always_visible
import com.github.jershell.shadcn.demoapp.generated.resources.scroll_area_basic
import com.github.jershell.shadcn.demoapp.generated.resources.scroll_area_configuration
import com.github.jershell.shadcn.demoapp.generated.resources.scroll_area_horizontal
import com.github.jershell.shadcn.demoapp.generated.resources.scroll_area_pass_horizontalstate_to_enable_horizont
import com.github.jershell.shadcn.demoapp.generated.resources.scroll_area_thumb_visibility_and_widt_t
import com.github.jershell.shadcn.demoapp.generated.resources.scroll_area_usage
import com.github.jershell.shadcn.demoapp.generated.resources.scroll_area_vertical_and_horizontal_scrolling
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.milliseconds

private val DemoScrollItems = listOf(
    "Personal account",
    "Inbox",
    "Settings",
    "Payments",
    "Team",
    "Activity",
    "Billing summary",
    "Security keys",
    "Connected apps",
    "Devices",
    "Language",
    "Export data",
    "Delete account",
)

@Composable
fun DemoScrollArea() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.scroll_area_basic),
            description = stringResource(Res.string.scroll_area_vertical_and_horizontal_scrolling),
        ) {
            ScrollArea(
                modifier = Modifier
                    .width(200.dp)
                    .height(280.dp),
                thumbVisibility = ThumbVisibility.AlwaysVisible,
            ) {
                repeat(20) { index ->
                    H4(DemoScrollItems[index % DemoScrollItems.size] + " ${index + 1}")
                }
            }
        }

        DemoSection(
            title = stringResource(Res.string.scroll_area_configuration),
            description = stringResource(Res.string.scroll_area_thumb_visibility_and_widt_t),
        ) {
            var alwaysVisible by remember { mutableStateOf(true) }
            var thumbWidth by remember { mutableFloatStateOf(10f) }

            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Switch(
                    checked = alwaysVisible,
                    onCheckedChange = { alwaysVisible = it },
                )
                Muted(stringResource(Res.string.scroll_area_always_visible))
                Muted("Width:")
                Slider(
                    value = thumbWidth,
                    onValueChange = { thumbWidth = it },
                    valueRange = 4f..24f,
                    modifier = Modifier.width(160.dp),
                )
                Muted("${thumbWidth.toInt()} dp")
            }

            ScrollArea(
                modifier = Modifier
                    .width(200.dp)
                    .height(280.dp),
                thumbVisibility = if (alwaysVisible) {
                    ThumbVisibility.AlwaysVisible
                } else {
                    ThumbVisibility.HideWhileIdle(
                        enter = fadeIn(),
                        exit = fadeOut(),
                        hideDelay = 800.milliseconds,
                    )
                },
                verticalThumbWidth = thumbWidth.dp,
            ) {
                repeat(60) { index ->
                    P(DemoScrollItems[index % DemoScrollItems.size] + " ${index + 1}")
                }
            }
        }

        DemoSection(
            title = stringResource(Res.string.scroll_area_horizontal),
            description = stringResource(Res.string.scroll_area_pass_horizontalstate_to_enable_horizont),
        ) {
            val cardBorder = Theme[ColorProps][ColorTokens.border]
            val cardBg = Theme[ColorProps][ColorTokens.muted]
            val borderWidth = Theme[DimProps][DimTokens.borderWidth]
            val cardShape = RoundedCornerShape(Theme[DimProps][DimTokens.radiusMd])
            ScrollArea(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                horizontalState = rememberScrollState(),
                thumbVisibility = ThumbVisibility.AlwaysVisible,
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    repeat(8) { index ->
                        Column(
                            modifier = Modifier
                                .width(220.dp)
                                .height(220.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(cardBg)
                                .border(borderWidth, cardBorder, cardShape),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            H4("Card ${index + 1}")
                            P(DemoScrollItems[index % DemoScrollItems.size])
                        }
                    }
                }
            }
        }

        Muted(stringResource(Res.string.scroll_area_usage))
        InlineCode(
            text = """
                ScrollArea(modifier = Modifier.height(280.dp), thumbVisibility = ThumbVisibility.AlwaysVisible) { ... }

                ScrollArea(
                    modifier = Modifier.height(280.dp),
                    thumbVisibility = ThumbVisibility.AlwaysVisible,
                    verticalThumbWidth = 10.dp,
                ) { ... }
            """.trimIndent(),
            selected = false,
            selectEnabled = true,
            copyEnabled = true,
        )
    }
}

@Composable
private fun DemoSection(
    title: String,
    description: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        H4(title)
        P(description)
        content()
    }
}
