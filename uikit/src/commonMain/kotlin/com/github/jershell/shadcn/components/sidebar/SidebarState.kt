package com.github.jershell.shadcn.components.sidebar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue

enum class SidebarSide {
    Left,
    Right,
}

enum class SidebarVariant {
    Sidebar,
    Floating,
    Inset,
}

enum class SidebarCollapsible {
    None,
    Offcanvas,
    Icon,
}

/**
 * Public handle on the sidebar expansion state.
 *
 * The controller is provided via [LocalSidebarController] inside [SidebarProvider]
 * and [Sidebar]. Use it to build custom toggles outside the sidebar DSL:
 *
 * ```
 * val controller = LocalSidebarController.current
 * controller?.toggle()
 * ```
 */
@Stable
class SidebarController internal constructor(
    val isExpanded: Boolean,
    val side: SidebarSide,
    internal val onExpandedChange: (Boolean) -> Unit,
) {
    fun toggle() {
        onExpandedChange(!isExpanded)
    }

    fun setExpanded(expanded: Boolean) {
        onExpandedChange(expanded)
    }
}

/**
 * Provides the [SidebarController] of the enclosing sidebar.
 * Null when there is no [SidebarProvider] / [Sidebar] above in the composition.
 */
val LocalSidebarController = compositionLocalOf<SidebarController?> { null }

internal class SidebarState(
    val controller: SidebarController,
    val collapsible: SidebarCollapsible,
    val variant: SidebarVariant,
) {
    val expanded: Boolean
        get() = controller.isExpanded

    val side: SidebarSide
        get() = controller.side

    val isIconCollapsed: Boolean
        get() = collapsible == SidebarCollapsible.Icon && !expanded

    val isOffcanvasHidden: Boolean
        get() = collapsible == SidebarCollapsible.Offcanvas && !expanded

    fun toggle() {
        controller.toggle()
    }
}

internal val LocalSidebarState = compositionLocalOf<SidebarState?> { null }

@Composable
internal fun requireSidebarState(): SidebarState {
    return LocalSidebarState.current
        ?: error("Sidebar subcomponents must be used inside Sidebar { }")
}

private class SidebarExpandedHolder(initialExpanded: Boolean) {
    var value by mutableStateOf(initialExpanded)
}

/**
 * Controlled/uncontrolled expansion state with a stable `onChange` lambda
 * (remembered across recompositions) so the state holder objects built on top
 * of it do not have to be recreated on every recomposition.
 */
@Composable
internal fun rememberSidebarExpandedState(
    expanded: Boolean?,
    defaultExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
): Pair<Boolean, (Boolean) -> Unit> {
    val holder = remember { SidebarExpandedHolder(defaultExpanded) }
    val isControlled = expanded != null
    val currentExpanded = expanded ?: holder.value

    val latestIsControlled by rememberUpdatedState(isControlled)
    val latestOnExpandedChange by rememberUpdatedState(onExpandedChange)
    val onChange = remember {
        { value: Boolean ->
            if (!latestIsControlled) {
                holder.value = value
            }
            latestOnExpandedChange(value)
        }
    }
    return currentExpanded to onChange
}
