package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuCheckboxItem
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuItem
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuItemText
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuItemVariant
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuRadioGroup
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuSeparator
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuShortcut
import com.github.jershell.shadcn.components.menubar.Menubar
import com.github.jershell.shadcn.components.toast.Toast
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_c
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_click_a_menu_to_open_it_hover_switches_between_o
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_close
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_closed
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_copied
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_copy
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_cut
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_dark
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_default
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_edit
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_file
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_help
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_light
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_n
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_new_tab
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_new_tab_2
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_new_window
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_new_window_2
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_redo
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_status_bar
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_t
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_undo
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_usage
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_view
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_w
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_x
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_z
import com.github.jershell.shadcn.demoapp.generated.resources.menubar_z_2
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoMenubar() {
    val s_menubar_copied = stringResource(Res.string.menubar_copied)
    val s_menubar_cut = stringResource(Res.string.menubar_cut)
    val s_menubar_redo = stringResource(Res.string.menubar_redo)
    val s_menubar_undo = stringResource(Res.string.menubar_undo)
    val s_menubar_closed = stringResource(Res.string.menubar_closed)
    val s_menubar_new_window = stringResource(Res.string.menubar_new_window)
    val s_menubar_new_tab = stringResource(Res.string.menubar_new_tab)
    val s_menubar_light = stringResource(Res.string.menubar_light)
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.menubar_default),
            description = stringResource(Res.string.menubar_click_a_menu_to_open_it_hover_switches_between_o),
        ) {
            var checked by remember { mutableStateOf(true) }
            var theme by remember { mutableStateOf(s_menubar_light) }
            Menubar {
                MenubarMenu(stringResource(Res.string.menubar_file)) {
                    DropdownMenuItem(onClick = { Toast(s_menubar_new_tab) }) {
                        DropdownMenuItemText(stringResource(Res.string.menubar_new_tab_2))
                        DropdownMenuShortcut(stringResource(Res.string.menubar_t))
                    }
                    DropdownMenuItem(onClick = { Toast(s_menubar_new_window) }) {
                        DropdownMenuItemText(stringResource(Res.string.menubar_new_window_2))
                        DropdownMenuShortcut(stringResource(Res.string.menubar_n))
                    }
                    DropdownMenuSeparator()
                    DropdownMenuItem(
                        onClick = { Toast(s_menubar_closed) },
                        variant = DropdownMenuItemVariant.Destructive,
                    ) {
                        DropdownMenuItemText(stringResource(Res.string.menubar_close))
                        DropdownMenuShortcut(stringResource(Res.string.menubar_w))
                    }
                }
                MenubarMenu(stringResource(Res.string.menubar_edit)) {
                    DropdownMenuItem(onClick = { Toast(s_menubar_undo) }) {
                        DropdownMenuItemText(stringResource(Res.string.menubar_undo))
                        DropdownMenuShortcut(stringResource(Res.string.menubar_z))
                    }
                    DropdownMenuItem(onClick = { Toast(s_menubar_redo) }) {
                        DropdownMenuItemText(stringResource(Res.string.menubar_redo))
                        DropdownMenuShortcut(stringResource(Res.string.menubar_z_2))
                    }
                    DropdownMenuSeparator()
                    DropdownMenuItem(onClick = { Toast(s_menubar_cut) }) {
                        DropdownMenuItemText(stringResource(Res.string.menubar_cut))
                        DropdownMenuShortcut(stringResource(Res.string.menubar_x))
                    }
                    DropdownMenuItem(onClick = { Toast(s_menubar_copied) }) {
                        DropdownMenuItemText(stringResource(Res.string.menubar_copy))
                        DropdownMenuShortcut(stringResource(Res.string.menubar_c))
                    }
                }
                MenubarMenu(stringResource(Res.string.menubar_view)) {
                    DropdownMenuCheckboxItem(
                        checked = checked,
                        onCheckedChange = { checked = it },
                    ) {
                        DropdownMenuItemText(stringResource(Res.string.menubar_status_bar))
                    }
                    DropdownMenuSeparator()
                    DropdownMenuRadioGroup(
                        selected = theme,
                        onSelectedChange = { theme = it },
                    ) {
                        DropdownMenuRadioItem(key = "Light") {
                            DropdownMenuItemText(stringResource(Res.string.menubar_light))
                        }
                        DropdownMenuRadioItem(key = "Dark") {
                            DropdownMenuItemText(stringResource(Res.string.menubar_dark))
                        }
                    }
                }
                MenubarMenu(stringResource(Res.string.menubar_help), enabled = false) {
                }
            }
        }

        P(stringResource(Res.string.menubar_usage))
        InlineCode(
            text = """
                Menubar {
                    MenubarMenu("File") {
                        DropdownMenuItem(onClick = { ... }) {
                            DropdownMenuItemText("New Tab")
                            DropdownMenuShortcut("⌘T")
                        }
                        DropdownMenuSeparator()
                        DropdownMenuItem(onClick = { ... }) {
                            DropdownMenuItemText("Close")
                        }
                    }
                    MenubarMenu("Edit") {
                        DropdownMenuItem(onClick = { ... }) {
                            DropdownMenuItemText("Undo")
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
