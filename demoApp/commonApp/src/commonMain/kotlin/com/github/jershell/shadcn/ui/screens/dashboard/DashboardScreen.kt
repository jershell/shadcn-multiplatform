package com.github.jershell.shadcn.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.CalendarDays
import com.composables.icons.lucide.CircleCheckBig
import com.composables.icons.lucide.CircleX
import com.composables.icons.lucide.Ellipsis
import com.composables.icons.lucide.LoaderCircle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.TrendingDown
import com.composables.icons.lucide.TrendingUp
import com.composables.icons.lucide.Upload
import com.composables.icons.lucide.X
import com.composeunstyled.Text
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.badge.Badge
import com.github.jershell.shadcn.components.badge.BadgeVariant
import com.github.jershell.shadcn.components.button.Button
import com.github.jershell.shadcn.components.button.ButtonIcon
import com.github.jershell.shadcn.components.button.ButtonSize
import com.github.jershell.shadcn.components.button.ButtonText
import com.github.jershell.shadcn.components.button.ButtonVariant
import com.github.jershell.shadcn.components.calendar.Calendar
import com.github.jershell.shadcn.components.calendar.rememberCalendarState
import com.github.jershell.shadcn.components.card.Card
import com.github.jershell.shadcn.components.card.CardContent
import com.github.jershell.shadcn.components.card.CardDescription
import com.github.jershell.shadcn.components.card.CardFooter
import com.github.jershell.shadcn.components.card.CardHeader
import com.github.jershell.shadcn.components.card.CardTitle
import com.github.jershell.shadcn.components.checkbox.Checkbox
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenu
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuContent
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuItem
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuItemIcon
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuItemText
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuItemVariant
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuLabel
import com.github.jershell.shadcn.components.dropdownmenu.DropdownMenuShortcut
import com.github.jershell.shadcn.components.icon.ShadcnIcon
import com.github.jershell.shadcn.components.icon.toShadcnIcon
import com.github.jershell.shadcn.components.input.Input
import com.github.jershell.shadcn.components.pagination.Pagination
import com.github.jershell.shadcn.components.pagination.PaginationContent
import com.github.jershell.shadcn.components.pagination.PaginationEllipsis
import com.github.jershell.shadcn.components.pagination.PaginationNext
import com.github.jershell.shadcn.components.pagination.PaginationPage
import com.github.jershell.shadcn.components.pagination.PaginationPrevious
import com.github.jershell.shadcn.components.pagination.PaginationToken
import com.github.jershell.shadcn.components.pagination.buildPaginationTokens
import com.github.jershell.shadcn.components.progress.Progress
import com.github.jershell.shadcn.components.radio.RadioGroup
import com.github.jershell.shadcn.components.select.Select
import com.github.jershell.shadcn.components.switch.Switch
import com.github.jershell.shadcn.components.table.DataTableColumn
import com.github.jershell.shadcn.components.table.Table
import com.github.jershell.shadcn.components.tabs.Tabs
import com.github.jershell.shadcn.components.textarea.Textarea
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.Small
import com.github.jershell.shadcn.models.DataItem
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.ShadcnPreset
import com.github.jershell.shadcn.theme.TypographyStyles
import com.github.jershell.shadcn.ui.components.demo.shadcnLineChartStyle
import com.github.jershell.shadcn.ui.components.demo.shadcnStackedBarChartStyle
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_12_5
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_12_months
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_30_days
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_3_months
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_4_2
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_actions
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_active_accounts
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_all_statuses
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_amount
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_astro
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_badges
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_c
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_cancel
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_copy_email
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_create_project
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_current_palette_tokens
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_dashboard
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_date_range
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_default
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_delta_down_20_1_from_last_month
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_delta_up_12_5_from_last_month
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_delta_up_15_3_from_last_month
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_delta_up_5_2_from_last_month
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_deploy
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_deploy_your_new_project_in_one_click
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_deploying
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_description
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_desktop
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_desktop_and_mobile_visitors
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_destructive
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_email_abigail_king
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_email_amelia_clark
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_email_ava_anderson
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_email_benjamin_hall
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_email_daniel_walker
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_email_emma_taylor
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_email_ethan_brown
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_email_evelyn_young
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_email_harper_lewis
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_email_henry_wright
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_email_isabella_nguyen
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_email_jackson_lee
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_email_liam_moore
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_email_lucas_rodriguez
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_email_mason_thomas
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_email_me_when_the_deploy_finishes
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_email_mia_wilson
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_email_noah_taylor
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_email_olivia_martin
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_email_sofia_davis
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_email_william_kim
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_export
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_failed
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_filter_sales
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_framework
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_growth_rate
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_i_agree_to_the_terms_and_conditions
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_jun
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_last_12_months
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_last_7_days
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_method
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_method_bank_transfer
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_method_credit_card
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_method_paypal
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_mobile
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_month_apr
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_month_aug
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_month_dec
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_month_feb
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_month_jan
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_month_jul
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_month_mar
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_month_may
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_month_nov
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_month_oct
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_monthly_performance_across_your_team
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_more_actions
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_name
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_name_abigail_king
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_name_amelia_clark
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_name_ava_anderson
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_name_benjamin_hall
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_name_daniel_walker
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_name_emma_taylor
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_name_ethan_brown
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_name_evelyn_young
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_name_harper_lewis
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_name_henry_wright
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_name_isabella_nguyen
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_name_jackson_lee
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_name_liam_moore
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_name_lucas_rodriguez
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_name_mason_thomas
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_name_mia_wilson
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_name_noah_taylor
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_name_of_your_project
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_name_olivia_martin
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_name_sofia_davis
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_name_william_kim
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_new_customers
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_nextjs
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_outline
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_page_x_of_y
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_passing
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_pick_a_deploy_date
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_recent_sales
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_refund
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_removable
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_row_actions
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_sale
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_schedule
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_secondary
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_select_all_rows_on_this_page
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_select_x
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_sep
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_status
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_status_failed
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_status_indicators_of_the_current_theme
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_status_processing
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_status_success
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_sveltekit
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_theme_colors
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_total_revenue
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_total_visitors
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_upload
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_view_details
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_view_report
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_week_1
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_week_2
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_week_3
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_week_4
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_what_is_it_about
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_x_selected
import com.github.jershell.shadcn.demoapp.generated.resources.dashboard_you_made_265_sales_this_month
import io.github.dautovicharis.charts.LineChart
import io.github.dautovicharis.charts.StackedBarChart
import io.github.dautovicharis.charts.model.toChartDataSet
import io.github.dautovicharis.charts.model.toMultiChartDataSet
import org.jetbrains.compose.resources.stringResource

