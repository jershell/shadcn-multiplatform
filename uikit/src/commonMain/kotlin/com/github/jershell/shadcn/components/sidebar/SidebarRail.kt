package com.github.jershell.shadcn.components.sidebar

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens

/**
 * Clickable rail along the sidebar edge (original shadcn `SidebarRail`):
 * a 16dp-wide strip centered on the sidebar border that toggles expansion on
 * click. On hover shows a 2dp line in `sidebarBorder` color at the edge.
 * Works even when the sidebar is collapsed offcanvas (width 0) — the half of
 * the rail outside the sidebar remains hoverable/clickable.
 */
@Composable
internal fun SidebarRail(
    controller: SidebarController,
    modifier: Modifier = Modifier,
) {
    val sidebarBorder = Theme[ColorProps][ColorTokens.sidebarBorder]
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val latestController by rememberUpdatedState(controller)

    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(SidebarDefaults.RailWidth)
            .hoverable(interactionSource = interactionSource)
            .pointerHoverIcon(PointerIcon.Hand)
            .pointerInput(Unit) {
                detectTapGestures { latestController.toggle() }
            }
            .drawBehind {
                if (isHovered) {
                    val centerX = size.width / 2f
                    drawLine(
                        color = sidebarBorder,
                        start = Offset(centerX, 0f),
                        end = Offset(centerX, size.height),
                        strokeWidth = SidebarDefaults.RailLineWidth.toPx(),
                    )
                }
            },
    )
}
