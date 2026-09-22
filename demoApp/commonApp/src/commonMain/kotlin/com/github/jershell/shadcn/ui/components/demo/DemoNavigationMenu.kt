package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jershell.shadcn.components.navigationmenu.NavigationMenu
import com.github.jershell.shadcn.components.navigationmenu.NavigationMenuLink
import com.github.jershell.shadcn.components.toast.Toast
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.navigation_menu_a_modal_window_that_overlays_the_content
import com.github.jershell.shadcn.demoapp.generated.resources.navigation_menu_a_short_introduction_to_the_project_and_its_goal
import com.github.jershell.shadcn.demoapp.generated.resources.navigation_menu_a_trigger_opens_its_card_panel_below_plain_links
import com.github.jershell.shadcn.demoapp.generated.resources.navigation_menu_alert
import com.github.jershell.shadcn.demoapp.generated.resources.navigation_menu_button
import com.github.jershell.shadcn.demoapp.generated.resources.navigation_menu_components
import com.github.jershell.shadcn.demoapp.generated.resources.navigation_menu_components_alert
import com.github.jershell.shadcn.demoapp.generated.resources.navigation_menu_components_button
import com.github.jershell.shadcn.demoapp.generated.resources.navigation_menu_components_dialog
import com.github.jershell.shadcn.demoapp.generated.resources.navigation_menu_default
import com.github.jershell.shadcn.demoapp.generated.resources.navigation_menu_design_tokens_and_dark_mode
import com.github.jershell.shadcn.demoapp.generated.resources.navigation_menu_dialog
import com.github.jershell.shadcn.demoapp.generated.resources.navigation_menu_displays_a_callout_for_user_attention
import com.github.jershell.shadcn.demoapp.generated.resources.navigation_menu_docs
import com.github.jershell.shadcn.demoapp.generated.resources.navigation_menu_docs_getting_started
import com.github.jershell.shadcn.demoapp.generated.resources.navigation_menu_docs_theming
import com.github.jershell.shadcn.demoapp.generated.resources.navigation_menu_getting_started
import com.github.jershell.shadcn.demoapp.generated.resources.navigation_menu_github
import com.github.jershell.shadcn.demoapp.generated.resources.navigation_menu_home
import com.github.jershell.shadcn.demoapp.generated.resources.navigation_menu_home_overview
import com.github.jershell.shadcn.demoapp.generated.resources.navigation_menu_installation_and_first_steps
import com.github.jershell.shadcn.demoapp.generated.resources.navigation_menu_overview
import com.github.jershell.shadcn.demoapp.generated.resources.navigation_menu_theming
import com.github.jershell.shadcn.demoapp.generated.resources.navigation_menu_triggers_an_action_or_event
import com.github.jershell.shadcn.demoapp.generated.resources.navigation_menu_usage
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoNavigationMenu() {
    val s_navigation_menu_github = stringResource(Res.string.navigation_menu_github)
    val s_navigation_menu_docs_theming = stringResource(Res.string.navigation_menu_docs_theming)
    val s_navigation_menu_docs_getting_started = stringResource(Res.string.navigation_menu_docs_getting_started)
    val s_navigation_menu_components_dialog = stringResource(Res.string.navigation_menu_components_dialog)
    val s_navigation_menu_components_button = stringResource(Res.string.navigation_menu_components_button)
    val s_navigation_menu_components_alert = stringResource(Res.string.navigation_menu_components_alert)
    val s_navigation_menu_home_overview = stringResource(Res.string.navigation_menu_home_overview)
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.navigation_menu_default),
            description = stringResource(Res.string.navigation_menu_a_trigger_opens_its_card_panel_below_plain_links),
        ) {
            NavigationMenu {
                NavigationMenuTrigger(stringResource(Res.string.navigation_menu_home)) {
                    NavigationMenuLink(
                        title = stringResource(Res.string.navigation_menu_overview),
                        description = stringResource(Res.string.navigation_menu_a_short_introduction_to_the_project_and_its_goal),
                        onSelect = { Toast(s_navigation_menu_home_overview) },
                    )
                }
                NavigationMenuTrigger(stringResource(Res.string.navigation_menu_components)) {
                    NavigationMenuLink(
                        title = stringResource(Res.string.navigation_menu_alert),
                        description = stringResource(Res.string.navigation_menu_displays_a_callout_for_user_attention),
                        onSelect = { Toast(s_navigation_menu_components_alert) },
                    )
                    NavigationMenuLink(
                        title = stringResource(Res.string.navigation_menu_button),
                        description = stringResource(Res.string.navigation_menu_triggers_an_action_or_event),
                        onSelect = { Toast(s_navigation_menu_components_button) },
                    )
                    NavigationMenuLink(
                        title = stringResource(Res.string.navigation_menu_dialog),
                        description = stringResource(Res.string.navigation_menu_a_modal_window_that_overlays_the_content),
                        onSelect = { Toast(s_navigation_menu_components_dialog) },
                    )
                }
                NavigationMenuTrigger(stringResource(Res.string.navigation_menu_docs)) {
                    NavigationMenuLink(
                        title = stringResource(Res.string.navigation_menu_getting_started),
                        description = stringResource(Res.string.navigation_menu_installation_and_first_steps),
                        onSelect = { Toast(s_navigation_menu_docs_getting_started) },
                    )
                    NavigationMenuLink(
                        title = stringResource(Res.string.navigation_menu_theming),
                        description = stringResource(Res.string.navigation_menu_design_tokens_and_dark_mode),
                        onSelect = { Toast(s_navigation_menu_docs_theming) },
                    )
                }
                NavigationMenuLink(
                    title = stringResource(Res.string.navigation_menu_github),
                    onSelect = { Toast(s_navigation_menu_github) },
                )
            }
        }

        P(stringResource(Res.string.navigation_menu_usage))
        InlineCode(
            text = """
                NavigationMenu {
                    NavigationMenuTrigger("Components") {
                        NavigationMenuLink(
                            title = "Button",
                            description = "Triggers an action.",
                            onSelect = { ... },
                        )
                        NavigationMenuLink("Release notes", onSelect = { ... })
                    }
                    NavigationMenuLink("GitHub", onSelect = { ... })
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
