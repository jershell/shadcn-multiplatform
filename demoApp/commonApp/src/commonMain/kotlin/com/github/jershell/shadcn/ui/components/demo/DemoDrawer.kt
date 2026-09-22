package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jershell.shadcn.components.button.Button
import com.github.jershell.shadcn.components.button.ButtonText
import com.github.jershell.shadcn.components.button.ButtonVariant
import com.github.jershell.shadcn.components.drawer.Drawer
import com.github.jershell.shadcn.components.drawer.DrawerDescription
import com.github.jershell.shadcn.components.drawer.DrawerFooter
import com.github.jershell.shadcn.components.drawer.DrawerHeader
import com.github.jershell.shadcn.components.drawer.DrawerTitle
import com.github.jershell.shadcn.components.toast.Toast
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.drawer_bottom_drawer
import com.github.jershell.shadcn.demoapp.generated.resources.drawer_cancel
import com.github.jershell.shadcn.demoapp.generated.resources.drawer_edit_profile
import com.github.jershell.shadcn.demoapp.generated.resources.drawer_make_changes_to_your_profile_here_click_save_whe
import com.github.jershell.shadcn.demoapp.generated.resources.drawer_open_drawer
import com.github.jershell.shadcn.demoapp.generated.resources.drawer_save
import com.github.jershell.shadcn.demoapp.generated.resources.drawer_saved
import com.github.jershell.shadcn.demoapp.generated.resources.drawer_the_base_scenario_drag_the_panel_or_the_handle_d
import com.github.jershell.shadcn.demoapp.generated.resources.drawer_the_panel_follows_the_pointer_while_dragging_and_2
import com.github.jershell.shadcn.demoapp.generated.resources.drawer_usage
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoDrawer() {
    val s_drawer_saved = stringResource(Res.string.drawer_saved)
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.drawer_bottom_drawer),
            description = stringResource(Res.string.drawer_the_base_scenario_drag_the_panel_or_the_handle_d),
        ) {
            var open by remember { mutableStateOf(false) }
            Button(onClick = { open = true }) {
                ButtonText(stringResource(Res.string.drawer_open_drawer))
            }
            Drawer(
                open = open,
                onOpenChange = { open = it },
            ) {
                DrawerHeader {
                    DrawerTitle(stringResource(Res.string.drawer_edit_profile))
                    DrawerDescription(stringResource(Res.string.drawer_make_changes_to_your_profile_here_click_save_whe))
                }
                P(
                    stringResource(Res.string.drawer_the_panel_follows_the_pointer_while_dragging_and_2),
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                DrawerFooter {
                    Button(onClick = { open = false }, variant = ButtonVariant.Outline) {
                        ButtonText(stringResource(Res.string.drawer_cancel))
                    }
                    Button(onClick = { Toast(s_drawer_saved); open = false }) {
                        ButtonText(stringResource(Res.string.drawer_save))
                    }
                }
            }
        }

        P(stringResource(Res.string.drawer_usage))
        InlineCode(
            text = """
                var open by remember { mutableStateOf(false) }
                Button(onClick = { open = true }) { ButtonText("Open") }
                Drawer(open = open, onOpenChange = { open = it }) {
                    DrawerHeader {
                        DrawerTitle("Edit profile")
                        DrawerDescription("Make changes...")
                    }
                    DrawerFooter {
                        Button(onClick = { open = false }) { ButtonText("Save") }
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
