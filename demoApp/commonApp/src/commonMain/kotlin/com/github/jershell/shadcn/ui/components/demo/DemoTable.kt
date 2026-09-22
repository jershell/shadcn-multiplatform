package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jershell.shadcn.components.table.DataTableColumn
import com.github.jershell.shadcn.components.table.Table
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.table_150_00
import com.github.jershell.shadcn.demoapp.generated.resources.table_200_00
import com.github.jershell.shadcn.demoapp.generated.resources.table_250_00
import com.github.jershell.shadcn.demoapp.generated.resources.table_350_00
import com.github.jershell.shadcn.demoapp.generated.resources.table_450_00
import com.github.jershell.shadcn.demoapp.generated.resources.table_550_00
import com.github.jershell.shadcn.demoapp.generated.resources.table_a_styled_table_built_on_top_of_lazytable_with_pi
import com.github.jershell.shadcn.demoapp.generated.resources.table_amount
import com.github.jershell.shadcn.demoapp.generated.resources.table_bank_transfer
import com.github.jershell.shadcn.demoapp.generated.resources.table_card
import com.github.jershell.shadcn.demoapp.generated.resources.table_cash
import com.github.jershell.shadcn.demoapp.generated.resources.table_data_table
import com.github.jershell.shadcn.demoapp.generated.resources.table_email
import com.github.jershell.shadcn.demoapp.generated.resources.table_failed
import com.github.jershell.shadcn.demoapp.generated.resources.table_header_participates_in_vertical_scroll_when_pinn
import com.github.jershell.shadcn.demoapp.generated.resources.table_inv001
import com.github.jershell.shadcn.demoapp.generated.resources.table_inv002
import com.github.jershell.shadcn.demoapp.generated.resources.table_inv003
import com.github.jershell.shadcn.demoapp.generated.resources.table_inv004
import com.github.jershell.shadcn.demoapp.generated.resources.table_inv005
import com.github.jershell.shadcn.demoapp.generated.resources.table_inv006
import com.github.jershell.shadcn.demoapp.generated.resources.table_invoice
import com.github.jershell.shadcn.demoapp.generated.resources.table_isabella_example_com
import com.github.jershell.shadcn.demoapp.generated.resources.table_jack_example_com
import com.github.jershell.shadcn.demoapp.generated.resources.table_mason_example_com
import com.github.jershell.shadcn.demoapp.generated.resources.table_method
import com.github.jershell.shadcn.demoapp.generated.resources.table_olivia_example_com
import com.github.jershell.shadcn.demoapp.generated.resources.table_paid
import com.github.jershell.shadcn.demoapp.generated.resources.table_paypal
import com.github.jershell.shadcn.demoapp.generated.resources.table_pending
import com.github.jershell.shadcn.demoapp.generated.resources.table_processing
import com.github.jershell.shadcn.demoapp.generated.resources.table_showing_recent_payments
import com.github.jershell.shadcn.demoapp.generated.resources.table_sophia_example_com
import com.github.jershell.shadcn.demoapp.generated.resources.table_status
import com.github.jershell.shadcn.demoapp.generated.resources.table_usage
import com.github.jershell.shadcn.demoapp.generated.resources.table_william_example_com
import com.github.jershell.shadcn.demoapp.generated.resources.table_without_pinned_header
import org.jetbrains.compose.resources.stringResource

private data class PaymentRow(
    val invoice: String,
    val status: String,
    val method: String,
    val email: String,
    val amount: String,
)

@Composable
private fun paymentRows() = listOf(
    PaymentRow(stringResource(Res.string.table_inv001), stringResource(Res.string.table_paid), stringResource(Res.string.table_card), stringResource(Res.string.table_olivia_example_com), stringResource(Res.string.table_250_00)),
    PaymentRow(stringResource(Res.string.table_inv002), stringResource(Res.string.table_pending), stringResource(Res.string.table_bank_transfer), stringResource(Res.string.table_jack_example_com), stringResource(Res.string.table_150_00)),
    PaymentRow(stringResource(Res.string.table_inv003), stringResource(Res.string.table_processing), stringResource(Res.string.table_card), stringResource(Res.string.table_isabella_example_com), stringResource(Res.string.table_350_00)),
    PaymentRow(stringResource(Res.string.table_inv004), stringResource(Res.string.table_paid), stringResource(Res.string.table_cash), stringResource(Res.string.table_william_example_com), stringResource(Res.string.table_450_00)),
    PaymentRow(stringResource(Res.string.table_inv005), stringResource(Res.string.table_failed), stringResource(Res.string.table_card), stringResource(Res.string.table_sophia_example_com), stringResource(Res.string.table_550_00)),
    PaymentRow(stringResource(Res.string.table_inv006), stringResource(Res.string.table_pending), stringResource(Res.string.table_paypal), stringResource(Res.string.table_mason_example_com), stringResource(Res.string.table_200_00)),
)

@Composable
private fun paymentColumns() = listOf(
    DataTableColumn<PaymentRow>(
        key = "invoice",
        header = stringResource(Res.string.table_invoice),
        width = 120.dp,
        cell = { it.invoice },
    ),
    DataTableColumn<PaymentRow>(
        key = "status",
        header = stringResource(Res.string.table_status),
        width = 140.dp,
        cell = { it.status },
    ),
    DataTableColumn<PaymentRow>(
        key = "method",
        header = stringResource(Res.string.table_method),
        width = 180.dp,
        cell = { it.method },
    ),
    DataTableColumn<PaymentRow>(
        key = "email",
        header = stringResource(Res.string.table_email),
        width = 280.dp,
        cell = { it.email },
    ),
    DataTableColumn<PaymentRow>(
        key = "amount",
        header = stringResource(Res.string.table_amount),
        width = 140.dp,
        cell = { it.amount },
    ),
)

@Composable
fun DemoTable() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.table_data_table),
            description = stringResource(Res.string.table_a_styled_table_built_on_top_of_lazytable_with_pi),
        ) {
            Table(
                rows = paymentRows(),
                columns = paymentColumns(),
                caption = stringResource(Res.string.table_showing_recent_payments),
            )
        }

        DemoSection(
            title = stringResource(Res.string.table_without_pinned_header),
            description = stringResource(Res.string.table_header_participates_in_vertical_scroll_when_pinn),
        ) {
            Table(
                rows = paymentRows() + paymentRows() + paymentRows(),
                columns = paymentColumns(),
                pinnedHeader = false,
            )
        }

        Muted(stringResource(Res.string.table_usage))
        InlineCode(
            text = """
                val columns = listOf(
                    DataTableColumn<User>("email", "Email", width = 280.dp) { it.email },
                    DataTableColumn<User>("role", "Role", width = 160.dp) { it.role },
                )
                
                Table(
                    rows = users,
                    columns = columns,
                    pinnedHeader = true,
                    caption = "User list",
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
