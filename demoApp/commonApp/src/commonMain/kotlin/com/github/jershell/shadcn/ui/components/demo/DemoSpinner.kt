package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.button.Button
import com.github.jershell.shadcn.components.button.ButtonText
import com.github.jershell.shadcn.components.button.ButtonVariant
import com.github.jershell.shadcn.components.input.Input
import com.github.jershell.shadcn.components.spinner.Spinner
import com.github.jershell.shadcn.components.spinner.SpinnerVariant
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.ic_cyclone
import com.github.jershell.shadcn.demoapp.generated.resources.ic_rotate_right
import com.github.jershell.shadcn.demoapp.generated.resources.spinner_an_indicator_that_can_be_used_to_show_a_loading
import com.github.jershell.shadcn.demoapp.generated.resources.spinner_basic
import com.github.jershell.shadcn.demoapp.generated.resources.spinner_cancel
import com.github.jershell.shadcn.demoapp.generated.resources.spinner_custom_icons
import com.github.jershell.shadcn.demoapp.generated.resources.spinner_empty_state
import com.github.jershell.shadcn.demoapp.generated.resources.spinner_in_buttons
import com.github.jershell.shadcn.demoapp.generated.resources.spinner_in_input
import com.github.jershell.shadcn.demoapp.generated.resources.spinner_loading
import com.github.jershell.shadcn.demoapp.generated.resources.spinner_please_wait
import com.github.jershell.shadcn.demoapp.generated.resources.spinner_please_wait_while_we_process_your_request_do_not
import com.github.jershell.shadcn.demoapp.generated.resources.spinner_processing_your_request
import com.github.jershell.shadcn.demoapp.generated.resources.spinner_search_in
import com.github.jershell.shadcn.demoapp.generated.resources.spinner_show_loading_state_inside_button_content
import com.github.jershell.shadcn.demoapp.generated.resources.spinner_standalone_loading_block_with_explanatory_text
import com.github.jershell.shadcn.demoapp.generated.resources.spinner_usage
import com.github.jershell.shadcn.demoapp.generated.resources.spinner_use_spinner_in_input_addon_scope
import com.github.jershell.shadcn.demoapp.generated.resources.spinner_use_your_own_drawable_resources_as_spinner_icons
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoSpinner() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.spinner_basic),
            description = stringResource(Res.string.spinner_an_indicator_that_can_be_used_to_show_a_loading),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Spinner()
                Spinner(modifier = Modifier.size(24.dp))
                Spinner(variant = SpinnerVariant.Alt)
            }
        }

        DemoSection(
            title = stringResource(Res.string.spinner_custom_icons),
            description = stringResource(Res.string.spinner_use_your_own_drawable_resources_as_spinner_icons),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Spinner(
                    painter = painterResource(Res.drawable.ic_cyclone),
                    size = 20.dp,
                )
                Spinner(
                    painter = painterResource(Res.drawable.ic_rotate_right),
                    size = 20.dp,
                )
            }
        }

        DemoSection(
            title = stringResource(Res.string.spinner_in_buttons),
            description = stringResource(Res.string.spinner_show_loading_state_inside_button_content),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {}) {
                    Spinner(tint = Theme[ColorProps][ColorTokens.primaryForeground])
                    ButtonText(stringResource(Res.string.spinner_loading))
                }
                Button(onClick = {}, variant = ButtonVariant.Outline, enabled = false) {
                    Spinner()
                    ButtonText(stringResource(Res.string.spinner_please_wait))
                }
            }
        }

        DemoSection(
            title = stringResource(Res.string.spinner_in_input),
            description = stringResource(Res.string.spinner_use_spinner_in_input_addon_scope),
        ) {
            val state = rememberTextFieldState(stringResource(Res.string.spinner_search_in))
            Input(
                state = state,
                placeholder = stringResource(Res.string.spinner_search_in),
                endAddon = {
                    Spinner()
                },
            )
        }

        DemoSection(
            title = stringResource(Res.string.spinner_empty_state),
            description = stringResource(Res.string.spinner_standalone_loading_block_with_explanatory_text),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Spinner(modifier = Modifier.size(20.dp))
                    P(stringResource(Res.string.spinner_processing_your_request))
                }
                Muted(stringResource(Res.string.spinner_please_wait_while_we_process_your_request_do_not))
                Button(onClick = {}, variant = ButtonVariant.Outline) {
                    ButtonText(stringResource(Res.string.spinner_cancel))
                }
            }
        }

        Muted(stringResource(Res.string.spinner_usage))
        InlineCode(
            text = """
                Spinner()
                
                Spinner(
                    painter = painterResource(Res.drawable.ic_cyclone),
                    size = 20.dp,
                )
                
                Button(onClick = {}) {
                    Spinner()
                    ButtonText("Loading...")
                }
                
                Input(
                    state = state,
                    endAddon = { Spinner() },
                )
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
