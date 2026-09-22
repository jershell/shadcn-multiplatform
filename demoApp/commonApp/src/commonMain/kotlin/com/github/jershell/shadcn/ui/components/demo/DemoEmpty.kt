package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Bell
import com.composables.icons.lucide.Lucide
import com.github.jershell.shadcn.components.button.Button
import com.github.jershell.shadcn.components.button.ButtonText
import com.github.jershell.shadcn.components.button.ButtonVariant
import com.github.jershell.shadcn.components.empty.Empty
import com.github.jershell.shadcn.components.icon.toShadcnIcon
import com.github.jershell.shadcn.components.empty.EmptyContent
import com.github.jershell.shadcn.components.empty.EmptyDescription
import com.github.jershell.shadcn.components.empty.EmptyHeader
import com.github.jershell.shadcn.components.empty.EmptyMedia
import com.github.jershell.shadcn.components.empty.EmptyMediaVariant
import com.github.jershell.shadcn.components.empty.EmptyTitle
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.empty_a_dashed_container_with_media_title_description
import com.github.jershell.shadcn.demoapp.generated.resources.empty_basic
import com.github.jershell.shadcn.demoapp.generated.resources.empty_go_back
import com.github.jershell.shadcn.demoapp.generated.resources.empty_notification_not_found
import com.github.jershell.shadcn.demoapp.generated.resources.empty_usage
import com.github.jershell.shadcn.demoapp.generated.resources.empty_you_do_not_have_any_notifications_actions_you_ta
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoEmpty() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.empty_basic),
            description = stringResource(Res.string.empty_a_dashed_container_with_media_title_description),
        ) {
            Empty {
                EmptyHeader {
                    EmptyMedia(
                        variant = EmptyMediaVariant.Icon,
                        icon = Lucide.Bell.toShadcnIcon(),
                    )
                    EmptyTitle(stringResource(Res.string.empty_notification_not_found))
                    EmptyDescription(
                        stringResource(Res.string.empty_you_do_not_have_any_notifications_actions_you_ta),
                    )
                }
                EmptyContent {
                    Button(onClick = {}, variant = ButtonVariant.Outline) {
                        ButtonText(stringResource(Res.string.empty_go_back))
                    }
                }
            }
        }

        Muted(stringResource(Res.string.empty_usage))
        InlineCode(
            text = """
                Empty {
                    EmptyHeader {
                        EmptyMedia(
                            variant = EmptyMediaVariant.Icon,
                            icon = Lucide.Bell.toShadcnIcon(),
                        )
                        EmptyTitle("Notification Not Found")
                        EmptyDescription("You do not have any notifications.")
                    }
                    EmptyContent {
                        Button(onClick = {}, variant = ButtonVariant.Outline) {
                            ButtonText("Go Back")
                        }
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
