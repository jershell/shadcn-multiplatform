package com.github.jershell.shadcn.components.resizable

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import java.awt.Cursor

internal actual fun Modifier.resizableHoverCursor(
    orientation: ResizableOrientation,
): Modifier {
    val awtType = when (orientation) {
        ResizableOrientation.Horizontal -> Cursor.E_RESIZE_CURSOR
        ResizableOrientation.Vertical -> Cursor.N_RESIZE_CURSOR
    }
    return pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(awtType)))
}
