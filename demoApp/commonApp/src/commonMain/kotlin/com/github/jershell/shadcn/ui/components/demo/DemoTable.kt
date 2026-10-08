package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.badge.Badge
import com.github.jershell.shadcn.components.badge.BadgeVariant
import com.github.jershell.shadcn.components.checkbox.Checkbox
import com.github.jershell.shadcn.components.pagination.Pagination
import com.github.jershell.shadcn.components.pagination.PaginationContent
import com.github.jershell.shadcn.components.pagination.PaginationEllipsis
import com.github.jershell.shadcn.components.pagination.PaginationNext
import com.github.jershell.shadcn.components.pagination.PaginationPage
import com.github.jershell.shadcn.components.pagination.PaginationPrevious
import com.github.jershell.shadcn.components.pagination.PaginationToken
import com.github.jershell.shadcn.components.pagination.buildPaginationTokens
import com.github.jershell.shadcn.components.table.DataTableColumn
import com.github.jershell.shadcn.components.table.Table
import com.github.jershell.shadcn.components.table.TableConfig
import com.github.jershell.shadcn.components.table.TableRowDragHandle
import com.github.jershell.shadcn.components.toggle.ToggleText
import com.github.jershell.shadcn.components.toggle.ToggleVariant
import com.github.jershell.shadcn.components.toggle.group.ToggleGroup
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.table_amount
import com.github.jershell.shadcn.demoapp.generated.resources.table_bank_transfer
import com.github.jershell.shadcn.demoapp.generated.resources.table_basic
import com.github.jershell.shadcn.demoapp.generated.resources.table_card
import com.github.jershell.shadcn.demoapp.generated.resources.table_cash
import com.github.jershell.shadcn.demoapp.generated.resources.table_country
import com.github.jershell.shadcn.demoapp.generated.resources.table_custom_cells
import com.github.jershell.shadcn.demoapp.generated.resources.table_custom_cells_description
import com.github.jershell.shadcn.demoapp.generated.resources.table_customer
import com.github.jershell.shadcn.demoapp.generated.resources.table_date
import com.github.jershell.shadcn.demoapp.generated.resources.table_drag_a_row_to_reorder_rows_the_cal
import com.github.jershell.shadcn.demoapp.generated.resources.table_drag_both
import com.github.jershell.shadcn.demoapp.generated.resources.table_drag_grip
import com.github.jershell.shadcn.demoapp.generated.resources.table_drag_row
import com.github.jershell.shadcn.demoapp.generated.resources.table_email
import com.github.jershell.shadcn.demoapp.generated.resources.table_empty
import com.github.jershell.shadcn.demoapp.generated.resources.table_empty_description
import com.github.jershell.shadcn.demoapp.generated.resources.table_failed
import com.github.jershell.shadcn.demoapp.generated.resources.table_hover
import com.github.jershell.shadcn.demoapp.generated.resources.table_hover_description
import com.github.jershell.shadcn.demoapp.generated.resources.table_hover_none
import com.github.jershell.shadcn.demoapp.generated.resources.table_hover_state
import com.github.jershell.shadcn.demoapp.generated.resources.table_id
import com.github.jershell.shadcn.demoapp.generated.resources.table_inv001
import com.github.jershell.shadcn.demoapp.generated.resources.table_inv002
import com.github.jershell.shadcn.demoapp.generated.resources.table_inv003
import com.github.jershell.shadcn.demoapp.generated.resources.table_inv004
import com.github.jershell.shadcn.demoapp.generated.resources.table_inv005
import com.github.jershell.shadcn.demoapp.generated.resources.table_inv006
import com.github.jershell.shadcn.demoapp.generated.resources.table_invoice
import com.github.jershell.shadcn.demoapp.generated.resources.table_isabella_example_com
import com.github.jershell.shadcn.demoapp.generated.resources.table_jack_example_com
import com.github.jershell.shadcn.demoapp.generated.resources.table_large
import com.github.jershell.shadcn.demoapp.generated.resources.table_large_description
import com.github.jershell.shadcn.demoapp.generated.resources.table_mason_example_com
import com.github.jershell.shadcn.demoapp.generated.resources.table_method
import com.github.jershell.shadcn.demoapp.generated.resources.table_moved
import com.github.jershell.shadcn.demoapp.generated.resources.table_no_results
import com.github.jershell.shadcn.demoapp.generated.resources.table_olivia_example_com
import com.github.jershell.shadcn.demoapp.generated.resources.table_paid
import com.github.jershell.shadcn.demoapp.generated.resources.table_pagination
import com.github.jershell.shadcn.demoapp.generated.resources.table_pagination_cuts_the_row_list_into
import com.github.jershell.shadcn.demoapp.generated.resources.table_paypal
import com.github.jershell.shadcn.demoapp.generated.resources.table_pending
import com.github.jershell.shadcn.demoapp.generated.resources.table_processing
import com.github.jershell.shadcn.demoapp.generated.resources.table_reorder
import com.github.jershell.shadcn.demoapp.generated.resources.table_rows_are_lazy_and_styled_with_tokens
import com.github.jershell.shadcn.demoapp.generated.resources.table_scroll
import com.github.jershell.shadcn.demoapp.generated.resources.table_scroll_with_more_rows_than_fit_t
import com.github.jershell.shadcn.demoapp.generated.resources.table_selected_count
import com.github.jershell.shadcn.demoapp.generated.resources.table_showing_recent_payments
import com.github.jershell.shadcn.demoapp.generated.resources.table_sophia_example_com
import com.github.jershell.shadcn.demoapp.generated.resources.table_status
import com.github.jershell.shadcn.demoapp.generated.resources.table_striped
import com.github.jershell.shadcn.demoapp.generated.resources.table_striped_description
import com.github.jershell.shadcn.demoapp.generated.resources.table_total
import com.github.jershell.shadcn.demoapp.generated.resources.table_usage
import com.github.jershell.shadcn.demoapp.generated.resources.table_william_example_com
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import org.jetbrains.compose.resources.stringResource

