package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.rememberTextFieldState
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
import com.github.jershell.shadcn.components.dialog.Dialog
import com.github.jershell.shadcn.components.dialog.DialogDescription
import com.github.jershell.shadcn.components.dialog.DialogFooter
import com.github.jershell.shadcn.components.dialog.DialogHeader
import com.github.jershell.shadcn.components.dialog.DialogTitle
import com.github.jershell.shadcn.components.dialog.Dialogs
import com.github.jershell.shadcn.components.input.Input
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_a_confirmation_with_cancel_and_destructive_confi
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_a_local_state_driven_dialog_the_panel_is_centere
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_account_deleted
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_alert
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_and_the_last_one
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_basic
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_cancel
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_close
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_closes_before_the_second_dialog_appears
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_confirm
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_create
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_create_account
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_delete
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_delete_account
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_delete_account_2
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_dialogs_can_host_any_content_including_form_inpu
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_edit_profile
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_fill_in_the_details_below_to_create_a_new_accoun
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_first
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_global_imperative_dialogs_render_into_the_dialog
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_just_kidding
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_make_changes_to_your_profile_here_click_save_whe
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_name
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_open_dialog
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_open_form_dialog
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_opens_right_after_the_first_one
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_queue
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_queue_three_dialogs
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_save_changes
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_saved
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_second
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_show_alert
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_the_host_renders_dialogs_one_at_a_time_fifo_the
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_third
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_this_will_permanently_remove_your_account_and_al
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_usage
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_username
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_with_form
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_you_can_also_dismiss_this_dialog_with_the_close
import com.github.jershell.shadcn.demoapp.generated.resources.dialog_your_changes_have_been_stored
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DemoDialog() {
    val s_dialog_and_the_last_one = stringResource(Res.string.dialog_and_the_last_one)
    val s_dialog_second = stringResource(Res.string.dialog_second)
    val s_dialog_first = stringResource(Res.string.dialog_first)
    val s_dialog_just_kidding = stringResource(Res.string.dialog_just_kidding)
    val s_dialog_closes_before_the_second = stringResource(Res.string.dialog_closes_before_the_second_dialog_appears)
    val s_dialog_opens_right_after_the_first = stringResource(Res.string.dialog_opens_right_after_the_first_one)
    val s_dialog_third = stringResource(Res.string.dialog_third)
    val s_dialog_account_deleted = stringResource(Res.string.dialog_account_deleted)
    val s_dialog_delete = stringResource(Res.string.dialog_delete)
    val s_dialog_this_will_permanently_remove_your_account_and_al = stringResource(Res.string.dialog_this_will_permanently_remove_your_account_and_al)
    val s_dialog_delete_account = stringResource(Res.string.dialog_delete_account)
    val s_dialog_your_changes_have_been_stored = stringResource(Res.string.dialog_your_changes_have_been_stored)
    val s_dialog_saved = stringResource(Res.string.dialog_saved)
    var basicOpen by remember { mutableStateOf(false) }
    var formOpen by remember { mutableStateOf(false) }
    val nameState = rememberTextFieldState()
    val usernameState = rememberTextFieldState()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.dialog_basic),
            description = stringResource(Res.string.dialog_a_local_state_driven_dialog_the_panel_is_centere),
        ) {
            Button(onClick = { basicOpen = true }) {
                ButtonText(stringResource(Res.string.dialog_open_dialog))
            }
            Dialog(
                open = basicOpen,
                onOpenChange = { basicOpen = it },
            ) {
                DialogHeader {
                    DialogTitle(stringResource(Res.string.dialog_edit_profile))
                    DialogDescription(stringResource(Res.string.dialog_make_changes_to_your_profile_here_click_save_whe))
                }
                P(stringResource(Res.string.dialog_you_can_also_dismiss_this_dialog_with_the_close))
                DialogFooter {
                    Button(onClick = { basicOpen = false }, variant = ButtonVariant.Outline) {
                        ButtonText(stringResource(Res.string.dialog_close))
                    }
                    Button(onClick = { basicOpen = false }) {
                        ButtonText(stringResource(Res.string.dialog_save_changes))
                    }
                }
            }
        }

        DemoSection(
            title = stringResource(Res.string.dialog_with_form),
            description = stringResource(Res.string.dialog_dialogs_can_host_any_content_including_form_inpu),
        ) {
            Button(onClick = { formOpen = true }) {
                ButtonText(stringResource(Res.string.dialog_open_form_dialog))
            }
            Dialog(
                open = formOpen,
                onOpenChange = { formOpen = it },
            ) {
                DialogHeader {
                    DialogTitle(stringResource(Res.string.dialog_create_account))
                    DialogDescription(stringResource(Res.string.dialog_fill_in_the_details_below_to_create_a_new_accoun))
                }
                Input(
                    state = nameState,
                    placeholder = stringResource(Res.string.dialog_name),
                )
                Input(
                    state = usernameState,
                    placeholder = stringResource(Res.string.dialog_username),
                )
                DialogFooter {
                    Button(onClick = { formOpen = false }, variant = ButtonVariant.Outline) {
                        ButtonText(stringResource(Res.string.dialog_cancel))
                    }
                    Button(onClick = { formOpen = false }) {
                        ButtonText(stringResource(Res.string.dialog_create))
                    }
                }
            }
        }

        DemoSection(
            title = stringResource(Res.string.dialog_alert),
            description = stringResource(Res.string.dialog_global_imperative_dialogs_render_into_the_dialog),
        ) {
            Button(onClick = {
                Dialogs.alert(
                    title = s_dialog_saved,
                    description = s_dialog_your_changes_have_been_stored,
                )
            }) {
                ButtonText(stringResource(Res.string.dialog_show_alert))
            }
        }

        DemoSection(
            title = stringResource(Res.string.dialog_confirm),
            description = stringResource(Res.string.dialog_a_confirmation_with_cancel_and_destructive_confi),
        ) {
            Button(onClick = {
                Dialogs.confirm(
                    title = s_dialog_delete_account,
                    description = s_dialog_this_will_permanently_remove_your_account_and_al,
                    confirmLabel = s_dialog_delete,
                    isDestructive = true,
                    onConfirm = {
                        Dialogs.alert(s_dialog_account_deleted, description = s_dialog_just_kidding)
                    },
                )
            }) {
                ButtonText(stringResource(Res.string.dialog_delete_account_2))
            }
        }

        DemoSection(
            title = stringResource(Res.string.dialog_queue),
            description = stringResource(Res.string.dialog_the_host_renders_dialogs_one_at_a_time_fifo_the),
        ) {
            Button(onClick = {
                Dialogs.alert(s_dialog_first, description = s_dialog_closes_before_the_second)
                Dialogs.alert(s_dialog_second, description = s_dialog_opens_right_after_the_first)
                Dialogs.confirm(s_dialog_third, description = s_dialog_and_the_last_one)
            }) {
                ButtonText(stringResource(Res.string.dialog_queue_three_dialogs))
            }
        }

        Muted(stringResource(Res.string.dialog_usage))
        InlineCode(
            text = """
                // Local, state-driven
                var open by remember { mutableStateOf(false) }
                Dialog(open = open, onOpenChange = { open = it }) {
                    DialogHeader {
                        DialogTitle("Edit profile")
                        DialogDescription("Make changes to your profile here.")
                    }
                    DialogFooter {
                        Button(onClick = { open = false }, variant = ButtonVariant.Outline) {
                            ButtonText("Cancel")
                        }
                        Button(onClick = { open = false }) {
                            ButtonText("Save Changes")
                        }
                    }
                }

                // Global imperative (renders into DialogHost inside ShadcnUI)
                Dialogs.alert("Saved", "Your changes have been stored.")
                Dialogs.confirm(
                    title = "Delete account",
                    confirmLabel = "Delete",
                    isDestructive = true,
                    onConfirm = { deleteAccount() },
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
    description: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        H4(title)
        P(description)
        content()
    }
}
