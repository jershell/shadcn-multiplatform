package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jershell.shadcn.components.button.ButtonText
import com.github.jershell.shadcn.components.textarea.Textarea
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.textarea_280_characters_left
import com.github.jershell.shadcn.demoapp.generated.resources.textarea_default
import com.github.jershell.shadcn.demoapp.generated.resources.textarea_disabled
import com.github.jershell.shadcn.demoapp.generated.resources.textarea_disabled_textarea
import com.github.jershell.shadcn.demoapp.generated.resources.textarea_displays_a_form_textarea_field_or_a_component_th
import com.github.jershell.shadcn.demoapp.generated.resources.textarea_send
import com.github.jershell.shadcn.demoapp.generated.resources.textarea_type_your_message_here
import com.github.jershell.shadcn.demoapp.generated.resources.textarea_usage
import com.github.jershell.shadcn.demoapp.generated.resources.textarea_with_button
import com.github.jershell.shadcn.demoapp.generated.resources.textarea_write_a_message
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoTextarea() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(title = stringResource(Res.string.textarea_default)) {
            val state = rememberTextFieldState()
            Textarea(
                state = state,
                placeholder = stringResource(Res.string.textarea_type_your_message_here),
            )
        }

        DemoSection(title = stringResource(Res.string.textarea_disabled)) {
            val state = rememberTextFieldState()
            Textarea(
                state = state,
                placeholder = stringResource(Res.string.textarea_disabled_textarea),
                enabled = false,
            )
        }

        DemoSection(title = stringResource(Res.string.textarea_with_button)) {
            val state = rememberTextFieldState()
            Textarea(
                state = state,
                placeholder = stringResource(Res.string.textarea_write_a_message),
                bottomAddon = {
                    Text(stringResource(Res.string.textarea_280_characters_left))
                    Button(onClick = {}) {
                        ButtonText(stringResource(Res.string.textarea_send))
                    }
                },
            )
        }

        Muted(stringResource(Res.string.textarea_usage))
        InlineCode(
            text = """
                val state = rememberTextFieldState()
                Textarea(state = state, placeholder = "Type your message here...")
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
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        H4(title)
        P(stringResource(Res.string.textarea_displays_a_form_textarea_field_or_a_component_th))
        content()
    }
}
