package com.github.jershell.shadcn.components.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import com.composeunstyled.AnchorAlignment
import com.composeunstyled.AnchorSide
import com.composeunstyled.UnstyledButton
import com.composeunstyled.UnstyledIcon
import com.composeunstyled.focusRing
import com.composeunstyled.theme.Theme
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.Lucide
import com.github.jershell.shadcn.components.calendar.Calendar
import com.github.jershell.shadcn.components.calendar.CalendarLocale
import com.github.jershell.shadcn.components.calendar.CalendarSelection
import com.github.jershell.shadcn.components.calendar.CalendarSelectionMode
import com.github.jershell.shadcn.components.calendar.CalendarState
import com.github.jershell.shadcn.components.calendar.rememberCalendarState
import com.github.jershell.shadcn.components.popover.Popover
import com.github.jershell.shadcn.theme.BaseTokens
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.Effects
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles
import com.github.jershell.shadcn.generated.resources.Res
import com.github.jershell.shadcn.generated.resources.date_picker_placeholder
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.DateTimeFormatBuilder

/**
 * Formats a date for the trigger label, e.g. `Jun 12, 2025`.
 */
public fun defaultDatePickerFormatter(locale: CalendarLocale): (LocalDate) -> String = { date ->
    "${locale.shortMonthNames(date.month)} ${date.dayOfMonth}, ${date.year}"
}

/**
 * Wraps a kotlinx-datetime [DateTimeFormat] of [LocalDate] into a trigger date
 * formatter, e.g.
 * ```
 * DatePicker(
 *     dateFormatter = datePickerFormatter(LocalDate.Formats.ISO), // 2025-06-12
 * )
 * ```
 *
 * Formatting errors surface as [kotlinx.datetime.DateTimeFormatException], like the
 * underlying kotlinx-datetime API.
 *
 * @param format The kotlinx-datetime format to use.
 */
public fun datePickerFormatter(
    format: DateTimeFormat<LocalDate>,
): (LocalDate) -> String = { date -> format.format(date) }

/**
 * Builds a trigger date formatter with the kotlinx-datetime format DSL:
 * ```
 * DatePicker(
 *     dateFormatter = datePickerFormatter(LocalDate.Format {
 *         day(); char('.'); monthNumber(); char('.'); year()
 *     }), // 12.06.2025
 * )
 * ```
 *
 * @param block The kotlinx-datetime format DSL, the same block as in [LocalDate.Format].
 */
public fun datePickerFormatter(
    block: DateTimeFormatBuilder.WithDate.() -> Unit,
): (LocalDate) -> String {
    val format = LocalDate.Format(block)
    return { date -> format.format(date) }
}

/**
 * A date picker styled after the shadcn/ui Date Picker example: an outline trigger
 * button showing the selected date (or a muted [placeholder]), opening a popover with
 * a single-mode [Calendar]. Selecting a date reports it through [onSelectedChange]
 * and closes the popover when [closeOnSelect] is set.
 *
 * The panel of the reference is `w-auto p-0` and the calendar carries its own `p-3`
 * padding; here the calendar needs a bounded width, so the panel defaults to a compact
 * `w-64` with `p-3` around the calendar grid and can be changed via [panelWidth].
 *
 * @param selected Currently selected date or `null`.
 * @param onSelectedChange Called when the user picks a date.
 * @param placeholder Trigger text when nothing is selected.
 * @param enabled Whether the trigger is interactive.
 * @param closeOnSelect Whether the popover closes after picking a date.
 * @param locale Calendar localization; also used by the default trigger text.
 * @param dateFormatter Formats the selected date for the trigger text.
 * @param panelWidth Width of the calendar panel.
 * @param triggerIcon Leading icon of the trigger.
 */
