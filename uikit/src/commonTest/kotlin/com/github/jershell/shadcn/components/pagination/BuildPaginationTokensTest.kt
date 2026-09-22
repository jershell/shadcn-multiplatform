package com.github.jershell.shadcn.components.pagination

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BuildPaginationTokensTest {

    private fun pageNumbers(tokens: List<PaginationToken>): List<Int> =
        tokens.filterIsInstance<PaginationToken.Page>().map { it.number }

    private fun ellipsisCount(tokens: List<PaginationToken>): Int =
        tokens.count { it == PaginationToken.Ellipsis }

    @Test
    fun emptyAndSingle() {
        assertEquals(emptyList(), buildPaginationTokens(0, 1))
        assertEquals(listOf(1), pageNumbers(buildPaginationTokens(1, 1)))
    }

    @Test
    fun twoPagesNoEllipsis() {
        val tokens = buildPaginationTokens(2, 1)
        assertEquals(listOf(1, 2), pageNumbers(tokens))
        assertEquals(0, ellipsisCount(tokens))
    }

    @Test
    fun currentAtFirstWithTotalTen() {
        // 1 2 ... 10
        val tokens = buildPaginationTokens(10, 1)
        assertEquals(listOf(1, 2, 10), pageNumbers(tokens))
        assertEquals(1, ellipsisCount(tokens))
    }

    @Test
    fun currentInMiddle() {
        // 1 ... 4 5 6 ... 10
        val tokens = buildPaginationTokens(10, 5)
        assertEquals(listOf(1, 4, 5, 6, 10), pageNumbers(tokens))
        assertEquals(2, ellipsisCount(tokens))
        // the current page sits between the two ellipses (the window pages)
        val firstEllipsis = tokens.indexOf(PaginationToken.Ellipsis)
        val lastEllipsis = tokens.lastIndexOf(PaginationToken.Ellipsis)
        val windowPages = pageNumbers(tokens.subList(firstEllipsis + 1, lastEllipsis))
        assertEquals(listOf(4, 5, 6), windowPages)
    }

    @Test
    fun currentAtLast() {
        // 1 ... 9 10
        val tokens = buildPaginationTokens(10, 10)
        assertEquals(listOf(1, 9, 10), pageNumbers(tokens))
        assertEquals(1, ellipsisCount(tokens))
    }

    @Test
    fun windowAdjacentToEdgesCollapses() {
        // current=2: window 1..3 — pages 2,3 contiguous with page 1, gap after -> one ellipsis
        val tokens = buildPaginationTokens(10, 2)
        assertEquals(listOf(1, 2, 3, 10), pageNumbers(tokens))
        assertEquals(1, ellipsisCount(tokens))
    }

    @Test
    fun noPageIsEverLostInShortRanges() {
        // all pages of a short range must be present (the old impl dropped page 2)
        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7), pageNumbers(buildPaginationTokens(7, 4)))
    }

    @Test
    fun currentPageIsAlwaysRendered() {
        for (current in 1..25) {
            val tokens = buildPaginationTokens(25, current)
            assertTrue(
                current in pageNumbers(tokens),
                "current page $current must be present, got: ${pageNumbers(tokens)}",
            )
        }
    }

    @Test
    fun noDuplicates() {
        val tokens = buildPaginationTokens(25, 13)
        val numbers = pageNumbers(tokens)
        assertEquals(numbers.size, numbers.distinct().size)
        assertFalse(tokens.zipWithNext().any { it.first == it.second })
    }

    @Test
    fun zeroSiblingsShowsOnlyEdgesAndCurrent() {
        val tokens = buildPaginationTokens(25, 13, siblingCount = 0)
        assertEquals(listOf(1, 13, 25), pageNumbers(tokens))
    }
}
