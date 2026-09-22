package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composeunstyled.AnchorSide
import com.github.jershell.shadcn.components.button.Button
import com.github.jershell.shadcn.components.button.ButtonText
import com.github.jershell.shadcn.components.button.ButtonVariant
import com.github.jershell.shadcn.components.tooltip.Tooltip
import com.github.jershell.shadcn.components.tooltip.TooltipText
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.tooltip_a_popup_that_displays_information_related_to_an
import com.github.jershell.shadcn.demoapp.generated.resources.tooltip_appears_after_300ms
import com.github.jershell.shadcn.demoapp.generated.resources.tooltip_bottom
import com.github.jershell.shadcn.demoapp.generated.resources.tooltip_default
import com.github.jershell.shadcn.demoapp.generated.resources.tooltip_delay
import com.github.jershell.shadcn.demoapp.generated.resources.tooltip_end
import com.github.jershell.shadcn.demoapp.generated.resources.tooltip_hover_me
import com.github.jershell.shadcn.demoapp.generated.resources.tooltip_side
import com.github.jershell.shadcn.demoapp.generated.resources.tooltip_simple_tooltip
import com.github.jershell.shadcn.demoapp.generated.resources.tooltip_start
import com.github.jershell.shadcn.demoapp.generated.resources.tooltip_tooltip_on_bottom
import com.github.jershell.shadcn.demoapp.generated.resources.tooltip_tooltip_on_end
import com.github.jershell.shadcn.demoapp.generated.resources.tooltip_tooltip_on_start
import com.github.jershell.shadcn.demoapp.generated.resources.tooltip_tooltip_on_top
import com.github.jershell.shadcn.demoapp.generated.resources.tooltip_top
import com.github.jershell.shadcn.demoapp.generated.resources.tooltip_usage
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoTooltip() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(title = stringResource(Res.string.tooltip_default)) {
            Tooltip(
                tooltip = { TooltipText(stringResource(Res.string.tooltip_simple_tooltip)) },
                anchor = {
                    Button(onClick = {}, variant = ButtonVariant.Outline) {
                        ButtonText(stringResource(Res.string.tooltip_hover_me))
                    }
                },
            )
        }

        DemoSection(title = stringResource(Res.string.tooltip_side)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Tooltip(
                    tooltip = { TooltipText(stringResource(Res.string.tooltip_tooltip_on_top)) },
                    side = AnchorSide.Top,
                    anchor = {
                        Button(onClick = {}, variant = ButtonVariant.Outline) {
                            ButtonText(stringResource(Res.string.tooltip_top))
                        }
                    },
                )
                Tooltip(
                    tooltip = { TooltipText(stringResource(Res.string.tooltip_tooltip_on_bottom)) },
                    side = AnchorSide.Bottom,
                    anchor = {
                        Button(onClick = {}, variant = ButtonVariant.Outline) {
                            ButtonText(stringResource(Res.string.tooltip_bottom))
                        }
                    },
                )
                Tooltip(
                    tooltip = { TooltipText(stringResource(Res.string.tooltip_tooltip_on_start)) },
                    side = AnchorSide.Start,
                    anchor = {
                        Button(onClick = {}, variant = ButtonVariant.Outline) {
                            ButtonText(stringResource(Res.string.tooltip_start))
                        }
                    },
                )
                Tooltip(
                    tooltip = { TooltipText(stringResource(Res.string.tooltip_tooltip_on_end)) },
                    side = AnchorSide.End,
                    anchor = {
                        Button(onClick = {}, variant = ButtonVariant.Outline) {
                            ButtonText(stringResource(Res.string.tooltip_end))
                        }
                    },
                )
            }
        }

        DemoSection(title = stringResource(Res.string.tooltip_delay)) {
            Tooltip(
                tooltip = { TooltipText(stringResource(Res.string.tooltip_appears_after_300ms)) },
                hoverDelayMillis = 300L,
                anchor = {
                    Button(onClick = {}, variant = ButtonVariant.Outline) {
                        ButtonText(stringResource(Res.string.tooltip_hover_me))
                    }
                },
            )
        }

        Muted(stringResource(Res.string.tooltip_usage))
        InlineCode(
            text = """
                Tooltip(
                    tooltip = { TooltipText("Simple Tooltip") },
                    side = AnchorSide.Top,
                    anchor = { Button(onClick = {}, variant = ButtonVariant.Outline) { ButtonText("Hover me") } },
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
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        H4(title)
        P(stringResource(Res.string.tooltip_a_popup_that_displays_information_related_to_an))
        content()
    }
}
