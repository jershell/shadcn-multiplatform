package com.github.jershell.shadcn

import com.github.jershell.shadcn.services.FileService
import org.koin.dsl.module

actual fun platformModule() = module {
    single<FileService>() {
        AndroidFileService()
    }
}
