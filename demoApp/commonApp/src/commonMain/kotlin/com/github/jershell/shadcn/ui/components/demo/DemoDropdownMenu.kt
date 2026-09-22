package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Pencil
import com.composables.icons.lucide.Plus
import com.composables.icons.lucide.Send
import com.composables.icons.lucide.Trash2
import com.github.jershell.shadcn.components.button.Button
import com.github.jershell.shadcn.components.button.ButtonText
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenu
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuCheckboxItem
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuContent
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuItem
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuItemIcon
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuItemText
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuItemVariant
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuLabel
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuRadioGroup
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuSeparator
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuShortcut
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuSub
import com.github.jershell.shadcn.components.toast.Toast
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_a_trigger_opens_a_panel_to_the_end_side_escape_o
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_actions
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_appearance_saved
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_archive
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_archived
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_billing_x
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_billing_x_2
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_checkable_rows_keep_the_menu_open_so_several_opt
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_checkbox_items
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_delete
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_deleted
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_disabled
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_duplicate
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_duplicated
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_e
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_edit
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_edited
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_items
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_labels_separators_shortcuts_icons_destructive_an
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_line_numbers_x
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_monthly
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_more
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_more_actions
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_move_to
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_moved
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_new_tab
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_open_menu
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_radio_group
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_rows_inside_a_group_are_mutually_exclusive_a_sel
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_s
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_save
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_share
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_show_line_numbers
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_sub_menu
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_t
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_text
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_usage
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_view_options
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_weekly
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_word_wrap_x
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_wrap_long_lines
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_yearly
import com.github.jershell.shadcn.demoapp.generated.resources.dropdown_menu_z
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoDropdownMenu() {
    val s_dropdown_menu_weekly = stringResource(Res.string.dropdown_menu_weekly)
    val s_dropdown_menu_monthly = stringResource(Res.string.dropdown_menu_monthly)
    val s_dropdown_menu_yearly = stringResource(Res.string.dropdown_menu_yearly)
    val s_dropdown_menu_billing_weekly = stringResource(Res.string.dropdown_menu_billing_x_2, s_dropdown_menu_weekly)
    val s_dropdown_menu_billing_monthly = stringResource(Res.string.dropdown_menu_billing_x_2, s_dropdown_menu_monthly)
    val s_dropdown_menu_billing_yearly = stringResource(Res.string.dropdown_menu_billing_x_2, s_dropdown_menu_yearly)
    val s_dropdown_menu_appearance_saved = stringResource(Res.string.dropdown_menu_appearance_saved)
    val s_dropdown_menu_line_numbers_on = stringResource(Res.string.dropdown_menu_line_numbers_x, true)
    val s_dropdown_menu_line_numbers_off = stringResource(Res.string.dropdown_menu_line_numbers_x, false)
    val s_dropdown_menu_word_wrap_on = stringResource(Res.string.dropdown_menu_word_wrap_x, true)
    val s_dropdown_menu_word_wrap_off = stringResource(Res.string.dropdown_menu_word_wrap_x, false)
    val s_dropdown_menu_archived = stringResource(Res.string.dropdown_menu_archived)
    val s_dropdown_menu_duplicated = stringResource(Res.string.dropdown_menu_duplicated)
    val s_dropdown_menu_moved = stringResource(Res.string.dropdown_menu_moved)
    val s_dropdown_menu_edited = stringResource(Res.string.dropdown_menu_edited)
    val s_dropdown_menu_deleted = stringResource(Res.string.dropdown_menu_deleted)
    val s_dropdown_menu_share = stringResource(Res.string.dropdown_menu_share)
    val s_dropdown_menu_new_tab = stringResource(Res.string.dropdown_menu_new_tab)
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.dropdown_menu_items),
            description = stringResource(Res.string.dropdown_menu_labels_separators_shortcuts_icons_destructive_an),
        ) {
            var open by remember { mutableStateOf(false) }
            DropdownMenu(
                expanded = open,
                onExpandedChange = { open = it },
                anchor = {
                    Button(onClick = { open = !open }) {
                        ButtonText(stringResource(Res.string.dropdown_menu_open_menu))
                    }
                },
            ) {
                DropdownMenuContent {
                    DropdownMenuLabel(stringResource(Res.string.dropdown_menu_actions))
                    DropdownMenuItem(onClick = { Toast(s_dropdown_menu_new_tab) }) {
                        DropdownMenuItemIcon(Lucide.Plus)
                        DropdownMenuItemText(stringResource(Res.string.dropdown_menu_new_tab))
                        DropdownMenuShortcut(stringResource(Res.string.dropdown_menu_t))
                    }
                    DropdownMenuItem(onClick = { Toast(s_dropdown_menu_share) }) {
                        DropdownMenuItemIcon(Lucide.Send)
                        DropdownMenuItemText(stringResource(Res.string.dropdown_menu_share))
                        DropdownMenuShortcut(stringResource(Res.string.dropdown_menu_s))
                    }
                    DropdownMenuSeparator()
                    DropdownMenuItem(
                        onClick = { Toast(s_dropdown_menu_deleted) },
                        variant = DropdownMenuItemVariant.Destructive,
                    ) {
                        DropdownMenuItemIcon(Lucide.Trash2)
                        DropdownMenuItemText(stringResource(Res.string.dropdown_menu_delete))
                        DropdownMenuShortcut(stringResource(Res.string.dropdown_menu_text))
                    }
                    DropdownMenuItem(onClick = {}, enabled = false) {
                        DropdownMenuItemText(stringResource(Res.string.dropdown_menu_disabled))
                    }
                }
            }
        }

        DemoSection(
            title = stringResource(Res.string.dropdown_menu_sub_menu),
            description = stringResource(Res.string.dropdown_menu_a_trigger_opens_a_panel_to_the_end_side_escape_o),
        ) {
            var open by remember { mutableStateOf(false) }
            var subOpen by remember { mutableStateOf(false) }
            LaunchedEffect(open) {
                if (!open) subOpen = false
            }
            DropdownMenu(
                expanded = open,
                onExpandedChange = { open = it },
                anchor = {
                    Button(onClick = { open = !open }) {
                        ButtonText(stringResource(Res.string.dropdown_menu_more_actions))
                    }
                },
            ) {
                DropdownMenuContent {
                    DropdownMenuItem(onClick = { Toast(s_dropdown_menu_edited) }) {
                        DropdownMenuItemIcon(Lucide.Pencil)
                        DropdownMenuItemText(stringResource(Res.string.dropdown_menu_edit))
                        DropdownMenuShortcut(stringResource(Res.string.dropdown_menu_e))
                    }
                    DropdownMenuSeparator()
                    DropdownMenuSub(
                        expanded = subOpen,
                        onExpandedChange = { subOpen = it },
                        trigger = {
                            DropdownMenuItemText(stringResource(Res.string.dropdown_menu_more))
                        },
                    ) {
                        DropdownMenuItem(onClick = { Toast(s_dropdown_menu_moved) }) {
                            DropdownMenuItemText(stringResource(Res.string.dropdown_menu_move_to))
                        }
                        DropdownMenuItem(onClick = { Toast(s_dropdown_menu_duplicated) }) {
                            DropdownMenuItemText(stringResource(Res.string.dropdown_menu_duplicate))
                        }
                        DropdownMenuSeparator()
                        DropdownMenuItem(
                            onClick = { Toast(s_dropdown_menu_archived) },
                            variant = DropdownMenuItemVariant.Destructive,
                        ) {
                            DropdownMenuItemText(stringResource(Res.string.dropdown_menu_archive))
                        }
                    }
                }
            }
        }

        DemoSection(
            title = stringResource(Res.string.dropdown_menu_checkbox_items),
            description = stringResource(Res.string.dropdown_menu_checkable_rows_keep_the_menu_open_so_several_opt),
        ) {
            var lineNumbers by remember { mutableStateOf(true) }
            var wrapText by remember { mutableStateOf(false) }
            var open by remember { mutableStateOf(false) }
            DropdownMenu(
                expanded = open,
                onExpandedChange = { open = it },
                anchor = {
                    Button(onClick = { open = !open }) {
                        ButtonText(stringResource(Res.string.dropdown_menu_view_options))
                    }
                },
            ) {
                DropdownMenuContent {
                    DropdownMenuCheckboxItem(
                        checked = lineNumbers,
                        onCheckedChange = { checked ->
                            lineNumbers = checked
                            Toast(if (checked) s_dropdown_menu_line_numbers_on else s_dropdown_menu_line_numbers_off)
                        },
                    ) {
                        DropdownMenuItemText(stringResource(Res.string.dropdown_menu_show_line_numbers))
                    }
                    DropdownMenuCheckboxItem(
                        checked = wrapText,
                        onCheckedChange = { checked ->
                            wrapText = checked
                            Toast(if (checked) s_dropdown_menu_word_wrap_on else s_dropdown_menu_word_wrap_off)
                        },
                    ) {
                        DropdownMenuItemText(stringResource(Res.string.dropdown_menu_wrap_long_lines))
                        DropdownMenuShortcut(stringResource(Res.string.dropdown_menu_z))
                    }
                    DropdownMenuSeparator()
                    DropdownMenuItem(onClick = { Toast(s_dropdown_menu_appearance_saved) }) {
                        DropdownMenuItemText(stringResource(Res.string.dropdown_menu_save))
                        DropdownMenuShortcut(stringResource(Res.string.dropdown_menu_s))
                    }
                }
            }
        }

        DemoSection(
            title = stringResource(Res.string.dropdown_menu_radio_group),
            description = stringResource(Res.string.dropdown_menu_rows_inside_a_group_are_mutually_exclusive_a_sel),
        ) {
            var billing by remember { mutableStateOf(s_dropdown_menu_monthly) }
            var open by remember { mutableStateOf(false) }
            DropdownMenu(
                expanded = open,
                onExpandedChange = { open = it },
                anchor = {
                    Button(onClick = { open = !open }) {
                        ButtonText(stringResource(Res.string.dropdown_menu_billing_x, billing))
                    }
                },
            ) {
                DropdownMenuContent {
                    DropdownMenuRadioGroup(
                        selected = billing,
                        onSelectedChange = {
                            billing = it
                            Toast(
                                when (it) {
                                    "Weekly" -> s_dropdown_menu_billing_weekly
                                    "Monthly" -> s_dropdown_menu_billing_monthly
                                    else -> s_dropdown_menu_billing_yearly
                                },
                            )
                        },
                    ) {
                        DropdownMenuRadioItem(key = "Weekly") {
                            DropdownMenuItemText(stringResource(Res.string.dropdown_menu_weekly))
                        }
                        DropdownMenuRadioItem(key = "Monthly") {
                            DropdownMenuItemText(stringResource(Res.string.dropdown_menu_monthly))
                        }
                        DropdownMenuRadioItem(key = "Yearly") {
                            DropdownMenuItemText(stringResource(Res.string.dropdown_menu_yearly))
                        }
                    }
                }
            }
        }

        P(stringResource(Res.string.dropdown_menu_usage))
        InlineCode(
            text = """
                var open by remember { mutableStateOf(false) }
                DropdownMenu(
                    expanded = open,
                    onExpandedChange = { open = it },
                    anchor = {
                        Button(onClick = { open = !open }) {
                            ButtonText("Open")
                        }
                    },
                ) {
                    DropdownMenuContent {
                        DropdownMenuLabel("Actions")
                        DropdownMenuItem(onClick = { Toast("New tab") }) {
                            DropdownMenuItemIcon(Lucide.Plus)
                            DropdownMenuItemText("New tab")
                            DropdownMenuShortcut("⌘T")
                        }
                        DropdownMenuSeparator()
                        DropdownMenuCheckboxItem(
                            checked = checked,
                            onCheckedChange = { checked = it },
                        ) {
                            DropdownMenuItemText("Show line numbers")
                        }
                        DropdownMenuRadioGroup(
                            selected = billing,
                            onSelectedChange = { billing = it },
                        ) {
                            DropdownMenuRadioItem(key = "Monthly") {
                                DropdownMenuItemText("Monthly")
                            }
                        }
                    }
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
