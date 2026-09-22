import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.buildConfig)
}

group = "com.github.jershell"
version = libs.versions.libversion.get()

kotlin {
    android {
        namespace = "com.github.jershell.shadcn.demoapp"
        compileSdk = 36
        minSdk = 24
        androidResources.enable = true
        compilerOptions { jvmTarget = JvmTarget.JVM_17 }
    }

    jvm {
        compilerOptions { jvmTarget = JvmTarget.JVM_17 }
    }

    wasmJs { browser() }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            api(project(":uikit"))

            api(libs.compose.runtime)
            api(libs.compose.ui)
            api(libs.compose.foundation)
            api(libs.compose.resources)
            api(libs.compose.ui.tooling.preview)
            api(libs.kotlinx.coroutines.core)
            api(libs.ktor.client.core)
            api(libs.ktor.client.content.negotiation)
            api(libs.ktor.serialization.json)
            api(libs.ktor.client.logging)
            api(libs.androidx.lifecycle.viewmodel)
            api(libs.androidx.lifecycle.runtime)
            api(libs.androidx.lifecycle.viewmodel.navigation3)
            api(libs.compose.nav3)
            api(libs.kotlinx.serialization.json)
            api(libs.coil)
            api(libs.coil.network.ktor)
            api(libs.kotlinx.datetime)
            api(libs.kotlinx.io.core)
            api(libs.koin.compose)
            api(libs.koin.compose.viewmodel)

            implementation(libs.icons.tabler.outline.cmp)
            implementation(libs.icons.tabler.filled.cmp)

            implementation(libs.charts)
            implementation(libs.kflate)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
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
            implementation(libs.slf4j.simple)
        }

        webMain.dependencies {
            implementation(libs.nav3.browser)
            implementation(libs.ktor.client.js)

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
                    baseName = "uikit-test-app"
                    isStatic = true
                }
            }
        }
}

compose {
    resources {
        publicResClass = true
        generateResClass = always
        packageOfResClass = "$group.shadcn.demoapp.generated.resources"
    }
    // kotlinCompilerPlugin.set("org.jetbrains.compose.compiler:compiler:1.5.8")
    // kotlinCompilerPluginArgs.add("suppressKotlinVersionCompatibilityCheck=1.9.23") // add this only if you want to use kotlin 1.8.20 instead of 1.8.21
}

dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling)
}

buildConfig {
    // BuildConfig configuration here.
    // https://github.com/gmazzo/gradle-buildconfig-plugin#usage-in-kts
}
