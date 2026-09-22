package com.github.jershell.shadcn.components.calendar

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month

typealias MonthNameProvider = (Month) -> String
typealias WeekdayNameProvider = (DayOfWeek) -> String

/**
 * Localization and formatting options for a [Calendar].
 *
 * The library provides only English defaults because [kotlinx.datetime] names are in English.
 * Pass a custom locale to translate month names, weekday names, and to change the first day of the week.
 *
 * @param monthNames Full month names, e.g. "January".
 * @param shortMonthNames Short month names, e.g. "Jan".
 * @param weekdayNames Full weekday names, e.g. "Monday".
 * @param shortWeekdayNames Short weekday names used for the grid header, e.g. "Mo".
 * @param firstDayOfWeek The first day of the week for the calendar grid.
 */
data class CalendarLocale(
    val monthNames: MonthNameProvider,
    val shortMonthNames: MonthNameProvider,
    val weekdayNames: WeekdayNameProvider,
    val shortWeekdayNames: WeekdayNameProvider,
    val firstDayOfWeek: DayOfWeek,
) {
    companion object {
        /**
         * English locale with Sunday as the first day of the week, matching the default shadcn/ui calendar.
         */
        val Default: CalendarLocale = CalendarLocale(
            monthNames = { month -> month.name.lowercase().replaceFirstChar { it.uppercaseChar() } },
            shortMonthNames = { month -> month.name.take(3).lowercase().replaceFirstChar { it.uppercaseChar() } },
            weekdayNames = { day -> day.name.lowercase().replaceFirstChar { it.uppercaseChar() } },
            shortWeekdayNames = { day -> day.name.take(2).lowercase().replaceFirstChar { it.uppercaseChar() } },
            firstDayOfWeek = DayOfWeek.SUNDAY,
        )
    }
}
