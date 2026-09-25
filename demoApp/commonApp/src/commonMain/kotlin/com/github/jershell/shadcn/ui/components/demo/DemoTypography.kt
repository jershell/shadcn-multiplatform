package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.github.jershell.shadcn.components.typography.Blockquote
import com.github.jershell.shadcn.components.typography.ExtraSmall
import com.github.jershell.shadcn.components.typography.H1
import com.github.jershell.shadcn.components.typography.H2
import com.github.jershell.shadcn.components.typography.H3
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Lead
import com.github.jershell.shadcn.components.typography.Large
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.components.typography.Small
import com.github.jershell.shadcn.components.typography.TypographyText
import com.github.jershell.shadcn.components.typography.UnorderedList
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.typography_are_you_absolutely_sure
import com.github.jershell.shadcn.demoapp.generated.resources.typography_bar
import com.github.jershell.shadcn.demoapp.generated.resources.typography_blockquote
import com.github.jershell.shadcn.demoapp.generated.resources.typography_blockquote_description
import com.github.jershell.shadcn.demoapp.generated.resources.typography_enter_your_email
import com.github.jershell.shadcn.demoapp.generated.resources.typography_extrasmall
import com.github.jershell.shadcn.demoapp.generated.resources.typography_extrasmall_description
import com.github.jershell.shadcn.demoapp.generated.resources.typography_foo
import com.github.jershell.shadcn.demoapp.generated.resources.typography_headings
import com.github.jershell.shadcn.demoapp.generated.resources.typography_headings_description
import com.github.jershell.shadcn.demoapp.generated.resources.typography_inline_code
import com.github.jershell.shadcn.demoapp.generated.resources.typography_inline_code_description
import com.github.jershell.shadcn.demoapp.generated.resources.typography_large
import com.github.jershell.shadcn.demoapp.generated.resources.typography_large_description
import com.github.jershell.shadcn.demoapp.generated.resources.typography_lead
import com.github.jershell.shadcn.demoapp.generated.resources.typography_lead_description
import com.github.jershell.shadcn.demoapp.generated.resources.typography_list
import com.github.jershell.shadcn.demoapp.generated.resources.typography_list_description
import com.github.jershell.shadcn.demoapp.generated.resources.typography_lorem_ipsum_dolor_sit_amet_consectetur_adipiscin
import com.github.jershell.shadcn.demoapp.generated.resources.typography_muted
import com.github.jershell.shadcn.demoapp.generated.resources.typography_muted_description
import com.github.jershell.shadcn.demoapp.generated.resources.typography_once_upon_a_time
import com.github.jershell.shadcn.demoapp.generated.resources.typography_paragraph
import com.github.jershell.shadcn.demoapp.generated.resources.typography_paragraph_description
import com.github.jershell.shadcn.demoapp.generated.resources.typography_predict_the_future
import com.github.jershell.shadcn.demoapp.generated.resources.typography_quick_brown_fox
import com.github.jershell.shadcn.demoapp.generated.resources.typography_radix_ui_react_alert_dialog
import com.github.jershell.shadcn.demoapp.generated.resources.typography_scale
import com.github.jershell.shadcn.demoapp.generated.resources.typography_scale_description
import com.github.jershell.shadcn.demoapp.generated.resources.typography_small
import com.github.jershell.shadcn.demoapp.generated.resources.typography_small_description
import com.github.jershell.shadcn.demoapp.generated.resources.typography_taxing_laughter
import com.github.jershell.shadcn.demoapp.generated.resources.typography_updated_two_minutes_ago
import com.github.jershell.shadcn.demoapp.generated.resources.typography_usage
import com.github.jershell.shadcn.theme.TypographyStyles
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoTypography() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        TypographySection(
            title = stringResource(Res.string.typography_headings),
            description = stringResource(Res.string.typography_headings_description),
            usage = """
                H1(text = "Taxing Laughter")
                H2(text = "Taxing Laughter")
                H3(text = "Taxing Laughter")
                H4(text = "Taxing Laughter")
            """.trimIndent(),
        ) {
            H1(text = stringResource(Res.string.typography_taxing_laughter))
            H2(text = stringResource(Res.string.typography_taxing_laughter))
            H3(text = stringResource(Res.string.typography_taxing_laughter))
            H4(text = stringResource(Res.string.typography_taxing_laughter))
        }
        TypographySection(
            title = stringResource(Res.string.typography_paragraph),
            description = stringResource(Res.string.typography_paragraph_description),
            usage = "P(text = \"Lorem ipsum dolor sit amet...\")",
        ) {
            P(stringResource(Res.string.typography_lorem_ipsum_dolor_sit_amet_consectetur_adipiscin))
        }
        TypographySection(
            title = stringResource(Res.string.typography_lead),
            description = stringResource(Res.string.typography_lead_description),
            usage = "Lead(text = \"Once upon a time...\")",
        ) {
            Lead(text = stringResource(Res.string.typography_once_upon_a_time))
        }
        TypographySection(
            title = stringResource(Res.string.typography_muted),
            description = stringResource(Res.string.typography_muted_description),
            usage = "Muted(text = \"Enter your email\")",
        ) {
            Muted(text = stringResource(Res.string.typography_enter_your_email))
        }
        TypographySection(
            title = stringResource(Res.string.typography_large),
            description = stringResource(Res.string.typography_large_description),
            usage = "Large(text = \"Are you absolutely sure?\")",
        ) {
            Large(text = stringResource(Res.string.typography_are_you_absolutely_sure))
        }
        TypographySection(
            title = stringResource(Res.string.typography_small),
            description = stringResource(Res.string.typography_small_description),
            usage = "Small(text = \"Enter your email\")",
        ) {
            Small(text = stringResource(Res.string.typography_enter_your_email))
        }
        TypographySection(
            title = stringResource(Res.string.typography_extrasmall),
            description = stringResource(Res.string.typography_extrasmall_description),
            usage = "ExtraSmall(text = \"Updated 2 minutes ago\")",
        ) {
            ExtraSmall(text = stringResource(Res.string.typography_updated_two_minutes_ago))
        }
        TypographySection(
            title = stringResource(Res.string.typography_blockquote),
            description = stringResource(Res.string.typography_blockquote_description),
            usage = "Blockquote(text = \"The best way to predict the future...\")",
        ) {
            Blockquote(text = stringResource(Res.string.typography_predict_the_future))
        }
        TypographySection(
            title = stringResource(Res.string.typography_list),
            description = stringResource(Res.string.typography_list_description),
            usage = "UnorderedList(items = listOf(\"foo\", \"bar\"))",
        ) {
            UnorderedList(
                items = listOf(
                    stringResource(Res.string.typography_foo),
                    stringResource(Res.string.typography_bar),
                ),
            )
        }
        TypographySection(
            title = stringResource(Res.string.typography_inline_code),
            description = stringResource(Res.string.typography_inline_code_description),
            usage = """
                InlineCode(
                    text = "@radix-ui/react-alert-dialog",
                    selected = false,
                    selectEnabled = true,
                    copyEnabled = true,
                )
            """.trimIndent(),
        ) {
            InlineCode(
                text = stringResource(Res.string.typography_radix_ui_react_alert_dialog),
                selected = false,
                selectEnabled = true,
                copyEnabled = true,
            )
        }
        TypographySection(
            title = stringResource(Res.string.typography_scale),
            description = stringResource(Res.string.typography_scale_description),
            usage = """
                TypographyText(
                    text = "The quick brown fox...",
                    style = TypographyStyles.textLgRegular,
                )
            """.trimIndent(),
        ) {
            ScaleRow("xs · 12/16", TypographyStyles.textXsRegular)
            ScaleRow("sm · 14/20", TypographyStyles.textSmRegular)
            ScaleRow("base · 16/24", TypographyStyles.textBaseRegular)
            ScaleRow("lg · 18/28", TypographyStyles.textLgRegular)
            ScaleRow("xl · 20/28", TypographyStyles.textXlRegular)
            ScaleRow("2xl · 24/32", TypographyStyles.textN2xlRegular)
            ScaleRow("3xl · 30/36", TypographyStyles.textN3xlRegular)
            ScaleRow("4xl · 36/40", TypographyStyles.textN4xlRegular)
            ScaleRow("5xl · 48/48", TypographyStyles.textN5xlRegular)
            ScaleRow("6xl · 60/60", TypographyStyles.textN6xlRegular)
            ScaleRow("7xl · 72/72", TypographyStyles.textN7xlRegular)
            ScaleRow("8xl · 96/96", TypographyStyles.textN8xlRegular)
            ScaleRow("9xl · 128/128", TypographyStyles.textN9xlRegular)
        }
    }
}

@Composable
private fun TypographySection(
    title: String,
    description: String,
    usage: String,
    example: @Composable ColumnScope.() -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        H4(title)
        P(description)
        example()
        Muted(stringResource(Res.string.typography_usage))
        InlineCode(
            text = usage,
            selected = false,
            selectEnabled = true,
            copyEnabled = true,
        )
    }
}

@Composable
private fun ScaleRow(label: String, style: TextStyle) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        ExtraSmall(text = label)
        TypographyText(
            text = stringResource(Res.string.typography_quick_brown_fox),
            style = style,
        )
    }
}