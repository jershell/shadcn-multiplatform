package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.ui.unit.dp
import com.composeunstyled.AnchorAlignment
import com.composeunstyled.AnchorSide
import com.github.jershell.shadcn.components.button.Button
import com.github.jershell.shadcn.components.button.ButtonText
import com.github.jershell.shadcn.components.input.Input
import com.github.jershell.shadcn.components.popover.Popover
import com.github.jershell.shadcn.components.popover.PopoverContent
import com.github.jershell.shadcn.components.popover.PopoverDescription
import com.github.jershell.shadcn.components.popover.PopoverHeader
import com.github.jershell.shadcn.components.popover.PopoverTitle
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.popover_bottom
import com.github.jershell.shadcn.demoapp.generated.resources.popover_bottom_side
import com.github.jershell.shadcn.demoapp.generated.resources.popover_default
import com.github.jershell.shadcn.demoapp.generated.resources.popover_dimensions
import com.github.jershell.shadcn.demoapp.generated.resources.popover_end
import com.github.jershell.shadcn.demoapp.generated.resources.popover_end_side
import com.github.jershell.shadcn.demoapp.generated.resources.popover_for_displaying_rich_content_in_a_portal
import com.github.jershell.shadcn.demoapp.generated.resources.popover_height
import com.github.jershell.shadcn.demoapp.generated.resources.popover_new_layer
import com.github.jershell.shadcn.demoapp.generated.resources.popover_open_popover
import com.github.jershell.shadcn.demoapp.generated.resources.popover_placements
import com.github.jershell.shadcn.demoapp.generated.resources.popover_print
import com.github.jershell.shadcn.demoapp.generated.resources.popover_set_the_dimensions_for_the_layer
import com.github.jershell.shadcn.demoapp.generated.resources.popover_start
import com.github.jershell.shadcn.demoapp.generated.resources.popover_start_side
import com.github.jershell.shadcn.demoapp.generated.resources.popover_the_panel_is_placed_on_the_bottom_side
import com.github.jershell.shadcn.demoapp.generated.resources.popover_the_panel_is_placed_on_the_end_side
import com.github.jershell.shadcn.demoapp.generated.resources.popover_the_panel_is_placed_on_the_start_side
import com.github.jershell.shadcn.demoapp.generated.resources.popover_the_panel_is_placed_on_the_top_side
import com.github.jershell.shadcn.demoapp.generated.resources.popover_top
import com.github.jershell.shadcn.demoapp.generated.resources.popover_top_side
import com.github.jershell.shadcn.demoapp.generated.resources.popover_usage
import com.github.jershell.shadcn.demoapp.generated.resources.popover_width
import com.github.jershell.shadcn.demoapp.generated.resources.popover_with_form
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DemoPopover() {
    var open by remember { mutableStateOf(false) }
    var topOpen by remember { mutableStateOf(false) }
    var bottomOpen by remember { mutableStateOf(false) }
    var startOpen by remember { mutableStateOf(false) }
    var endOpen by remember { mutableStateOf(false) }
    var formOpen by remember { mutableStateOf(false) }
    val widthState = rememberTextFieldState()
    val heightState = rememberTextFieldState()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(title = stringResource(Res.string.popover_default)) {
            Popover(
                expanded = open,
                onExpandedChange = { open = it },
                sideOffset = TwDimensions.gapGapToken1,
                anchor = {
                    Button(onClick = { open = !open }) {
                        ButtonText(stringResource(Res.string.popover_open_popover))
                    }
                },
            ) {
                PopoverContent {
                    PopoverHeader {
                        PopoverTitle(stringResource(Res.string.popover_dimensions))
                        PopoverDescription(stringResource(Res.string.popover_set_the_dimensions_for_the_layer))
                    }
                }
            }
        }

        DemoSection(title = stringResource(Res.string.popover_placements)) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                PlacementPopover(
                    label = stringResource(Res.string.popover_top),
                    title = stringResource(Res.string.popover_top_side),
                    description = stringResource(Res.string.popover_the_panel_is_placed_on_the_top_side),
                    expanded = topOpen,
                    onExpandedChange = { topOpen = it },
                    side = AnchorSide.Top,
                )
                PlacementPopover(
                    label = stringResource(Res.string.popover_bottom),
                    title = stringResource(Res.string.popover_bottom_side),
                    description = stringResource(Res.string.popover_the_panel_is_placed_on_the_bottom_side),
                    expanded = bottomOpen,
                    onExpandedChange = { bottomOpen = it },
                    side = AnchorSide.Bottom,
                )
                PlacementPopover(
                    label = stringResource(Res.string.popover_start),
                    title = stringResource(Res.string.popover_start_side),
                    description = stringResource(Res.string.popover_the_panel_is_placed_on_the_start_side),
                    expanded = startOpen,
                    onExpandedChange = { startOpen = it },
                    side = AnchorSide.Start,
                )
                PlacementPopover(
                    label = stringResource(Res.string.popover_end),
                    title = stringResource(Res.string.popover_end_side),
                    description = stringResource(Res.string.popover_the_panel_is_placed_on_the_end_side),
                    expanded = endOpen,
                    onExpandedChange = { endOpen = it },
                    side = AnchorSide.End,
                )
            }
        }

        DemoSection(title = stringResource(Res.string.popover_with_form)) {
            Popover(
                expanded = formOpen,
                onExpandedChange = { formOpen = it },
                sideOffset = TwDimensions.gapGapToken1,
                anchor = {
                    Button(onClick = { formOpen = !formOpen }) {
                        ButtonText(stringResource(Res.string.popover_new_layer))
                    }
                },
            ) {
                PopoverContent {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        PopoverHeader {
                            PopoverTitle(stringResource(Res.string.popover_dimensions))
                            PopoverDescription(stringResource(Res.string.popover_set_the_dimensions_for_the_layer))
                        }
                        Input(
                            state = widthState,
                            placeholder = stringResource(Res.string.popover_width),
                        )
                        Input(
                            state = heightState,
                            placeholder = stringResource(Res.string.popover_height),
                        )
                        Button(
                            onClick = { formOpen = false },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            ButtonText(stringResource(Res.string.popover_print))
                        }
                    }
                }
            }
        }

        Muted(stringResource(Res.string.popover_usage))
        InlineCode(
            text = """
                var open by remember { mutableStateOf(false) }
                Popover(
                    expanded = open,
                    onExpandedChange = { open = it },
                    anchor = {
                        Button(onClick = { open = !open }) {
                            ButtonText("Open popover")
                        }
                    },
                ) {
                    PopoverContent {
                        PopoverHeader {
                            PopoverTitle("Dimensions")
                            PopoverDescription("Set the dimensions for the layer.")
                        }
                    }
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
private fun PlacementPopover(
    label: String,
    title: String,
    description: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    side: AnchorSide,
) {
    Popover(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        side = side,
        alignment = AnchorAlignment.Center,
        sideOffset = TwDimensions.gapGapToken1,
        anchor = {
            Button(onClick = { onExpandedChange(!expanded) }) {
                ButtonText(label)
            }
        },
    ) {
        PopoverContent {
            PopoverHeader {
                PopoverTitle(title)
                PopoverDescription(description)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DemoSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        H4(title)
        P(stringResource(Res.string.popover_for_displaying_rich_content_in_a_portal))
        content()
    }
}
