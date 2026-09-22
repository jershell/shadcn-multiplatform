package com.github.jershell.shadcn.components.pagination

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.BasicText
import org.jetbrains.compose.resources.stringResource
import com.composables.icons.lucide.ChevronLeft
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.Ellipsis
import com.composables.icons.lucide.Lucide
import com.github.jershell.shadcn.components.button.Button
import com.github.jershell.shadcn.components.button.ButtonIcon
import com.github.jershell.shadcn.components.button.ButtonSize
import com.github.jershell.shadcn.components.button.ButtonText
import com.github.jershell.shadcn.components.button.ButtonVariant
import com.github.jershell.shadcn.generated.resources.Res
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.generated.resources.pagination_more_pages
import com.github.jershell.shadcn.generated.resources.pagination_next
import com.github.jershell.shadcn.generated.resources.pagination_next_page
import com.github.jershell.shadcn.generated.resources.pagination_previous
import com.github.jershell.shadcn.generated.resources.pagination_previous_page

/**
 * Pagination container, matching the shadcn/ui `Pagination` (`nav` element):
 * `mx-auto flex w-full justify-center`.
 *
 * Full-page content row; page items go into [PaginationContent].
 */
@Composable
fun Pagination(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

/**
 * The items row of a [Pagination], matching `PaginationContent`
 * (`flex flex-row items-center gap-1`).
 */
@Composable
fun PaginationContent(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken1),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

/**
 * «Previous» link, matching `PaginationPrevious` (a `size=default` link with the
 * chevron; the label is hidden on touch-only layouts via [showLabel]).
 */
@Composable
fun PaginationPrevious(
    onClick: () -> Unit,
    enabled: Boolean,
    showLabel: Boolean = true,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        variant = ButtonVariant.Ghost,
        size = if (showLabel) ButtonSize.Sm else ButtonSize.Icon,
    ) {
        ButtonIcon(
            imageVector = Lucide.ChevronLeft,
            contentDescription = stringResource(Res.string.pagination_previous_page),
        )
        if (showLabel) {
            ButtonText(stringResource(Res.string.pagination_previous))
        }
    }
}

/**
 * «Next» link, matching `PaginationNext`.
 */
@Composable
fun PaginationNext(
    onClick: () -> Unit,
    enabled: Boolean,
    showLabel: Boolean = true,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        variant = ButtonVariant.Ghost,
        size = if (showLabel) ButtonSize.Sm else ButtonSize.Icon,
    ) {
        if (showLabel) {
            ButtonText(stringResource(Res.string.pagination_next))
        }
        ButtonIcon(
            imageVector = Lucide.ChevronRight,
            contentDescription = stringResource(Res.string.pagination_next_page),
        )
    }
}

/**
 * A page link, matching `PaginationLink`: `outline` when [active] (with
 * `aria-current="page"` semantics), `ghost` otherwise.
 */
@Composable
fun PaginationPage(
    page: Int,
    active: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        variant = if (active) ButtonVariant.Outline else ButtonVariant.Ghost,
        size = ButtonSize.Icon,
    ) {
        ButtonText(page.toString())
    }
}

/**
 * The «more pages» placeholder, matching `PaginationEllipsis`
 * (`flex size-9 items-center justify-center` + `MoreHorizontalIcon`).
 */
@Composable
fun PaginationEllipsis(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(TwDimensions.heightHToken9),
        contentAlignment = Alignment.Center,
    ) {
        ButtonIcon(
            imageVector = Lucide.Ellipsis,
            contentDescription = stringResource(Res.string.pagination_more_pages),
        )
    }
}

sealed interface PaginationToken {
    data class Page(val number: Int) : PaginationToken
    data object Ellipsis : PaginationToken
}

/**
 * Page tokens of a pagination: first and last page are always shown, the current
 * page is surrounded by [siblingCount] siblings on each side, gaps longer than one
 * page become [PaginationToken.Ellipsis] (the shadcn/ui pattern).
 *
 * Example (`totalPages = 10`, `currentPage = 5`):
 * `1 … 4 5 6 … 10`.
 */
fun buildPaginationTokens(
    totalPages: Int,
    currentPage: Int,
    siblingCount: Int = 1,
): List<PaginationToken> {
    if (totalPages <= 0) return emptyList()
    if (totalPages <= 2) return (1..totalPages).map(PaginationToken::Page)

    val current = currentPage.coerceIn(1, totalPages)
    val siblings = siblingCount.coerceAtLeast(0)

    // the window around the current page, clamped to the inner range 2..last-1
    val start = (current - siblings).coerceIn(2, totalPages - 1)
    val end = (current + siblings).coerceIn(2, totalPages - 1)

    val result = mutableListOf<PaginationToken>()
    result += PaginationToken.Page(1)

    // pages between 1 and the window start: a single straggler is rendered as a
    // page, a gap of two or more pages collapses into an ellipsis (MUI pattern)
    if (start > 2) {
        if (start - 2 > 1) {
            result += PaginationToken.Ellipsis
        } else {
            result += PaginationToken.Page(2)
        }
    }
    result += (start..end).map(PaginationToken::Page)

    val tailGap = totalPages - 1 - end
    if (end < totalPages - 1) {
        if (tailGap > 1) {
            result += PaginationToken.Ellipsis
        } else {
            result += PaginationToken.Page(totalPages - 1)
        }
    }
    result += PaginationToken.Page(totalPages)

    // NOTE: no distinct() here — two Ellipsis tokens are legitimate (left and right
    // gaps) and distinct() used to swallow the trailing one
    return result
}
