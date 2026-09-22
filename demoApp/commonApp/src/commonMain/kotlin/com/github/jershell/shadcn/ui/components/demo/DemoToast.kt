package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.button.Button
import com.github.jershell.shadcn.components.button.ButtonText
import com.github.jershell.shadcn.components.button.ButtonVariant
import com.github.jershell.shadcn.components.toast.Toast
import com.github.jershell.shadcn.components.toast.ToastAction
import com.github.jershell.shadcn.components.toast.ToastHost
import com.github.jershell.shadcn.components.toast.ToastManager
import com.github.jershell.shadcn.components.toast.ToastPosition
import com.github.jershell.shadcn.components.toast.ToasterConfig
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.toast_a_toasthost_can_render_its_own_manager_anywhere
import com.github.jershell.shadcn.demoapp.generated.resources.toast_add_toast
import com.github.jershell.shadcn.demoapp.generated.resources.toast_an_optional_action_button_renders_inside_the_toa
import com.github.jershell.shadcn.demoapp.generated.resources.toast_archive_file
import com.github.jershell.shadcn.demoapp.generated.resources.toast_bottom_center
import com.github.jershell.shadcn.demoapp.generated.resources.toast_clear
import com.github.jershell.shadcn.demoapp.generated.resources.toast_column_mode
import com.github.jershell.shadcn.demoapp.generated.resources.toast_default
import com.github.jershell.shadcn.demoapp.generated.resources.toast_destructive
import com.github.jershell.shadcn.demoapp.generated.resources.toast_duration
import com.github.jershell.shadcn.demoapp.generated.resources.toast_event_has_been_created
import com.github.jershell.shadcn.demoapp.generated.resources.toast_file_archived
import com.github.jershell.shadcn.demoapp.generated.resources.toast_file_restored
import com.github.jershell.shadcn.demoapp.generated.resources.toast_hover_the_stack_to_expand
import com.github.jershell.shadcn.demoapp.generated.resources.toast_local_toast_created
import com.github.jershell.shadcn.demoapp.generated.resources.toast_new_toasts_appear_in_front_queue_more_than_the_l
import com.github.jershell.shadcn.demoapp.generated.resources.toast_persistent_notification
import com.github.jershell.shadcn.demoapp.generated.resources.toast_queue_three
import com.github.jershell.shadcn.demoapp.generated.resources.toast_short_1_5s
import com.github.jershell.shadcn.demoapp.generated.resources.toast_short_lived_toast
import com.github.jershell.shadcn.demoapp.generated.resources.toast_something_went_wrong_2
import com.github.jershell.shadcn.demoapp.generated.resources.toast_stacking
import com.github.jershell.shadcn.demoapp.generated.resources.toast_sticky
import com.github.jershell.shadcn.demoapp.generated.resources.toast_the_file_could_not_be_deleted_check_the_permissi
import com.github.jershell.shadcn.demoapp.generated.resources.toast_the_file_has_been_moved_to_the_archive
import com.github.jershell.shadcn.demoapp.generated.resources.toast_toast_stage_local_toasthost_is_anchored_inside_t
import com.github.jershell.shadcn.demoapp.generated.resources.toast_toast_x_created
import com.github.jershell.shadcn.demoapp.generated.resources.toast_toasts_auto_dismiss_after_5_seconds_by_default_p
import com.github.jershell.shadcn.demoapp.generated.resources.toast_top_center
import com.github.jershell.shadcn.demoapp.generated.resources.toast_two_variants_backed_by_design_tokens_default_for
import com.github.jershell.shadcn.demoapp.generated.resources.toast_undo
import com.github.jershell.shadcn.demoapp.generated.resources.toast_usage
import com.github.jershell.shadcn.demoapp.generated.resources.toast_variants
import com.github.jershell.shadcn.demoapp.generated.resources.toast_with_action
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DemoToast() {
    val s_toast_toast_1_created = stringResource(Res.string.toast_toast_x_created, 1)
    val s_toast_toast_2_created = stringResource(Res.string.toast_toast_x_created, 2)
    val s_toast_toast_3_created = stringResource(Res.string.toast_toast_x_created, 3)
    val s_toast_hover_the_stack = stringResource(Res.string.toast_hover_the_stack_to_expand)
    val s_toast_persistent_notification = stringResource(Res.string.toast_persistent_notification)
    val s_toast_short_lived_toast = stringResource(Res.string.toast_short_lived_toast)
    val s_toast_file_restored = stringResource(Res.string.toast_file_restored)
    val s_toast_undo = stringResource(Res.string.toast_undo)
    val s_toast_the_file_has_been_moved_to_the_archive = stringResource(Res.string.toast_the_file_has_been_moved_to_the_archive)
    val s_toast_file_archived = stringResource(Res.string.toast_file_archived)
    val s_toast_the_file_could_not_be_deleted_check_the_permissi = stringResource(Res.string.toast_the_file_could_not_be_deleted_check_the_permissi)
    val s_toast_event_has_been_created = stringResource(Res.string.toast_event_has_been_created)
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.toast_variants),
            description = stringResource(Res.string.toast_two_variants_backed_by_design_tokens_default_for),
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(onClick = {
                    Toast(s_toast_event_has_been_created)
                }) {
                    ButtonText(stringResource(Res.string.toast_default))
                }
                val s_toast_something_went_wrong = stringResource(Res.string.toast_something_went_wrong_2)
                Button(onClick = {
                    Toast.destructive(
                        s_toast_something_went_wrong,
                        description = s_toast_the_file_could_not_be_deleted_check_the_permissi,
                    )
                }) {
                    ButtonText(stringResource(Res.string.toast_destructive))
                }
            }
        }

        DemoSection(
            title = stringResource(Res.string.toast_with_action),
            description = stringResource(Res.string.toast_an_optional_action_button_renders_inside_the_toa),
        ) {
            Button(onClick = {
                Toast(
                    s_toast_file_archived,
                    description = s_toast_the_file_has_been_moved_to_the_archive,
                    action = ToastAction(label = s_toast_undo, onClick = {
                        Toast(s_toast_file_restored)
                    }),
                )
            }) {
                ButtonText(stringResource(Res.string.toast_archive_file))
            }
        }

        DemoSection(
            title = stringResource(Res.string.toast_duration),
            description = stringResource(Res.string.toast_toasts_auto_dismiss_after_5_seconds_by_default_p),
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(onClick = {
                    Toast(s_toast_short_lived_toast, durationMillis = 1500)
                }) {
                    ButtonText(stringResource(Res.string.toast_short_1_5s))
                }
                Button(onClick = {
                    Toast(s_toast_persistent_notification, durationMillis = 0)
                }) {
                    ButtonText(stringResource(Res.string.toast_sticky))
                }
            }
        }

        DemoSection(
            title = stringResource(Res.string.toast_stacking),
            description = stringResource(Res.string.toast_new_toasts_appear_in_front_queue_more_than_the_l),
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(onClick = {
                    repeat(3) { index ->
                        Toast(
                            listOf(
                                s_toast_toast_1_created,
                                s_toast_toast_2_created,
                                s_toast_toast_3_created,
                            )[index],
                            description = if (index == 0) s_toast_hover_the_stack else null,
                        )
                    }
                }) {
                    ButtonText(stringResource(Res.string.toast_queue_three))
                }
                Button(onClick = {
                    Toast.clear()
                }) {
                    ButtonText(stringResource(Res.string.toast_clear))
                }
            }
        }

        DemoSection(
            title = stringResource(Res.string.toast_column_mode),
            description = stringResource(Res.string.toast_a_toasthost_can_render_its_own_manager_anywhere),
        ) {
            val localManager = remember { ToastManager() }
            var localPosition by remember { mutableStateOf(ToastPosition.BottomCenter) }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val s_toast_local_toast_created = stringResource(Res.string.toast_local_toast_created)
                Button(onClick = {
                    localManager.add(s_toast_local_toast_created)
                }) {
                    ButtonText(stringResource(Res.string.toast_add_toast))
                }
                Button(
                    onClick = { localPosition = ToastPosition.BottomCenter },
                    variant = if (localPosition == ToastPosition.BottomCenter) {
                        ButtonVariant.Default
                    } else {
                        ButtonVariant.Outline
                    },
                ) {
                    ButtonText(stringResource(Res.string.toast_bottom_center))
                }
                Button(
                    onClick = { localPosition = ToastPosition.TopCenter },
                    variant = if (localPosition == ToastPosition.TopCenter) {
                        ButtonVariant.Default
                    } else {
                        ButtonVariant.Outline
                    },
                ) {
                    ButtonText(stringResource(Res.string.toast_top_center))
                }
            }

            val borderWidth = Theme[DimProps][DimTokens.borderWidth]
            val borderColor = Theme[ColorProps][ColorTokens.border]
            val muted = Theme[ColorProps][ColorTokens.muted]
            val radius = Theme[DimProps][DimTokens.radiusMd]
            val stageShape = RoundedCornerShape(radius)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
                    .clip(stageShape)
                    .background(muted)
                    .border(borderWidth, borderColor, stageShape),
                contentAlignment = Alignment.Center,
            ) {
                Muted(stringResource(Res.string.toast_toast_stage_local_toasthost_is_anchored_inside_t))
                ToastHost(
                    manager = localManager,
                    config = ToasterConfig(
                        position = localPosition,
                        stacked = false,
                    ),
                )
            }
        }

        Muted(stringResource(Res.string.toast_usage))
        InlineCode(
            text = """
                // Global facade, renders into the default ToastHost inside ShadcnUI
                Toast("Event has been created", description = "Monday at 10:00")
                Toast.destructive(stringResource(Res.string.toast_something_went_wrong))
                Toast(
                    "File archived",
                    action = ToastAction(label = "Undo", onClick = { Toast("Restored") }),
                )
                Toast("Persistent notification", durationMillis = 0)
                Toast.close(id)
                Toast.clear()

                // Custom host with its own manager
                val manager = remember { ToastManager() }
                ToastHost(
                    manager = manager,
                    config = ToasterConfig(
                        position = ToastPosition.BottomCenter,
                        stacked = false,
                    ),
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
// MARKER-XYZ
