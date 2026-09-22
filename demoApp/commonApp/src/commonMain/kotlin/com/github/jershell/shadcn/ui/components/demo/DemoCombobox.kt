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
import com.github.jershell.shadcn.components.combobox.Combobox
import com.github.jershell.shadcn.components.combobox.ComboboxChip
import com.github.jershell.shadcn.components.combobox.ComboboxItem
import com.github.jershell.shadcn.components.combobox.ComboboxItemIndicator
import com.github.jershell.shadcn.components.combobox.ComboboxItemText
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.models.DataItem
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_a_searchable_select_that_lets_the_user_pick_from
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_admin
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_c
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_custom_chip_content
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_custom_filter
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_custom_item_content
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_disabled
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_disabled_combobox
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_editor
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_go
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_java
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_kotlin
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_multi_searchable_select_with_chips
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_no_language_found
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_no_language_starts_with_this_query
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_no_role_found
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_pick_a_role
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_python
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_rust
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_select_a_language
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_select_languages
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_single_searchable_select
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_starts_with
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_swift
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_typescript
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_usage
import com.github.jershell.shadcn.demoapp.generated.resources.combobox_viewer
import org.jetbrains.compose.resources.stringResource

@Composable
private fun languageItems() = listOf(
    DataItem(key = 1, title = stringResource(Res.string.combobox_kotlin), data = "kotlin"),
    DataItem(key = 2, title = stringResource(Res.string.combobox_java), data = "java"),
    DataItem(key = 3, title = stringResource(Res.string.combobox_swift), data = "swift"),
    DataItem(key = 4, title = stringResource(Res.string.combobox_rust), data = "rust"),
    DataItem(key = 5, title = stringResource(Res.string.combobox_go), data = "go", enabled = false),
    DataItem(key = 6, title = stringResource(Res.string.combobox_python), data = "python"),
    DataItem(key = 7, title = stringResource(Res.string.combobox_typescript), data = "typescript"),
    DataItem(key = 8, title = stringResource(Res.string.combobox_c), data = "cpp"),
)

@Composable
private fun roleItems() = listOf(
    DataItem(key = 10, title = stringResource(Res.string.combobox_admin), data = "admin"),
    DataItem(key = 11, title = stringResource(Res.string.combobox_editor), data = "editor"),
    DataItem(key = 12, title = stringResource(Res.string.combobox_viewer), data = "viewer"),
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DemoCombobox() {
    var singleSelected by remember { mutableStateOf(setOf<Int>()) }
    var multiSelected by remember { mutableStateOf(setOf<Int>()) }
    var disabledSelected by remember { mutableStateOf(setOf<Int>()) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(title = stringResource(Res.string.combobox_single_searchable_select)) {
            Combobox(
                items = languageItems(),
                selected = singleSelected,
                onSelectedChange = { singleSelected = it },
                placeholder = stringResource(Res.string.combobox_select_a_language),
                emptyText = stringResource(Res.string.combobox_no_language_found),
            )
        }

        DemoSection(title = stringResource(Res.string.combobox_multi_searchable_select_with_chips)) {
            Combobox(
                items = languageItems(),
                selected = multiSelected,
                onSelectedChange = { multiSelected = it },
                placeholder = stringResource(Res.string.combobox_select_languages),
                emptyText = stringResource(Res.string.combobox_no_language_found),
                multiple = true,
                showClear = true,
            )
        }

        DemoSection(title = stringResource(Res.string.combobox_disabled)) {
            Combobox(
                items = languageItems(),
                selected = disabledSelected,
                onSelectedChange = { disabledSelected = it },
                placeholder = stringResource(Res.string.combobox_disabled_combobox),
                enabled = false,
            )
        }

        DemoSection(title = stringResource(Res.string.combobox_custom_item_content)) {
            Combobox(
                items = roleItems(),
                selected = singleSelected,
                onSelectedChange = { singleSelected = it },
                placeholder = stringResource(Res.string.combobox_pick_a_role),
                emptyText = stringResource(Res.string.combobox_no_role_found),
                itemContent = { item, selected, onClick ->
                    ComboboxItem(
                        selected = selected,
                        onClick = onClick,
                    ) {
                        ComboboxItemText(item.title.uppercase())
                        if (selected) {
                            ComboboxItemIndicator()
                        }
                    }
                },
            )
        }

        DemoSection(title = stringResource(Res.string.combobox_custom_filter)) {
            var customSelected by remember { mutableStateOf(setOf<Int>()) }
            Combobox(
                items = languageItems(),
                selected = customSelected,
                onSelectedChange = { customSelected = it },
                placeholder = stringResource(Res.string.combobox_starts_with),
                emptyText = stringResource(Res.string.combobox_no_language_starts_with_this_query),
                itemFilter = { item, query ->
                    item.title.startsWith(query, ignoreCase = true)
                },
            )
        }

        DemoSection(title = stringResource(Res.string.combobox_custom_chip_content)) {
            var customChipSelected by remember { mutableStateOf(setOf<Int>()) }
            Combobox(
                items = languageItems(),
                selected = customChipSelected,
                onSelectedChange = { customChipSelected = it },
                placeholder = stringResource(Res.string.combobox_select_languages),
                emptyText = stringResource(Res.string.combobox_no_language_found),
                multiple = true,
                chipContent = { item, onDismiss ->
                    ComboboxChip(
                        item = item,
                        onDismiss = onDismiss,
                        text = item.title.uppercase(),
                    )
                },
            )
        }

        Muted(stringResource(Res.string.combobox_usage))
        InlineCode(
            text = """
                Combobox(
                    items = items,
                    selected = selected,
                    onSelectedChange = { selected = it },
                    placeholder = "Select a language",
                    emptyText = "No language found.",
                )

                Combobox(
                    items = items,
                    selected = selected,
                    onSelectedChange = { selected = it },
                    placeholder = "Select languages",
                    emptyText = "No language found.",
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
        P(stringResource(Res.string.combobox_a_searchable_select_that_lets_the_user_pick_from))
        content()
    }
}
