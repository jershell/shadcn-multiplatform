package com.github.jershell.util

import net.swiftzer.semver.SemVer

fun getVersionCode(raw: String): Long {
    return SemVer.parse(raw).run {
        "$major$minor$patch".toLong()
    }
}