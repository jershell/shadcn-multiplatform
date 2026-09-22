package com.github.jershell.shadcn.androidApp

import android.app.Application
import com.github.jershell.shadcn.commonModule
import com.github.jershell.shadcn.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AndroidApplication: Application() {
    override fun onCreate() {
        startKoin {
            androidLogger()
            androidContext(this@AndroidApplication)
            modules(commonModule, platformModule())
        }

        super.onCreate()
    }

}
