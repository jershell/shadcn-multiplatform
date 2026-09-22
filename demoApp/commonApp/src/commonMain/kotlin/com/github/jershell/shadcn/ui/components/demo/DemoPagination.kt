package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jershell.shadcn.components.pagination.Pagination
import com.github.jershell.shadcn.components.pagination.PaginationContent
import com.github.jershell.shadcn.components.pagination.PaginationEllipsis
import com.github.jershell.shadcn.components.pagination.PaginationNext
import com.github.jershell.shadcn.components.pagination.PaginationPage
import com.github.jershell.shadcn.components.pagination.PaginationPrevious
import com.github.jershell.shadcn.components.pagination.PaginationToken
import com.github.jershell.shadcn.components.pagination.buildPaginationTokens
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.pagination_default
import com.github.jershell.shadcn.demoapp.generated.resources.pagination_icons_only
import com.github.jershell.shadcn.demoapp.generated.resources.pagination_pagination_with_previous_next_controls_and_ellip
import com.github.jershell.shadcn.demoapp.generated.resources.pagination_usage
import com.github.jershell.shadcn.demoapp.generated.resources.pagination_use_just_previous_and_next_buttons_without_page
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoPagination() {
    val totalPages = 25
    var currentPage by remember { mutableIntStateOf(10) }
    var iconOnlyPage by remember { mutableIntStateOf(1) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.pagination_default),
            description = stringResource(Res.string.pagination_pagination_with_previous_next_controls_and_ellip),
        ) {
            Pagination {
                PaginationContent {
                    PaginationPrevious(
                        enabled = currentPage > 1,
                        onClick = { currentPage = (currentPage - 1).coerceAtLeast(1) },
                    )

                    buildPaginationTokens(
                        totalPages = totalPages,
                        currentPage = currentPage,
                    ).forEach { token ->
                        when (token) {
                            PaginationToken.Ellipsis -> PaginationEllipsis()
                            is PaginationToken.Page -> PaginationPage(
                                page = token.number,
                                active = token.number == currentPage,
                                onClick = { currentPage = token.number },
                            )
                        }
                    }

                    PaginationNext(
                        enabled = currentPage < totalPages,
                        onClick = { currentPage = (currentPage + 1).coerceAtMost(totalPages) },
                    )
                }
            }
        }

        DemoSection(
            title = stringResource(Res.string.pagination_icons_only),
            description = stringResource(Res.string.pagination_use_just_previous_and_next_buttons_without_page),
        ) {
            Pagination {
                PaginationContent {
                    PaginationPrevious(
                        enabled = iconOnlyPage > 1,
                        showLabel = false,
                        onClick = { iconOnlyPage = (iconOnlyPage - 1).coerceAtLeast(1) },
                    )
                    PaginationNext(
                        enabled = iconOnlyPage < totalPages,
                        showLabel = false,
                        onClick = { iconOnlyPage = (iconOnlyPage + 1).coerceAtMost(totalPages) },
                    )
                }
            }
        }

        Muted(stringResource(Res.string.pagination_usage))
        InlineCode(
            text = """
                var page by remember { mutableIntStateOf(1) }
                val totalPages = 20
                
                Pagination {
                    PaginationContent {
                        PaginationPrevious(
                            enabled = page > 1,
                            onClick = { page-- },
                        )
                        
                        buildPaginationTokens(totalPages, page).forEach { token ->
                            when (token) {
                                PaginationToken.Ellipsis -> PaginationEllipsis()
                                is PaginationToken.Page -> PaginationPage(
                                    page = token.number,
                                    active = token.number == page,
                                    onClick = { page = token.number },
                                )
                            }
                        }
                        
                        PaginationNext(
                            enabled = page < totalPages,
                            onClick = { page++ },
                        )
                    }
                }

                Pagination {
                    PaginationContent {
                        PaginationPrevious(
                            enabled = page > 1,
                            showLabel = false,
                            onClick = { page-- },
                        )
                        PaginationNext(
                            enabled = page < totalPages,
                            showLabel = false,
                            onClick = { page++ },
                        )
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
