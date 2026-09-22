package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Terminal
import com.github.jershell.shadcn.components.alert.Alert
import com.github.jershell.shadcn.components.alert.AlertDescription
import com.github.jershell.shadcn.components.alert.AlertTitle
import com.github.jershell.shadcn.components.alert.AlertVariant
import com.github.jershell.shadcn.components.icon.toShadcnIcon
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.alert_a_neutral_callout_with_a_leading_icon
import com.github.jershell.shadcn.demoapp.generated.resources.alert_an_alert_can_also_be_rendered_without_an_icon
import com.github.jershell.shadcn.demoapp.generated.resources.alert_default
import com.github.jershell.shadcn.demoapp.generated.resources.alert_destructive
import com.github.jershell.shadcn.demoapp.generated.resources.alert_error
import com.github.jershell.shadcn.demoapp.generated.resources.alert_heads_up
import com.github.jershell.shadcn.demoapp.generated.resources.alert_note
import com.github.jershell.shadcn.demoapp.generated.resources.alert_this_alert_has_no_leading_icon
import com.github.jershell.shadcn.demoapp.generated.resources.alert_usage
import com.github.jershell.shadcn.demoapp.generated.resources.alert_use_the_destructive_variant_for_errors
import com.github.jershell.shadcn.demoapp.generated.resources.alert_without_icon
import com.github.jershell.shadcn.demoapp.generated.resources.alert_you_can_add_components_to_your_app_using_the_cli
import com.github.jershell.shadcn.demoapp.generated.resources.alert_your_session_has_expired_please_log_in_again
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoAlert() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.alert_default),
            description = stringResource(Res.string.alert_a_neutral_callout_with_a_leading_icon),
        ) {
            Alert(
                leadingIcon = Lucide.Terminal.toShadcnIcon(),
            ) {
                AlertTitle(stringResource(Res.string.alert_heads_up))
                AlertDescription(stringResource(Res.string.alert_you_can_add_components_to_your_app_using_the_cli))
            }
        }

        DemoSection(
            title = stringResource(Res.string.alert_destructive),
            description = stringResource(Res.string.alert_use_the_destructive_variant_for_errors),
        ) {
            Alert(
                variant = AlertVariant.Destructive,
                leadingIcon = Lucide.CircleAlert.toShadcnIcon(),
            ) {
                AlertTitle(stringResource(Res.string.alert_error))
                AlertDescription(stringResource(Res.string.alert_your_session_has_expired_please_log_in_again))
            }
        }

        DemoSection(
            title = stringResource(Res.string.alert_without_icon),
            description = stringResource(Res.string.alert_an_alert_can_also_be_rendered_without_an_icon),
        ) {
            Alert {
                AlertTitle(stringResource(Res.string.alert_note))
                AlertDescription(stringResource(Res.string.alert_this_alert_has_no_leading_icon))
            }
        }

        Muted(stringResource(Res.string.alert_usage))
        InlineCode(
            text = """
                Alert(
                    leadingIcon = Lucide.Terminal.toShadcnIcon(),
                ) {
                    AlertTitle("Heads up!")
                    AlertDescription("You can add components to your app using the cli.")
                }

                Alert(
                    variant = AlertVariant.Destructive,
                    leadingIcon = Lucide.CircleAlert.toShadcnIcon(),
                ) {
                    AlertTitle("Error")
                    AlertDescription("Your session has expired. Please log in again.")
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
