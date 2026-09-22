package com.github.jershell.shadcn.components.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlin.time.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn

/**
 * State holder for a [Calendar].
 *
 * @param initialMonth The month currently displayed. The day is normalized to the first day of the month.
 * @param initialSelection Initial selection state.
 * @param mode Selection mode.
 * @param disabledDates Predicate that returns `true` for dates that cannot be selected.
 */
class CalendarState(
    initialMonth: LocalDate,
    initialSelection: CalendarSelection,
    val mode: CalendarSelectionMode,
    val disabledDates: (LocalDate) -> Boolean,
) {
    /** The month currently displayed. */
    var currentMonth: LocalDate by mutableStateOf(initialMonth.toMonthStart())

    /** The current selection. */
    var selection: CalendarSelection by mutableStateOf(initialSelection)

    /** Move to the previous month. */
    fun previousMonth() {
        currentMonth = currentMonth.minus(1, DateTimeUnit.MONTH)
    }

    /** Move to the next month. */
    fun nextMonth() {
        currentMonth = currentMonth.plus(1, DateTimeUnit.MONTH)
    }

    /** Display the given month, keeping the current year. */
    fun setMonth(month: Month) {
        currentMonth = LocalDate(currentMonth.year, month, 1)
    }

    /** Display the given year, keeping the current month. */
    fun setYear(year: Int) {
        currentMonth = LocalDate(year, currentMonth.month, 1)
    }

    /** Select or toggle a day, depending on the [mode]. */
    fun selectDay(day: LocalDate) {
        if (disabledDates(day)) return
        selection = when (mode) {
            CalendarSelectionMode.Single -> CalendarSelection.Single(day)
            CalendarSelectionMode.Multiple -> toggleMultiple(day)
            CalendarSelectionMode.Range -> updateRange(day)
        }
    }

    private fun toggleMultiple(day: LocalDate): CalendarSelection.Multiple {
        val current = (selection as? CalendarSelection.Multiple)?.dates ?: emptyList()
        return if (current.contains(day)) {
            CalendarSelection.Multiple(current - day)
        } else {
            CalendarSelection.Multiple(current + day)
        }
    }

    private fun updateRange(day: LocalDate): CalendarSelection.Range {
        val current = selection as? CalendarSelection.Range
        return when {
            current == null || current.start == null -> CalendarSelection.Range(start = day, end = null)
            current.end == null -> if (day < current.start) {
                CalendarSelection.Range(start = day, end = current.start)
            } else {
                CalendarSelection.Range(start = current.start, end = day)
            }
            else -> CalendarSelection.Range(start = day, end = null)
        }
    }
}

/**
 * Creates and remembers a [CalendarState].
 *
 * @param initialMonth The month to display initially. Defaults to the current date.
 * @param initialSelection Initial selection state.
 * @param mode Selection mode.
 * @param disabledDates Predicate for disabled dates.
 */
@Composable
fun rememberCalendarState(
    initialMonth: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    initialSelection: CalendarSelection = CalendarSelection.Single(),
    mode: CalendarSelectionMode = CalendarSelectionMode.Single,
    disabledDates: (LocalDate) -> Boolean = { false },
): CalendarState = remember {
    CalendarState(initialMonth.toMonthStart(), initialSelection, mode, disabledDates)
}

private fun LocalDate.toMonthStart(): LocalDate = LocalDate(year, month, 1)
