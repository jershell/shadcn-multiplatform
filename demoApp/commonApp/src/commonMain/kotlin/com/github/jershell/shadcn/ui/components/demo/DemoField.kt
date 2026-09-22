package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.CreditCard
import com.composables.icons.lucide.MapPin
import com.github.jershell.shadcn.components.field.Field
import com.github.jershell.shadcn.components.field.FieldContent
import com.github.jershell.shadcn.components.field.FieldDescription
import com.github.jershell.shadcn.components.field.FieldError
import com.github.jershell.shadcn.components.field.FieldGroup
import com.github.jershell.shadcn.components.field.FieldLabel
import com.github.jershell.shadcn.components.field.FieldLegend
import com.github.jershell.shadcn.components.field.FieldOrientation
import com.github.jershell.shadcn.components.field.FieldSeparator
import com.github.jershell.shadcn.components.field.FieldSet
import com.github.jershell.shadcn.components.field.FieldTitle
import com.github.jershell.shadcn.components.input.Input
import com.github.jershell.shadcn.components.switch.Switch
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.field_221b_baker_street
import com.github.jershell.shadcn.demoapp.generated.resources.field_at_least_3_characters
import com.github.jershell.shadcn.demoapp.generated.resources.field_billing_address
import com.github.jershell.shadcn.demoapp.generated.resources.field_choose_a_unique_name_for_your_profile_url
import com.github.jershell.shadcn.demoapp.generated.resources.field_city
import com.github.jershell.shadcn.demoapp.generated.resources.field_email
import com.github.jershell.shadcn.demoapp.generated.resources.field_field_label_input_description_error
import com.github.jershell.shadcn.demoapp.generated.resources.field_fieldgroup_fieldset_and_fieldseparator
import com.github.jershell.shadcn.demoapp.generated.resources.field_groups_stack_fields_with_wide_gaps_the_separator
import com.github.jershell.shadcn.demoapp.generated.resources.field_horizontal_with_fieldcontent
import com.github.jershell.shadcn.demoapp.generated.resources.field_london
import com.github.jershell.shadcn.demoapp.generated.resources.field_nw1_6xe
import com.github.jershell.shadcn.demoapp.generated.resources.field_or_continue_with
import com.github.jershell.shadcn.demoapp.generated.resources.field_plain_text_overloads_render_the_standard_pieces
import com.github.jershell.shadcn.demoapp.generated.resources.field_push_notifications
import com.github.jershell.shadcn.demoapp.generated.resources.field_receive_push_notifications_for_every_message
import com.github.jershell.shadcn.demoapp.generated.resources.field_reuse_the_shipping_address_for_billing
import com.github.jershell.shadcn.demoapp.generated.resources.field_same_as_shipping_address
import com.github.jershell.shadcn.demoapp.generated.resources.field_slots
import com.github.jershell.shadcn.demoapp.generated.resources.field_street
import com.github.jershell.shadcn.demoapp.generated.resources.field_switch_rows_fieldcontent_keeps_title_and_descrip
import com.github.jershell.shadcn.demoapp.generated.resources.field_text_slots
import com.github.jershell.shadcn.demoapp.generated.resources.field_toggle_validity
import com.github.jershell.shadcn.demoapp.generated.resources.field_usage
import com.github.jershell.shadcn.demoapp.generated.resources.field_username
import com.github.jershell.shadcn.demoapp.generated.resources.field_we_will_send_confirmations_to_this_address
import com.github.jershell.shadcn.demoapp.generated.resources.field_you_example_com
import com.github.jershell.shadcn.demoapp.generated.resources.field_zip
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoField() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.field_slots),
            description = stringResource(Res.string.field_field_label_input_description_error),
        ) {
            var value by remember { mutableStateOf("") }
            Field(
                label = { FieldLabel(stringResource(Res.string.field_username)) },
                description = { FieldDescription(stringResource(Res.string.field_choose_a_unique_name_for_your_profile_url)) },
                error = {
                    if (value.length < 3) {
                        FieldError(stringResource(Res.string.field_at_least_3_characters))
                    }
                },
                isInvalid = value.length < 3,
            ) {
                Input(
                    state = rememberTextFieldState(),
                    placeholder = "jershell",
                )
            }
        }

        DemoSection(
            title = stringResource(Res.string.field_text_slots),
            description = stringResource(Res.string.field_plain_text_overloads_render_the_standard_pieces),
        ) {
            var invalid by remember { mutableStateOf(true) }
            Field(
                label = stringResource(Res.string.field_email),
                description = stringResource(Res.string.field_we_will_send_confirmations_to_this_address),
                error = if (invalid) "This email is already taken." else null,
                isInvalid = invalid,
            ) {
                Input(
                    state = rememberTextFieldState(),
                    placeholder = stringResource(Res.string.field_you_example_com),
                )
            }
            Field(
                label = stringResource(Res.string.field_toggle_validity),
                input = {
                    Switch(
                        checked = invalid,
                        onCheckedChange = { invalid = it },
                    )
                },
            )
        }

        DemoSection(
            title = stringResource(Res.string.field_horizontal_with_fieldcontent),
            description = stringResource(Res.string.field_switch_rows_fieldcontent_keeps_title_and_descrip),
        ) {
            var notifications by remember { mutableStateOf(true) }
            Field(
                orientation = FieldOrientation.Horizontal,
                label = {
                    FieldContent {
                        FieldTitle(stringResource(Res.string.field_push_notifications), icon = Lucide.CreditCard)
                        FieldDescription(stringResource(Res.string.field_receive_push_notifications_for_every_message))
                    }
                },
            ) {
                Switch(
                    checked = notifications,
                    onCheckedChange = { notifications = it },
                )
            }
        }

        DemoSection(
            title = stringResource(Res.string.field_fieldgroup_fieldset_and_fieldseparator),
            description = stringResource(Res.string.field_groups_stack_fields_with_wide_gaps_the_separator),
        ) {
            FieldSet {
                FieldLegend(stringResource(Res.string.field_billing_address))
                FieldGroup {
                    Field(
                        label = stringResource(Res.string.field_street),
                    ) {
                        Input(state = rememberTextFieldState(), placeholder = stringResource(Res.string.field_221b_baker_street))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Field(
                            label = stringResource(Res.string.field_city),
                            modifier = Modifier.weight(1f),
                        ) {
                            Input(state = rememberTextFieldState(), placeholder = stringResource(Res.string.field_london))
                        }
                        Field(
                            label = stringResource(Res.string.field_zip),
                            modifier = Modifier.weight(1f),
                        ) {
                            Input(state = rememberTextFieldState(), placeholder = stringResource(Res.string.field_nw1_6xe))
                        }
                    }
                    FieldSeparator(text = stringResource(Res.string.field_or_continue_with))
                    Field(
                        orientation = FieldOrientation.Horizontal,
                        label = {
                            FieldContent {
                                FieldTitle(stringResource(Res.string.field_same_as_shipping_address), icon = Lucide.MapPin)
                                FieldDescription(stringResource(Res.string.field_reuse_the_shipping_address_for_billing))
                            }
                        },
                    ) {
                        Switch(
                            checked = true,
                            onCheckedChange = { },
                        )
                    }
                }
            }
        }

        P(stringResource(Res.string.field_usage))
        InlineCode(
            text = """
                Field(
                    label = "Email",
                    description = "We will send a confirmation.",
                    error = if (isError) "Already taken." else null,
                    isInvalid = isError,
                ) {
                    Input(state = state, placeholder = "you@example.com")
                }

                // composable slots for custom pieces:
                Field(
                    label = { FieldLabel("Username") },
                    description = { FieldDescription("...") },
                ) {
                    Input(state = state)
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
