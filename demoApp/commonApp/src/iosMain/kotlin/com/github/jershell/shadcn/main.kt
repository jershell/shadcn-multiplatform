package com.github.jershell.shadcn

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.ComposeUIViewController
import com.github.jershell.shadcn.theme.Mode
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.UIViewController
import platform.UIKit.setStatusBarStyle

fun MainViewController(): UIViewController = ComposeUIViewController {
    App(onModeChanged = { ThemeChanged(it) })
}

@Composable
private fun ThemeChanged(mode: Mode) {
    LaunchedEffect(mode) {
        UIApplication.sharedApplication.setStatusBarStyle(
            if (mode == Mode.Dark) UIStatusBarStyleDarkContent else UIStatusBarStyleLightContent
        )
    }
}