private enum class PaymentStatus { Paid, Pending, Processing, Failed }

private data class PaymentRow(
    val id: Int,
    val invoice: String,
    val status: PaymentStatus,
    val method: String,
    val email: String,
    val amountCents: Long,
)

private fun formatAmount(cents: Long): String {
    val dollars = cents / 100
    val rest = (cents % 100).toString().padStart(2, '0')
    val grouped = dollars.toString().reversed().chunked(3).joinToString(",").reversed()
    return "$$grouped.$rest"
}

@Composable
private fun statusLabel(status: PaymentStatus): String = when (status) {
    PaymentStatus.Paid -> stringResource(Res.string.table_paid)
    PaymentStatus.Pending -> stringResource(Res.string.table_pending)
    PaymentStatus.Processing -> stringResource(Res.string.table_processing)
    PaymentStatus.Failed -> stringResource(Res.string.table_failed)
}

private fun statusVariant(status: PaymentStatus): BadgeVariant = when (status) {
    PaymentStatus.Paid -> BadgeVariant.Default
    PaymentStatus.Pending -> BadgeVariant.Secondary
    PaymentStatus.Processing -> BadgeVariant.Outline
    PaymentStatus.Failed -> BadgeVariant.Destructive
}

@Composable
private fun paymentRows(): List<PaymentRow> {
    val card = stringResource(Res.string.table_card)
    return listOf(
        PaymentRow(1, stringResource(Res.string.table_inv001), PaymentStatus.Paid, card, stringResource(Res.string.table_olivia_example_com), 25_000),
        PaymentRow(2, stringResource(Res.string.table_inv002), PaymentStatus.Pending, stringResource(Res.string.table_bank_transfer), stringResource(Res.string.table_jack_example_com), 15_000),
        PaymentRow(3, stringResource(Res.string.table_inv003), PaymentStatus.Processing, card, stringResource(Res.string.table_isabella_example_com), 35_000),
        PaymentRow(4, stringResource(Res.string.table_inv004), PaymentStatus.Paid, stringResource(Res.string.table_cash), stringResource(Res.string.table_william_example_com), 45_000),
        PaymentRow(5, stringResource(Res.string.table_inv005), PaymentStatus.Failed, card, stringResource(Res.string.table_sophia_example_com), 55_000),
        PaymentRow(6, stringResource(Res.string.table_inv006), PaymentStatus.Pending, stringResource(Res.string.table_paypal), stringResource(Res.string.table_mason_example_com), 20_000),
    )
}

