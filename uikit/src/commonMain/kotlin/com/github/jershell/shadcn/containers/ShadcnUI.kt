package com.github.jershell.shadcn.containers

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.composeunstyled.ModalHost
import com.github.jershell.shadcn.components.dialog.DialogHost
import com.github.jershell.shadcn.components.toast.ToastHost
import com.github.jershell.shadcn.theme.Mode
import com.github.jershell.shadcn.theme.ShadcnPreset
import com.github.jershell.shadcn.theme.ShadcnTheme

@Composable
fun ShadcnUI(
    mode: Mode = if (isSystemInDarkTheme()) Mode.Dark else Mode.Light,
    onModeChanged: @Composable (mode: Mode) -> Unit = {},
    preset: ShadcnPreset? = null,
    content: @Composable () -> Unit
) {
    ShadcnTheme(preset = preset, mode = mode, onModeChanged = onModeChanged) {
        ModalHost {
            content()
            DialogHost()
            ToastHost()
        }
    }
}