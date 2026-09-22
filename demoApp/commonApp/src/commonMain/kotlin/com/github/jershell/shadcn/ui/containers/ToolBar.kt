package com.github.jershell.features.root

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jershell.shadcn.components.sidebar.SidebarTrigger
import com.github.jershell.shadcn.theme.Mode
import com.github.jershell.shadcn.ui.components.demo.ModeToggle
import com.github.jershell.shadcn.ui.containers.root.RootLayoutViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ToolBar() {
    val viewModel = koinViewModel<RootLayoutViewModel>()
    val isDark by viewModel.isDark.collectAsState()
    val mode = if (isDark) Mode.Dark else Mode.Light

    Row(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SidebarTrigger()
        Spacer(Modifier.weight(1f))
        ModeToggle(
            mode = mode,
            onModeChange = { viewModel.setIsDark(it == Mode.Dark) },
        )
    }
}
