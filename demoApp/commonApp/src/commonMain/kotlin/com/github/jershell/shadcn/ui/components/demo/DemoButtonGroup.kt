package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Copy
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Scissors
import com.github.jershell.shadcn.components.button.ButtonIcon
import com.github.jershell.shadcn.components.button.ButtonSize
import com.github.jershell.shadcn.components.button.ButtonText
import com.github.jershell.shadcn.components.button.ButtonVariant
import com.github.jershell.shadcn.components.button.group.ButtonGroup
import com.github.jershell.shadcn.components.button.group.ButtonGroupOrientation
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.button_group_12_99
import com.github.jershell.shadcn.demoapp.generated.resources.button_group_a_muted_label_block_joined_with_a_primary_action
import com.github.jershell.shadcn.demoapp.generated.resources.button_group_a_separator_line_between_icon_buttons_stretching
import com.github.jershell.shadcn.demoapp.generated.resources.button_group_accept
import com.github.jershell.shadcn.demoapp.generated.resources.button_group_basic
import com.github.jershell.shadcn.demoapp.generated.resources.button_group_bottom
import com.github.jershell.shadcn.demoapp.generated.resources.button_group_copy
import com.github.jershell.shadcn.demoapp.generated.resources.button_group_cut
import com.github.jershell.shadcn.demoapp.generated.resources.button_group_decline
import com.github.jershell.shadcn.demoapp.generated.resources.button_group_items_keep_their_own_variants_joining_only_affec
import com.github.jershell.shadcn.demoapp.generated.resources.button_group_joined_buttons_inner_corners_are_squared_and_bor
import com.github.jershell.shadcn.demoapp.generated.resources.button_group_later
import com.github.jershell.shadcn.demoapp.generated.resources.button_group_middle
import com.github.jershell.shadcn.demoapp.generated.resources.button_group_paste
import com.github.jershell.shadcn.demoapp.generated.resources.button_group_purchase
import com.github.jershell.shadcn.demoapp.generated.resources.button_group_top
import com.github.jershell.shadcn.demoapp.generated.resources.button_group_usage
import com.github.jershell.shadcn.demoapp.generated.resources.button_group_variants
import com.github.jershell.shadcn.demoapp.generated.resources.button_group_vertical
import com.github.jershell.shadcn.demoapp.generated.resources.button_group_vertical_orientation_outer_top_bottom_corners_ke
import com.github.jershell.shadcn.demoapp.generated.resources.button_group_with_separator
import com.github.jershell.shadcn.demoapp.generated.resources.button_group_with_text
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DemoButtonGroup() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.button_group_basic),
            description = stringResource(Res.string.button_group_joined_buttons_inner_corners_are_squared_and_bor),
        ) {
            ButtonGroup {
                Item(onClick = {}) { ButtonText(stringResource(Res.string.button_group_copy)) }
                Item(onClick = {}) { ButtonText(stringResource(Res.string.button_group_paste)) }
                Item(onClick = {}) { ButtonText(stringResource(Res.string.button_group_cut)) }
            }
        }

        DemoSection(
            title = stringResource(Res.string.button_group_variants),
            description = stringResource(Res.string.button_group_items_keep_their_own_variants_joining_only_affec),
        ) {
            ButtonGroup {
                Item(onClick = {}, variant = ButtonVariant.Default) { ButtonText(stringResource(Res.string.button_group_accept)) }
                Item(onClick = {}, variant = ButtonVariant.Secondary) { ButtonText(stringResource(Res.string.button_group_later)) }
                Item(onClick = {}, variant = ButtonVariant.Outline) { ButtonText(stringResource(Res.string.button_group_decline)) }
            }
        }

        DemoSection(
            title = stringResource(Res.string.button_group_with_separator),
            description = stringResource(Res.string.button_group_a_separator_line_between_icon_buttons_stretching),
        ) {
            ButtonGroup {
                Item(onClick = {}, size = ButtonSize.Icon) {
                    ButtonIcon(imageVector = Lucide.Scissors, contentDescription = stringResource(Res.string.button_group_cut))
                }
                Separator()
                Item(onClick = {}, size = ButtonSize.Icon) {
                    ButtonIcon(imageVector = Lucide.Copy, contentDescription = stringResource(Res.string.button_group_copy))
                }
            }
        }

        DemoSection(
            title = stringResource(Res.string.button_group_with_text),
            description = stringResource(Res.string.button_group_a_muted_label_block_joined_with_a_primary_action),
        ) {
            ButtonGroup {
                Text(text = stringResource(Res.string.button_group_12_99))
                Item(onClick = {}, variant = ButtonVariant.Default) { ButtonText(stringResource(Res.string.button_group_purchase)) }
            }
        }

        DemoSection(
            title = stringResource(Res.string.button_group_vertical),
            description = stringResource(Res.string.button_group_vertical_orientation_outer_top_bottom_corners_ke),
        ) {
            ButtonGroup(orientation = ButtonGroupOrientation.Vertical) {
                Item(onClick = {}) { ButtonText(stringResource(Res.string.button_group_top)) }
                Item(onClick = {}) { ButtonText(stringResource(Res.string.button_group_middle)) }
                Item(onClick = {}) { ButtonText(stringResource(Res.string.button_group_bottom)) }
            }
        }

        Muted(stringResource(Res.string.button_group_usage))
        InlineCode(
            text = """
                ButtonGroup {
                    Item(onClick = {}) { ButtonText("Copy") }
                    Item(onClick = {}) { ButtonText("Paste") }
                }
                ButtonGroup(orientation = ButtonGroupOrientation.Vertical) {
                    Item(onClick = {}, variant = ButtonVariant.Outline) { ButtonText("Top") }
                }
                ButtonGroup {
                    Text(text = "€ 12.99")
                    Item(onClick = {}) { ButtonText("Purchase") }
                }
            """.trimIndent(),
            selected = false,
            selectEnabled = true,
            copyEnabled = true,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
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
