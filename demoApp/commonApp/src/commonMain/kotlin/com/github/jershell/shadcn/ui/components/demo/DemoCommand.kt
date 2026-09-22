package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Settings
import com.composables.icons.lucide.SquareArrowOutUpRight
import com.composables.icons.lucide.Trash2
import com.composables.icons.lucide.User
import com.github.jershell.shadcn.components.button.Button
import com.github.jershell.shadcn.components.button.ButtonText
import com.github.jershell.shadcn.components.command.Command
import com.github.jershell.shadcn.components.command.CommandGroupScopeSpec
import com.github.jershell.shadcn.components.command.CommandSpec
import com.github.jershell.shadcn.components.dialog.Dialog
import com.github.jershell.shadcn.components.icon.toShadcnIcon
import com.github.jershell.shadcn.components.toast.Toast
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.command_project_deleted
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.command_a_modal_palette_centered_on_screen_like_the_shad
import com.github.jershell.shadcn.demoapp.generated.resources.command_actions
import com.github.jershell.shadcn.demoapp.generated.resources.command_basic
import com.github.jershell.shadcn.demoapp.generated.resources.command_calendar_opened
import com.github.jershell.shadcn.demoapp.generated.resources.command_delete_project
import com.github.jershell.shadcn.demoapp.generated.resources.command_open_command_palette
import com.github.jershell.shadcn.demoapp.generated.resources.command_open_profile
import com.github.jershell.shadcn.demoapp.generated.resources.command_open_settings
import com.github.jershell.shadcn.demoapp.generated.resources.command_profile_opened
import com.github.jershell.shadcn.demoapp.generated.resources.command_s
import com.github.jershell.shadcn.demoapp.generated.resources.command_settings_opened
import com.github.jershell.shadcn.demoapp.generated.resources.command_share
import com.github.jershell.shadcn.demoapp.generated.resources.command_shared
import com.github.jershell.shadcn.demoapp.generated.resources.command_show_calendar
import com.github.jershell.shadcn.demoapp.generated.resources.command_text
import com.github.jershell.shadcn.demoapp.generated.resources.command_tools
import com.github.jershell.shadcn.demoapp.generated.resources.command_usage
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoCommand() {
    val s_command_project_deleted = stringResource(Res.string.command_project_deleted)
    val s_command_delete_project = stringResource(Res.string.command_delete_project)
    val s_command_shared = stringResource(Res.string.command_shared)
    val s_command_s = stringResource(Res.string.command_s)
    val s_command_share = stringResource(Res.string.command_share)
    val s_command_calendar_opened = stringResource(Res.string.command_calendar_opened)
    val s_command_show_calendar = stringResource(Res.string.command_show_calendar)
    val s_command_tools = stringResource(Res.string.command_tools)
    val s_command_profile_opened = stringResource(Res.string.command_profile_opened)
    val s_command_open_profile = stringResource(Res.string.command_open_profile)
    val s_command_settings_opened = stringResource(Res.string.command_settings_opened)
    val s_command_text = stringResource(Res.string.command_text)
    val s_command_open_settings = stringResource(Res.string.command_open_settings)
    val s_command_actions = stringResource(Res.string.command_actions)
    var open by remember { mutableStateOf(false) }
    val state = rememberTextFieldState()

    val groups = remember {
        listOf(
            CommandGroupScopeSpec(
                title = s_command_actions,
                items = listOf(
                    CommandSpec(
                        key = "settings",
                        label = s_command_open_settings,
                        icon = Lucide.Settings.toShadcnIcon(),
                        shortcut = s_command_text,
                        onSelect = {
                            Toast(s_command_settings_opened)
                            open = false
                        },
                    ),
                    CommandSpec(
                        key = "profile",
                        label = s_command_open_profile,
                        icon = Lucide.User.toShadcnIcon(),
                        onSelect = {
                            Toast(s_command_profile_opened)
                            open = false
                        },
                    ),
                ),
            ),
            CommandGroupScopeSpec(
                title = s_command_tools,
                items = listOf(
                    CommandSpec(
                        key = "calendar",
                        label = s_command_show_calendar,
                        icon = Lucide.Calendar.toShadcnIcon(),
                        onSelect = {
                            Toast(s_command_calendar_opened)
                            open = false
                        },
                    ),
                    CommandSpec(
                        key = "share",
                        label = s_command_share,
                        icon = Lucide.SquareArrowOutUpRight.toShadcnIcon(),
                        shortcut = s_command_s,
                        onSelect = {
                            Toast(s_command_shared)
                            open = false
                        },
                    ),
                    CommandSpec(
                        key = "delete",
                        label = s_command_delete_project,
                        icon = Lucide.Trash2.toShadcnIcon(),
                        onSelect = {
                            Toast.destructive(s_command_project_deleted)
                            open = false
                        },
                    ),
                ),
            ),
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.command_basic),
            description = stringResource(Res.string.command_a_modal_palette_centered_on_screen_like_the_shad),
        ) {
            Button(onClick = { open = true }) {
                ButtonText(stringResource(Res.string.command_open_command_palette))
            }
            Dialog(
                open = open,
                onOpenChange = { open = it },
                showCloseButton = false,
                contentPadding = 0.dp,
                modifier = Modifier.width(480.dp),
            ) {
                Command(
                    groups = groups,
                    state = state,
                    autoFocus = true,
                )
            }
        }

        Muted(stringResource(Res.string.command_usage))
        InlineCode(
            text = """
                var open by remember { mutableStateOf(false) }
                Button(onClick = { open = true }) {
                    ButtonText("Open Command Palette")
                }
                Dialog(
                    open = open,
                    onOpenChange = { open = it },
                    showCloseButton = false,
                    contentPadding = 0.dp,
                ) {
                    Command(groups = groups, autoFocus = true)
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
