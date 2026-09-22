package com.github.jershell.util

import org.gradle.api.Project
import java.util.Properties

private const val FILE_NAME_LOCAL_PROPERTIES = "local.properties"


fun Project.getProperty(key: String): String {
    val localPropertiesFile = rootProject.file(FILE_NAME_LOCAL_PROPERTIES)

    val valueFromLocalProperties = if (localPropertiesFile.exists()) {
        val properties = Properties()
        localPropertiesFile.inputStream().buffered().use { input ->
            properties.load(input)
        }
        properties.getProperty(key)?.takeIf { it.isNotBlank() }
    } else {
        localPropertiesFile.createNewFile()
        null
    }

    val value = valueFromLocalProperties ?: System.getenv(key) ?: ""

    return value.filterIndexed { index, ch ->
        !((index == 0 || index == value.lastIndex) && ch in "'\" ")
    }
}