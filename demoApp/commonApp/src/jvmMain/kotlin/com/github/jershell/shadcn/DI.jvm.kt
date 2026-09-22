package com.github.jershell.shadcn

import com.github.jershell.shadcn.services.FileService
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<FileService>() {
        JvmFileService()
    }
}