@Composable
public fun DatePicker(
    selected: LocalDate?,
    onSelectedChange: (LocalDate?) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = stringResource(Res.string.date_picker_placeholder),
    locale: CalendarLocale = CalendarLocale.Default,
    enabled: Boolean = true,
    closeOnSelect: Boolean = true,
    dateFormatter: (LocalDate) -> String = defaultDatePickerFormatter(locale),
    panelWidth: Dp = BaseTokens.token256, // compact, like the original
    triggerIcon: ImageVector = Lucide.Calendar,
) {
    var open by remember { mutableStateOf(false) }
    val state: CalendarState = rememberCalendarState(
        mode = CalendarSelectionMode.Single,
        initialSelection = CalendarSelection.Single(selected),
    )

    // External changes -> calendar state.
    LaunchedEffect(selected) {
        val current = (state.selection as? CalendarSelection.Single)?.date
        if (current != selected) {
            state.selection = CalendarSelection.Single(selected)
        }
    }

    // Calendar picks -> report and close.
    LaunchedEffect(state.selection) {
        val date = (state.selection as? CalendarSelection.Single)?.date
        if (date != selected) {
            onSelectedChange(date)
            if (closeOnSelect && date != null) {
                open = false
            }
        }
    }

    Popover(
        expanded = open,
        onExpandedChange = { open = it },
        modifier = modifier,
        side = AnchorSide.Bottom,
        alignment = AnchorAlignment.Start,
        sideOffset = TwDimensions.gapGapToken1, // 4px
        anchor = {
            DatePickerTrigger(
                text = selected?.let(dateFormatter) ?: placeholder,
                isPlaceholder = selected == null,
                enabled = enabled,
                icon = triggerIcon,
                onClick = { if (enabled) open = !open },
            )
        },
    ) {
        DatePickerPanel(state = state, locale = locale, width = panelWidth)
    }
}

@Composable
private fun DatePickerTrigger(
    text: String,
    isPlaceholder: Boolean,
    enabled: Boolean,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val colors = resolveDatePickerTriggerColors(
        isHovered = isHovered,
        isPressed = isPressed,
    )
    val radius = Theme[DimProps][DimTokens.radiusMd]
    val shape = RoundedCornerShape(radius)
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val focusRingColor = Theme[ColorProps][ColorTokens.ring]
    val focusRingWidth = Effects.boxShadowFocusRing.spread

    UnstyledButton(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        indication = null,
        role = androidx.compose.ui.semantics.Role.Button,
        modifier = Modifier
            .alpha(if (enabled) 1f else 0.5f)
            .width(BaseTokens.token288) // w-72, equivalent of the reference w-[280px]
            .height(TwDimensions.heightHToken9) // h-9
            .clip(shape)
            .border(borderWidth, colors.border, shape)
            .background(colors.background)
            .focusRing(
                interactionSource = interactionSource,
                width = focusRingWidth,
                color = focusRingColor,
                shape = shape,
            ),
        content = {
            androidx.compose.foundation.layout.Row(
                modifier = Modifier
                    .padding(horizontal = TwDimensions.paddingPxToken3) // px-3
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken2),
            ) {
                UnstyledIcon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(TwDimensions.heightHToken4), // size-4
                    tint = if (isPlaceholder) colors.placeholder else colors.content,
                )
                BasicText(
                    text = text,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    style = TypographyStyles.textSmRegular.copy(
                        color = if (isPlaceholder) colors.placeholder else colors.content,
                    ),
                )
            }
        },
    )
}

@Composable
private fun DatePickerPanel(
    state: CalendarState,
    locale: CalendarLocale,
    width: Dp,
) {
    val radius = Theme[DimProps][DimTokens.radiusMd]
    val shape = RoundedCornerShape(radius)
    val borderColor = Theme[ColorProps][ColorTokens.border]
    val background = Theme[ColorProps][ColorTokens.popover]

    Column(
        modifier = Modifier
            .width(width)
            .clip(shape)
            .shadow(
                elevation = Effects.boxShadowShadowMdToken0.radius,
                shape = shape,
                clip = false,
                ambientColor = Effects.boxShadowShadowMdToken0.color,
                spotColor = Effects.boxShadowShadowMdToken1.color,
            )
            .background(background)
            .border(Theme[DimProps][DimTokens.borderWidth], borderColor, shape)
            .padding(TwDimensions.paddingPxToken3), // p-3: padding around the calendar
        verticalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken1),
    ) {
        Calendar(state = state, locale = locale)
    }
}
