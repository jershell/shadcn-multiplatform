package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import io.github.dautovicharis.charts.style.BarChartDefaults
import io.github.dautovicharis.charts.style.BarChartStyle
import io.github.dautovicharis.charts.style.ChartViewDefaults
import io.github.dautovicharis.charts.style.ChartViewStyle
import io.github.dautovicharis.charts.style.HistogramChartDefaults
import io.github.dautovicharis.charts.style.HistogramChartStyle
import io.github.dautovicharis.charts.style.LineChartDefaults
import io.github.dautovicharis.charts.style.LineChartStyle
import io.github.dautovicharis.charts.style.PieChartDefaults
import io.github.dautovicharis.charts.style.PieChartStyle
import io.github.dautovicharis.charts.style.RadarChartDefaults
import io.github.dautovicharis.charts.style.RadarChartStyle
import io.github.dautovicharis.charts.style.StackedAreaChartDefaults
import io.github.dautovicharis.charts.style.StackedAreaChartStyle
import io.github.dautovicharis.charts.style.StackedBarChartDefaults
import io.github.dautovicharis.charts.style.StackedBarChartStyle

/**
 * HDCharts styling to match shadcn/ui:
 * - chart viewport = card (bg `card`, `radiusLg` corner, no compose shadow)
 * - grid and axes = `border`, labels = `muted-foreground`
 * - series = `chartToken1..5` tokens (analog of `--chart-1..5` from shadcn)
 * - solid fill (alpha = 1), as in shadcn charts
 *
 * Library limitation: title and legend styling is hardcoded in ChartViewDefaults
 * (Material colorScheme) and is not exposed as parameters.
 */

private val CHART_TOKEN_GETTERS: List<@Composable () -> Color> = listOf(
    { Theme[ColorProps][ColorTokens.chartToken1] },
    { Theme[ColorProps][ColorTokens.chartToken2] },
    { Theme[ColorProps][ColorTokens.chartToken3] },
    { Theme[ColorProps][ColorTokens.chartToken4] },
    { Theme[ColorProps][ColorTokens.chartToken5] },
)

/**
 * shadcn series palette (chartToken1..5), cycled up to [count].
 *
 * Color semantics in HDCharts differs per chart type (verified by validation):
 * - Bar / Histogram / StackedBar / Radar / Pie — color PER POINT/CATEGORY
 *   (list size == number of points);
 * - Line (incl. MultiLine) / StackedArea — color PER SERIES
 *   (list size == number of series/items).
 */
@Composable
internal fun shadcnChartColors(count: Int): List<Color> =
    List(count.coerceAtLeast(1)) { i -> CHART_TOKEN_GETTERS[i % CHART_TOKEN_GETTERS.size]() }

/** Chart viewport styled as a shadcn card. Canvas height is [chartHeight], 256dp by default. */
@Composable
internal fun shadcnChartViewStyle(chartHeight: Dp = 256.dp): ChartViewStyle =
    ChartViewDefaults.style(
        width = 360.dp,
        outerPadding = 0.dp,
        innerPadding = 16.dp,
        cornerRadius = Theme[DimProps][DimTokens.radiusLg],
        // compose shadow removed (see BACKLOG), the card already has a frame in context
        shadow = 0.dp,
        backgroundColor = Theme[ColorProps][ColorTokens.card],
        // instead of aspectRatio(1f): fixed canvas height
        modifierChart = Modifier.fillMaxWidth().height(chartHeight),
    )

/** Grid/axis/label colors shared by all cartesian charts. */
internal class ShadcnAxisColors(
    val grid: Color,
    val axis: Color,
    val label: Color,
)

@Composable
internal fun shadcnAxisColors(): ShadcnAxisColors = ShadcnAxisColors(
    grid = Theme[ColorProps][ColorTokens.border],
    axis = Theme[ColorProps][ColorTokens.border],
    label = Theme[ColorProps][ColorTokens.mutedForeground],
)

