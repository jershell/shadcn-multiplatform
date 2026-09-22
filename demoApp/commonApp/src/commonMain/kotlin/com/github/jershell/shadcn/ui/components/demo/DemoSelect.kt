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
import com.github.jershell.shadcn.components.select.Select
import com.github.jershell.shadcn.components.select.SelectItem
import com.github.jershell.shadcn.components.select.SelectItemIndicator
import com.github.jershell.shadcn.components.select.SelectItemText
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.models.DataItem
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.select_apple
import com.github.jershell.shadcn.demoapp.generated.resources.select_banana
import com.github.jershell.shadcn.demoapp.generated.resources.select_custom_item_content
import com.github.jershell.shadcn.demoapp.generated.resources.select_dark
import com.github.jershell.shadcn.demoapp.generated.resources.select_disabled
import com.github.jershell.shadcn.demoapp.generated.resources.select_disabled_select
import com.github.jershell.shadcn.demoapp.generated.resources.select_displays_a_list_of_options_for_the_user_to_pick
import com.github.jershell.shadcn.demoapp.generated.resources.select_grapes
import com.github.jershell.shadcn.demoapp.generated.resources.select_light
import com.github.jershell.shadcn.demoapp.generated.resources.select_multi_select
import com.github.jershell.shadcn.demoapp.generated.resources.select_orange
import com.github.jershell.shadcn.demoapp.generated.resources.select_pick_a_theme
import com.github.jershell.shadcn.demoapp.generated.resources.select_select_a_fruit
import com.github.jershell.shadcn.demoapp.generated.resources.select_select_fruits
import com.github.jershell.shadcn.demoapp.generated.resources.select_single_select
import com.github.jershell.shadcn.demoapp.generated.resources.select_strawberry
import com.github.jershell.shadcn.demoapp.generated.resources.select_system
import com.github.jershell.shadcn.demoapp.generated.resources.select_usage
import org.jetbrains.compose.resources.stringResource

@Composable
private fun fruitItems() = listOf(
    DataItem(key = 1, title = stringResource(Res.string.select_apple), data = "apple"),
    DataItem(key = 2, title = stringResource(Res.string.select_banana), data = "banana"),
    DataItem(key = 3, title = stringResource(Res.string.select_orange), data = "orange"),
    DataItem(key = 4, title = stringResource(Res.string.select_strawberry), data = "strawberry"),
    DataItem(key = 5, title = stringResource(Res.string.select_grapes), data = "grapes", enabled = false),
)

@Composable
private fun themeItems() = listOf(
    DataItem(key = 10, title = stringResource(Res.string.select_light), data = "light"),
    DataItem(key = 11, title = stringResource(Res.string.select_dark), data = "dark"),
    DataItem(key = 12, title = stringResource(Res.string.select_system), data = "system"),
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DemoSelect() {
    var singleSelected by remember { mutableStateOf(setOf<Int>()) }
    var multiSelected by remember { mutableStateOf(setOf<Int>()) }
    var disabledSelected by remember { mutableStateOf(setOf<Int>()) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(title = stringResource(Res.string.select_single_select)) {
            Select(
                items = fruitItems(),
                selected = singleSelected,
                onSelectedChange = { singleSelected = it },
                placeholder = stringResource(Res.string.select_select_a_fruit),
            )
        }

        DemoSection(title = stringResource(Res.string.select_multi_select)) {
            Select(
                items = fruitItems(),
                selected = multiSelected,
                onSelectedChange = { multiSelected = it },
                placeholder = stringResource(Res.string.select_select_fruits),
                multiple = true,
            )
        }

        DemoSection(title = stringResource(Res.string.select_disabled)) {
            Select(
                items = fruitItems(),
                selected = disabledSelected,
                onSelectedChange = { disabledSelected = it },
                placeholder = stringResource(Res.string.select_disabled_select),
                enabled = false,
            )
        }

        DemoSection(title = stringResource(Res.string.select_custom_item_content)) {
            Select(
                items = themeItems(),
                selected = singleSelected,
                onSelectedChange = { singleSelected = it },
                placeholder = stringResource(Res.string.select_pick_a_theme),
                itemContent = { item, selected, onClick ->
                    SelectItem(
                        selected = selected,
                        onClick = onClick,
                    ) {
                        SelectItemText(item.title.uppercase())
                        if (selected) {
                            SelectItemIndicator()
                        }
                    }
                },
            )
        }

        Muted(stringResource(Res.string.select_usage))
        InlineCode(
            text = """
                Select(
                    items = items,
                    selected = selected,
                    onSelectedChange = { selected = it },
                    placeholder = "Select a fruit",
                )
                
                Select(
                    items = items,
                    selected = selected,
                    onSelectedChange = { selected = it },
                    placeholder = "Select fruits",
                    multiple = true,
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
        P(stringResource(Res.string.select_displays_a_list_of_options_for_the_user_to_pick))
        content()
    }
}