private enum class SaleStatus {
    Success,
    Processing,
    Failed,
}

private data class SaleRow(
    val id: Int,
    val name: String,
    val email: String,
    val status: SaleStatus,
    val method: String,
    val amount: Double,
)

private enum class SortColumn {
    Name,
    Status,
    Method,
    Amount,
}

@Composable
private fun sales(): List<SaleRow> = listOf(
    SaleRow(1, stringResource(Res.string.dashboard_name_olivia_martin), stringResource(Res.string.dashboard_email_olivia_martin), SaleStatus.Success, stringResource(Res.string.dashboard_method_credit_card), 1999.00),
    SaleRow(2, stringResource(Res.string.dashboard_name_jackson_lee), stringResource(Res.string.dashboard_email_jackson_lee), SaleStatus.Processing, stringResource(Res.string.dashboard_method_paypal), 39.00),
    SaleRow(3, stringResource(Res.string.dashboard_name_isabella_nguyen), stringResource(Res.string.dashboard_email_isabella_nguyen), SaleStatus.Success, stringResource(Res.string.dashboard_method_bank_transfer), 299.00),
    SaleRow(4, stringResource(Res.string.dashboard_name_william_kim), stringResource(Res.string.dashboard_email_william_kim), SaleStatus.Failed, stringResource(Res.string.dashboard_method_credit_card), 99.00),
    SaleRow(5, stringResource(Res.string.dashboard_name_sofia_davis), stringResource(Res.string.dashboard_email_sofia_davis), SaleStatus.Success, stringResource(Res.string.dashboard_method_paypal), 450.00),
    SaleRow(6, stringResource(Res.string.dashboard_name_ethan_brown), stringResource(Res.string.dashboard_email_ethan_brown), SaleStatus.Processing, stringResource(Res.string.dashboard_method_credit_card), 120.00),
    SaleRow(7, stringResource(Res.string.dashboard_name_mia_wilson), stringResource(Res.string.dashboard_email_mia_wilson), SaleStatus.Success, stringResource(Res.string.dashboard_method_bank_transfer), 780.00),
    SaleRow(8, stringResource(Res.string.dashboard_name_noah_taylor), stringResource(Res.string.dashboard_email_noah_taylor), SaleStatus.Failed, stringResource(Res.string.dashboard_method_paypal), 65.00),
    SaleRow(9, stringResource(Res.string.dashboard_name_ava_anderson), stringResource(Res.string.dashboard_email_ava_anderson), SaleStatus.Success, stringResource(Res.string.dashboard_method_credit_card), 320.00),
    SaleRow(10, stringResource(Res.string.dashboard_name_liam_moore), stringResource(Res.string.dashboard_email_liam_moore), SaleStatus.Success, stringResource(Res.string.dashboard_method_paypal), 210.00),
    SaleRow(11, stringResource(Res.string.dashboard_name_emma_taylor), stringResource(Res.string.dashboard_email_emma_taylor), SaleStatus.Processing, stringResource(Res.string.dashboard_method_bank_transfer), 940.00),
    SaleRow(12, stringResource(Res.string.dashboard_name_mason_thomas), stringResource(Res.string.dashboard_email_mason_thomas), SaleStatus.Success, stringResource(Res.string.dashboard_method_credit_card), 155.00),
    SaleRow(13, stringResource(Res.string.dashboard_name_amelia_clark), stringResource(Res.string.dashboard_email_amelia_clark), SaleStatus.Failed, stringResource(Res.string.dashboard_method_paypal), 88.00),
    SaleRow(14, stringResource(Res.string.dashboard_name_lucas_rodriguez), stringResource(Res.string.dashboard_email_lucas_rodriguez), SaleStatus.Success, stringResource(Res.string.dashboard_method_credit_card), 512.00),
    SaleRow(15, stringResource(Res.string.dashboard_name_harper_lewis), stringResource(Res.string.dashboard_email_harper_lewis), SaleStatus.Success, stringResource(Res.string.dashboard_method_bank_transfer), 275.00),
    SaleRow(16, stringResource(Res.string.dashboard_name_benjamin_hall), stringResource(Res.string.dashboard_email_benjamin_hall), SaleStatus.Processing, stringResource(Res.string.dashboard_method_paypal), 133.00),
    SaleRow(17, stringResource(Res.string.dashboard_name_evelyn_young), stringResource(Res.string.dashboard_email_evelyn_young), SaleStatus.Success, stringResource(Res.string.dashboard_method_credit_card), 640.00),
    SaleRow(18, stringResource(Res.string.dashboard_name_daniel_walker), stringResource(Res.string.dashboard_email_daniel_walker), SaleStatus.Processing, stringResource(Res.string.dashboard_method_bank_transfer), 97.00),
    SaleRow(19, stringResource(Res.string.dashboard_name_abigail_king), stringResource(Res.string.dashboard_email_abigail_king), SaleStatus.Success, stringResource(Res.string.dashboard_method_paypal), 388.00),
    SaleRow(20, stringResource(Res.string.dashboard_name_henry_wright), stringResource(Res.string.dashboard_email_henry_wright), SaleStatus.Failed, stringResource(Res.string.dashboard_method_credit_card), 44.00),
)

