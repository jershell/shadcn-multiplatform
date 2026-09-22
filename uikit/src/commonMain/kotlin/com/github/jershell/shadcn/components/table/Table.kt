package com.github.jershell.shadcn.components.table

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles
import eu.wewox.lazytable.LazyTable
import eu.wewox.lazytable.LazyTableItem
import eu.wewox.lazytable.LazyTableState
import eu.wewox.lazytable.lazyTableDimensions
import eu.wewox.lazytable.lazyTablePinConfiguration
import eu.wewox.lazytable.rememberSaveableLazyTableState

data class DataTableColumn<T>(
    val key: String,
    val header: String,
    /** Minimal column width; also the fallback when the container width is unbounded. */
    val width: Dp = 180.dp,
    /**
     * When set, the column shares the leftover width of the table container with
     * the other weighted columns proportionally (like a `flex-grow`). Fixed-width
     * columns keep [width]; a weighted column never shrinks below [width].
     */
    val weight: Float? = null,
    val cell: ((T) -> String)? = null,
    val alignment: Alignment = Alignment.CenterStart,
)

@Composable
fun <T> Table(
    modifier: Modifier = Modifier,
    rows: List<T>,
    columns: List<DataTableColumn<T>>,
    state: LazyTableState = rememberSaveableLazyTableState(),
    rowHeight: Dp = 44.dp,
    headerHeight: Dp = 40.dp,
    pinnedHeader: Boolean = true,
    caption: String? = null,
    headerContent: (@Composable (columnIndex: Int, column: DataTableColumn<T>) -> Unit)? = null,
    cellContent: (@Composable (rowIndex: Int, column: DataTableColumn<T>, row: T) -> Unit)? = null,
    rowBackground: (@Composable (row: T, rowIndex: Int) -> Color)? = null,
    cellPadding: PaddingValues = PaddingValues(
        horizontal = TwDimensions.paddingPxToken4,
        vertical = TwDimensions.paddingPxToken2,
    ),
) {
    DataTable(
        modifier = modifier,
        rows = rows,
        columns = columns,
        state = state,
        rowHeight = rowHeight,
        headerHeight = headerHeight,
        pinnedHeader = pinnedHeader,
        caption = caption,
        headerContent = headerContent,
        cellContent = cellContent,
        rowBackground = rowBackground,
        cellPadding = cellPadding,
    )
}

@Composable
fun <T> DataTable(
    modifier: Modifier = Modifier,
    rows: List<T>,
    columns: List<DataTableColumn<T>>,
    state: LazyTableState = rememberSaveableLazyTableState(),
    rowHeight: Dp = 44.dp,
    headerHeight: Dp = 40.dp,
    pinnedHeader: Boolean = true,
    caption: String? = null,
    headerContent: (@Composable (columnIndex: Int, column: DataTableColumn<T>) -> Unit)? = null,
    cellContent: (@Composable (rowIndex: Int, column: DataTableColumn<T>, row: T) -> Unit)? = null,
    rowBackground: (@Composable (row: T, rowIndex: Int) -> Color)? = null,
    cellPadding: PaddingValues = PaddingValues(
        horizontal = TwDimensions.paddingPxToken4,
        vertical = TwDimensions.paddingPxToken2,
    ),
) {
    if (columns.isEmpty()) return

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val resolved = resolveColumnWidths(columns, maxWidth)
        DataTableImpl(
            modifier = modifier,
            rows = rows,
            columns = resolved,
            state = state,
            rowHeight = rowHeight,
            headerHeight = headerHeight,
            pinnedHeader = pinnedHeader,
            caption = caption,
            headerContent = headerContent,
            cellContent = cellContent,
            rowBackground = rowBackground,
            cellPadding = cellPadding,
        )
    }
}

/**
 * Resolves weighted columns: when the container width is finite, weighted columns
 * share the space left after the fixed columns; otherwise (or when no weights are
 * used) the declared [DataTableColumn.width] is kept as is.
 */
@Composable
private fun <T> resolveColumnWidths(
    columns: List<DataTableColumn<T>>,
    containerWidth: Dp,
): List<DataTableColumn<T>> {
    val weighted = columns.filter { it.weight != null }
    if (weighted.isEmpty() || containerWidth == Dp.Infinity) return columns

    val fixedWidth = columns
        .filter { it.weight == null }
        .sumOf { it.width.value.toDouble() }
        .dp
    val totalWeight = weighted.sumOf { it.weight!!.toDouble() }.toFloat()
    if (totalWeight <= 0f) return columns

    val flexibleWidth = (containerWidth - fixedWidth).coerceAtLeast(0.dp)
    return columns.map { column ->
        if (column.weight == null) {
            column
        } else {
            column.copy(width = maxOf(column.width, flexibleWidth * (column.weight / totalWeight)))
        }
    }
}