@Composable
internal fun shadcnLineChartStyle(
    colors: List<Color> = shadcnChartColors(1),
    chartHeight: Dp = 256.dp,
    xAxisLabelsVisible: Boolean = true,
    yAxisLabelsVisible: Boolean = true,
    zoomControlsVisible: Boolean = false,
): LineChartStyle {
    val axis = shadcnAxisColors()
    return LineChartDefaults.style(
        lineColor = colors.first(),
        lineColors = colors,
        lineAlpha = 1f,
        bezier = true,
        axisColor = axis.axis,
        yAxisLabelColor = axis.label,
        xAxisLabelColor = axis.label,
        zoomControlsVisible = zoomControlsVisible,
        xAxisLabelsVisible = xAxisLabelsVisible,
        yAxisLabelsVisible = yAxisLabelsVisible,
        chartViewStyle = shadcnChartViewStyle(chartHeight),
    )
}

@Composable
internal fun shadcnBarChartStyle(
    chartHeight: Dp = 256.dp,
    xAxisLabelsVisible: Boolean = true,
    yAxisLabelsVisible: Boolean = true,
    zoomControlsVisible: Boolean = false,
): BarChartStyle {
    val axis = shadcnAxisColors()
    return BarChartDefaults.style(
        barColor = shadcnChartColors(1).first(),
        barAlpha = 1f,
        gridColor = axis.grid,
        axisColor = axis.axis,
        xAxisLabelColor = axis.label,
        yAxisLabelColor = axis.label,
        selectionLineColor = Theme[ColorProps][ColorTokens.mutedForeground],
        zoomControlsVisible = zoomControlsVisible,
        xAxisLabelsVisible = xAxisLabelsVisible,
        yAxisLabelsVisible = yAxisLabelsVisible,
        chartViewStyle = shadcnChartViewStyle(chartHeight),
    )
}

@Composable
internal fun shadcnStackedBarChartStyle(
    pointCount: Int,
    chartHeight: Dp = 256.dp,
): StackedBarChartStyle {
    val axis = shadcnAxisColors()
    return StackedBarChartDefaults.style(
        barColors = shadcnChartColors(pointCount),
        barAlpha = 1f,
        xAxisLabelColor = axis.label,
        yAxisLabelColor = axis.label,
        selectionLineColor = Theme[ColorProps][ColorTokens.mutedForeground],
        zoomControlsVisible = false,
        chartViewStyle = shadcnChartViewStyle(chartHeight),
    )
}

@Composable
internal fun shadcnHistogramChartStyle(
    chartHeight: Dp = 256.dp,
): HistogramChartStyle {
    val axis = shadcnAxisColors()
    return HistogramChartDefaults.style(
        barColor = shadcnChartColors(1).first(),
        barAlpha = 1f,
        gridColor = axis.grid,
        axisColor = axis.axis,
        xAxisLabelColor = axis.label,
        yAxisLabelColor = axis.label,
        zoomControlsVisible = false,
        chartViewStyle = shadcnChartViewStyle(chartHeight),
    )
}

@Composable
internal fun shadcnStackedAreaChartStyle(
    seriesCount: Int,
    chartHeight: Dp = 256.dp,
): StackedAreaChartStyle {
    val axis = shadcnAxisColors()
    val colors = shadcnChartColors(seriesCount)
    return StackedAreaChartDefaults.style(
        areaColors = colors,
        lineColors = colors,
        lineColor = colors.first(),
        areaColor = colors.first(),
        fillAlpha = 0.8f,
        xAxisLabelColor = axis.label,
        yAxisLabelColor = axis.label,
        zoomControlsVisible = false,
        chartViewStyle = shadcnChartViewStyle(chartHeight),
    )
}

@Composable
internal fun shadcnPieChartStyle(
    pointCount: Int,
    chartHeight: Dp = 256.dp,
): PieChartStyle =
    PieChartDefaults.style(
        pieColors = shadcnChartColors(pointCount),
        pieAlpha = 1f,
        // segment separators = card background
        borderColor = Theme[ColorProps][ColorTokens.card],
        borderWidth = 2f,
        chartViewStyle = shadcnChartViewStyle(chartHeight),
    )

@Composable
internal fun shadcnRadarChartStyle(
    pointCount: Int,
    chartHeight: Dp = 256.dp,
): RadarChartStyle {
    val axis = shadcnAxisColors()
    val colors = shadcnChartColors(pointCount)
    return RadarChartDefaults.style(
        gridColor = axis.grid,
        axisLineColor = axis.axis,
        axisLabelColor = axis.label,
        pointColor = colors.first(),
        categoryColors = colors,
        chartViewStyle = shadcnChartViewStyle(chartHeight),
    )
}
