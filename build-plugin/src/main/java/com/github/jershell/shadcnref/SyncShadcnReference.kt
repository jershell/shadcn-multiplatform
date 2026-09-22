package com.github.jershell.shadcnref

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Instant

/**
 * Synchronizes the local snapshot of the shadcn/ui reference (imports/shadcn-reference/):
 * component sources (*.tsx) and the token theme (theme-globals.css).
 *
 * Source: the https://github.com/shadcn-ui/ui repository pinned to [REF].
 * The component list is taken from COMPONENTS.md (status sections, except "Out of scope").
 * Files missing from the reference are skipped with a warning.
 */
abstract class SyncShadcnReference : DefaultTask() {

    @get:InputFile
    abstract val componentsFile: RegularFileProperty

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @get:Input
    abstract val repo: Property<String>

    @get:Input
    abstract val ref: Property<String>

    @get:Input
    abstract val style: Property<String>

    init {
        group = "theme"
        repo.convention("shadcn-ui/ui")
        ref.convention(REF)
        style.convention(STYLE)
    }

    @TaskAction
    fun sync() {
        val resolvedRef = ref.get()
        val resolvedStyle = style.get()
        val outDir = outputDirectory.get().asFile

        val componentNames = parseComponentNames(componentsFile.get().asFile)
        logger.lifecycle("shadcn/ui reference sync: repo=${repo.get()} ref=$resolvedRef style=$resolvedStyle")
        logger.lifecycle("Components from COMPONENTS.md: ${componentNames.size}")

        val client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build()

        val synced = mutableListOf<Pair<String, String>>() // local to remote

        val themeTarget = "theme-globals.css"
        val themeRemote = "apps/v4/app/globals.css"
        if (download(client, resolvedRef, themeRemote, outDir.resolve(themeTarget))) {
            synced += themeTarget to themeRemote
            logger.lifecycle("  $themeTarget")
        } else {
            throw GradleException("Failed to download theme css: $themeRemote")
        }

        for (name in componentNames) {
            val fileName = componentFileName(name) ?: continue
            val local = "components/$fileName.tsx"
            val remote = "apps/v4/registry/$resolvedStyle/ui/$fileName.tsx"
            when (download(client, resolvedRef, remote, outDir.resolve(local))) {
                true -> {
                    synced += local to remote
                    logger.lifecycle("  $local")
                }
                false -> logger.warn("  SKIP $name ($remote not found in reference)")
            }
        }

        val manifest = buildJsonObject {
            put("repo", repo.get())
            put("ref", resolvedRef)
            put("style", resolvedStyle)
            put("syncedAt", Instant.now().toString())
            put(
                "files",
                JsonArray(
                    synced.map { (local, remote) ->
                        buildJsonObject {
                            put("local", local)
                            put("remote", remote)
                        }
                    },
                ),
            )
        }
        outDir.resolve("manifest.json").writeText(manifest.toString())
        logger.lifecycle("Synced ${synced.size} files, manifest written to ${outDir.resolve("manifest.json")}")
    }

    private fun download(
        client: HttpClient,
        resolvedRef: String,
        remotePath: String,
        target: File,
    ): Boolean {
        val url = "https://raw.githubusercontent.com/${repo.get()}/$resolvedRef/$remotePath"
        val request = HttpRequest.newBuilder(URI.create(url))
            .header("User-Agent", "shadcn-multiplatform-build")
            .GET()
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofByteArray())
        when (response.statusCode()) {
            200 -> {
                target.parentFile.mkdirs()
                target.writeBytes(response.body())
                return true
            }
            404 -> return false
            else -> throw GradleException("HTTP ${response.statusCode()} for $url")
        }
    }

    private fun parseComponentNames(file: File): List<String> {
        val includedSections = setOf(
            "Done",
            "In progress",
            "Planned",
            "Planned (not a uikit component)",
        )
        val names = mutableListOf<String>()
        var inSection = false
        file.readLines().forEach { line ->
            val trimmed = line.trim()
            if (trimmed.startsWith("## ")) {
                inSection = trimmed.removePrefix("## ").trim() in includedSections
                return@forEach
            }
            if (!inSection || !trimmed.startsWith("|")) return@forEach
            val name = trimmed.split("|").getOrNull(1)?.trim().orEmpty()
            if (name.isEmpty() || name == "Component" || name.contains("---")) return@forEach
            names += name
        }
        return names.distinct()
    }

    private fun componentFileName(raw: String): String? {
        var name = raw.substringBefore(":").trim() // "Dialog: Alert / Confirm" -> "Dialog"
        name = name.replace(Regex("\\(.*?\\)"), "").trim() // "Toast (Sonner)" -> "Toast"
        if (name.isEmpty()) return null
        return name.lowercase().replace(" ", "-")
    }

    companion object {
        /** Pinned version of the shadcn/ui reference: a commit in shadcn-ui/ui. */
        const val REF = "3ba91b1cc83e1bbe4ab35a422ff2a694849c5048" // 2026-09-08
        const val STYLE = "new-york-v4"
    }
}
