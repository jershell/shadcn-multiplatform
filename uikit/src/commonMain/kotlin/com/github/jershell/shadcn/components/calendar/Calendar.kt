package com.github.jershell.shadcn.components.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.AnchorAlignment
import com.composeunstyled.AnchorSide
import com.composeunstyled.DropdownMenuPanel
import com.composeunstyled.MenuItem
import com.composeunstyled.UnstyledButton
import com.composeunstyled.UnstyledDropdownMenu
import com.composeunstyled.theme.Theme
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.ChevronLeft
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.Lucide
import com.github.jershell.shadcn.components.icon.ShadcnIconContent
import com.github.jershell.shadcn.components.icon.toShadcnIcon
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles
import com.github.jershell.shadcn.generated.resources.Res
import com.github.jershell.shadcn.generated.resources.calendar_next_month
import com.github.jershell.shadcn.generated.resources.calendar_previous_month
import kotlin.time.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn

private val DayPadding = 2.dp
private val TodayBorderWidth = 2.dp
private val RangeBarVerticalPadding = 4.dp

/**
 * A calendar component styled after shadcn/ui.
 *
 * Supports single, multiple, and range selection. Localization is provided through a [CalendarLocale];
 * the default locale uses English names and Sunday as the first day of the week.
 *
 * @param state Calendar state holder. Create with [rememberCalendarState].
 * @param modifier Modifier applied to the calendar container.
 * @param locale Localization and first-day-of-week configuration.
 * @param showOutsideDays Whether to render days from the previous and next months.
 */
@Composable
fun Calendar(
    state: CalendarState,
    modifier: Modifier = Modifier,
    locale: CalendarLocale = CalendarLocale.Default,
    showOutsideDays: Boolean = true,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken3),
    ) {
        CalendarHeader(state, locale)
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val cellWidth = maxWidth / 7
            Column(modifier = Modifier.fillMaxWidth()) {
                CalendarWeekdayHeader(locale)
                CalendarMonthGrid(state, locale, showOutsideDays, cellWidth)
            }
        }
    }
}

@Composable
private fun CalendarHeader(state: CalendarState, locale: CalendarLocale) {
    val colors = resolveCalendarColors()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        UnstyledButton(onClick = { state.previousMonth() }) {
            ShadcnIconContent(
                icon = Lucide.ChevronLeft.toShadcnIcon(),
                contentDescription = stringResource(Res.string.calendar_previous_month),
                modifier = Modifier.size(TwDimensions.heightHToken5),
                tint = colors.navigationButtonForeground,
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken2),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val monthOptions = remember(locale) {
                Month.entries.map { locale.monthNames(it) }
            }
            CalendarHeaderDropdown(
                value = locale.monthNames(state.currentMonth.month),
                options = monthOptions,
                scrollToIndex = state.currentMonth.month.ordinal,
                onOptionSelected = { state.setMonth(Month.entries[it]) },
            )
            val yearOptions = remember { (1900..2100).map { it.toString() } }
            val yearIndex = yearOptions.indexOf(state.currentMonth.year.toString())
            CalendarHeaderDropdown(
                value = state.currentMonth.year.toString(),
                options = yearOptions,
                scrollToIndex = yearIndex,
                onOptionSelected = { state.setYear(1900 + it) },
            )
        }
        UnstyledButton(onClick = { state.nextMonth() }) {
            ShadcnIconContent(
                icon = Lucide.ChevronRight.toShadcnIcon(),
                contentDescription = stringResource(Res.string.calendar_next_month),
                modifier = Modifier.size(TwDimensions.heightHToken5),
                tint = colors.navigationButtonForeground,
            )
        }
    }
}

