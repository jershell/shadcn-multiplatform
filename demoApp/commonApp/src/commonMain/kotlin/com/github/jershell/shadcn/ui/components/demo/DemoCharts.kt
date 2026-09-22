package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.charts_1_color_list_semantics_differ_per_chart_type_for_2
import com.github.jershell.shadcn.demoapp.generated.resources.charts_2_the_chart_title_and_legend_are_rendered_by_the_2
import com.github.jershell.shadcn.demoapp.generated.resources.charts_3_the_canvas_is_a_fixed_256dp_tall_360dp_wide_bl_2
import com.github.jershell.shadcn.demoapp.generated.resources.charts_barchart
import com.github.jershell.shadcn.demoapp.generated.resources.charts_daily_net_cash_flow
import com.github.jershell.shadcn.demoapp.generated.resources.charts_daily_support_tickets
import com.github.jershell.shadcn.demoapp.generated.resources.charts_free_plan
import com.github.jershell.shadcn.demoapp.generated.resources.charts_hdcharts_styled_with_shadcn_design_tokens_card_v_2
import com.github.jershell.shadcn.demoapp.generated.resources.charts_heating
import com.github.jershell.shadcn.demoapp.generated.resources.charts_histogram
import com.github.jershell.shadcn.demoapp.generated.resources.charts_household_energy
import com.github.jershell.shadcn.demoapp.generated.resources.charts_https_charts_hdcode_dev
import com.github.jershell.shadcn.demoapp.generated.resources.charts_jan
import com.github.jershell.shadcn.demoapp.generated.resources.charts_linechart
import com.github.jershell.shadcn.demoapp.generated.resources.charts_mon
import com.github.jershell.shadcn.demoapp.generated.resources.charts_monthly_active_subscribers_by_plan
import com.github.jershell.shadcn.demoapp.generated.resources.charts_multiline
import com.github.jershell.shadcn.demoapp.generated.resources.charts_north_america
import com.github.jershell.shadcn.demoapp.generated.resources.charts_notes_on_working_with_the_library_verified_again_2
import com.github.jershell.shadcn.demoapp.generated.resources.charts_overview
import com.github.jershell.shadcn.demoapp.generated.resources.charts_performance
import com.github.jershell.shadcn.demoapp.generated.resources.charts_piechart
import com.github.jershell.shadcn.demoapp.generated.resources.charts_platform_readiness_score
import com.github.jershell.shadcn.demoapp.generated.resources.charts_q1
import com.github.jershell.shadcn.demoapp.generated.resources.charts_quarterly_revenue_by_region
import com.github.jershell.shadcn.demoapp.generated.resources.charts_radar
import com.github.jershell.shadcn.demoapp.generated.resources.charts_request_duration_distribution
import com.github.jershell.shadcn.demoapp.generated.resources.charts_stackedarea
import com.github.jershell.shadcn.demoapp.generated.resources.charts_stackedbar
import com.github.jershell.shadcn.demoapp.generated.resources.charts_text
import com.github.jershell.shadcn.demoapp.generated.resources.charts_web_store
import com.github.jershell.shadcn.demoapp.generated.resources.charts_week_1
import com.github.jershell.shadcn.demoapp.generated.resources.charts_weekly_revenue_by_channel
import io.github.dautovicharis.charts.BarChart
import io.github.dautovicharis.charts.HistogramChart
import io.github.dautovicharis.charts.LineChart
import io.github.dautovicharis.charts.PieChart
import io.github.dautovicharis.charts.RadarChart
import io.github.dautovicharis.charts.StackedAreaChart
import io.github.dautovicharis.charts.StackedBarChart
import io.github.dautovicharis.charts.model.toChartDataSet
import io.github.dautovicharis.charts.model.toMultiChartDataSet
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoCharts() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(title = stringResource(Res.string.charts_overview)) {
            P(stringResource(Res.string.charts_hdcharts_styled_with_shadcn_design_tokens_card_v_2) +
                "muted-foreground labels and chart-token series colors.")
            P(stringResource(Res.string.charts_https_charts_hdcode_dev))
            P(stringResource(Res.string.charts_notes_on_working_with_the_library_verified_again_2) +
                "sources, version 2.4.0):")
            P(stringResource(Res.string.charts_1_color_list_semantics_differ_per_chart_type_for_2) +
                "StackedBar, Radar and Pie the color list is PER DATA POINT / CATEGORY " +
                "(list size must equal the number of points, or validation fails with " +
                "\"Colors size N does not match expected M\"). For Line (including " +
                "multi-series) and StackedArea the color list is PER SERIES (list size " +
                "must equal the number of series/items). Omitting the color list falls " +
                "back to a single color and skips the validation.")
            P(stringResource(Res.string.charts_2_the_chart_title_and_legend_are_rendered_by_the_2) +
                "MaterialTheme.colorScheme and their TextStyle is not exposed through " +
                "ChartViewDefaults - they cannot be fully themed without an upstream " +
                "change.")
            P(stringResource(Res.string.charts_3_the_canvas_is_a_fixed_256dp_tall_360dp_wide_bl_2) +
                "card token, dashed-free), set through modifierChart in the view style.")
        }

        DemoSection(title = stringResource(Res.string.charts_linechart)) {
            val dataSet = listOf(42f, 38f, 45f, 51f, 47f, 54f, 49f).toChartDataSet(
                title = stringResource(Res.string.charts_daily_support_tickets),
                labels = listOf(stringResource(Res.string.charts_mon), "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
            )
            LineChart(dataSet, shadcnLineChartStyle())
        }

        DemoSection(title = stringResource(Res.string.charts_multiline)) {
            val items = listOf(
                stringResource(Res.string.charts_web_store) to listOf(420f, 510f, 480f, 530f, 560f, 590f),
                "Mobile App" to listOf(360f, 420f, 410f, 460f, 500f, 540f),
                "Partner Sales" to listOf(280f, 320f, 340f, 360f, 390f, 420f),
            )
            val dataSet = items.toMultiChartDataSet(
                title = stringResource(Res.string.charts_weekly_revenue_by_channel),
                prefix = stringResource(Res.string.charts_text),
                categories = listOf(stringResource(Res.string.charts_week_1), "Week 2", "Week 3", "Week 4", "Week 5", "Week 6"),
            )
            LineChart(dataSet, shadcnLineChartStyle(shadcnChartColors(3)))
        }

        DemoSection(title = stringResource(Res.string.charts_barchart)) {
            val dataSet = listOf(45f, -12f, 38f, 27f, -19f, 42f, 31f).toChartDataSet(
                title = stringResource(Res.string.charts_daily_net_cash_flow),
                prefix = stringResource(Res.string.charts_text),
                labels = listOf(stringResource(Res.string.charts_mon), "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
            )
            BarChart(dataSet, shadcnBarChartStyle())
        }

        DemoSection(title = stringResource(Res.string.charts_stackedbar)) {
            val items = listOf(
                stringResource(Res.string.charts_north_america) to listOf(320f, 340f, 360f, 390f),
                "Europe" to listOf(210f, 230f, 245f, 260f),
                "Asia Pacific" to listOf(180f, 205f, 225f, 250f),
            )
            val dataSet = items.toMultiChartDataSet(
                title = stringResource(Res.string.charts_quarterly_revenue_by_region),
                prefix = stringResource(Res.string.charts_text),
                categories = listOf(stringResource(Res.string.charts_q1), "Q2", "Q3", "Q4"),
            )
            StackedBarChart(dataSet, shadcnStackedBarChartStyle(pointCount = 4))
        }

        DemoSection(title = stringResource(Res.string.charts_histogram)) {
            val dataSet = listOf(3f, 6f, 11f, 16f, 14f, 9f, 5f).toChartDataSet(
                title = stringResource(Res.string.charts_request_duration_distribution),
                labels = listOf("0-50ms", "50-100ms", "100-150ms", "150-200ms", "200-250ms", "250-300ms", "300ms+"),
            )
            HistogramChart(dataSet, shadcnHistogramChartStyle())
        }

        DemoSection(title = stringResource(Res.string.charts_stackedarea)) {
            val items = listOf(
                stringResource(Res.string.charts_free_plan) to listOf(620f, 650f, 690f, 720f, 760f, 800f),
                "Standard Plan" to listOf(240f, 260f, 285f, 310f, 340f, 365f),
                "Premium Plan" to listOf(90f, 95f, 105f, 118f, 130f, 142f),
            )
            val dataSet = items.toMultiChartDataSet(
                title = stringResource(Res.string.charts_monthly_active_subscribers_by_plan),
                categories = listOf(stringResource(Res.string.charts_jan), "Feb", "Mar", "Apr", "May", "Jun"),
            )
            StackedAreaChart(dataSet, shadcnStackedAreaChartStyle(seriesCount = 3))
        }

        DemoSection(title = stringResource(Res.string.charts_piechart)) {
            val dataSet = listOf(32f, 21f, 24f, 14f, 9f).toChartDataSet(
                title = stringResource(Res.string.charts_household_energy),
                postfix = "%",
                labels = listOf(stringResource(Res.string.charts_heating), "Cooling", "Appliances", "Water Heating", "Lighting"),
            )
            PieChart(dataSet, shadcnPieChartStyle(pointCount = 5))
        }

        DemoSection(title = stringResource(Res.string.charts_radar)) {
            val dataSet = listOf(84f, 79f, 76f, 88f, 82f, 74f).toChartDataSet(
                title = stringResource(Res.string.charts_platform_readiness_score),
                labels = listOf(
                    stringResource(Res.string.charts_performance),
                    "Reliability",
                    "Usability",
                    "Security",
                    "Scalability",
                    "Observability",
                ),
            )
            RadarChart(dataSet, shadcnRadarChartStyle(pointCount = 6))
        }
    }
}

@Composable
private fun DemoSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        H4(title)
        content()
    }
}
