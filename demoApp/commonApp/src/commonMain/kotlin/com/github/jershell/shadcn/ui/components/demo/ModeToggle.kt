package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.composables.icons.tabler.Tabler
import com.composables.icons.tabler.outline.*
import com.composeunstyled.UnstyledIcon
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.switch.Switch
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.Mode
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.mode_toggle_dark_mode
import com.github.jershell.shadcn.demoapp.generated.resources.mode_toggle_light_mode
import org.jetbrains.compose.resources.stringResource

@Composable
fun ModeToggle(
    mode: Mode,
    onModeChange: (Mode) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDark = mode == Mode.Dark
    val iconTint = Theme[ColorProps][ColorTokens.foreground]

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        UnstyledIcon(
            painter = rememberVectorPainter(Tabler.Outline.Sun),
            contentDescription = stringResource(Res.string.mode_toggle_light_mode),
            modifier = Modifier.size(16.dp),
            tint = iconTint,
        )
        Switch(
            checked = isDark,
            onCheckedChange = { onModeChange(if (it) Mode.Dark else Mode.Light) },
        )
        UnstyledIcon(
            painter = rememberVectorPainter(Tabler.Outline.Moon),
            contentDescription = stringResource(Res.string.mode_toggle_dark_mode),
            modifier = Modifier.size(16.dp),
            tint = iconTint,
        )
    }
}
