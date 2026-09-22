package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jershell.shadcn.components.button.Button
import com.github.jershell.shadcn.components.button.ButtonText
import com.github.jershell.shadcn.components.button.ButtonVariant
import com.github.jershell.shadcn.components.card.Card
import com.github.jershell.shadcn.components.card.CardContent
import com.github.jershell.shadcn.components.card.CardDescription
import com.github.jershell.shadcn.components.card.CardFooter
import com.github.jershell.shadcn.components.card.CardHeader
import com.github.jershell.shadcn.components.card.CardTitle
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.card_a_card_with_header_content_and_footer
import com.github.jershell.shadcn.demoapp.generated.resources.card_a_card_with_only_content
import com.github.jershell.shadcn.demoapp.generated.resources.card_cancel
import com.github.jershell.shadcn.demoapp.generated.resources.card_create_project
import com.github.jershell.shadcn.demoapp.generated.resources.card_default
import com.github.jershell.shadcn.demoapp.generated.resources.card_deploy
import com.github.jershell.shadcn.demoapp.generated.resources.card_deploy_your_new_project_in_one_click
import com.github.jershell.shadcn.demoapp.generated.resources.card_simple_card
import com.github.jershell.shadcn.demoapp.generated.resources.card_this_card_has_no_header_or_footer_just_a_simple
import com.github.jershell.shadcn.demoapp.generated.resources.card_usage
import com.github.jershell.shadcn.demoapp.generated.resources.card_your_project_will_be_created_with_a_default_conf
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoCard() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.card_default),
            description = stringResource(Res.string.card_a_card_with_header_content_and_footer),
        ) {
            Card {
                CardHeader {
                    CardTitle(stringResource(Res.string.card_create_project))
                    CardDescription(stringResource(Res.string.card_deploy_your_new_project_in_one_click))
                }
                CardContent {
                    P(stringResource(Res.string.card_your_project_will_be_created_with_a_default_conf))
                }
                CardFooter {
                    Button(
                        onClick = {},
                        variant = ButtonVariant.Outline,
                    ) {
                        ButtonText(stringResource(Res.string.card_cancel))
                    }
                    Button(onClick = {}) {
                        ButtonText(stringResource(Res.string.card_deploy))
                    }
                }
            }
        }

        DemoSection(
            title = stringResource(Res.string.card_simple_card),
            description = stringResource(Res.string.card_a_card_with_only_content),
        ) {
            Card {
                CardContent {
                    P(stringResource(Res.string.card_this_card_has_no_header_or_footer_just_a_simple))
                }
            }
        }

        Muted(stringResource(Res.string.card_usage))
        InlineCode(
            text = """
                Card {
                    CardHeader {
                        CardTitle("Create project")
                        CardDescription("Deploy your new project in one-click.")
                    }
                    CardContent {
                        P("Your project details go here.")
                    }
                    CardFooter {
                        Button(onClick = {}) { P("Cancel") }
                        Button(onClick = {}) { P("Deploy") }
                    }
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