@Composable
private fun saleStatusLabel(status: SaleStatus): String = when (status) {
    SaleStatus.Success -> stringResource(Res.string.dashboard_status_success)
    SaleStatus.Processing -> stringResource(Res.string.dashboard_status_processing)
    SaleStatus.Failed -> stringResource(Res.string.dashboard_status_failed)
}

private enum class RangeKey {
    M3,
    D30,
    M12,
}

@Composable
private fun rangeKeyLabel(range: RangeKey): String = when (range) {
    RangeKey.M3 -> stringResource(Res.string.dashboard_3_months)
    RangeKey.D30 -> stringResource(Res.string.dashboard_30_days)
    RangeKey.M12 -> stringResource(Res.string.dashboard_12_months)
}

@Composable
private fun visitorsDataset(range: RangeKey): Pair<List<Pair<String, List<Float>>>, List<String>> {
    val s_dashboard_sep = stringResource(Res.string.dashboard_sep)
    val s_dashboard_week_1 = stringResource(Res.string.dashboard_week_1)
    val s_dashboard_jun = stringResource(Res.string.dashboard_jun)
    val s_dashboard_desktop = stringResource(Res.string.dashboard_desktop)
    val s_dashboard_mobile = stringResource(Res.string.dashboard_mobile)
    val s_dashboard_jul = stringResource(Res.string.dashboard_month_jul)
    val s_dashboard_aug = stringResource(Res.string.dashboard_month_aug)
    val s_dashboard_week_2 = stringResource(Res.string.dashboard_week_2)
    val s_dashboard_week_3 = stringResource(Res.string.dashboard_week_3)
    val s_dashboard_week_4 = stringResource(Res.string.dashboard_week_4)
    val s_dashboard_oct = stringResource(Res.string.dashboard_month_oct)
    val s_dashboard_nov = stringResource(Res.string.dashboard_month_nov)
    val s_dashboard_dec = stringResource(Res.string.dashboard_month_dec)
    val s_dashboard_jan = stringResource(Res.string.dashboard_month_jan)
    val s_dashboard_feb = stringResource(Res.string.dashboard_month_feb)
    val s_dashboard_mar = stringResource(Res.string.dashboard_month_mar)
    val s_dashboard_apr = stringResource(Res.string.dashboard_month_apr)
    val s_dashboard_may = stringResource(Res.string.dashboard_month_may)
    return when (range) {
        RangeKey.M3 -> listOf(
            s_dashboard_desktop to listOf(186f, 305f, 237f),
            s_dashboard_mobile to listOf(73f, 128f, 91f),
        ) to listOf(s_dashboard_jun, s_dashboard_jul, s_dashboard_aug)

        RangeKey.D30 -> listOf(
            s_dashboard_desktop to listOf(275f, 201f, 224f, 289f),
            s_dashboard_mobile to listOf(92f, 113f, 78f, 141f),
        ) to listOf(s_dashboard_week_1, s_dashboard_week_2, s_dashboard_week_3, s_dashboard_week_4)

        RangeKey.M12 -> listOf(
            s_dashboard_desktop to listOf(186f, 205f, 232f, 268f, 241f, 289f, 305f, 274f, 291f, 318f, 334f, 352f),
            s_dashboard_mobile to listOf(73f, 84f, 96f, 118f, 109f, 128f, 131f, 145f, 152f, 164f, 171f, 186f),
        ) to listOf(
            s_dashboard_sep, s_dashboard_oct, s_dashboard_nov, s_dashboard_dec, s_dashboard_jan,
            s_dashboard_feb, s_dashboard_mar, s_dashboard_apr, s_dashboard_may, s_dashboard_jun,
            s_dashboard_jul, s_dashboard_aug,
        )
    }
}

