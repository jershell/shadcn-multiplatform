package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jershell.shadcn.components.label.Label
import com.github.jershell.shadcn.components.switch.Switch
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.switch_a_control_that_allows_the_user_to_toggle_between
import com.github.jershell.shadcn.demoapp.generated.resources.switch_airplane_mode
import com.github.jershell.shadcn.demoapp.generated.resources.switch_checked
import com.github.jershell.shadcn.demoapp.generated.resources.switch_default
import com.github.jershell.shadcn.demoapp.generated.resources.switch_disabled
import com.github.jershell.shadcn.demoapp.generated.resources.switch_disabled_switches_are_not_interactive_and_appear
import com.github.jershell.shadcn.demoapp.generated.resources.switch_the_switch_can_be_forced_into_the_checked_state
import com.github.jershell.shadcn.demoapp.generated.resources.switch_usage
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoSwitch() {
    var checked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.switch_default),
            description = stringResource(Res.string.switch_a_control_that_allows_the_user_to_toggle_between),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Switch(
                    checked = checked,
                    onCheckedChange = { checked = it },
                )
                Label(
                    text = stringResource(Res.string.switch_airplane_mode),
                    modifier = Modifier.clickable { checked = !checked },
                )
            }
        }

        DemoSection(
            title = stringResource(Res.string.switch_checked),
            description = stringResource(Res.string.switch_the_switch_can_be_forced_into_the_checked_state),
        ) {
            Switch(
                checked = true,
                onCheckedChange = {},
            )
        }

        DemoSection(
            title = stringResource(Res.string.switch_disabled),
            description = stringResource(Res.string.switch_disabled_switches_are_not_interactive_and_appear),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Switch(
                    checked = false,
                    onCheckedChange = {},
                    enabled = false,
                )
                Switch(
                    checked = true,
                    onCheckedChange = {},
                    enabled = false,
                )
            }
        }

        Muted(stringResource(Res.string.switch_usage))
        InlineCode(
            text = """
                var checked by remember { mutableStateOf(false) }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = checked,
                        onCheckedChange = { checked = it },
                    )
                    Label(
                        text = "Airplane Mode",
                        modifier = Modifier.clickable { checked = !checked }
                    )
                }
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
