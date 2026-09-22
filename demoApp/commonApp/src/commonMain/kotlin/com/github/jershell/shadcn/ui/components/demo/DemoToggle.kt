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
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Bold
import com.composables.icons.lucide.Italic
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Strikethrough
import com.composables.icons.lucide.Underline
import com.github.jershell.shadcn.components.toggle.Toggle
import com.github.jershell.shadcn.components.toggle.ToggleIcon
import com.github.jershell.shadcn.components.toggle.ToggleSize
import com.github.jershell.shadcn.components.toggle.ToggleText
import com.github.jershell.shadcn.components.toggle.ToggleVariant
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.toggle_a_two_state_button_that_can_be_either_on_or_off
import com.github.jershell.shadcn.demoapp.generated.resources.toggle_default
import com.github.jershell.shadcn.demoapp.generated.resources.toggle_disabled
import com.github.jershell.shadcn.demoapp.generated.resources.toggle_outline
import com.github.jershell.shadcn.demoapp.generated.resources.toggle_sizes
import com.github.jershell.shadcn.demoapp.generated.resources.toggle_toggle_bold
import com.github.jershell.shadcn.demoapp.generated.resources.toggle_toggle_italic
import com.github.jershell.shadcn.demoapp.generated.resources.toggle_toggle_strikethrough
import com.github.jershell.shadcn.demoapp.generated.resources.toggle_toggle_underline
import com.github.jershell.shadcn.demoapp.generated.resources.toggle_usage
import com.github.jershell.shadcn.demoapp.generated.resources.toggle_with_text
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DemoToggle() {
    var bold by remember { mutableStateOf(false) }
    var italic by remember { mutableStateOf(false) }
    var underline by remember { mutableStateOf(false) }
    var strikethrough by remember { mutableStateOf(false) }
    var italicText by remember { mutableStateOf(false) }
    var disabledOn by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(title = stringResource(Res.string.toggle_default)) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Toggle(
                    checked = bold,
                    onCheckedChange = { bold = it },
                ) {
                    ToggleIcon(
                        imageVector = Lucide.Bold,
                        contentDescription = stringResource(Res.string.toggle_toggle_bold),
                    )
                }
                Toggle(
                    checked = underline,
                    onCheckedChange = { underline = it },
                ) {
                    ToggleIcon(
                        imageVector = Lucide.Underline,
                        contentDescription = stringResource(Res.string.toggle_toggle_underline),
                    )
                }
                Toggle(
                    checked = strikethrough,
                    onCheckedChange = { strikethrough = it },
                ) {
                    ToggleIcon(
                        imageVector = Lucide.Strikethrough,
                        contentDescription = stringResource(Res.string.toggle_toggle_strikethrough),
                    )
                }
            }
        }

        DemoSection(title = stringResource(Res.string.toggle_outline)) {
            Toggle(
                checked = italic,
                onCheckedChange = { italic = it },
                variant = ToggleVariant.Outline,
            ) {
                ToggleIcon(
                    imageVector = Lucide.Italic,
                    contentDescription = stringResource(Res.string.toggle_toggle_italic),
                )
            }
        }

        DemoSection(title = stringResource(Res.string.toggle_sizes)) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Toggle(
                    checked = underline,
                    onCheckedChange = { underline = it },
                    size = ToggleSize.Sm,
                    variant = ToggleVariant.Outline,
                ) {
                    ToggleIcon(
                        imageVector = Lucide.Underline,
                        contentDescription = stringResource(Res.string.toggle_toggle_underline),
                    )
                }
                Toggle(
                    checked = bold,
                    onCheckedChange = { bold = it },
                ) {
                    ToggleIcon(
                        imageVector = Lucide.Bold,
                        contentDescription = stringResource(Res.string.toggle_toggle_bold),
                    )
                }
                Toggle(
                    checked = strikethrough,
                    onCheckedChange = { strikethrough = it },
                    size = ToggleSize.Lg,
                    variant = ToggleVariant.Outline,
                ) {
                    ToggleIcon(
                        imageVector = Lucide.Strikethrough,
                        contentDescription = stringResource(Res.string.toggle_toggle_strikethrough),
                    )
                }
            }
        }

        DemoSection(title = stringResource(Res.string.toggle_with_text)) {
            Toggle(
                checked = italicText,
                onCheckedChange = { italicText = it },
            ) {
                ToggleText(stringResource(Res.string.toggle_toggle_italic))
                ToggleIcon(
                    imageVector = Lucide.Italic,
                    contentDescription = null,
                )
            }
        }

        DemoSection(title = stringResource(Res.string.toggle_disabled)) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Toggle(
                    checked = false,
                    onCheckedChange = {},
                    enabled = false,
                ) {
                    ToggleIcon(
                        imageVector = Lucide.Bold,
                        contentDescription = stringResource(Res.string.toggle_toggle_bold),
                    )
                }
                Toggle(
                    checked = disabledOn,
                    onCheckedChange = { disabledOn = it },
                    enabled = false,
                    variant = ToggleVariant.Outline,
                ) {
                    ToggleIcon(
                        imageVector = Lucide.Italic,
                        contentDescription = stringResource(Res.string.toggle_toggle_italic),
                    )
                }
            }
        }

        Muted(stringResource(Res.string.toggle_usage))
        InlineCode(
            text = """
                var bold by remember { mutableStateOf(false) }
                Toggle(
                    checked = bold,
                    onCheckedChange = { bold = it },
                ) {
                    ToggleIcon(imageVector = Lucide.Bold, contentDescription = null)
                }
                Toggle(
                    checked = italicText,
                    onCheckedChange = { italicText = it },
                    variant = ToggleVariant.Outline,
                ) {
                    ToggleText("Toggle italic")
                    ToggleIcon(imageVector = Lucide.Italic, contentDescription = null)
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
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        H4(title)
        P(stringResource(Res.string.toggle_a_two_state_button_that_can_be_either_on_or_off))
        content()
    }
}