private val pageRows = 5

private fun formatAmount(amount: Double): String {
    val cents = (amount * 100).toInt()
    val dollars = cents / 100
    val fraction = (cents % 100).toString().padStart(2, '0')
    return "+$" + dollars + "." + fraction
}

private fun SaleStatus.badgeVariant(): BadgeVariant = when (this) {
    SaleStatus.Success -> BadgeVariant.Default
    SaleStatus.Processing -> BadgeVariant.Secondary
    SaleStatus.Failed -> BadgeVariant.Destructive
}

private fun SaleStatus.badgeIcon(): ShadcnIcon = when (this) {
    SaleStatus.Success -> Lucide.CircleCheckBig.toShadcnIcon()
    SaleStatus.Processing -> Lucide.LoaderCircle.toShadcnIcon()
    SaleStatus.Failed -> Lucide.CircleX.toShadcnIcon()
}

/**
 * dashboard-01 block from shadcn/ui: a themed preview of an app shell
 * (header + metric cards + interactive chart + data table) rendered with the
 * current theme preset.
 *
 * The screen has no own scrolling: the host wraps it in a two-dimensional
 * scroll area (vertical + horizontal scrollbars) like the reference create preview.
 */
@Composable
fun DashboardScreen(
    preset: ShadcnPreset?,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        SiteHeader()
        SectionCards()
        ChartAreaInteractive()
        RecentSalesTable()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            PaletteCard(modifier = Modifier.weight(1f))
            BadgesCard(modifier = Modifier.weight(1f))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            FormCard(modifier = Modifier.weight(1f))
            CalendarCard(modifier = Modifier.weight(1f))
        }
    }
}

/**
 * The theme palette of the running app: current token colors with their names.
 */
@Composable
private fun PaletteCard(modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        CardHeader {
            CardTitle(stringResource(Res.string.dashboard_theme_colors))
            CardDescription(stringResource(Res.string.dashboard_current_palette_tokens))
        }
        CardContent {
            val tokens = listOf(
                "background" to ColorTokens.background,
                "foreground" to ColorTokens.foreground,
                "card" to ColorTokens.card,
                "primary" to ColorTokens.primary,
                "primaryForeground" to ColorTokens.primaryForeground,
                "secondary" to ColorTokens.secondary,
                "muted" to ColorTokens.muted,
                "accent" to ColorTokens.accent,
                "destructive" to ColorTokens.destructive,
                "border" to ColorTokens.border,
                "ring" to ColorTokens.ring,
                "chart1" to ColorTokens.chartToken1,
                "chart2" to ColorTokens.chartToken2,
                "chart3" to ColorTokens.chartToken3,
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                tokens.forEach { (name, token) ->
                    val color = Theme[ColorProps][token]
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(1.dp, Theme[ColorProps][ColorTokens.border], CircleShape),
                        )
                        Small(name, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            hexOf(color),
                            style = TypographyStyles.textXsRegular.copy(
                                color = Theme[ColorProps][ColorTokens.mutedForeground],
                            ),
                        )
                    }
                }
            }
        }
    }
}

private fun hexOf(color: Color): String {
    val argb = (color.toArgb() and 0xFFFFFF).toString(16).uppercase().padStart(6, '0')
    return "#" + argb
}

/**
 * Form card: inputs, checkbox, radio group, switch, progress and action buttons.
 */
@Composable
private fun FormCard(modifier: Modifier = Modifier) {
    var agreed by remember { mutableStateOf(false) }
    var notified by remember { mutableStateOf(true) }
    var plan by remember { mutableStateOf(0) }
    Card(modifier = modifier) {
        CardHeader {
            CardTitle(stringResource(Res.string.dashboard_create_project))
            CardDescription(stringResource(Res.string.dashboard_deploy_your_new_project_in_one_click))
        }
        CardContent {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Small(stringResource(Res.string.dashboard_name))
                    Input(state = rememberTextFieldState(), placeholder = stringResource(Res.string.dashboard_name_of_your_project))
                }
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Small(stringResource(Res.string.dashboard_description))
                    Textarea(state = rememberTextFieldState(), placeholder = stringResource(Res.string.dashboard_what_is_it_about))
                }
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Small(stringResource(Res.string.dashboard_framework))
                    RadioGroup(
                        items = listOf(
                            DataItem(0, stringResource(Res.string.dashboard_nextjs), 0),
                            DataItem(1, stringResource(Res.string.dashboard_sveltekit), 1),
                            DataItem(2, stringResource(Res.string.dashboard_astro), 2),
                        ),
                        selectedKey = plan,
                        onSelectedChange = { plan = it },
                    )
                }
                Checkbox(
                    checked = agreed,
                    onCheckedChange = { agreed = it },
                    label = stringResource(Res.string.dashboard_i_agree_to_the_terms_and_conditions),
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(checked = notified, onCheckedChange = { notified = it })
                    Spacer(Modifier.width(8.dp))
                    Small(stringResource(Res.string.dashboard_email_me_when_the_deploy_finishes))
                }
                Progress(value = 0.6f)
            }
        }
        CardFooter {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(onClick = {}, variant = ButtonVariant.Outline, modifier = Modifier.weight(1f)) {
                    ButtonText(stringResource(Res.string.dashboard_cancel))
                }
                Button(onClick = {}, modifier = Modifier.weight(1f)) {
                    ButtonText(stringResource(Res.string.dashboard_deploy))
                }
            }
        }
    }
}

