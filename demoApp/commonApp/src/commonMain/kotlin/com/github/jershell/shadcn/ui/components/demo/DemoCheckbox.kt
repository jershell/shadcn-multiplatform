package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jershell.shadcn.components.checkbox.Checkbox
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.checkbox_a_control_that_allows_the_user_to_toggle_between
import com.github.jershell.shadcn.demoapp.generated.resources.checkbox_accept_terms_and_conditions
import com.github.jershell.shadcn.demoapp.generated.resources.checkbox_checked
import com.github.jershell.shadcn.demoapp.generated.resources.checkbox_default
import com.github.jershell.shadcn.demoapp.generated.resources.checkbox_disabled
import com.github.jershell.shadcn.demoapp.generated.resources.checkbox_disabled_checked
import com.github.jershell.shadcn.demoapp.generated.resources.checkbox_disabled_unchecked
import com.github.jershell.shadcn.demoapp.generated.resources.checkbox_usage
import com.github.jershell.shadcn.demoapp.generated.resources.checkbox_without_label
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DemoCheckbox() {
    var checked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(title = stringResource(Res.string.checkbox_default)) {
            Checkbox(
                checked = checked,
                onCheckedChange = { checked = it },
                label = stringResource(Res.string.checkbox_accept_terms_and_conditions),
            )
        }

        DemoSection(title = stringResource(Res.string.checkbox_checked)) {
            Checkbox(
                checked = true,
                onCheckedChange = {},
                label = stringResource(Res.string.checkbox_checked),
            )
        }

        DemoSection(title = stringResource(Res.string.checkbox_disabled)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Checkbox(
                    checked = false,
                    onCheckedChange = {},
                    label = stringResource(Res.string.checkbox_disabled_unchecked),
                    enabled = false,
                )
                Checkbox(
                    checked = true,
                    onCheckedChange = {},
                    label = stringResource(Res.string.checkbox_disabled_checked),
                    enabled = false,
                )
            }
        }

        DemoSection(title = stringResource(Res.string.checkbox_without_label)) {
            Checkbox(
                checked = checked,
                onCheckedChange = { checked = it },
            )
        }

        Muted(stringResource(Res.string.checkbox_usage))
        InlineCode(
            text = """
                var checked by remember { mutableStateOf(false) }
                Checkbox(
                    checked = checked,
                    onCheckedChange = { checked = it },
                    label = "Accept terms",
                )
            """.trimIndent(),
            selected = false,
            selectEnabled = true,
            copyEnabled = true,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DemoSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        H4(title)
        P(stringResource(Res.string.checkbox_a_control_that_allows_the_user_to_toggle_between))
        content()
    }
}