/** Text columns of the payments table; [total] adds a footer to the amount column. */
@Composable
private fun paymentColumns(total: String? = null): List<DataTableColumn<PaymentRow>> {
    val statusLabels = PaymentStatus.entries.associateWith { statusLabel(it) }
    return listOf(
        DataTableColumn(
            key = "invoice",
            header = stringResource(Res.string.table_invoice),
            width = 110.dp,
            footer = total?.let { stringResource(Res.string.table_total) },
        ) { it.invoice },
        DataTableColumn(key = "status", header = stringResource(Res.string.table_status), width = 120.dp) {
            statusLabels.getValue(it.status)
        },
        DataTableColumn(key = "method", header = stringResource(Res.string.table_method), width = 140.dp) { it.method },
        DataTableColumn(key = "email", header = stringResource(Res.string.table_email), width = 220.dp, weight = 1f) { it.email },
        DataTableColumn(
            key = "amount",
            header = stringResource(Res.string.table_amount),
            width = 110.dp,
            alignment = Alignment.CenterEnd,
            footer = total,
        ) { formatAmount(it.amountCents) },
    )
}

/** Moves an item the way Table.onRowMoved expects. */
private fun <T> List<T>.moved(from: Int, to: Int): List<T> = toMutableList().apply { add(to, removeAt(from)) }

