package com.github.jershell.shadcn.components.sidebar

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.PanelLeft
import com.composables.icons.lucide.PanelRight
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens

/**
 * Collapse/expand toggle styled after the original shadcn `SidebarTrigger`:
 * a ghost icon button (28dp) with the `PanelLeft` / `PanelRight` icon depending
 * on [side]. Calls `sidebarState.toggle()` — no manual onClick needed.
 */
@Composable
internal fun SidebarTriggerButton(
    onClick: () -> Unit,
    side: SidebarSide,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val radiusMd = Theme[DimProps][DimTokens.radiusMd]
    val icon = if (side == SidebarSide.Left) Lucide.PanelLeft else Lucide.PanelRight

    SidebarActionButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.size(SidebarDefaults.TriggerSize),
        shape = RoundedCornerShape(radiusMd),
        content = {
            SidebarIcon(
                imageVector = icon,
                modifier = Modifier.size(SidebarDefaults.IconSize),
            )
        },
    )
}

/**
 * Standalone collapse/expand toggle for the sidebar.
 *
 * Works anywhere under [SidebarProvider] or inside `Sidebar { }`:
 * it reads the expansion state from [LocalSidebarController] and toggles it.
 * Useful when the sidebar is collapsed with [SidebarCollapsible.Offcanvas] and
 * the trigger inside the sidebar is no longer reachable.
 *
 * @param side direction of the panel icon; defaults to the side of the
 *   enclosing sidebar ([SidebarController.side] from the nearest controller).
 */
@Composable
fun SidebarTrigger(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    side: SidebarSide? = null,
) {
    val controller = LocalSidebarController.current
        ?: error("SidebarTrigger must be used inside SidebarProvider { } or Sidebar { }")

    SidebarTriggerButton(
        onClick = controller::toggle,
        side = side ?: controller.side,
        modifier = modifier,
        enabled = enabled,
    )
}
