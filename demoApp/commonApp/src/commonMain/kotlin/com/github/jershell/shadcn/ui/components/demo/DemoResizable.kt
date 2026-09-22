package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.resizable.ResizableOrientation
import com.github.jershell.shadcn.components.resizable.ResizablePanelGroup
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.resizable_drag_the_separator_between_panels
import com.github.jershell.shadcn.demoapp.generated.resources.resizable_horizontal
import com.github.jershell.shadcn.demoapp.generated.resources.resizable_one
import com.github.jershell.shadcn.demoapp.generated.resources.resizable_three
import com.github.jershell.shadcn.demoapp.generated.resources.resizable_two
import com.github.jershell.shadcn.demoapp.generated.resources.resizable_usage
import com.github.jershell.shadcn.demoapp.generated.resources.resizable_visible_drag_handles_styled_like_shadcn
import com.github.jershell.shadcn.demoapp.generated.resources.resizable_with_handle
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoResizable() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.resizable_horizontal),
            description = stringResource(Res.string.resizable_drag_the_separator_between_panels),
        ) {
            ResizablePanelGroup(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                orientation = ResizableOrientation.Horizontal,
            ) {
                panel {
                    DemoPanel(stringResource(Res.string.resizable_one))
                }
                handle()
                panel {
                    DemoPanel(stringResource(Res.string.resizable_two))
                }
                handle()
                panel {
                    DemoPanel(stringResource(Res.string.resizable_three))
                }
            }
        }

        DemoSection(
            title = stringResource(Res.string.resizable_with_handle),
            description = stringResource(Res.string.resizable_visible_drag_handles_styled_like_shadcn),
        ) {
            val shape = RoundedCornerShape(Theme[DimProps][DimTokens.radiusLg])
            ResizablePanelGroup(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .border(
                        width = Theme[DimProps][DimTokens.borderWidth],
                        color = Theme[ColorProps][ColorTokens.border],
                        shape = shape,
                    )
                    .background(
                        color = Theme[ColorProps][ColorTokens.background],
                        shape = shape,
                    ),
                orientation = ResizableOrientation.Horizontal,
            ) {
                panel {
                    DemoPanel(stringResource(Res.string.resizable_one))
                }
                handle(withHandle = true)
                panel {
                    ResizablePanelGroup(
                        modifier = Modifier.fillMaxSize(),
                        orientation = ResizableOrientation.Vertical,
                    ) {
                        panel {
                            DemoPanel(stringResource(Res.string.resizable_two))
                        }
                        handle(withHandle = true)
                        panel {
                            DemoPanel(stringResource(Res.string.resizable_three))
                        }
                    }
                }
            }
        }

        Muted(stringResource(Res.string.resizable_usage))
        InlineCode(
            text = """
                ResizablePanelGroup(
                    orientation = ResizableOrientation.Horizontal
                ) {
                    panel { /* content */ }
                    handle(withHandle = true)
                    panel {
                        ResizablePanelGroup(
                            orientation = ResizableOrientation.Vertical
                        ) {
                            panel { /* content */ }
                            handle(withHandle = true)
                            panel { /* content */ }
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

@Composable
private fun DemoPanel(
    label: String,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme[ColorProps][ColorTokens.background]),
        contentAlignment = Alignment.Center,
    ) {
        P(label)
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
