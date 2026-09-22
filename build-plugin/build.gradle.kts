plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    alias(libs.plugins.kotlinx.serialization)
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
    maven("https://jitpack.io")
}

gradlePlugin {
    plugins {
        create("build-plugin") {
            id = "com.github.jershell.gradle.plugin.build"
            implementationClass = "com.github.jershell.BuildPlugin"
        }
    }
}
dependencies {
    implementation(libs.kotlin.stdlib)
//    implementation(libs.kotlin.android.extensions)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.core)
    implementation(libs.semver)
    implementation("com.github.jershell:rjpath:1.1.1")
    implementation("com.squareup:kotlinpoet:2.2.0")

    // Test dependencies
    testImplementation(gradleTestKit())
    testImplementation(kotlin("test"))
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}

tasks.withType<Test> {
    useJUnitPlatform()
}