@Composable
fun DemoTable() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        val rows = paymentRows()
        val total = formatAmount(rows.sumOf { it.amountCents })

        // ── Basic ────────────────────────────────────────────────────────────────
        DemoSection(
            title = stringResource(Res.string.table_basic),
            description = stringResource(Res.string.table_rows_are_lazy_and_styled_with_tokens),
        ) {
            Table(
                rows = rows,
                columns = paymentColumns(total = total),
                rowKey = { _, row -> row.id },
                config = TableConfig(caption = stringResource(Res.string.table_showing_recent_payments)),
            )
        }
        UsageBlock(
            code = """
                Table(
                    rows = payments,
                    columns = listOf(
                        DataTableColumn<Payment>("invoice", header = "Invoice", footer = "Total") { it.invoice },
                        DataTableColumn<Payment>("email", header = "Email", weight = 1f) { it.email },
                        DataTableColumn<Payment>(
                            "amount", header = "Amount",
                            alignment = Alignment.CenterEnd, footer = "$2,500.00",
                        ) { it.amount },
                    ),
                    rowKey = { _, payment -> payment.id },
                    config = TableConfig(caption = "Showing recent payments."),
                )
            """,
        )

        // ── Hover ────────────────────────────────────────────────────────────────
        DemoSection(
            title = stringResource(Res.string.table_hover),
            description = stringResource(Res.string.table_hover_description),
        ) {
            var hoveredRow by remember { mutableStateOf<PaymentRow?>(null) }
            var hoveredColumn by remember { mutableStateOf<String?>(null) }
            val muted = Theme[ColorProps][ColorTokens.muted]
            Table(
                rows = rows,
                columns = paymentColumns(),
                rowKey = { _, row -> row.id },
                config = TableConfig(
                    rowHoverBackground = muted.copy(alpha = muted.alpha * 0.5f),
                    cellHoverBackground = Theme[ColorProps][ColorTokens.accent],
                ),
                onRowHover = { _, row, hovered ->
                    if (hovered) hoveredRow = row else if (hoveredRow == row) hoveredRow = null
                },
                onCellHover = { _, column, _, hovered ->
                    if (hovered) hoveredColumn = column.header else if (hoveredColumn == column.header) hoveredColumn = null
                },
            )
            val row = hoveredRow
            Muted(
                if (row == null) {
                    stringResource(Res.string.table_hover_none)
                } else {
                    stringResource(Res.string.table_hover_state, rows.indexOf(row), row.invoice, hoveredColumn ?: "—")
                },
            )
        }
        UsageBlock(
            code = """
                Table(
                    rows = payments,
                    columns = paymentColumns(),
                    config = TableConfig(
                        rowHoverBackground = Theme[ColorProps][ColorTokens.muted].copy(alpha = 0.5f),
                        cellHoverBackground = Theme[ColorProps][ColorTokens.accent],
                    ),
                    onRowHover = { index, row, hovered -> ... },
                    onCellHover = { index, column, row, hovered -> ... },
                )
            """,
        )

        // ── Row reordering ───────────────────────────────────────────────────────
        DemoSection(
            title = stringResource(Res.string.table_reorder),
            description = stringResource(Res.string.table_drag_a_row_to_reorder_rows_the_cal),
        ) {
            var handle by remember { mutableStateOf(TableRowDragHandle.Grip) }
            var reorderable by remember { mutableStateOf(List(12) { i -> rows[i % rows.size].copy(id = i + 1) }) }
            var lastMove by remember { mutableStateOf<Triple<String, Int, Int>?>(null) }

            ToggleGroup(
                value = handle.name,
                onValueChange = { value -> value?.let { handle = TableRowDragHandle.valueOf(it) } },
                variant = ToggleVariant.Outline,
            ) {
                Item(TableRowDragHandle.Grip.name) { ToggleText(stringResource(Res.string.table_drag_grip)) }
                Item(TableRowDragHandle.Row.name) { ToggleText(stringResource(Res.string.table_drag_row)) }
                Item(TableRowDragHandle.GripAndRow.name) { ToggleText(stringResource(Res.string.table_drag_both)) }
            }
            val muted = Theme[ColorProps][ColorTokens.muted]
            Table(
                rows = reorderable,
                columns = paymentColumns(),
                modifier = Modifier.height(320.dp),
                rowKey = { _, row -> row.id },
                config = TableConfig(
                    reorderRows = true,
                    rowDragHandle = handle,
                    rowHoverBackground = muted.copy(alpha = muted.alpha * 0.5f),
                ),
                onRowMoved = { from, to ->
                    val row = reorderable[from]
                    reorderable = reorderable.moved(from, to)
                    lastMove = Triple(row.invoice, from + 1, to + 1)
                },
            )
            lastMove?.let { (invoice, from, to) -> Muted(stringResource(Res.string.table_moved, invoice, from, to)) }
        }
        UsageBlock(
            code = """
                var payments by remember { mutableStateOf(initial) }
                Table(
                    rows = payments,
                    columns = paymentColumns(),
                    rowKey = { _, payment -> payment.id },
                    config = TableConfig(
                        reorderRows = true,
                        rowDragHandle = TableRowDragHandle.GripAndRow, // Grip | Row | GripAndRow
                    ),
                    onRowMoved = { from, to ->
                        // single source of truth: reorder (or send to the backend)
                        payments = payments.toMutableList().apply { add(to, removeAt(from)) }
                    },
                )
            """,
        )

        // ── Custom cells ─────────────────────────────────────────────────────────
        DemoSection(
            title = stringResource(Res.string.table_custom_cells),
            description = stringResource(Res.string.table_custom_cells_description),
        ) {
            var selected by remember { mutableStateOf(setOf<Int>()) }
            val statusLabels = PaymentStatus.entries.associateWith { statusLabel(it) }
            val columns = listOf(DataTableColumn<PaymentRow>(key = "select", header = "", width = 48.dp, alignment = Alignment.Center)) +
                paymentColumns(total = total)
            val muted = Theme[ColorProps][ColorTokens.muted]
            Table(
                rows = rows,
                columns = columns,
                rowKey = { _, row -> row.id },
                config = TableConfig(rowHoverBackground = muted.copy(alpha = muted.alpha * 0.5f)),
                rowBackground = { row, _ ->
                    if (row.id in selected) Theme[ColorProps][ColorTokens.muted] else Theme[ColorProps][ColorTokens.background]
                },
                headerContent = { _, column ->
                    if (column.key == "select") {
                        Checkbox(
                            checked = rows.isNotEmpty() && rows.all { it.id in selected },
                            onCheckedChange = { checked -> selected = if (checked) rows.map { it.id }.toSet() else emptySet() },
                        )
                    } else {
                        Muted(column.header)
                    }
                },
                cellContent = { _, column, row ->
                    when (column.key) {
                        "select" -> Checkbox(
                            checked = row.id in selected,
                            onCheckedChange = { checked -> selected = if (checked) selected + row.id else selected - row.id },
                        )
                        "status" -> Badge(text = statusLabels.getValue(row.status), variant = statusVariant(row.status))
                        else -> P(column.cell?.invoke(row).orEmpty())
                    }
                },
            )
            Muted(stringResource(Res.string.table_selected_count, selected.size, rows.size))
        }
        UsageBlock(
            code = """
                Table(
                    rows = payments,
                    columns = listOf(DataTableColumn<Payment>("select", header = "", width = 48.dp)) + columns,
                    rowBackground = { row, _ -> if (row.id in selected) muted else background },
                    headerContent = { _, column -> if (column.key == "select") Checkbox(allSelected, ...) else Muted(column.header) },
                    cellContent = { _, column, row ->
                        when (column.key) {
                            "select" -> Checkbox(row.id in selected, ...)
                            "status" -> Badge(text = row.status, variant = ...)
                            else -> P(column.cell?.invoke(row).orEmpty())
                        }
                    },
                )
            """,
        )

        // ── Striped rows ─────────────────────────────────────────────────────────
        DemoSection(
            title = stringResource(Res.string.table_striped),
            description = stringResource(Res.string.table_striped_description),
        ) {
            val muted = Theme[ColorProps][ColorTokens.muted]
            val background = Theme[ColorProps][ColorTokens.background]
            Table(
                rows = rows,
                columns = paymentColumns(),
                rowKey = { _, row -> row.id },
                config = TableConfig(cellHoverBackground = Theme[ColorProps][ColorTokens.accent]),
                rowBackground = { _, index -> if (index % 2 == 1) muted.copy(alpha = muted.alpha * 0.5f) else background },
            )
        }
        UsageBlock(
            code = """
                Table(
                    rows = payments,
                    columns = paymentColumns(),
                    rowBackground = { _, index -> if (index % 2 == 1) muted.copy(alpha = 0.5f) else background },
                )
            """,
        )

        // ── Scroll ───────────────────────────────────────────────────────────────
        DemoSection(
            title = stringResource(Res.string.table_scroll),
            description = stringResource(Res.string.table_scroll_with_more_rows_than_fit_t),
        ) {
            Table(
                rows = List(60) { index -> rows[index % rows.size].copy(id = index) },
                columns = paymentColumns(),
                rowKey = { _, row -> row.id },
                modifier = Modifier.fillMaxWidth().height(320.dp),
            )
        }
        UsageBlock(
            code = """
                // The frame takes the given height; rows scroll lazily and the
                // vertical scrollbar sits outside the frame, only on overflow.
                Table(
                    rows = manyPayments,
                    columns = paymentColumns(),
                    modifier = Modifier.fillMaxWidth().height(320.dp),
                )
            """,
        )

        // ── Large dataset ────────────────────────────────────────────────────────
        DemoSection(
            title = stringResource(Res.string.table_large),
            description = stringResource(Res.string.table_large_description),
        ) {
            LargeTableDemo(rows)
        }
        UsageBlock(
            code = """
                Table(
                    rows = thousandRows,
                    columns = wideColumns, // wider than the container → horizontal scroll
                    rowKey = { _, row -> row.id },
                    modifier = Modifier.height(400.dp),
                    config = TableConfig(reorderRows = true),
                    onRowMoved = { from, to -> rows = rows.toMutableList().apply { add(to, removeAt(from)) } },
                )
            """,
        )

        // ── Empty ────────────────────────────────────────────────────────────────
        DemoSection(
            title = stringResource(Res.string.table_empty),
            description = stringResource(Res.string.table_empty_description),
        ) {
            val noResults = stringResource(Res.string.table_no_results)
            Table(
                rows = emptyList(),
                columns = paymentColumns(),
                emptyContent = { P(noResults) },
            )
        }
        UsageBlock(
            code = """
                Table(
                    rows = filtered,
                    columns = paymentColumns(),
                    emptyContent = { P("No results.") },
                )
            """,
        )

        // ── Pagination ───────────────────────────────────────────────────────────
        DemoSection(
            title = stringResource(Res.string.table_pagination),
            description = stringResource(Res.string.table_pagination_cuts_the_row_list_into),
        ) {
            val pageSize = 4
            var page by remember { mutableStateOf(0) }
            val all = remember(rows) { List(30) { i -> rows[i % rows.size].copy(id = i) } }
            val pages = (all.size + pageSize - 1) / pageSize
            val pageRows = all.drop(page * pageSize).take(pageSize)

            Table(
                rows = pageRows,
                columns = paymentColumns(),
                rowKey = { _, row -> row.id },
            )

            Pagination(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                PaginationPrevious(onClick = { page = (page - 1).coerceAtLeast(0) }, enabled = page > 0)
                PaginationContent {
                    buildPaginationTokens(totalPages = pages, currentPage = page + 1).forEach { token ->
                        when (token) {
                            is PaginationToken.Page -> PaginationPage(
                                page = token.number,
                                active = token.number == page + 1,
                                onClick = { page = token.number - 1 },
                            )
                            PaginationToken.Ellipsis -> PaginationEllipsis()
                        }
                    }
                }
                PaginationNext(onClick = { page = (page + 1).coerceAtMost(pages - 1) }, enabled = page < pages - 1)
            }
        }
        UsageBlock(
            code = """
                val pageSize = 10
                var page by remember { mutableStateOf(0) }
                Table(
                    rows = all.drop(page * pageSize).take(pageSize),
                    columns = paymentColumns(),
                )
                Pagination {
                    PaginationPrevious(..., enabled = page > 0)
                    PaginationContent {
                        buildPaginationTokens(pages, page + 1).forEach { token -> ... }
                    }
                    PaginationNext(..., enabled = page < pages - 1)
                }
            """,
        )
    }
}

