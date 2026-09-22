package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jershell.shadcn.components.kbd.Kbd
import com.github.jershell.shadcn.components.kbd.KbdGroup
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.kbd_ctrl
import com.github.jershell.shadcn.demoapp.generated.resources.kbd_displays_a_keyboard_shortcut
import com.github.jershell.shadcn.demoapp.generated.resources.kbd_enter
import com.github.jershell.shadcn.demoapp.generated.resources.kbd_group
import com.github.jershell.shadcn.demoapp.generated.resources.kbd_k
import com.github.jershell.shadcn.demoapp.generated.resources.kbd_keys
import com.github.jershell.shadcn.demoapp.generated.resources.kbd_shift
import com.github.jershell.shadcn.demoapp.generated.resources.kbd_to_open_command_menu
import com.github.jershell.shadcn.demoapp.generated.resources.kbd_usage
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoKbd() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(title = stringResource(Res.string.kbd_keys)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Kbd(text = stringResource(Res.string.kbd_enter))
                Kbd(text = stringResource(Res.string.kbd_shift))
                Kbd(text = stringResource(Res.string.kbd_ctrl))
            }
        }

        DemoSection(title = stringResource(Res.string.kbd_group)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                KbdGroup {
                    Kbd(text = stringResource(Res.string.kbd_ctrl))
                    Kbd(text = stringResource(Res.string.kbd_k))
                }
                P(stringResource(Res.string.kbd_to_open_command_menu))
            }
        }

        Muted(stringResource(Res.string.kbd_usage))
        InlineCode(
            text = """
                Kbd(text = "Enter")
                KbdGroup {
                    Kbd(text = "Ctrl")
                    Kbd(text = "K")
                }
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
        P(stringResource(Res.string.kbd_displays_a_keyboard_shortcut))
        content()
    }
}
