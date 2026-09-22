package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.collapsible.Collapsible
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.collapsible_a_bordered_trigger_row_with_a_rotating_chevron_a
import com.github.jershell.shadcn.demoapp.generated.resources.collapsible_basic
import com.github.jershell.shadcn.demoapp.generated.resources.collapsible_peduarte_starred_3_repositories
import com.github.jershell.shadcn.demoapp.generated.resources.collapsible_radix_ui_colors
import com.github.jershell.shadcn.demoapp.generated.resources.collapsible_radix_ui_primitives
import com.github.jershell.shadcn.demoapp.generated.resources.collapsible_stitches_react
import com.github.jershell.shadcn.demoapp.generated.resources.collapsible_usage
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoCollapsible() {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.collapsible_basic),
            description = stringResource(Res.string.collapsible_a_bordered_trigger_row_with_a_rotating_chevron_a),
        ) {
            Collapsible(
                expanded = expanded,
                onExpandedChange = { expanded = it },
            ) {
                Trigger(text = stringResource(Res.string.collapsible_peduarte_starred_3_repositories))
                Content {
                    CollapsibleRow(stringResource(Res.string.collapsible_radix_ui_primitives))
                    CollapsibleRow(stringResource(Res.string.collapsible_radix_ui_colors))
                    CollapsibleRow(stringResource(Res.string.collapsible_stitches_react))
                }
            }
        }

        Muted(stringResource(Res.string.collapsible_usage))
        InlineCode(
            text = """
                var expanded by remember { mutableStateOf(false) }
                Collapsible(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                ) {
                    Trigger(text = "@peduarte starred 3 repositories")
                    Content {
                        CollapsibleRow("@radix-ui/primitives")
                        CollapsibleRow("@radix-ui/colors")
                    }
                }
            """.trimIndent(),
            selected = false,
            selectEnabled = true,
            copyEnabled = true,
        )
    }
}

@Composable
private fun CollapsibleRow(text: String) {
    val border = Theme[ColorProps][ColorTokens.border]
    val foreground = Theme[ColorProps][ColorTokens.foreground]
    val radius = Theme[DimProps][DimTokens.radiusMd]
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(radius))
            .border(borderWidth, border, RoundedCornerShape(radius))
            .padding(
                horizontal = TwDimensions.paddingPxToken4,
                vertical = TwDimensions.paddingPxToken3,
            ),
    ) {
        BasicText(
            text = text,
            style = TypographyStyles.textSmRegular.copy(
                color = foreground,
            ),
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
