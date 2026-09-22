package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.Code
import com.composables.icons.lucide.FileText
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Inbox
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Search
import com.composables.icons.lucide.Settings
import com.github.jershell.shadcn.components.button.Button
import com.github.jershell.shadcn.components.button.ButtonText
import com.github.jershell.shadcn.components.button.ButtonVariant
import com.github.jershell.shadcn.components.sidebar.Sidebar
import com.github.jershell.shadcn.components.sidebar.SidebarCollapsible
import com.github.jershell.shadcn.components.sidebar.SidebarIcon
import com.github.jershell.shadcn.components.sidebar.SidebarInset
import com.github.jershell.shadcn.components.sidebar.SidebarProvider
import com.github.jershell.shadcn.components.sidebar.SidebarSide
import com.github.jershell.shadcn.components.sidebar.SidebarTrigger
import com.github.jershell.shadcn.components.sidebar.SidebarVariant
import com.github.jershell.shadcn.components.toast.Toast
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.components.typography.Small
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_24
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_acme
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_acme_inc
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_api
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_basic
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_collapsed_keeps_icons_only_scroll_position_survi
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_collapsible_to_icons
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_content_pane_on_the_left_of_the_right_sidebar
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_docs
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_floating
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_header_content_with_groups_label_menu_button_ite
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_help
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_help_opened
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_home
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_how_to_use_sidebar_header_content_footer_content
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_inbox
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_inset_content_pane
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_inset_right_side
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_item_one
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_item_two
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_more_actions
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_november
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_october
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_page_content
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_platform
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_playground
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_reports
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_search
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_settings
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_sidebarcollapsible_icon_trigger_panelleft_icon_o
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_support
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_usage
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_variant_floating_card_with_shadow_or_inset_card
import com.github.jershell.shadcn.demoapp.generated.resources.sidebar_variants
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoSidebar() {
    val s_sidebar_inset_right_side = stringResource(Res.string.sidebar_inset_right_side)
    val s_sidebar_item_two = stringResource(Res.string.sidebar_item_two)
    val s_sidebar_item_one = stringResource(Res.string.sidebar_item_one)
    val s_sidebar_floating = stringResource(Res.string.sidebar_floating)
    val s_sidebar_home = stringResource(Res.string.sidebar_home)
    val s_sidebar_help_opened = stringResource(Res.string.sidebar_help_opened)
    val s_sidebar_settings = stringResource(Res.string.sidebar_settings)
    val s_sidebar_search = stringResource(Res.string.sidebar_search)
    val s_sidebar_support = stringResource(Res.string.sidebar_support)
    val s_sidebar_november = stringResource(Res.string.sidebar_november)
    val s_sidebar_october = stringResource(Res.string.sidebar_october)
    val s_sidebar_reports = stringResource(Res.string.sidebar_reports)
    val s_sidebar_more_actions = stringResource(Res.string.sidebar_more_actions)
    val s_sidebar_24 = stringResource(Res.string.sidebar_24)
    val s_sidebar_inbox = stringResource(Res.string.sidebar_inbox)
    val s_sidebar_api = stringResource(Res.string.sidebar_api)
    val s_sidebar_docs = stringResource(Res.string.sidebar_docs)
    val s_sidebar_playground = stringResource(Res.string.sidebar_playground)
    val s_sidebar_platform = stringResource(Res.string.sidebar_platform)
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.sidebar_basic),
            description = stringResource(Res.string.sidebar_header_content_with_groups_label_menu_button_ite),
        ) {
            var selected by remember { mutableStateOf("playground") }
            Row(modifier = Modifier.height(420.dp)) {
                Sidebar(modifier = Modifier.weight(0.35f)) {
                    Header {
                        P(stringResource(Res.string.sidebar_acme_inc))
                    }
                    Content {
                        Group {
                            Label(s_sidebar_platform)
                            Menu {
                                Button(
                                    label = s_sidebar_playground,
                                    onClick = { selected = "playground" },
                                    isSelected = selected == "playground",
                                    icon = { SidebarIcon(Lucide.House) },
                                )
                                Button(
                                    label = s_sidebar_docs,
                                    onClick = { selected = "docs" },
                                    isSelected = selected == "docs",
                                    icon = { SidebarIcon(Lucide.FileText) },
                                )
                                Button(
                                    label = s_sidebar_api,
                                    onClick = { selected = "api" },
                                    isSelected = selected == "api",
                                    icon = { SidebarIcon(Lucide.Code) },
                                )
                                Item {
                                    Button(
                                        label = s_sidebar_inbox,
                                        onClick = { selected = "inbox" },
                                        isSelected = selected == "inbox",
                                        icon = { SidebarIcon(Lucide.Inbox) },
                                    )
                                    Badge(s_sidebar_24)
                                    Action(onClick = { Toast(s_sidebar_more_actions) })
                                }
                                Item {
                                    Button(
                                        label = s_sidebar_reports,
                                        onClick = { selected = "reports" },
                                        isSelected = selected == "reports",
                                        icon = { SidebarIcon(Lucide.Calendar) },
                                    )
                                    Sub {
                                        Item {
                                            Button(
                                                label = s_sidebar_october,
                                                onClick = { selected = "october" },
                                                isSelected = selected == "october",
                                            )
                                        }
                                        Item {
                                            Button(
                                                label = s_sidebar_november,
                                                onClick = { selected = "november" },
                                                isSelected = selected == "november",
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Separator()
                        Group {
                            Label(s_sidebar_support)
                            Menu {
                                Button(
                                    label = s_sidebar_search,
                                    onClick = { },
                                    icon = { SidebarIcon(Lucide.Search) },
                                )
                                Button(
                                    label = s_sidebar_settings,
                                    onClick = { },
                                    icon = { SidebarIcon(Lucide.Settings) },
                                )
                            }
                        }
                    }
                    Footer {
                        Button(
                            onClick = { Toast(s_sidebar_help_opened) },
                            variant = ButtonVariant.Outline,
                        ) {
                            ButtonText(stringResource(Res.string.sidebar_help))
                        }
                    }
                }
                SidebarInset(modifier = Modifier.weight(0.65f)) {
                    PlaceholderPane(stringResource(Res.string.sidebar_page_content))
                }
            }
        }

        DemoSection(
            title = stringResource(Res.string.sidebar_collapsible_to_icons),
            description = stringResource(Res.string.sidebar_sidebarcollapsible_icon_trigger_panelleft_icon_o),
        ) {
            var expanded by remember { mutableStateOf(true) }
            var selected by remember { mutableStateOf("docs") }
            Row(modifier = Modifier.height(360.dp)) {
                Sidebar(
                    modifier = Modifier.weight(0.35f),
                    collapsible = SidebarCollapsible.Icon,
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                ) {
                    Header {
                        Trigger()
                        if (isExpanded) {
                            P(stringResource(Res.string.sidebar_acme), modifier = Modifier)
                        }
                    }
                    Content {
                        Group {
                            Menu {
                                Button(
                                    label = s_sidebar_home,
                                    onClick = { selected = "home" },
                                    isSelected = selected == "home",
                                    icon = { SidebarIcon(Lucide.House) },
                                )
                                Button(
                                    label = s_sidebar_docs,
                                    onClick = { selected = "docs" },
                                    isSelected = selected == "docs",
                                    icon = { SidebarIcon(Lucide.FileText) },
                                )
                                Button(
                                    label = s_sidebar_settings,
                                    onClick = { selected = "settings" },
                                    isSelected = selected == "settings",
                                    icon = { SidebarIcon(Lucide.Settings) },
                                )
                            }
                        }
                    }
                }
                SidebarInset(modifier = Modifier.weight(0.65f)) {
                    PlaceholderPane(stringResource(Res.string.sidebar_collapsed_keeps_icons_only_scroll_position_survi))
                }
            }
        }

        DemoSection(
            title = stringResource(Res.string.sidebar_variants),
            description = stringResource(Res.string.sidebar_variant_floating_card_with_shadow_or_inset_card),
        ) {
            var floatingExpanded by remember { mutableStateOf(true) }
            SidebarProvider(
                expanded = floatingExpanded,
                onExpandedChange = { floatingExpanded = it },
            ) {
                Row(modifier = Modifier.height(280.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Sidebar(
                        modifier = Modifier.weight(0.5f),
                        variant = SidebarVariant.Floating,
                        collapsible = SidebarCollapsible.Offcanvas,
                    ) {
                        Header {
                            Trigger()
                        }
                        Content {
                            Group {
                                Label(s_sidebar_floating)
                                Menu {
                                    Button(label = s_sidebar_item_one, onClick = { }, isSelected = true)
                                    Button(label = s_sidebar_item_two, onClick = { })
                                }
                            }
                        }
                    }
                    SidebarInset(modifier = Modifier.weight(0.5f)) {
                        Column(modifier = Modifier.fillMaxHeight()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                            ) {
                                SidebarTrigger()
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center,
                            ) {
                                PlaceholderPane(stringResource(Res.string.sidebar_inset_content_pane))
                            }
                        }
                    }
                }
            }
            Row(modifier = Modifier.height(280.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Sidebar(
                    modifier = Modifier.weight(0.35f),
                    variant = SidebarVariant.Inset,
                    side = SidebarSide.Right,
                ) {
                    Content {
                        Group {
                            Label(s_sidebar_inset_right_side)
                            Menu {
                                Button(label = s_sidebar_item_one, onClick = { }, isSelected = true)
                                Button(label = s_sidebar_item_two, onClick = { })
                            }
                        }
                    }
                }
                SidebarInset(modifier = Modifier.weight(0.65f)) {
                    PlaceholderPane(stringResource(Res.string.sidebar_content_pane_on_the_left_of_the_right_sidebar))
                }
            }
        }

        Small(stringResource(Res.string.sidebar_how_to_use_sidebar_header_content_footer_content))

        P(stringResource(Res.string.sidebar_usage))
        InlineCode(
            text = """
                var selected by remember { mutableStateOf("home") }
                Sidebar(
                    collapsible = SidebarCollapsible.Icon,
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                ) {
                    Header {
                        Trigger()
                        if (isExpanded) P("Acme") // SidebarSectionScope.isExpanded
                    }
                    Content {
                        Group {
                            Label("Platform")
                            Menu {
                                Button(
                                    label = "Home",
                                    onClick = { selected = "home" },
                                    isSelected = selected == "home",
                                    icon = { Icon(Lucide.House, null) },
                                )
                                Item {
                                    Button(label = "Inbox", icon = { Icon(Lucide.Inbox, null) })
                                    Badge("24")
                                    Action(onClick = { ... })
                                }
                                Item {
                                    Button(label = "Reports")
                                    Sub {
                                        Item { Button(label = "October", isSelected = true) }
                                    }
                                }
                            }
                        }
                    }
                    Footer { Button(label = "Help", variant = Outline) }
                }
            """.trimIndent(),
            selected = false,
            selectEnabled = true,
            copyEnabled = true,
        )
    }
}

@Composable
private fun PlaceholderPane(text: String) {
    Box(
        modifier = Modifier
            .fillMaxHeight(),
        contentAlignment = Alignment.Center,
    ) {
        Small(text)
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