/**
 * Calendar card: the date picker of the theme preview.
 */
@Composable
private fun CalendarCard(modifier: Modifier = Modifier) {
    val calendarState = rememberCalendarState()
    Card(modifier = modifier) {
        CardHeader {
            CardTitle(stringResource(Res.string.dashboard_schedule))
            CardDescription(stringResource(Res.string.dashboard_pick_a_deploy_date))
        }
        CardContent {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Theme[DimProps][DimTokens.radiusMd]))
                    .border(
                        Theme[DimProps][DimTokens.borderWidth],
                        Theme[ColorProps][ColorTokens.border],
                        RoundedCornerShape(Theme[DimProps][DimTokens.radiusMd]),
                    )
                    .padding(12.dp),
            ) {
                Calendar(state = calendarState)
            }
        }
    }
}

/**
 * Badges card: every badge variant with icons.
 */
@Composable
private fun BadgesCard(modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        CardHeader {
            CardTitle(stringResource(Res.string.dashboard_badges))
            CardDescription(stringResource(Res.string.dashboard_status_indicators_of_the_current_theme))
        }
        CardContent {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Badge(stringResource(Res.string.dashboard_default), variant = BadgeVariant.Default)
                    Badge(stringResource(Res.string.dashboard_secondary), variant = BadgeVariant.Secondary)
                    Badge(stringResource(Res.string.dashboard_destructive), variant = BadgeVariant.Destructive)
                    Badge(stringResource(Res.string.dashboard_outline), variant = BadgeVariant.Outline)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Badge(
                        text = stringResource(Res.string.dashboard_passing),
                        variant = BadgeVariant.Default,
                        leadingIcon = Lucide.CircleCheckBig.toShadcnIcon(),
                    )
                    Badge(
                        text = stringResource(Res.string.dashboard_deploying),
                        variant = BadgeVariant.Secondary,
                        leadingIcon = Lucide.LoaderCircle.toShadcnIcon(),
                    )
                    Badge(
                        text = stringResource(Res.string.dashboard_failed),
                        variant = BadgeVariant.Destructive,
                        leadingIcon = Lucide.CircleX.toShadcnIcon(),
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Badge(
                        text = stringResource(Res.string.dashboard_12_5),
                        variant = BadgeVariant.Outline,
                        leadingIcon = Lucide.TrendingUp.toShadcnIcon(),
                    )
                    Badge(
                        text = stringResource(Res.string.dashboard_4_2),
                        variant = BadgeVariant.Outline,
                        leadingIcon = Lucide.TrendingDown.toShadcnIcon(),
                    )
                    Badge(
                        text = stringResource(Res.string.dashboard_removable),
                        variant = BadgeVariant.Outline,
                        trailingIcon = Lucide.X.toShadcnIcon(),
                        onTrailingIconClick = {},
                    )
                }
            }
        }
    }
}

@Composable
private fun SiteHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            H4(stringResource(Res.string.dashboard_dashboard))
            Small(stringResource(Res.string.dashboard_monthly_performance_across_your_team))
        }
        Box(modifier = Modifier.weight(1f))
        Button(onClick = {}, variant = ButtonVariant.Outline) {
            ButtonIcon(imageVector = Lucide.CalendarDays, contentDescription = stringResource(Res.string.dashboard_date_range))
            ButtonText(stringResource(Res.string.dashboard_last_12_months))
        }
        Button(onClick = {}, variant = ButtonVariant.Outline) {
            ButtonIcon(imageVector = Lucide.Upload, contentDescription = null)
            ButtonText(stringResource(Res.string.dashboard_export))
        }
        var actionsOpen by remember { mutableStateOf(false) }
        DropdownMenu(
            expanded = actionsOpen,
            onExpandedChange = { actionsOpen = it },
            anchor = {
                Button(
                    onClick = { actionsOpen = true },
                    variant = ButtonVariant.Outline,
                    size = ButtonSize.Icon,
                    content = {
                        ButtonIcon(imageVector = Lucide.Ellipsis, contentDescription = stringResource(Res.string.dashboard_more_actions))
                    },
                )
            },
        ) {
            DropdownMenuContent {
                DropdownMenuLabel(stringResource(Res.string.dashboard_actions))
                DropdownMenuItem(onClick = { actionsOpen = false }) {
                    DropdownMenuItemIcon(Lucide.Upload)
                    DropdownMenuItemText(stringResource(Res.string.dashboard_upload))
                }
                DropdownMenuItem(onClick = { actionsOpen = false }) {
                    DropdownMenuItemIcon(Lucide.TrendingUp)
                    DropdownMenuItemText(stringResource(Res.string.dashboard_view_report))
                }
            }
        }
    }
}

