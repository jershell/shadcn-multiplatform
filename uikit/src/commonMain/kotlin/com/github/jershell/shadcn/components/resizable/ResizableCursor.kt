package com.github.jershell.shadcn.components.resizable

import androidx.compose.ui.Modifier

internal expect fun Modifier.resizableHoverCursor(
    orientation: ResizableOrientation,
): Modifier
