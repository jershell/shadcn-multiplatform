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
import com.github.jershell.shadcn.components.button.Button
import com.github.jershell.shadcn.components.button.ButtonText
import com.github.jershell.shadcn.components.drawer.Drawer
import com.github.jershell.shadcn.components.drawer.DrawerDescription
import com.github.jershell.shadcn.components.drawer.DrawerFooter
import com.github.jershell.shadcn.components.drawer.DrawerHeader
import com.github.jershell.shadcn.components.drawer.DrawerSide
import com.github.jershell.shadcn.components.drawer.DrawerTitle
import com.github.jershell.shadcn.components.toast.Toast
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.sheet_a_slide_over_panel_from_the_left_edge
import com.github.jershell.shadcn.demoapp.generated.resources.sheet_configure_what_you_get_notified_about
import com.github.jershell.shadcn.demoapp.generated.resources.sheet_done
import com.github.jershell.shadcn.demoapp.generated.resources.sheet_navigation
import com.github.jershell.shadcn.demoapp.generated.resources.sheet_notifications
import com.github.jershell.shadcn.demoapp.generated.resources.sheet_open_navigation
import com.github.jershell.shadcn.demoapp.generated.resources.sheet_open_notifications
import com.github.jershell.shadcn.demoapp.generated.resources.sheet_preferences_saved
import com.github.jershell.shadcn.demoapp.generated.resources.sheet_save_changes
import com.github.jershell.shadcn.demoapp.generated.resources.sheet_sheet_from_the_left
import com.github.jershell.shadcn.demoapp.generated.resources.sheet_sheet_from_the_right
import com.github.jershell.shadcn.demoapp.generated.resources.sheet_sheet_is_a_drawer_configured_to_slide_from_a_sid
import com.github.jershell.shadcn.demoapp.generated.resources.sheet_the_same_configuration_sliding_from_the_left_edg
import com.github.jershell.shadcn.demoapp.generated.resources.sheet_usage
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoSheet() {
    val s_sheet_preferences_saved = stringResource(Res.string.sheet_preferences_saved)
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.sheet_sheet_from_the_right),
            description = stringResource(Res.string.sheet_sheet_is_a_drawer_configured_to_slide_from_a_sid),
        ) {
            var open by remember { mutableStateOf(false) }
            Button(onClick = { open = true }) {
                ButtonText(stringResource(Res.string.sheet_open_notifications))
            }
            Drawer(
                open = open,
                onOpenChange = { open = it },
                side = DrawerSide.Right,
                draggable = false,
                showDragHandle = false,
                showCloseButton = true,
            ) {
                DrawerHeader {
                    DrawerTitle(stringResource(Res.string.sheet_notifications))
                    DrawerDescription(stringResource(Res.string.sheet_configure_what_you_get_notified_about))
                }
                DrawerFooter {
                    Button(onClick = { Toast(s_sheet_preferences_saved); open = false }) {
                        ButtonText(stringResource(Res.string.sheet_save_changes))
                    }
                }
            }
        }

        DemoSection(
            title = stringResource(Res.string.sheet_sheet_from_the_left),
            description = stringResource(Res.string.sheet_the_same_configuration_sliding_from_the_left_edg),
        ) {
            var open by remember { mutableStateOf(false) }
            Button(onClick = { open = true }) {
                ButtonText(stringResource(Res.string.sheet_open_navigation))
            }
            Drawer(
                open = open,
                onOpenChange = { open = it },
                side = DrawerSide.Left,
                draggable = false,
                showDragHandle = false,
                showCloseButton = true,
            ) {
                DrawerHeader {
                    DrawerTitle(stringResource(Res.string.sheet_navigation))
                    DrawerDescription(stringResource(Res.string.sheet_a_slide_over_panel_from_the_left_edge))
                }
                DrawerFooter {
                    Button(onClick = { open = false }) {
                        ButtonText(stringResource(Res.string.sheet_done))
                    }
                }
            }
        }

        P(stringResource(Res.string.sheet_usage))
        InlineCode(
            text = """
                var open by remember { mutableStateOf(false) }
                Button(onClick = { open = true }) { ButtonText("Open") }
                Drawer(
                    open = open,
                    onOpenChange = { open = it },
                    side = DrawerSide.Right,
                    draggable = false,
                    showDragHandle = false,
                    showCloseButton = true,
                ) {
                    DrawerHeader {
                        DrawerTitle("Notifications")
                        DrawerDescription("Configure what you get notified about.")
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