@Composable
private fun SectionCards() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        MetricCard(
            modifier = Modifier.weight(1f),
            title = stringResource(Res.string.dashboard_total_revenue),
            headline = "$1,250.00",
            delta = stringResource(Res.string.dashboard_delta_up_12_5_from_last_month),
            isUp = true,
            values = listOf(410f, 430f, 445f, 425f, 460f, 470f, 480f),
        )
        MetricCard(
            modifier = Modifier.weight(1f),
            title = stringResource(Res.string.dashboard_new_customers),
            headline = "1,234",
            delta = stringResource(Res.string.dashboard_delta_down_20_1_from_last_month),
            isUp = false,
            values = listOf(320f, 300f, 315f, 340f, 330f, 355f, 370f),
        )
        MetricCard(
            modifier = Modifier.weight(1f),
            title = stringResource(Res.string.dashboard_active_accounts),
            headline = "2,145",
            delta = stringResource(Res.string.dashboard_delta_up_15_3_from_last_month),
            isUp = true,
            values = listOf(280f, 310f, 290f, 320f, 335f, 350f, 365f),
        )
        MetricCard(
            modifier = Modifier.weight(1f),
            title = stringResource(Res.string.dashboard_growth_rate),
            headline = "4.5%",
            delta = stringResource(Res.string.dashboard_delta_up_5_2_from_last_month),
            isUp = true,
            values = listOf(150f, 175f, 160f, 190f, 210f, 225f, 240f),
        )
    }
}

@Composable
private fun MetricCard(
    title: String,
    headline: String,
    delta: String,
    isUp: Boolean,
    values: List<Float>,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        CardHeader {
            CardTitle(title)
            CardDescription(stringResource(Res.string.dashboard_last_7_days))
        }
        CardContent {
            Badge(
                text = delta,
                variant = BadgeVariant.Outline,
                leadingIcon = if (isUp) Lucide.TrendingUp.toShadcnIcon() else Lucide.TrendingDown.toShadcnIcon(),
            )
            Spacer(Modifier.height(10.dp))
            Text(headline, style = TypographyStyles.textLgMedium)
            Spacer(Modifier.height(8.dp))
            val dataSet = remember(values) {
                values.toChartDataSet(
                    title = "",
                    labels = values.indices.map { "${it + 1}" },
                )
            }
            LineChart(
                dataSet = dataSet,
                style = shadcnLineChartStyle(
                    chartHeight = 72.dp,
                    yAxisLabelsVisible = false
                )
            )
        }
    }
}

@Composable
private fun ChartAreaInteractive() {
    var range by remember { mutableStateOf(RangeKey.D30) }
    Card {
        CardHeader {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    CardTitle(stringResource(Res.string.dashboard_total_visitors))
                    CardDescription(stringResource(Res.string.dashboard_desktop_and_mobile_visitors))
                }
                Box(modifier = Modifier.weight(1f))
                val rangeLabels = listOf(
                    rangeKeyLabel(RangeKey.M3),
                    rangeKeyLabel(RangeKey.D30),
                    rangeKeyLabel(RangeKey.M12),
                )
                Tabs(
                    items = RangeKey.entries.mapIndexed { i, key -> DataItem(i, rangeLabels[i], key) },
                    selectedKey = range.ordinal,
                    onSelectedChange = { selected -> range = RangeKey.entries[selected] },
                )
            }
        }
        CardContent {
            val (series, categories) = visitorsDataset(range)
            val dataSet = remember(series, categories) {
                series.toMultiChartDataSet(
                    title = "",
                    categories = categories,
                )
            }
            StackedBarChart(
                dataSet = dataSet,
                style = shadcnStackedBarChartStyle(pointCount = categories.size),
            )
            // legend like in the reference chart card
            val chart1 = Theme[ColorProps][ColorTokens.chartToken1]
            val chart2 = Theme[ColorProps][ColorTokens.chartToken2]
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ChartLegend(stringResource(Res.string.dashboard_desktop), chart1)
                ChartLegend(stringResource(Res.string.dashboard_mobile), chart2)
            }
        }
    }
}

@Composable
private fun ChartLegend(
    label: String,
    color: Color,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color),
        )
        Small(label, color = Theme[ColorProps][ColorTokens.mutedForeground])
    }
}

