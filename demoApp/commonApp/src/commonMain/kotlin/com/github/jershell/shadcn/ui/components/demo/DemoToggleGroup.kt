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
import com.composables.icons.lucide.Bold
import com.composables.icons.lucide.Italic
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Underline
import com.github.jershell.shadcn.components.toggle.ToggleIcon
import com.github.jershell.shadcn.components.toggle.ToggleSize
import com.github.jershell.shadcn.components.toggle.ToggleVariant
import com.github.jershell.shadcn.components.toggle.group.ToggleGroup
import com.github.jershell.shadcn.components.toggle.group.ToggleGroupMultiple
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.toggle_group_a_positive_spacing_turns_items_into_standalone_t
import com.github.jershell.shadcn.demoapp.generated.resources.toggle_group_bold
import com.github.jershell.shadcn.demoapp.generated.resources.toggle_group_italic
import com.github.jershell.shadcn.demoapp.generated.resources.toggle_group_multiple
import com.github.jershell.shadcn.demoapp.generated.resources.toggle_group_one_item_selected_at_a_time_clicking_the_selecte
import com.github.jershell.shadcn.demoapp.generated.resources.toggle_group_several_items_can_be_selected_at_once
import com.github.jershell.shadcn.demoapp.generated.resources.toggle_group_single
import com.github.jershell.shadcn.demoapp.generated.resources.toggle_group_underline
import com.github.jershell.shadcn.demoapp.generated.resources.toggle_group_usage
import com.github.jershell.shadcn.demoapp.generated.resources.toggle_group_with_spacing
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DemoToggleGroup() {
    var single by remember { mutableStateOf<String?>(null) }
    var multiple by remember { mutableStateOf(setOf("bold", "underline")) }
    var spaced by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.toggle_group_single),
            description = stringResource(Res.string.toggle_group_one_item_selected_at_a_time_clicking_the_selecte),
        ) {
            ToggleGroup(
                value = single,
                onValueChange = { single = it },
                variant = ToggleVariant.Outline,
            ) {
                Item(value = "bold") {
                    ToggleIcon(imageVector = Lucide.Bold, contentDescription = stringResource(Res.string.toggle_group_bold))
                }
                Item(value = "italic") {
                    ToggleIcon(imageVector = Lucide.Italic, contentDescription = stringResource(Res.string.toggle_group_italic))
                }
                Item(value = "underline") {
                    ToggleIcon(imageVector = Lucide.Underline, contentDescription = stringResource(Res.string.toggle_group_underline))
                }
            }
        }

        DemoSection(
            title = stringResource(Res.string.toggle_group_multiple),
            description = stringResource(Res.string.toggle_group_several_items_can_be_selected_at_once),
        ) {
            ToggleGroupMultiple(
                values = multiple,
                onValuesChange = { multiple = it },
                size = ToggleSize.Sm,
            ) {
                Item(value = "bold") {
                    ToggleIcon(imageVector = Lucide.Bold, contentDescription = stringResource(Res.string.toggle_group_bold))
                }
                Item(value = "italic") {
                    ToggleIcon(imageVector = Lucide.Italic, contentDescription = stringResource(Res.string.toggle_group_italic))
                }
                Item(value = "underline") {
                    ToggleIcon(imageVector = Lucide.Underline, contentDescription = stringResource(Res.string.toggle_group_underline))
                }
            }
        }

        DemoSection(
            title = stringResource(Res.string.toggle_group_with_spacing),
            description = stringResource(Res.string.toggle_group_a_positive_spacing_turns_items_into_standalone_t),
        ) {
            ToggleGroup(
                value = spaced,
                onValueChange = { spaced = it },
                variant = ToggleVariant.Outline,
                spacing = 8.dp,
            ) {
                Item(value = "bold") {
                    ToggleIcon(imageVector = Lucide.Bold, contentDescription = stringResource(Res.string.toggle_group_bold))
                }
                Item(value = "italic") {
                    ToggleIcon(imageVector = Lucide.Italic, contentDescription = stringResource(Res.string.toggle_group_italic))
                }
                Item(value = "underline") {
                    ToggleIcon(imageVector = Lucide.Underline, contentDescription = stringResource(Res.string.toggle_group_underline))
                }
            }
        }

        Muted(stringResource(Res.string.toggle_group_usage))
        InlineCode(
            text = """
                var format by remember { mutableStateOf<String?>(null) }
                ToggleGroup(
                    value = format,
                    onValueChange = { format = it },
                    variant = ToggleVariant.Outline,
                ) {
                    Item(value = "bold") {
                        ToggleIcon(imageVector = Lucide.Bold, contentDescription = "Bold")
                    }
                    Item(value = "italic") {
                        ToggleIcon(imageVector = Lucide.Italic, contentDescription = "Italic")
                    }
                }

                var styles by remember { mutableStateOf(setOf("bold")) }
                ToggleGroupMultiple(values = styles, onValuesChange = { styles = it }) {
                    Item(value = "bold") { ToggleIcon(Lucide.Bold, "Bold") }
                }
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
    description: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        H4(title)
        P(description)
        content()
    }
}
