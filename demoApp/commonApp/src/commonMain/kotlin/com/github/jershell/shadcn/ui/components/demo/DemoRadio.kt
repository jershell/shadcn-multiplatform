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
import com.github.jershell.shadcn.components.radio.RadioGroup
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.models.DataItem
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.radio_a_set_of_checkable_buttons_where_only_one_can_be
import com.github.jershell.shadcn.demoapp.generated.resources.radio_default
import com.github.jershell.shadcn.demoapp.generated.resources.radio_disabled_group
import com.github.jershell.shadcn.demoapp.generated.resources.radio_enterprise
import com.github.jershell.shadcn.demoapp.generated.resources.radio_free
import com.github.jershell.shadcn.demoapp.generated.resources.radio_pro
import com.github.jershell.shadcn.demoapp.generated.resources.radio_usage
import org.jetbrains.compose.resources.stringResource

@Composable
private fun planItems() = listOf(
    DataItem(key = 1, title = stringResource(Res.string.radio_free), data = "free"),
    DataItem(key = 2, title = stringResource(Res.string.radio_pro), data = "pro"),
    DataItem(key = 3, title = stringResource(Res.string.radio_enterprise), data = "enterprise", enabled = false),
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DemoRadio() {
    var selected by remember { mutableStateOf<Int?>(1) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(title = stringResource(Res.string.radio_default)) {
            RadioGroup(
                items = planItems(),
                selectedKey = selected,
                onSelectedChange = { selected = it },
            )
        }

        DemoSection(title = stringResource(Res.string.radio_disabled_group)) {
            RadioGroup(
                items = planItems(),
                selectedKey = selected,
                onSelectedChange = {},
                enabled = false,
            )
        }

        Muted(stringResource(Res.string.radio_usage))
        InlineCode(
            text = """
                var selected by remember { mutableStateOf<Int?>(null) }
                RadioGroup(
                    items = items,
                    selectedKey = selected,
                    onSelectedChange = { selected = it },
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
        P(stringResource(Res.string.radio_a_set_of_checkable_buttons_where_only_one_can_be))
        content()
    }
}
