package com.github.jershell.shadcn.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler

@Composable fun openUri(s: String) {
    val uriHandler = LocalUriHandler.current
    uriHandler.openUri(s)
}
