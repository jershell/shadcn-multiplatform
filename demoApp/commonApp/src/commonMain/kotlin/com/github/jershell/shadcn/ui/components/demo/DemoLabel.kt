package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jershell.shadcn.components.label.Label
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.label_a_label_styled_for_form_controls
import com.github.jershell.shadcn.demoapp.generated.resources.label_default
import com.github.jershell.shadcn.demoapp.generated.resources.label_dimmed_when_the_associated_control_is_disabled
import com.github.jershell.shadcn.demoapp.generated.resources.label_disabled
import com.github.jershell.shadcn.demoapp.generated.resources.label_disabled_label
import com.github.jershell.shadcn.demoapp.generated.resources.label_email_address
import com.github.jershell.shadcn.demoapp.generated.resources.label_usage
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoLabel() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.label_default),
            description = stringResource(Res.string.label_a_label_styled_for_form_controls),
        ) {
            Label(text = stringResource(Res.string.label_email_address))
        }

        DemoSection(
            title = stringResource(Res.string.label_disabled),
            description = stringResource(Res.string.label_dimmed_when_the_associated_control_is_disabled),
        ) {
            Label(text = stringResource(Res.string.label_disabled_label), enabled = false)
        }

        Muted(stringResource(Res.string.label_usage))
        InlineCode(
            text = """
                Label(text = "Email address")
                Label(text = "Disabled label", enabled = false)
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
