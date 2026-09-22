package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jershell.shadcn.components.tabs.Tabs
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.models.DataItem
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.tabs_a_set_of_layered_sections_of_content_known_as_ta
import com.github.jershell.shadcn.demoapp.generated.resources.tabs_account
import com.github.jershell.shadcn.demoapp.generated.resources.tabs_as_a_radio_like_list
import com.github.jershell.shadcn.demoapp.generated.resources.tabs_content_for_x
import com.github.jershell.shadcn.demoapp.generated.resources.tabs_content_for_x_2
import com.github.jershell.shadcn.demoapp.generated.resources.tabs_default
import com.github.jershell.shadcn.demoapp.generated.resources.tabs_enterprise
import com.github.jershell.shadcn.demoapp.generated.resources.tabs_notifications
import com.github.jershell.shadcn.demoapp.generated.resources.tabs_password
import com.github.jershell.shadcn.demoapp.generated.resources.tabs_usage
import com.github.jershell.shadcn.demoapp.generated.resources.tabs_vertical
import org.jetbrains.compose.resources.stringResource

@Composable
private fun tabItems() = listOf(
    DataItem(key = 1, title = stringResource(Res.string.tabs_account), data = "account"),
    DataItem(key = 2, title = stringResource(Res.string.tabs_password), data = "password"),
    DataItem(key = 3, title = stringResource(Res.string.tabs_notifications), data = "notifications"),
    DataItem(key = 4, title = stringResource(Res.string.tabs_enterprise), data = "enterprise", enabled = false),
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DemoTabs() {
    var defaultSelected by remember { mutableStateOf(1) }
    var verticalSelected by remember { mutableStateOf(1) }
    var radioLikeSelected by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(title = stringResource(Res.string.tabs_default)) {
            Tabs(
                items = tabItems(),
                selectedKey = defaultSelected,
                onSelectedChange = { defaultSelected = it },
                panelContent = { item ->
                    P(
                        text = stringResource(Res.string.tabs_content_for_x, item.title),
                        modifier = Modifier.padding(top = 12.dp),
                    )
                },
            )
        }

        DemoSection(title = stringResource(Res.string.tabs_vertical)) {
            Tabs(
                items = tabItems(),
                selectedKey = verticalSelected,
                onSelectedChange = { verticalSelected = it },
                orientation = Orientation.Vertical,
                panelContent = { item ->
                    P(
                        text = stringResource(Res.string.tabs_content_for_x_2, item.title),
                        modifier = Modifier.padding(start = 12.dp),
                    )
                },
            )
        }

        DemoSection(title = stringResource(Res.string.tabs_as_a_radio_like_list)) {
            Tabs(
                items = tabItems(),
                selectedKey = radioLikeSelected,
                onSelectedChange = { radioLikeSelected = it },
            )
        }

        Muted(stringResource(Res.string.tabs_usage))
        InlineCode(
            text = """
                var selected by remember { mutableStateOf(1) }
                Tabs(
                    items = items,
                    selectedKey = selected,
                    onSelectedChange = { selected = it },
                    panelContent = { item ->
                        P("Content for ${'$'}{item.title}")
                    },
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
        P(stringResource(Res.string.tabs_a_set_of_layered_sections_of_content_known_as_ta))
        content()
    }
}