@Composable
private fun CalendarHeaderDropdown(
    value: String,
    options: List<String>,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    scrollToIndex: Int = 0,
) {
    var open by remember { mutableStateOf(false) }
    val colors = resolveCalendarColors()
    val radius = Theme[DimProps][DimTokens.radiusMd]
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val panelShape = RoundedCornerShape(radius)
    val background = Theme[ColorProps][ColorTokens.popover]
    val border = Theme[ColorProps][ColorTokens.border]
    val content = Theme[ColorProps][ColorTokens.foreground]
    val selectedBg = Theme[ColorProps][ColorTokens.accent]
    val selectedContent = Theme[ColorProps][ColorTokens.accentForeground]
    val itemRadius = Theme[DimProps][DimTokens.radiusSm]

    UnstyledDropdownMenu(
        expanded = open,
        onExpandedChange = { open = it },
        modifier = modifier,
        side = AnchorSide.Bottom,
        alignment = AnchorAlignment.Start,
        sideOffset = TwDimensions.gapGapToken1,
        anchor = {
            UnstyledButton(
                onClick = { open = !open },
                indication = null,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken1),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BasicText(
                        text = value,
                        style = TypographyStyles.textSmSemiBold.copy(color = colors.headerForeground),
                    )
                    ShadcnIconContent(
                        icon = Lucide.ChevronDown.toShadcnIcon(),
                        contentDescription = null,
                        modifier = Modifier.size(TwDimensions.heightHToken4),
                        tint = colors.headerForeground,
                    )
                }
            }
        },
        panel = {
            DropdownMenuPanel(
                modifier = Modifier
                    .width(120.dp)
                    .heightIn(max = 240.dp)
                    .clip(panelShape)
                    .background(background)
                    .border(borderWidth, border, panelShape)
                    .padding(vertical = TwDimensions.paddingPxToken1),
            ) {
                val listState = rememberLazyListState()
                LaunchedEffect(Unit) {
                    if (scrollToIndex in options.indices) {
                        listState.scrollToItem(scrollToIndex)
                    }
                }
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    itemsIndexed(options) { index, option ->
                        val selected = option == value
                        MenuItem(
                            onClick = {
                                onOptionSelected(index)
                                open = false
                            },
                            closeOnClick = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = TwDimensions.paddingPxToken1, vertical = TwDimensions.paddingPPx)
                                .clip(RoundedCornerShape(itemRadius))
                                .background(if (selected) selectedBg else Color.Transparent),
                        ) {
                            BasicText(
                                text = option,
                                modifier = Modifier.padding(
                                    horizontal = TwDimensions.paddingPxToken2,
                                    vertical = TwDimensions.paddingPxToken1,
                                ),
                                style = TypographyStyles.textSmRegular.copy(
                                    color = if (selected) selectedContent else content,
                                ),
                            )
                        }
                    }
                }
            }
        },
    )
}

@Composable
private fun CalendarWeekdayHeader(locale: CalendarLocale) {
    val colors = resolveCalendarColors()
    Row(modifier = Modifier.fillMaxWidth()) {
        val days = remember(locale.firstDayOfWeek) {
            orderedDaysOfWeek(locale.firstDayOfWeek)
        }
        days.forEach { day ->
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                BasicText(
                    text = locale.shortWeekdayNames(day),
                    style = TypographyStyles.textXsMedium.copy(color = colors.weekdayHeaderForeground),
                )
            }
        }
    }
}

