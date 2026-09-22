package com.github.jershell.shadcn.components.calendar

import kotlinx.datetime.LocalDate

/**
 * Selection state of a [Calendar].
 */
sealed class CalendarSelection {
    /**
     * A single selected date.
     *
     * @param date Selected date or `null` if nothing is selected.
     */
    data class Single(val date: LocalDate? = null) : CalendarSelection()

    /**
     * Multiple selected dates.
     *
     * @param dates Selected dates.
     */
    data class Multiple(val dates: List<LocalDate> = emptyList()) : CalendarSelection()

    /**
     * A selected date range.
     *
     * @param start Start of the range or `null`.
     * @param end End of the range or `null`.
     */
    data class Range(
        val start: LocalDate? = null,
        val end: LocalDate? = null,
    ) : CalendarSelection()
}

/**
 * Selection mode of a [Calendar].
 */
enum class CalendarSelectionMode {
    Single,
    Multiple,
    Range,
}
