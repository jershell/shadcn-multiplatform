import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

/*
tasks.configureEach {
    if (name.startsWith("compileKotlin")) {
        dependsOn(rootProject.tasks.named("importThemeFromFigmaTokens"))
    }
}*/

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.vanniktech.maven.publish)
}

group = "com.github.jershell"
version = libs.versions.libversion.get()

kotlin {
    android {
        namespace = "com.github.jershell.shadcn"
        compileSdk = 37
        minSdk = 23
        androidResources.enable = true
        compilerOptions { jvmTarget = JvmTarget.JVM_17 }
    }

    jvm {
        compilerOptions { jvmTarget = JvmTarget.JVM_17 }
    }

    // io.github.oleksandrbalan:lazytable has not for js
    // js { browser() }
    wasmJs { browser() }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {

            // compose
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.foundation)
            implementation(libs.compose.resources)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.compose.nav3)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime)
            implementation(libs.androidx.lifecycle.viewmodel.navigation3)

            // kotlin
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)

            // etc
            implementation(libs.coil)
            implementation(libs.coil.network.ktor)

            // composeunstyled
            api(libs.composeunstyled.anchored)
            api(libs.composeunstyled.breakpoints)
            api(libs.composeunstyled.bottom.sheet)
            api(libs.composeunstyled.build.modifier)
            api(libs.composeunstyled.button)
            api(libs.composeunstyled.checkbox)
            api(libs.composeunstyled.colored.indication)
            api(libs.composeunstyled.dialog)
            api(libs.composeunstyled.disclosure)
            api(libs.composeunstyled.dropdown.menu)
            api(libs.composeunstyled.escape.handler)
            api(libs.composeunstyled.focus.ring)
            api(libs.composeunstyled.icon)
            api(libs.composeunstyled.modal)
            api(libs.composeunstyled.modal.bottom.sheet)
            api(libs.composeunstyled.outline)
            api(libs.composeunstyled.platformtheme)
            api(libs.composeunstyled.portal)
            api(libs.composeunstyled.progress)
            api(libs.composeunstyled.radio.group)
            api(libs.composeunstyled.scrollbars)
            api(libs.composeunstyled.separators)
            api(libs.composeunstyled.slider)
            api(libs.composeunstyled.stack)
            api(libs.composeunstyled.tab.group)
            api(libs.composeunstyled.text.field)
            api(libs.composeunstyled.theming)
            api(libs.composeunstyled.toggle.switch)
            api(libs.composeunstyled.tooltip)
            api(libs.composeunstyled.tri.state.checkbox)
            api(libs.composeunstyled.window.container.size)
            api(libs.composables.ripple)
            api(libs.composables.uri.painter)
            api(libs.icons.lucide.cmp)

            implementation(libs.lazytable)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui.test)
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.okhttp)
        }

        webMain.dependencies {
            implementation(libs.nav3.browser)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

    }

    targets
        .withType<KotlinNativeTarget>()
        .matching { it.konanTarget.family.isAppleFamily }
        .configureEach {
            binaries {
                framework {
                    baseName = "uikit"
                    isStatic = true
                }
            }
        }
}

compose.resources {
    publicResClass = true
    generateResClass = always
    packageOfResClass = "$group.shadcn.generated.resources"
}

dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling)
}

buildConfig {
    // BuildConfig configuration here.
    // https://github.com/gmazzo/gradle-buildconfig-plugin#usage-in-kts
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)

    signAllPublications()

    coordinates(
        groupId = "com.github.jershell",
        artifactId = "shadcn-multiplatform",
        version = version.toString()
    )

    // Mandatory POM metadata (Maven Central requirement)
    pom {
        name.set("shadcn/ui for Compose Multiplatform")
        description.set("shadcn/ui components for Compose Multiplatform built on compose-unstyled primitives, styled from Figma design tokens with light/dark theme support.")
        url.set("https://github.com/jershell/shadcn-multiplatform")

        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
            }
        }
        developers {
            developer {
                id.set("jershell")
                name.set("jershell")
                email.set("jershell@mail.ru")
            }
        }
        scm {
            connection.set("scm:git:https://github.com/jershell/shadcn-multiplatform.git")
            developerConnection.set("scm:git:ssh://github.com/jershell/shadcn-multiplatform.git")
            url.set("https://github.com/jershell/shadcn-multiplatform")
        }
    }
}