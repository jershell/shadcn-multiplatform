package com.github.jershell.shadcn

import com.github.jershell.shadcn.services.AppService
import com.github.jershell.shadcn.ui.containers.root.RootLayoutViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {
    single<AppService>() {
        AppService(get())
    }
    viewModel<RootLayoutViewModel> {
        RootLayoutViewModel(get())
    }
}

expect fun platformModule(): Module