@Composable
private fun <T> DataTableImpl(
    modifier: Modifier = Modifier,
    rows: List<T>,
    columns: List<DataTableColumn<T>>,
    state: LazyTableState,
    rowHeight: Dp,
    headerHeight: Dp,
    pinnedHeader: Boolean,
    caption: String?,
    headerContent: (@Composable (columnIndex: Int, column: DataTableColumn<T>) -> Unit)?,
    cellContent: (@Composable (rowIndex: Int, column: DataTableColumn<T>, row: T) -> Unit)?,
    rowBackground: (@Composable (row: T, rowIndex: Int) -> Color)?,
    cellPadding: PaddingValues,
) {

    val colors = resolveDataTableColors()
    val shape = RoundedCornerShape(Theme[DimProps][DimTokens.radiusMd])
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val headerRows = 1
    val totalRows = rows.size + headerRows
    val lastRowIndex = totalRows - 1
    val lastColumnIndex = columns.lastIndex

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .border(borderWidth, colors.border, shape)
                .background(colors.background),
        ) {
            LazyTable(
                modifier = Modifier.fillMaxWidth(),
                state = state,
                dimensions = lazyTableDimensions(
                    columnsCount = columns.size,
                    rowsCount = totalRows,
                    columnSize = { index -> columns[index].width },
                    rowSize = { index -> if (index == 0) headerHeight else rowHeight },
                ),
                pinConfiguration = lazyTablePinConfiguration(rows = if (pinnedHeader) 1 else 0),
                contentPadding = PaddingValues(0.dp),
            ) {
                items(
                    count = columns.size * totalRows,
                    layoutInfo = { index ->
                        val columnIndex = index % columns.size
                        val rowIndex = index / columns.size
                        LazyTableItem(column = columnIndex, row = rowIndex)
                    },
                ) { index ->
                    val columnIndex = index % columns.size
                    val rowIndex = index / columns.size
                    val isHeader = rowIndex == 0
                    val column = columns[columnIndex]
                    val row = if (isHeader) null else rows[rowIndex - 1]
                    val text = when {
                        isHeader -> column.header
                        else -> column.cell?.invoke(rows[rowIndex - 1]) ?: ""
                    }
                    val customContent: (@Composable () -> Unit)? = when {
                        isHeader && headerContent != null -> {
                            { headerContent(columnIndex, column) }
                        }

                        row != null && cellContent != null -> {
                            { cellContent(rowIndex - 1, column, row) }
                        }

                        else -> null
                    }
                    val rowBg = row?.let { r -> rowBackground?.invoke(r, rowIndex - 1) } ?: colors.background

                    TableCell(
                        text = text,
                        isHeader = isHeader,
                        colors = colors,
                        borderWidth = borderWidth,
                        rowIndex = rowIndex,
                        columnIndex = columnIndex,
                        lastRowIndex = lastRowIndex,
                        lastColumnIndex = lastColumnIndex,
                        alignment = column.alignment,
                        background = rowBg,
                        customContent = customContent,
                        padding = cellPadding,
                    )
                }
            }
        }

        if (!caption.isNullOrBlank()) {
            BasicText(
                text = caption,
                modifier = Modifier.padding(
                    top = TwDimensions.paddingPxToken2,
                    start = TwDimensions.paddingPxToken1,
                ),
                style = TypographyStyles.textSmRegular.copy(color = colors.captionForeground),
            )
        }
    }
}

@Composable
private fun TableCell(
    text: String,
    isHeader: Boolean,
    colors: DataTableColors,
    borderWidth: Dp,
    rowIndex: Int,
    columnIndex: Int,
    lastRowIndex: Int,
    lastColumnIndex: Int,
    alignment: Alignment = Alignment.CenterStart,
    background: Color = colors.background,
    customContent: (@Composable () -> Unit)? = null,
    padding: PaddingValues = PaddingValues(
        horizontal = TwDimensions.paddingPxToken4,
        vertical = TwDimensions.paddingPxToken2,
    ),
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isHeader) colors.headerBackground else background)
            .drawBehind {
                val stroke = borderWidth.toPx()
                if (columnIndex < lastColumnIndex) {
                    drawLine(
                        color = colors.border,
                        start = Offset(size.width, 0f),
                        end = Offset(size.width, size.height),
                        strokeWidth = stroke,
                    )
                }
                if (rowIndex < lastRowIndex) {
                    drawLine(
                        color = colors.border,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = stroke,
                    )
                }
            }
            .padding(padding),
        contentAlignment = alignment,
    ) {
        if (customContent != null) {
            customContent()
        } else {
            BasicText(
                text = text,
                style = if (isHeader) {
                    TypographyStyles.textSmMedium.copy(color = colors.headerForeground)
                } else {
                    TypographyStyles.textSmRegular.copy(color = colors.cellForeground)
                },
            )
        }
    }
}