private data class LargeRow(
    val id: Int,
    val customer: String,
    val email: String,
    val country: String,
    val date: String,
    val status: PaymentStatus,
    val method: String,
    val amountCents: Long,
)

private val Countries = listOf("Germany", "France", "Japan", "Brazil", "Canada", "Kazakhstan", "Spain", "Australia")
private val FirstNames = listOf("Olivia", "Jack", "Isabella", "William", "Sophia", "Mason", "Emma", "Liam")

@Composable
private fun LargeTableDemo(seed: List<PaymentRow>) {
    var data by remember(seed) {
        mutableStateOf(
            List(1_000) { i ->
                val base = seed[i % seed.size]
                val name = FirstNames[(i * 7) % FirstNames.size]
                LargeRow(
                    id = i + 1,
                    customer = "$name ${('A' + (i % 26))}.",
                    email = "${name.lowercase()}.${i + 1}@example.com",
                    country = Countries[(i * 3) % Countries.size],
                    date = "2026-${((i % 12) + 1).toString().padStart(2, '0')}-${((i % 28) + 1).toString().padStart(2, '0')}",
                    status = base.status,
                    method = base.method,
                    amountCents = 1_000L + (i * 7_919L) % 990_000L,
                )
            },
        )
    }
    val statusLabels = PaymentStatus.entries.associateWith { statusLabel(it) }
    val columns = listOf(
        DataTableColumn<LargeRow>(key = "id", header = stringResource(Res.string.table_id), width = 72.dp) { "#${it.id}" },
        DataTableColumn(key = "customer", header = stringResource(Res.string.table_customer), width = 180.dp) { it.customer },
        DataTableColumn(key = "email", header = stringResource(Res.string.table_email), width = 260.dp) { it.email },
        DataTableColumn(key = "country", header = stringResource(Res.string.table_country), width = 160.dp) { it.country },
        DataTableColumn(key = "date", header = stringResource(Res.string.table_date), width = 140.dp) { it.date },
        DataTableColumn(key = "status", header = stringResource(Res.string.table_status), width = 140.dp) { statusLabels.getValue(it.status) },
        DataTableColumn(key = "method", header = stringResource(Res.string.table_method), width = 160.dp) { it.method },
        DataTableColumn(
            key = "amount",
            header = stringResource(Res.string.table_amount),
            width = 140.dp,
            alignment = Alignment.CenterEnd,
        ) { formatAmount(it.amountCents) },
    )
    val muted = Theme[ColorProps][ColorTokens.muted]
    Table(
        rows = data,
        columns = columns,
        modifier = Modifier.fillMaxWidth().height(400.dp),
        rowKey = { _, row -> row.id },
        config = TableConfig(
            reorderRows = true,
            rowHoverBackground = muted.copy(alpha = muted.alpha * 0.5f),
        ),
        onRowMoved = { from, to -> data = data.moved(from, to) },
    )
}

@Composable
private fun UsageBlock(code: String) {
    Muted(stringResource(Res.string.table_usage))
    InlineCode(
        text = code.trimIndent(),
        selected = false,
        selectEnabled = true,
        copyEnabled = true,
    )
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
