package com.github.jershell.shadcn.util

import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

internal val Path.absolutePath: String
    get() {
        return generateSequence(SystemFileSystem.resolve(Path("./"))) {
            it.parent
        }.toList().reversed().joinToString("/") { it.name }
    }
