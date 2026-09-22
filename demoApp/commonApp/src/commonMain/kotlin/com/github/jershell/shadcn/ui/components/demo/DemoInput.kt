package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.input.Input
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.input_default
import com.github.jershell.shadcn.demoapp.generated.resources.input_disabled
import com.github.jershell.shadcn.demoapp.generated.resources.input_displays_a_form_input_field_or_a_component_that
import com.github.jershell.shadcn.demoapp.generated.resources.input_email
import com.github.jershell.shadcn.demoapp.generated.resources.input_invalid
import com.github.jershell.shadcn.demoapp.generated.resources.input_invalid_email
import com.github.jershell.shadcn.demoapp.generated.resources.input_please_enter_a_valid_email_address
import com.github.jershell.shadcn.demoapp.generated.resources.input_usage
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoInput() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(title = stringResource(Res.string.input_default)) {
            val state = rememberTextFieldState()
            Input(
                state = state,
                placeholder = stringResource(Res.string.input_email),
            )
        }

        DemoSection(title = stringResource(Res.string.input_disabled)) {
            val state = rememberTextFieldState()
            Input(
                state = state,
                placeholder = stringResource(Res.string.input_disabled),
                enabled = false,
            )
        }

        DemoSection(title = stringResource(Res.string.input_invalid)) {
            val state = rememberTextFieldState(stringResource(Res.string.input_invalid_email))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Input(
                    state = state,
                    placeholder = stringResource(Res.string.input_email),
                    isInvalid = true,
                )
                P(
                    text = stringResource(Res.string.input_please_enter_a_valid_email_address),
                    color = Theme[ColorProps][ColorTokens.destructive],
                )
            }
        }

        Muted(stringResource(Res.string.input_usage))
        InlineCode(
            text = """
                val state = rememberTextFieldState()
                Input(state = state, placeholder = "Email")
                Input(state = state, isInvalid = true)
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
        P(stringResource(Res.string.input_displays_a_form_input_field_or_a_component_that))
        content()
    }
}
