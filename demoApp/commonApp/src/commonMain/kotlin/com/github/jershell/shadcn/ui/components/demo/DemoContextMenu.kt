package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Scissors
import com.composables.icons.lucide.Trash2
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.contextmenu.ContextMenu
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuCheckboxItem
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuItem
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuItemIcon
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuItemText
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuItemVariant
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuLabel
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuRadioGroup
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuSeparator
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuShortcut
import com.github.jershell.shadcn.components.toast.Toast
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.TypographyStyles
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.context_menu_actions
import com.github.jershell.shadcn.demoapp.generated.resources.context_menu_c
import com.github.jershell.shadcn.demoapp.generated.resources.context_menu_checkbox_and_radio
import com.github.jershell.shadcn.demoapp.generated.resources.context_menu_clipboard
import com.github.jershell.shadcn.demoapp.generated.resources.context_menu_copied
import com.github.jershell.shadcn.demoapp.generated.resources.context_menu_copy
import com.github.jershell.shadcn.demoapp.generated.resources.context_menu_cut
import com.github.jershell.shadcn.demoapp.generated.resources.context_menu_delete
import com.github.jershell.shadcn.demoapp.generated.resources.context_menu_deleted
import com.github.jershell.shadcn.demoapp.generated.resources.context_menu_large
import com.github.jershell.shadcn.demoapp.generated.resources.context_menu_medium
import com.github.jershell.shadcn.demoapp.generated.resources.context_menu_paste
import com.github.jershell.shadcn.demoapp.generated.resources.context_menu_pasted
import com.github.jershell.shadcn.demoapp.generated.resources.context_menu_right_click_anywhere_in_this_area
import com.github.jershell.shadcn.demoapp.generated.resources.context_menu_right_click_the_area_to_open_the_menu_at_the_poi
import com.github.jershell.shadcn.demoapp.generated.resources.context_menu_show_grid
import com.github.jershell.shadcn.demoapp.generated.resources.context_menu_small
import com.github.jershell.shadcn.demoapp.generated.resources.context_menu_text
import com.github.jershell.shadcn.demoapp.generated.resources.context_menu_the_same_toolkit_as_the_dropdown_menu_checkable
import com.github.jershell.shadcn.demoapp.generated.resources.context_menu_usage
import com.github.jershell.shadcn.demoapp.generated.resources.context_menu_v
import com.github.jershell.shadcn.demoapp.generated.resources.context_menu_x
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoContextMenu() {
    val s_context_menu_medium = stringResource(Res.string.context_menu_medium)
    val s_context_menu_deleted = stringResource(Res.string.context_menu_deleted)
    val s_context_menu_pasted = stringResource(Res.string.context_menu_pasted)
    val s_context_menu_copied = stringResource(Res.string.context_menu_copied)
    val s_context_menu_cut = stringResource(Res.string.context_menu_cut)
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.context_menu_actions),
            description = stringResource(Res.string.context_menu_right_click_the_area_to_open_the_menu_at_the_poi),
        ) {
            ContextMenu(
                menu = {
                    DropdownMenuLabel(stringResource(Res.string.context_menu_clipboard))
                    DropdownMenuItem(onClick = { Toast(s_context_menu_cut) }) {
                        DropdownMenuItemIcon(Lucide.Scissors)
                        DropdownMenuItemText(stringResource(Res.string.context_menu_cut))
                        DropdownMenuShortcut(stringResource(Res.string.context_menu_x))
                    }
                    DropdownMenuItem(onClick = { Toast(s_context_menu_copied) }) {
                        DropdownMenuItemText(stringResource(Res.string.context_menu_copy))
                        DropdownMenuShortcut(stringResource(Res.string.context_menu_c))
                    }
                    DropdownMenuItem(onClick = { Toast(s_context_menu_pasted) }) {
                        DropdownMenuItemText(stringResource(Res.string.context_menu_paste))
                        DropdownMenuShortcut(stringResource(Res.string.context_menu_v))
                    }
                    DropdownMenuSeparator()
                    DropdownMenuItem(
                        onClick = { Toast(s_context_menu_deleted) },
                        variant = DropdownMenuItemVariant.Destructive,
                    ) {
                        DropdownMenuItemIcon(Lucide.Trash2)
                        DropdownMenuItemText(stringResource(Res.string.context_menu_delete))
                        DropdownMenuShortcut(stringResource(Res.string.context_menu_text))
                    }
                },
            ) {
                ContextMenuArea()
            }
        }

        DemoSection(
            title = stringResource(Res.string.context_menu_checkbox_and_radio),
            description = stringResource(Res.string.context_menu_the_same_toolkit_as_the_dropdown_menu_checkable),
        ) {
            var showGrid by remember { mutableStateOf(true) }
            var size by remember { mutableStateOf(s_context_menu_medium) }
            ContextMenu(
                menu = {
                    DropdownMenuCheckboxItem(
                        checked = showGrid,
                        onCheckedChange = { showGrid = it },
                    ) {
                        DropdownMenuItemText(stringResource(Res.string.context_menu_show_grid))
                    }
                    DropdownMenuSeparator()
                    DropdownMenuRadioGroup(
                        selected = size,
                        onSelectedChange = { size = it },
                    ) {
                        DropdownMenuRadioItem(key = "Small") {
                            DropdownMenuItemText(stringResource(Res.string.context_menu_small))
                        }
                        DropdownMenuRadioItem(key = "Medium") {
                            DropdownMenuItemText(stringResource(Res.string.context_menu_medium))
                        }
                        DropdownMenuRadioItem(key = "Large") {
                            DropdownMenuItemText(stringResource(Res.string.context_menu_large))
                        }
                    }
                },
            ) {
                ContextMenuArea()
            }
        }

        P(stringResource(Res.string.context_menu_usage))
        InlineCode(
            text = """
                ContextMenu(
                    menu = {
                        DropdownMenuItem(onClick = { ... }) {
                            DropdownMenuItemIcon(Lucide.Scissors)
                            DropdownMenuItemText("Cut")
                            DropdownMenuShortcut("⌘X")
                        }
                        DropdownMenuSeparator()
                        DropdownMenuItem(
                            onClick = { ... },
                            variant = DropdownMenuItemVariant.Destructive,
                        ) {
                            DropdownMenuItemText("Delete")
                        }
                    },
                ) {
                    // the right-clickable area
                }
            """.trimIndent(),
            selected = false,
            selectEnabled = true,
            copyEnabled = true,
        )
    }
}

@Composable
private fun ContextMenuArea() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Theme[ColorProps][ColorTokens.muted])
            .border(
                width = 1.dp,
                color = Theme[ColorProps][ColorTokens.border],
                shape = RoundedCornerShape(8.dp),
            ),
        contentAlignment = Alignment.Center,
        content = {
            BasicText(
                text = stringResource(Res.string.context_menu_right_click_anywhere_in_this_area),
                style = TypographyStyles.textSmRegular.copy(
                    color = Theme[ColorProps][ColorTokens.mutedForeground],
                ),
            )
        },
    )
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
