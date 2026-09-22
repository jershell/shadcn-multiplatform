package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ArrowUpRight
import com.composables.icons.lucide.Lucide
import com.github.jershell.shadcn.components.button.Button
import com.github.jershell.shadcn.components.button.ButtonIcon
import com.github.jershell.shadcn.components.button.ButtonSize
import com.github.jershell.shadcn.components.button.ButtonText
import com.github.jershell.shadcn.components.button.ButtonVariant
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.button_button
import com.github.jershell.shadcn.demoapp.generated.resources.button_default
import com.github.jershell.shadcn.demoapp.generated.resources.button_destructive
import com.github.jershell.shadcn.demoapp.generated.resources.button_disabled
import com.github.jershell.shadcn.demoapp.generated.resources.button_displays_a_button_or_a_component_that_looks_like
import com.github.jershell.shadcn.demoapp.generated.resources.button_extra_small
import com.github.jershell.shadcn.demoapp.generated.resources.button_ghost
import com.github.jershell.shadcn.demoapp.generated.resources.button_invalid
import com.github.jershell.shadcn.demoapp.generated.resources.button_large
import com.github.jershell.shadcn.demoapp.generated.resources.button_link
import com.github.jershell.shadcn.demoapp.generated.resources.button_login
import com.github.jershell.shadcn.demoapp.generated.resources.button_outline
import com.github.jershell.shadcn.demoapp.generated.resources.button_secondary
import com.github.jershell.shadcn.demoapp.generated.resources.button_sizes
import com.github.jershell.shadcn.demoapp.generated.resources.button_small
import com.github.jershell.shadcn.demoapp.generated.resources.button_submit
import com.github.jershell.shadcn.demoapp.generated.resources.button_the_aria_invalid_state_destructive_border_and_fo
import com.github.jershell.shadcn.demoapp.generated.resources.button_usage
import com.github.jershell.shadcn.demoapp.generated.resources.button_with_icon
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DemoButton() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(title = stringResource(Res.string.button_default)) {
            Button(onClick = {}) {
                ButtonText(stringResource(Res.string.button_button))
            }
        }

        DemoSection(title = stringResource(Res.string.button_secondary)) {
            Button(onClick = {}, variant = ButtonVariant.Secondary) {
                ButtonText(stringResource(Res.string.button_secondary))
            }
        }

        DemoSection(title = stringResource(Res.string.button_destructive)) {
            Button(onClick = {}, variant = ButtonVariant.Destructive) {
                ButtonText(stringResource(Res.string.button_destructive))
            }
        }

        DemoSection(title = stringResource(Res.string.button_outline)) {
            Button(onClick = {}, variant = ButtonVariant.Outline) {
                ButtonText(stringResource(Res.string.button_outline))
            }
        }

        DemoSection(title = stringResource(Res.string.button_ghost)) {
            Button(onClick = {}, variant = ButtonVariant.Ghost) {
                ButtonText(stringResource(Res.string.button_ghost))
            }
        }

        DemoSection(title = stringResource(Res.string.button_link)) {
            Button(onClick = {}, variant = ButtonVariant.Link) {
                ButtonText(stringResource(Res.string.button_link))
            }
        }

        DemoSection(title = stringResource(Res.string.button_with_icon)) {
            Button(onClick = {}) {
                ButtonText(stringResource(Res.string.button_login))
                ButtonIcon(
                    imageVector = Lucide.ArrowUpRight,
                    contentDescription = null,
                )
            }
        }

        DemoSection(title = stringResource(Res.string.button_disabled)) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(onClick = {}, enabled = false) {
                    ButtonText(stringResource(Res.string.button_disabled))
                }
                Button(onClick = {}, enabled = false, variant = ButtonVariant.Outline) {
                    ButtonText(stringResource(Res.string.button_disabled))
                }
            }
        }

        DemoSection(title = stringResource(Res.string.button_invalid)) {
            P(stringResource(Res.string.button_the_aria_invalid_state_destructive_border_and_fo))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(onClick = {}, isInvalid = true, variant = ButtonVariant.Outline) {
                    ButtonText(stringResource(Res.string.button_invalid))
                }
                Button(onClick = {}, isInvalid = true, variant = ButtonVariant.Destructive) {
                    ButtonText(stringResource(Res.string.button_destructive))
                }
            }
        }

        DemoSection(title = stringResource(Res.string.button_sizes)) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SizeRow(label = stringResource(Res.string.button_extra_small), size = ButtonSize.Xs)
                SizeRow(label = stringResource(Res.string.button_small), size = ButtonSize.Sm)
                SizeRow(label = stringResource(Res.string.button_default), size = ButtonSize.Default)
                SizeRow(label = stringResource(Res.string.button_large), size = ButtonSize.Lg)
            }
        }

        Muted(stringResource(Res.string.button_usage))
        InlineCode(
            text = """
                Button(onClick = {}) {
                    ButtonText("Button")
                }
                Button(onClick = {}, variant = ButtonVariant.Outline) {
                    ButtonText("Outline")
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
        P(stringResource(Res.string.button_displays_a_button_or_a_component_that_looks_like))
        content()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SizeRow(
    label: String,
    size: ButtonSize,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Button(onClick = {}, size = size, variant = ButtonVariant.Outline) {
            ButtonText(label)
        }
        Button(
            onClick = {},
            size = size.toIconSize(),
            variant = ButtonVariant.Outline,
        ) {
            ButtonIcon(
                imageVector = Lucide.ArrowUpRight,
                contentDescription = stringResource(Res.string.button_submit),
            )
        }
    }
}

private fun ButtonSize.toIconSize(): ButtonSize = when (this) {
    ButtonSize.Xs -> ButtonSize.IconXs
    ButtonSize.Sm -> ButtonSize.IconSm
    ButtonSize.Default -> ButtonSize.Icon
    ButtonSize.Lg -> ButtonSize.IconLg
    else -> this
}
