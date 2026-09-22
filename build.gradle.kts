import com.github.jershell.shadcnref.SyncShadcnReference
import com.github.jershell.theme.ParseColorDesignTokens

plugins {
    alias(libs.plugins.kotlin.multiplatform).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)
    alias(libs.plugins.compose.multiplatform).apply(false)
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.android.kmp.library).apply(false)
    alias(libs.plugins.kotlin.jvm).apply(false)
    alias(libs.plugins.kotlinx.serialization).apply(false)
    alias(libs.plugins.buildConfig).apply(false)
    id("com.github.jershell.gradle.plugin.build") apply true
    alias(libs.plugins.vanniktech.maven.publish).apply(false)
}

tasks.register<ParseColorDesignTokens>("importThemeFromFigmaTokens") {
    this.group = "shadcn"
    inputFiles.from(
        rootProject.layout.projectDirectory.file("imports/design-tokens.json").asFile,
    )
    outputDirectory.set(
        rootProject.layout.projectDirectory.dir("uikit/src/commonMain/kotlin"),
    )
    packageName.set("com.github.jershell.shadcn.theme")
    varNamesCase.set(ParseColorDesignTokens.TokenCase.CAMEL_CASE)
}

tasks.register<SyncShadcnReference>("syncShadcnReference") {
    this.group = "shadcn"
    componentsFile.set(
        rootProject.layout.projectDirectory.file("COMPONENTS.md"),
    )
    outputDirectory.set(
        rootProject.layout.projectDirectory.dir("imports/shadcn-reference"),
    )
}

// demoApp is not a Gradle project (only its subprojects are included in settings.gradle.kts),
// so :demoApp:clean does not exist; this is a convenience wrapper over clean for all modules.
val cleanAll by tasks.registering {
    group = "build"
    description = "Runs :clean for every included project."
    dependsOn(subprojects.mapNotNull { it.tasks.findByName("clean") })
}