@Composable
private fun CalendarMonthGrid(
    state: CalendarState,
    locale: CalendarLocale,
    showOutsideDays: Boolean,
    cellWidth: Dp,
) {
    val days = remember(state.currentMonth, locale.firstDayOfWeek) {
        generateMonthGrid(state.currentMonth, locale.firstDayOfWeek)
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken1),
    ) {
        days.chunked(7).forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { day ->
                    CalendarDay(
                        day = day,
                        state = state,
                        locale = locale,
                        showOutsideDays = showOutsideDays,
                        cellWidth = cellWidth,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarDay(
    day: LocalDate,
    state: CalendarState,
    locale: CalendarLocale,
    showOutsideDays: Boolean,
    cellWidth: Dp,
    modifier: Modifier = Modifier,
) {
    val colors = resolveCalendarColors()
    val dayState = remember(day, state.selection, state.currentMonth, state.disabledDates) {
        computeDayState(day, state)
    }
    if (dayState.isOutside && !showOutsideDays) {
        Box(modifier = modifier.aspectRatio(1f))
        return
    }
    val radius = Theme[DimProps][DimTokens.radiusMd]

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isSelectedLike = dayState.isSelected || dayState.isRangeStart || dayState.isRangeEnd
    val isInAnySelection = isSelectedLike || dayState.isRangeMiddle

    val innerFillColor = when {
        isSelectedLike -> colors.selectedBackground
        dayState.isRangeMiddle -> Color.Transparent
        isHovered && !dayState.isDisabled -> colors.hoverBackground
        dayState.isToday -> Color.Transparent
        else -> Color.Transparent
    }
    val textColor = when {
        isSelectedLike -> colors.selectedForeground
        dayState.isRangeMiddle -> colors.rangeForeground
        dayState.isOutside -> colors.outsideForeground
        else -> colors.dayForeground
    }
    val showTodayBorder = dayState.isToday && !isInAnySelection
    val textAlpha = if (dayState.isDisabled) colors.disabledAlpha else 1f

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .then(
                if (showTodayBorder) {
                    Modifier.border(TodayBorderWidth, colors.todayBorder, RoundedCornerShape(radius + TodayBorderWidth))
                } else {
                    Modifier
                },
            )
            .clickable(
                enabled = !dayState.isDisabled,
                onClick = { state.selectDay(day) },
                indication = null,
                interactionSource = interactionSource,
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (dayState.isRangeStart || dayState.isRangeEnd || dayState.isRangeMiddle) {
            val barHeight = cellWidth - RangeBarVerticalPadding * 2
            val halfBarHeight = barHeight / 2
            val barShape = when {
                dayState.isRangeStart && !dayState.isRangeEnd -> RoundedCornerShape(
                    topStart = halfBarHeight,
                    topEnd = 0.dp,
                    bottomStart = halfBarHeight,
                    bottomEnd = 0.dp,
                )
                dayState.isRangeEnd && !dayState.isRangeStart -> RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = halfBarHeight,
                    bottomStart = 0.dp,
                    bottomEnd = halfBarHeight,
                )
                else -> RectangleShape
            }
            Box(
                modifier = Modifier
                    .padding(vertical = RangeBarVerticalPadding)
                    .fillMaxSize()
                    .background(colors.rangeBackground, barShape),
            )
        }
        if (innerFillColor != Color.Transparent) {
            Box(
                modifier = Modifier
                    .padding(DayPadding)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(radius))
                    .background(innerFillColor),
                contentAlignment = Alignment.Center,
            ) {
                BasicText(
                    text = day.day.toString(),
                    modifier = Modifier.alpha(textAlpha),
                    style = TypographyStyles.textSmMedium.copy(color = textColor),
                )
            }
        } else {
            BasicText(
                text = day.day.toString(),
                modifier = Modifier.alpha(textAlpha),
                style = TypographyStyles.textSmMedium.copy(color = textColor),
            )
        }
    }
}

private data class DayState(
    val isSelected: Boolean,
    val isToday: Boolean,
    val isRangeSingle: Boolean,
    val isRangeStart: Boolean,
    val isRangeEnd: Boolean,
    val isRangeMiddle: Boolean,
    val isOutside: Boolean,
    val isDisabled: Boolean,
)

private fun computeDayState(day: LocalDate, state: CalendarState): DayState {
    val currentMonth = state.currentMonth
    val isOutside = day.month != currentMonth.month || day.year != currentMonth.year
    val isDisabled = state.disabledDates(day)
    val isToday = day == Clock.System.todayIn(TimeZone.currentSystemDefault())
    val isSingleSelected = when (val sel = state.selection) {
        is CalendarSelection.Single -> sel.date == day
        is CalendarSelection.Multiple -> sel.dates.contains(day)
        is CalendarSelection.Range -> false
    }
    val range = state.selection as? CalendarSelection.Range
    val isRangeSingle = range?.start != null && range.start == range.end
    val isRangePending = range?.start == day && range.end == null
    val isRangeStart = range?.start == day && range.end != null && range.end != day
    val isRangeEnd = range?.end == day && range.start != null && range.start != day
    val isRangeMiddle = range?.start != null && range.end != null && range.start != range.end &&
        day > range.start && day < range.end
    return DayState(
        isSelected = isSingleSelected || isRangeSingle || isRangePending,
        isToday = isToday,
        isRangeSingle = isRangeSingle,
        isRangeStart = isRangeStart,
        isRangeEnd = isRangeEnd,
        isRangeMiddle = isRangeMiddle,
        isOutside = isOutside,
        isDisabled = isDisabled,
    )
}

private fun generateMonthGrid(month: LocalDate, firstDayOfWeek: DayOfWeek): List<LocalDate> {
    val monthStart = LocalDate(month.year, month.month, 1)
    val firstDayWeekIndex = monthStart.dayOfWeek.isoDayNumber - 1 // 0=Monday
    val startDayWeekIndex = firstDayOfWeek.isoDayNumber - 1 // 0=Monday
    val offset = (firstDayWeekIndex - startDayWeekIndex + 7) % 7
    val gridStart = monthStart.minus(offset, DateTimeUnit.DAY)
    return (0 until 42).map { gridStart.plus(it, DateTimeUnit.DAY) }
}

private fun orderedDaysOfWeek(firstDayOfWeek: DayOfWeek): List<DayOfWeek> {
    return (0..6).map { index ->
        val isoDayNumber = (firstDayOfWeek.isoDayNumber - 1 + index) % 7 + 1
        DayOfWeek.entries.first { it.isoDayNumber == isoDayNumber }
    }
}
