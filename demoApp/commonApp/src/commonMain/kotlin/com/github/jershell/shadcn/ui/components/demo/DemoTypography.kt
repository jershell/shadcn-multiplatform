package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.runtime.Composable
import com.github.jershell.shadcn.components.typography.H1
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Lead
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.components.typography.UnorderedList
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.typography_bar
import com.github.jershell.shadcn.demoapp.generated.resources.typography_enter_your_email
import com.github.jershell.shadcn.demoapp.generated.resources.typography_foo
import com.github.jershell.shadcn.demoapp.generated.resources.typography_lorem_ipsum_dolor_sit_amet_consectetur_adipiscin
import com.github.jershell.shadcn.demoapp.generated.resources.typography_once_upon_a_time
import com.github.jershell.shadcn.demoapp.generated.resources.typography_radix_ui_react_alert_dialog
import com.github.jershell.shadcn.demoapp.generated.resources.typography_taxing_laughter
import com.github.jershell.shadcn.demoapp.generated.resources.typography_usage
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoTypography() {
    H1(text = stringResource(Res.string.typography_taxing_laughter))
    Lead(text = stringResource(Res.string.typography_once_upon_a_time))
    Muted(text = stringResource(Res.string.typography_enter_your_email))
    InlineCode(
        text = stringResource(Res.string.typography_radix_ui_react_alert_dialog),
        selected = false,
        selectEnabled = true,
        copyEnabled = true,
    )
    P(stringResource(Res.string.typography_lorem_ipsum_dolor_sit_amet_consectetur_adipiscin))
    UnorderedList(items = listOf(stringResource(Res.string.typography_foo), stringResource(Res.string.typography_bar)))

    Muted(stringResource(Res.string.typography_usage))
    InlineCode(
        text = """
                H1(text = "Taxing Laughter")
                Lead(text = "Once upon a time...")
                Muted(text = "Enter your email")
                InlineCode(
                    text = "@radix-ui/react-alert-dialog",
                    selected = false,
                    selectEnabled = true,
                    copyEnabled = true,
                )
                UnorderedList(items = listOf(stringResource(Res.string.typography_foo), stringResource(Res.string.typography_bar)))
        """.trimIndent(),
        selected = false,
        selectEnabled = true,
        copyEnabled = true,
    )
}
