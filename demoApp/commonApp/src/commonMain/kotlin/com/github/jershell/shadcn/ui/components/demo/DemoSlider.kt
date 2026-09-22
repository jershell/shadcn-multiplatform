package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jershell.shadcn.components.slider.RangeSlider
import com.github.jershell.shadcn.components.slider.Slider
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.slider_basic
import com.github.jershell.shadcn.demoapp.generated.resources.slider_disabled
import com.github.jershell.shadcn.demoapp.generated.resources.slider_disabled_sliders_are_dimmed_and_ignore_all_inter
import com.github.jershell.shadcn.demoapp.generated.resources.slider_drag_the_thumb_tap_the_track_to_jump_or_use_arro
import com.github.jershell.shadcn.demoapp.generated.resources.slider_range
import com.github.jershell.shadcn.demoapp.generated.resources.slider_range_x_x
import com.github.jershell.shadcn.demoapp.generated.resources.slider_steps_10_snaps_the_value_to_11_discrete_position
import com.github.jershell.shadcn.demoapp.generated.resources.slider_two_thumbs_select_an_interval_taps_and_drags_go
import com.github.jershell.shadcn.demoapp.generated.resources.slider_usage
import com.github.jershell.shadcn.demoapp.generated.resources.slider_value_x_100
import com.github.jershell.shadcn.demoapp.generated.resources.slider_vertical
import com.github.jershell.shadcn.demoapp.generated.resources.slider_vertical_orientation_the_slider_needs_an_externa
import com.github.jershell.shadcn.demoapp.generated.resources.slider_with_steps
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoSlider() {
    var basic by remember { mutableStateOf(50f) }
    var stepped by remember { mutableStateOf(30f) }
    var vertical by remember { mutableStateOf(60f) }
    var range by remember { mutableStateOf(25f..75f) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.slider_basic),
            description = stringResource(Res.string.slider_drag_the_thumb_tap_the_track_to_jump_or_use_arro),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Slider(
                    value = basic,
                    onValueChange = { basic = it },
                    valueRange = 0f..100f,
                )
                Muted(stringResource(Res.string.slider_value_x_100, basic.toInt()))
            }
        }

        DemoSection(
            title = stringResource(Res.string.slider_with_steps),
            description = stringResource(Res.string.slider_steps_10_snaps_the_value_to_11_discrete_position),
        ) {
            Slider(
                value = stepped,
                onValueChange = { stepped = it },
                valueRange = 0f..100f,
                steps = 10,
            )
        }

        DemoSection(
            title = stringResource(Res.string.slider_range),
            description = stringResource(Res.string.slider_two_thumbs_select_an_interval_taps_and_drags_go),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                RangeSlider(
                    value = range,
                    onValueChange = { range = it },
                    valueRange = 0f..100f,
                )
                Muted(stringResource(Res.string.slider_range_x_x, range.start.toInt(), range.endInclusive.toInt()))
            }
        }

        DemoSection(
            title = stringResource(Res.string.slider_disabled),
            description = stringResource(Res.string.slider_disabled_sliders_are_dimmed_and_ignore_all_inter),
        ) {
            Slider(
                value = 40f,
                onValueChange = {},
                valueRange = 0f..100f,
                enabled = false,
            )
        }

        DemoSection(
            title = stringResource(Res.string.slider_vertical),
            description = stringResource(Res.string.slider_vertical_orientation_the_slider_needs_an_externa),
        ) {
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(176.dp),
            ) {
                Slider(
                    value = vertical,
                    onValueChange = { vertical = it },
                    orientation = Orientation.Vertical,
                )
            }
        }

        Muted(stringResource(Res.string.slider_usage))
        InlineCode(
            text = """
                var volume by remember { mutableFloatStateOf(50f) }
                Slider(
                    value = volume,
                    onValueChange = { volume = it },
                    valueRange = 0f..100f,
                )
                var price by remember { mutableFloatStateOf(25f..75f) }
                RangeSlider(
                    value = price,
                    onValueChange = { price = it },
                    valueRange = 0f..100f,
                )
                Slider(
                    value = volume,
                    onValueChange = { volume = it },
                    steps = 10,
                )
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
