package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jershell.shadcn.components.button.Button
import com.github.jershell.shadcn.components.button.ButtonText
import com.github.jershell.shadcn.components.button.ButtonVariant
import com.github.jershell.shadcn.components.progress.Progress
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.progress_10
import com.github.jershell.shadcn.demoapp.generated.resources.progress_10_2
import com.github.jershell.shadcn.demoapp.generated.resources.progress_a_progress_bar_that_shows_completion_percentage
import com.github.jershell.shadcn.demoapp.generated.resources.progress_animated_value
import com.github.jershell.shadcn.demoapp.generated.resources.progress_current_value_x
import com.github.jershell.shadcn.demoapp.generated.resources.progress_default
import com.github.jershell.shadcn.demoapp.generated.resources.progress_usage
import com.github.jershell.shadcn.demoapp.generated.resources.progress_use_controls_to_change_progress_and_see_animated
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoProgress() {
    var value by remember { mutableFloatStateOf(0.45f) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.progress_default),
            description = stringResource(Res.string.progress_a_progress_bar_that_shows_completion_percentage),
        ) {
            Progress(value = 0.33f)
        }

        DemoSection(
            title = stringResource(Res.string.progress_animated_value),
            description = stringResource(Res.string.progress_use_controls_to_change_progress_and_see_animated),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Progress(value = value)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { value = (value - 0.1f).coerceAtLeast(0f) },
                        variant = ButtonVariant.Outline,
                    ) {
                        ButtonText(stringResource(Res.string.progress_10))
                    }
                    Button(
                        onClick = { value = (value + 0.1f).coerceAtMost(1f) },
                    ) {
                        ButtonText(stringResource(Res.string.progress_10_2))
                    }
                }
                P(stringResource(Res.string.progress_current_value_x, (value * 100).toInt()))
            }
        }

        Muted(stringResource(Res.string.progress_usage))
        InlineCode(
            text = """
                Progress(value = 0.6f)
                
                var progress by remember { mutableFloatStateOf(0.25f) }
                Progress(value = progress, animate = true)
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
