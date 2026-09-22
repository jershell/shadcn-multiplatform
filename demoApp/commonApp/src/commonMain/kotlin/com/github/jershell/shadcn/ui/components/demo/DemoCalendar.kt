package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.calendar.Calendar
import com.github.jershell.shadcn.components.calendar.CalendarLocale
import com.github.jershell.shadcn.components.calendar.CalendarSelection
import com.github.jershell.shadcn.components.calendar.CalendarSelectionMode
import com.github.jershell.shadcn.components.calendar.rememberCalendarState
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.calendar_date
import com.github.jershell.shadcn.demoapp.generated.resources.calendar_date_range
import com.github.jershell.shadcn.demoapp.generated.resources.calendar_disabled_dates
import com.github.jershell.shadcn.demoapp.generated.resources.calendar_localized_month_and_weekday_names_monday_as_the
import com.github.jershell.shadcn.demoapp.generated.resources.calendar_multiple_dates
import com.github.jershell.shadcn.demoapp.generated.resources.calendar_pick_any_number_of_dates
import com.github.jershell.shadcn.demoapp.generated.resources.calendar_russian_locale
import com.github.jershell.shadcn.demoapp.generated.resources.calendar_select_a_start_and_end_date
import com.github.jershell.shadcn.demoapp.generated.resources.calendar_selected_x
import com.github.jershell.shadcn.demoapp.generated.resources.calendar_selected_x_2
import com.github.jershell.shadcn.demoapp.generated.resources.calendar_selected_x_3
import com.github.jershell.shadcn.demoapp.generated.resources.calendar_selected_x_4
import com.github.jershell.shadcn.demoapp.generated.resources.calendar_selected_x_5
import com.github.jershell.shadcn.demoapp.generated.resources.calendar_single_date_selection
import com.github.jershell.shadcn.demoapp.generated.resources.calendar_usage
import com.github.jershell.shadcn.demoapp.generated.resources.calendar_weekends_are_not_selectable
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoCalendar() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        val singleState = rememberCalendarState()
        DemoSection(
            title = stringResource(Res.string.calendar_date),
            description = stringResource(Res.string.calendar_single_date_selection),
        ) {
            CalendarBox { Calendar(state = singleState) }
            Muted(stringResource(Res.string.calendar_selected_x, singleState.selection.toDisplayString()))
        }

        val rangeState = rememberCalendarState(mode = CalendarSelectionMode.Range)
        DemoSection(
            title = stringResource(Res.string.calendar_date_range),
            description = stringResource(Res.string.calendar_select_a_start_and_end_date),
        ) {
            CalendarBox { Calendar(state = rangeState) }
            Muted(stringResource(Res.string.calendar_selected_x_2, rangeState.selection.toDisplayString()))
        }

        val multipleState = rememberCalendarState(mode = CalendarSelectionMode.Multiple)
        DemoSection(
            title = stringResource(Res.string.calendar_multiple_dates),
            description = stringResource(Res.string.calendar_pick_any_number_of_dates),
        ) {
            CalendarBox { Calendar(state = multipleState) }
            Muted(stringResource(Res.string.calendar_selected_x_3, multipleState.selection.toDisplayString()))
        }

        val disabledState = rememberCalendarState(
            disabledDates = {
                it.dayOfWeek == DayOfWeek.SATURDAY || it.dayOfWeek == DayOfWeek.SUNDAY
            },
        )
        DemoSection(
            title = stringResource(Res.string.calendar_disabled_dates),
            description = stringResource(Res.string.calendar_weekends_are_not_selectable),
        ) {
            CalendarBox { Calendar(state = disabledState) }
            Muted(stringResource(Res.string.calendar_selected_x_4, disabledState.selection.toDisplayString()))
        }

        val russianState = rememberCalendarState()
        DemoSection(
            title = stringResource(Res.string.calendar_russian_locale),
            description = stringResource(Res.string.calendar_localized_month_and_weekday_names_monday_as_the),
        ) {
            CalendarBox { Calendar(state = russianState, locale = russianLocale) }
            Muted(stringResource(Res.string.calendar_selected_x_5, russianState.selection.toDisplayString()))
        }

        Muted(stringResource(Res.string.calendar_usage))
        InlineCode(
            text = """
                val single = rememberCalendarState()
                Calendar(state = single)

                val range = rememberCalendarState(mode = CalendarSelectionMode.Range)
                Calendar(state = range)
            """.trimIndent(),
            selected = false,
            selectEnabled = true,
            copyEnabled = true,
        )
    }
}

