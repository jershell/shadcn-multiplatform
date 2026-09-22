package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jershell.shadcn.components.datepicker.DatePicker
import com.github.jershell.shadcn.components.datepicker.datePickerFormatter
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.date_picker_an_externally_provided_date_picking_another_one
import com.github.jershell.shadcn.demoapp.generated.resources.date_picker_birthday
import com.github.jershell.shadcn.demoapp.generated.resources.date_picker_closeonselect_false_browse_the_calendar_without
import com.github.jershell.shadcn.demoapp.generated.resources.date_picker_datepickerformatter_localdate_formats_iso_the_tr
import com.github.jershell.shadcn.demoapp.generated.resources.date_picker_default
import com.github.jershell.shadcn.demoapp.generated.resources.date_picker_initial_value_comes_from_outside_the_trigger_ref
import com.github.jershell.shadcn.demoapp.generated.resources.date_picker_iso_date
import com.github.jershell.shadcn.demoapp.generated.resources.date_picker_keep_open
import com.github.jershell.shadcn.demoapp.generated.resources.date_picker_kotlinx_datetime_formatter
import com.github.jershell.shadcn.demoapp.generated.resources.date_picker_multiple_picks
import com.github.jershell.shadcn.demoapp.generated.resources.date_picker_pick_a_date_the_popover_closes_on_selection_the
import com.github.jershell.shadcn.demoapp.generated.resources.date_picker_preselected
import com.github.jershell.shadcn.demoapp.generated.resources.date_picker_selected_x
import com.github.jershell.shadcn.demoapp.generated.resources.date_picker_selected_x_2
import com.github.jershell.shadcn.demoapp.generated.resources.date_picker_selected_x_3
import com.github.jershell.shadcn.demoapp.generated.resources.date_picker_usage
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoDatePicker() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.date_picker_default),
            description = stringResource(Res.string.date_picker_pick_a_date_the_popover_closes_on_selection_the),
        ) {
            var date by remember { mutableStateOf<LocalDate?>(null) }
            DatePicker(
                selected = date,
                onSelectedChange = { date = it },
            )
            val selectedDate1 = date
            if (selectedDate1 != null) {
                P(stringResource(Res.string.date_picker_selected_x, selectedDate1))
            }
        }

        DemoSection(
            title = stringResource(Res.string.date_picker_preselected),
            description = stringResource(Res.string.date_picker_an_externally_provided_date_picking_another_one),
        ) {
            var date by remember { mutableStateOf<LocalDate?>(null) }
            DatePicker(
                selected = date,
                onSelectedChange = { date = it },
                placeholder = stringResource(Res.string.date_picker_birthday),
            )
            Muted(stringResource(Res.string.date_picker_initial_value_comes_from_outside_the_trigger_ref))
        }

        DemoSection(
            title = stringResource(Res.string.date_picker_keep_open),
            description = stringResource(Res.string.date_picker_closeonselect_false_browse_the_calendar_without),
        ) {
            var date by remember { mutableStateOf<LocalDate?>(null) }
            DatePicker(
                selected = date,
                onSelectedChange = { date = it },
                closeOnSelect = false,
                placeholder = stringResource(Res.string.date_picker_multiple_picks),
            )
            val selectedDate_2 = date
            if (selectedDate_2 != null) {
                P(stringResource(Res.string.date_picker_selected_x_2, selectedDate_2))
            }
        }

        DemoSection(
            title = stringResource(Res.string.date_picker_kotlinx_datetime_formatter),
            description = stringResource(Res.string.date_picker_datepickerformatter_localdate_formats_iso_the_tr),
        ) {
            var date by remember { mutableStateOf<LocalDate?>(null) }
            DatePicker(
                selected = date,
                onSelectedChange = { date = it },
                dateFormatter = datePickerFormatter(LocalDate.Formats.ISO),
                placeholder = stringResource(Res.string.date_picker_iso_date),
            )
            val selectedDate_3 = date
            if (selectedDate_3 != null) {
                P(stringResource(Res.string.date_picker_selected_x_3, selectedDate_3))
            }
        }

        P(stringResource(Res.string.date_picker_usage))
        InlineCode(
            text = """
                var date by remember { mutableStateOf<LocalDate?>(null) }
                DatePicker(
                    selected = date,
                    onSelectedChange = { date = it },
                    dateFormatter = datePickerFormatter(LocalDate.Formats.ISO),
                    placeholder = "Pick a date",
                )
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
