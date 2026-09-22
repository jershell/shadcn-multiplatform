package com.github.jershell.shadcn.ui.containers.root

import androidx.lifecycle.ViewModel
import com.github.jershell.shadcn.services.AppService

class RootLayoutViewModel(private val appService: AppService): ViewModel() {
    val isDark = appService.isDark

    fun setIsDark(isDark: Boolean) {
        appService.setIsDark(isDark)
    }
}
