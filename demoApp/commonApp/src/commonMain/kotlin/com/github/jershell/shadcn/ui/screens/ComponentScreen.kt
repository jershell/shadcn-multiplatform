package com.github.jershell.shadcn.ui.screens

import androidx.compose.runtime.Composable
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.ui.containers.ScreenScaffold
import com.github.jershell.shadcn.ui.navigation.ComponentId
import com.github.jershell.shadcn.ui.navigation.ComponentsRegistry
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.component_component_with_id_x_need_add_to_componentsregist
import org.jetbrains.compose.resources.stringResource

@Composable
fun ComponentScreen(id: ComponentId) {
    val component = ComponentsRegistry.get(id)
    if (component == null) {
        P(stringResource(Res.string.component_component_with_id_x_need_add_to_componentsregist, id))
        return
    }

    ScreenScaffold(
        title = component.name,
        description = component.description,
    ) {
        component.demo()
    }
}