@Composable
private fun CalendarBox(content: @Composable () -> Unit) {
    val background = Theme[ColorProps][ColorTokens.card]
    val border = Theme[ColorProps][ColorTokens.border]
    val radius = Theme[DimProps][DimTokens.radiusMd]
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val shape = RoundedCornerShape(radius)

    Box(
        modifier = Modifier
            .width(300.dp)
            .clip(shape)
            .background(background, shape)
            .border(borderWidth, border, shape)
            .padding(TwDimensions.paddingPxToken4),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

private val russianLocale = CalendarLocale(
    monthNames = { it.russianName() },
    shortMonthNames = { it.russianShortName() },
    weekdayNames = { it.russianName() },
    shortWeekdayNames = { it.russianShortName() },
    firstDayOfWeek = DayOfWeek.MONDAY,
)

private fun Month.russianName(): String = when (this) {
    Month.JANUARY -> "Январь"
    Month.FEBRUARY -> "Февраль"
    Month.MARCH -> "Март"
    Month.APRIL -> "Апрель"
    Month.MAY -> "Май"
    Month.JUNE -> "Июнь"
    Month.JULY -> "Июль"
    Month.AUGUST -> "Август"
    Month.SEPTEMBER -> "Сентябрь"
    Month.OCTOBER -> "Октябрь"
    Month.NOVEMBER -> "Ноябрь"
    Month.DECEMBER -> "Декабрь"
}

private fun Month.russianShortName(): String = when (this) {
    Month.JANUARY -> "Янв"
    Month.FEBRUARY -> "Фев"
    Month.MARCH -> "Мар"
    Month.APRIL -> "Апр"
    Month.MAY -> "Май"
    Month.JUNE -> "Июн"
    Month.JULY -> "Июл"
    Month.AUGUST -> "Авг"
    Month.SEPTEMBER -> "Сен"
    Month.OCTOBER -> "Окт"
    Month.NOVEMBER -> "Ноя"
    Month.DECEMBER -> "Дек"
}

private fun DayOfWeek.russianName(): String = when (this) {
    DayOfWeek.MONDAY -> "Понедельник"
    DayOfWeek.TUESDAY -> "Вторник"
    DayOfWeek.WEDNESDAY -> "Среда"
    DayOfWeek.THURSDAY -> "Четверг"
    DayOfWeek.FRIDAY -> "Пятница"
    DayOfWeek.SATURDAY -> "Суббота"
    DayOfWeek.SUNDAY -> "Воскресенье"
}

private fun DayOfWeek.russianShortName(): String = when (this) {
    DayOfWeek.MONDAY -> "Пн"
    DayOfWeek.TUESDAY -> "Вт"
    DayOfWeek.WEDNESDAY -> "Ср"
    DayOfWeek.THURSDAY -> "Чт"
    DayOfWeek.FRIDAY -> "Пт"
    DayOfWeek.SATURDAY -> "Сб"
    DayOfWeek.SUNDAY -> "Вс"
}

private fun CalendarSelection.toDisplayString(): String = when (this) {
    is CalendarSelection.Single -> date?.toString() ?: "None"
    is CalendarSelection.Multiple -> if (dates.isEmpty()) "None" else dates.joinToString()
    is CalendarSelection.Range -> "${start ?: "…"} - ${end ?: "…"}"
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
