package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jershell.shadcn.components.breadcrumb.Breadcrumb
import com.github.jershell.shadcn.components.breadcrumb.BreadcrumbEllipsis
import com.github.jershell.shadcn.components.breadcrumb.BreadcrumbItem
import com.github.jershell.shadcn.components.breadcrumb.BreadcrumbLink
import com.github.jershell.shadcn.components.breadcrumb.BreadcrumbList
import com.github.jershell.shadcn.components.breadcrumb.BreadcrumbPage
import com.github.jershell.shadcn.components.breadcrumb.BreadcrumbSeparator
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.breadcrumb_basic_breadcrumb_with_links_and_a_current_page
import com.github.jershell.shadcn.demoapp.generated.resources.breadcrumb_breadcrumb
import com.github.jershell.shadcn.demoapp.generated.resources.breadcrumb_collapsed_middle_items_using_an_ellipsis_indicat
import com.github.jershell.shadcn.demoapp.generated.resources.breadcrumb_components
import com.github.jershell.shadcn.demoapp.generated.resources.breadcrumb_default
import com.github.jershell.shadcn.demoapp.generated.resources.breadcrumb_home
import com.github.jershell.shadcn.demoapp.generated.resources.breadcrumb_usage
import com.github.jershell.shadcn.demoapp.generated.resources.breadcrumb_with_ellipsis
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoBreadcrumb() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.breadcrumb_default),
            description = stringResource(Res.string.breadcrumb_basic_breadcrumb_with_links_and_a_current_page),
        ) {
            Breadcrumb {
                BreadcrumbList {
                    BreadcrumbItem {
                        BreadcrumbLink(stringResource(Res.string.breadcrumb_home), onClick = {})
                    }
                    BreadcrumbSeparator()
                    BreadcrumbItem {
                        BreadcrumbLink(stringResource(Res.string.breadcrumb_components), onClick = {})
                    }
                    BreadcrumbSeparator()
                    BreadcrumbItem {
                        BreadcrumbPage(stringResource(Res.string.breadcrumb_breadcrumb))
                    }
                }
            }
        }

        DemoSection(
            title = stringResource(Res.string.breadcrumb_with_ellipsis),
            description = stringResource(Res.string.breadcrumb_collapsed_middle_items_using_an_ellipsis_indicat),
        ) {
            Breadcrumb {
                BreadcrumbList {
                    BreadcrumbItem {
                        BreadcrumbLink(stringResource(Res.string.breadcrumb_home), onClick = {})
                    }
                    BreadcrumbSeparator()
                    BreadcrumbEllipsis()
                    BreadcrumbSeparator()
                    BreadcrumbItem {
                        BreadcrumbLink(stringResource(Res.string.breadcrumb_components), onClick = {})
                    }
                    BreadcrumbSeparator()
                    BreadcrumbItem {
                        BreadcrumbPage(stringResource(Res.string.breadcrumb_breadcrumb))
                    }
                }
            }
        }

        Muted(stringResource(Res.string.breadcrumb_usage))
        InlineCode(
            text = """
                Breadcrumb {
                    BreadcrumbList {
                        BreadcrumbItem {
                            BreadcrumbLink("Home", onClick = { /* ... */ })
                        }
                        BreadcrumbSeparator()
                        BreadcrumbItem {
                            BreadcrumbLink("Components", onClick = { /* ... */ })
                        }
                        BreadcrumbSeparator()
                        BreadcrumbItem {
                            BreadcrumbPage("Breadcrumb")
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
