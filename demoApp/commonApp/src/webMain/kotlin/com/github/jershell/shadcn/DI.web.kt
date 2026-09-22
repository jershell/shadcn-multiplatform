package com.github.jershell.shadcn

import com.github.jershell.shadcn.services.FileService
import kotlinx.browser.localStorage
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module {
    return module {
        single<FileService>() {
            object :FileService {
                override fun readTextFile(filename: String): String? {
                    return localStorage.getItem(filename)
                }

                override fun writeTextFile(filename: String, content: String) {
                    localStorage.setItem(filename, content)
                }

                override fun fileExists(filename: String): Boolean {
                    return localStorage.getItem(filename) != null
                }
            }
        }
    }
}