@Composable
private fun RecentSalesTable() {
    val filterState = rememberTextFieldState()
    var statusFilter by remember { mutableStateOf<SaleStatus?>(null) }
    var selectedIds by remember { mutableStateOf(setOf<Int>()) }
    var sortColumn by remember { mutableStateOf<SortColumn?>(null) }
    var sortDescending by remember { mutableStateOf(false) }
    var page by remember { mutableStateOf(0) }

    val query = filterState.text.toString().trim().lowercase()
    val salesRows = sales()
    val filtered = remember(salesRows, query, statusFilter) {
        salesRows.filter { row ->
            (statusFilter == null || row.status == statusFilter) &&
                (query.isEmpty() ||
                    row.name.lowercase().contains(query) ||
                    row.email.lowercase().contains(query))
        }
    }
    val sorted = remember(filtered, sortColumn, sortDescending) {
        if (sortColumn == null) {
            filtered
        } else {
            val comparator = when (sortColumn) {
                SortColumn.Name -> compareBy<SaleRow> { it.name }
                SortColumn.Status -> compareBy { it.status.ordinal }
                SortColumn.Method -> compareBy { it.method }
                else -> compareBy { it.amount }
            }
            if (sortDescending) filtered.sortedWith(comparator.reversed()) else filtered.sortedWith(comparator)
        }
    }
    val pageCount = maxOf(1, (sorted.size + pageRows - 1) / pageRows)
    val safePage = page.coerceIn(0, pageCount - 1)
    val pageItems = sorted.drop(safePage * pageRows).take(pageRows)
    val pageIds = pageItems.map { it.id }.toSet()

    val columns = listOf(
        DataTableColumn<SaleRow>(key = "select", header = "", width = 48.dp),
        DataTableColumn(key = "name", header = stringResource(Res.string.dashboard_name), width = 260.dp, weight = 2.6f),
        DataTableColumn(key = "status", header = stringResource(Res.string.dashboard_status), width = 160.dp, weight = 1.3f),
        DataTableColumn(key = "method", header = stringResource(Res.string.dashboard_method), width = 160.dp, weight = 1.3f),
        DataTableColumn(
            key = "amount",
            header = stringResource(Res.string.dashboard_amount),
            width = 140.dp,
            weight = 1.0f,
            cell = { formatAmount(it.amount) },
            alignment = Alignment.CenterEnd,
        ),
        DataTableColumn(key = "actions", header = "", width = 64.dp),
    )

    Card {
        CardHeader {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    CardTitle(stringResource(Res.string.dashboard_recent_sales))
                    CardDescription(stringResource(Res.string.dashboard_you_made_265_sales_this_month))
                }
                Box(modifier = Modifier.weight(1f))
                if (selectedIds.isNotEmpty()) {
                    Small(stringResource(Res.string.dashboard_x_selected, selectedIds.size))
                }
                val allStatusesLabel = stringResource(Res.string.dashboard_all_statuses)
                val statusItems = listOf<SaleStatus?>(null) + SaleStatus.entries
                val statusLabels = listOf(
                    allStatusesLabel,
                    saleStatusLabel(SaleStatus.Success),
                    saleStatusLabel(SaleStatus.Processing),
                    saleStatusLabel(SaleStatus.Failed),
                )
                Select(
                    items = statusItems.mapIndexed { i, status ->
                        DataItem(i, statusLabels[i], status)
                    },
                    selected = setOf(statusItems.indexOfFirst { it == statusFilter }),
                    onSelectedChange = { keys ->
                        statusFilter = statusItems.getOrNull(keys.firstOrNull() ?: 0)
                        page = 0
                    },
                    placeholder = stringResource(Res.string.dashboard_status),
                    modifier = Modifier.width(170.dp),
                )
                Input(
                    state = filterState,
                    placeholder = stringResource(Res.string.dashboard_filter_sales),
                    modifier = Modifier.width(220.dp),
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        // table edge-to-edge inside the card (the reference layout): no CardContent
        // padding around it — only the table's own cell padding
        Table(
            modifier = Modifier.fillMaxWidth(),
            rows = pageItems,
            columns = columns,
            pinnedHeader = true,
            rowHeight = 52.dp,
            headerHeight = 40.dp,
            cellPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            rowBackground = { row, _ ->
                when {
                    row.id in selectedIds -> Theme[ColorProps][ColorTokens.muted]
                    else -> Theme[ColorProps][ColorTokens.background]
                }
            },
            headerContent = { columnIndex, column ->
                when (column.key) {
                    "select" -> Checkbox(
                        checked = pageItems.isNotEmpty() && pageItems.all { it.id in selectedIds },
                        onCheckedChange = { checked ->
                            selectedIds = if (checked) {
                                selectedIds + pageIds
                            } else {
                                selectedIds - pageIds.toSet()
                            }
                        },
                        label = stringResource(Res.string.dashboard_select_all_rows_on_this_page),
                    )

                    "actions" -> Unit

                    else -> {
                        val sortColumnOf = when (column.key) {
                            "name" -> SortColumn.Name
                            "status" -> SortColumn.Status
                            "method" -> SortColumn.Method
                            else -> SortColumn.Amount
                        }
                        SortHeaderCell(
                            label = column.header,
                            column = sortColumnOf,
                            sortColumn = sortColumn,
                            sortDescending = sortDescending,
                            onSort = { clicked ->
                                when {
                                    sortColumn != clicked -> {
                                        sortColumn = clicked
                                        sortDescending = false
                                    }

                                    !sortDescending -> sortDescending = true

                                    else -> {
                                        sortColumn = null
                                        sortDescending = false
                                    }
                                }
                                page = 0
                            },
                            alignEnd = column.alignment == Alignment.CenterEnd,
                        )
                    }
                }
            },
            cellContent = { _, column, row ->
                when (column.key) {
                    "select" -> Checkbox(
                        checked = row.id in selectedIds,
                        onCheckedChange = { checked ->
                            selectedIds = if (checked) selectedIds + row.id else selectedIds - row.id
                        },
                        label = stringResource(Res.string.dashboard_select_x, row.name),
                    )

                    "name" -> Column {
                        Small(row.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Small(
                            row.email,
                            color = Theme[ColorProps][ColorTokens.mutedForeground],
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    "status" -> Badge(
                        text = saleStatusLabel(row.status),
                        variant = row.status.badgeVariant(),
                        leadingIcon = row.status.badgeIcon(),
                    )

                    "method" -> Small(row.method, maxLines = 1, overflow = TextOverflow.Ellipsis)

                    "amount" -> Text(
                        formatAmount(row.amount),
                        style = TypographyStyles.textSmMedium,
                        textAlign = TextAlign.End,
                    )

                    "actions" -> {
                        var menuOpen by remember { mutableStateOf(false) }
                        DropdownMenu(
                            expanded = menuOpen,
                            onExpandedChange = { menuOpen = it },
                            anchor = {
                                Button(
                                    onClick = { menuOpen = true },
                                    variant = ButtonVariant.Ghost,
                                    size = ButtonSize.Icon,
                                    content = {
                                        ButtonIcon(
                                            imageVector = Lucide.Ellipsis,
                                            contentDescription = stringResource(Res.string.dashboard_row_actions),
                                        )
                                    },
                                )
                            },
                        ) {
                            DropdownMenuContent {
                                DropdownMenuLabel(stringResource(Res.string.dashboard_sale) + row.id)
                                DropdownMenuItem(onClick = { menuOpen = false }) {
                                    DropdownMenuItemText(stringResource(Res.string.dashboard_view_details))
                                }
                                DropdownMenuItem(onClick = { menuOpen = false }) {
                                    DropdownMenuItemText(stringResource(Res.string.dashboard_copy_email))
                                    DropdownMenuShortcut(stringResource(Res.string.dashboard_c))
                                }
                                DropdownMenuItem(
                                    onClick = { menuOpen = false },
                                    variant = DropdownMenuItemVariant.Destructive,
                                ) {
                                    DropdownMenuItemText(stringResource(Res.string.dashboard_refund))
                                }
                            }
                        }
                    }
                }
            },
        )
        CardContent {
            Pagination(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Small(
                    stringResource(Res.string.dashboard_page_x_of_y, safePage + 1, pageCount, sorted.size),
                    color = Theme[ColorProps][ColorTokens.mutedForeground],
                )
                Spacer(modifier = Modifier.weight(1f))
                PaginationContent {
                    PaginationPrevious(
                        enabled = safePage > 0,
                        onClick = { page = safePage - 1 },
                    )
                    buildPaginationTokens(
                        totalPages = pageCount,
                        currentPage = safePage + 1,
                    ).forEach { token ->
                        when (token) {
                            PaginationToken.Ellipsis -> PaginationEllipsis()
                            is PaginationToken.Page -> PaginationPage(
                                page = token.number,
                                active = token.number == safePage + 1,
                                onClick = { page = token.number - 1 },
                            )
                        }
                    }
                    PaginationNext(
                        enabled = safePage < pageCount - 1,
                        onClick = { page = safePage + 1 },
                    )
                }
            }
        }
    }
}

@Composable
private fun SortHeaderCell(
    label: String,
    column: SortColumn,
    sortColumn: SortColumn?,
    sortDescending: Boolean,
    onSort: (SortColumn) -> Unit,
    alignEnd: Boolean = false,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = { onSort(column) }),
        horizontalArrangement = if (alignEnd) {
            Arrangement.spacedBy(4.dp, Alignment.End)
        } else {
            Arrangement.spacedBy(4.dp)
        },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            label,
            textAlign = if (alignEnd) TextAlign.End else TextAlign.Start,
            style = TypographyStyles.textXsMedium.copy(
                color = Theme[ColorProps][ColorTokens.mutedForeground],
            ),
        )
        Text(
            when {
                sortColumn == column && !sortDescending -> "↑"
                sortColumn == column && sortDescending -> "↓"
                else -> "⇅"
            },
            style = TypographyStyles.textXsRegular.copy(
                color = if (sortColumn == column) {
                    Theme[ColorProps][ColorTokens.foreground]
                } else {
                    Theme[ColorProps][ColorTokens.mutedForeground]
                },
            ),
        )
    }
}
