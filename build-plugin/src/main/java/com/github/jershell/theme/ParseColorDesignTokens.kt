package com.github.jershell.theme

import com.github.jershell.util.JSON
import kotlinx.serialization.json.JsonElement
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

abstract class ParseColorDesignTokens @Inject constructor(
    private val objects: ObjectFactory,
) : DefaultTask() {

    init {
        group = "theme"
    }

    @get:InputFiles
    abstract val inputFiles: ConfigurableFileCollection

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @get:Input
    abstract val packageName: Property<String>

    @get:Input
    val varNamesCase = objects.property(ParseColorDesignTokens.TokenCase::class.java)

    @get:Input
    val varTransforms = objects.listProperty(Transform::class.java)

    enum class TokenCase {
        CAMEL_CASE,
        PASCAL_CASE,
        SNAKE_CASE,
        SCREAMING_SNAKE_CASE,
    }

    sealed interface Transform : java.io.Serializable {
        data class Replace(
            val oldValue: String,
            val newValue: String,
            val ignoreCase: Boolean = false,
        ) : Transform
    }

    @TaskAction
    fun task() {
        logger.lifecycle("Starting design tokens import...")

        if (inputFiles.isEmpty) {
            logger.error("No input files specified!")
            return
        }

        val case = varNamesCase.orNull ?: TokenCase.CAMEL_CASE
        val collector = TokenCollector(logger)

        val primitiveLayers = listOf(
            PrimitiveLayerConfig("BaseTokens", "$.tokens..[?(@.type)]", "tokens.", KotlinTokenType.DIMENSION),
            PrimitiveLayerConfig("TwColors", "$.tw.colors..[?(@.type == 'color')]", "tw.colors.", KotlinTokenType.COLOR),
            PrimitiveLayerConfig("TwDimensions", "$.tw..[?(@.type == 'dimension')]", "tw.", KotlinTokenType.DIMENSION),
            PrimitiveLayerConfig("RdxColors", "$.rdx.colors..[?(@.type == 'color')]", "rdx.colors.", KotlinTokenType.COLOR),
            PrimitiveLayerConfig("FontStyles", "$.font..[?(@.type == 'custom-fontStyle')]", "font.", KotlinTokenType.TEXT_STYLE),
            PrimitiveLayerConfig("Effects", "$.effect..[?(@.type == 'custom-shadow')]", "effect.", KotlinTokenType.SHADOW),
        )

        val lightMode = ThemeModeConfig(
            modeName = "light",
            modePathPrefix = "mode.light mode",
            selector = "$.mode.light mode.[?(@.type)]",
        )
        val darkMode = ThemeModeConfig(
            modeName = "dark",
            modePathPrefix = "mode.dark mode",
            selector = "$.mode.dark mode.[?(@.type)]",
        )

        inputFiles.forEach { inputFile ->
            if (!inputFile.exists()) {
                logger.warn("Input file not found: ${inputFile.absolutePath}")
                return@forEach
            }

            logger.lifecycle("Processing: ${inputFile.absolutePath}")
            val json = JSON.decodeFromString<JsonElement>(inputFile.readText())

            val primitiveTokens = TokenDeduplicator.deduplicateByObject(
                collector.collectPrimitives(json, primitiveLayers, case),
            )
            val lightSemantic = collector.collectThemeMode(json, lightMode, case)
            val darkSemantic = collector.collectThemeMode(json, darkMode, case)
            val typographyTokens = TokenDeduplicator.deduplicateByObject(
                collector.collectTypographyStyles(json, "$.typography", "TypographyStyles", case),
            )

            logger.lifecycle(
                "Collected: primitives=${primitiveTokens.size}, " +
                    "light=${lightSemantic.size}, dark=${darkSemantic.size}, " +
                    "typography=${typographyTokens.size}",
            )

            val generator = ThemeCodeGenerator(
                packageName = packageName.get(),
                case = case,
            )
            generator.generate(
                outputDirectory = outputDirectory.get().asFile,
                primitiveTokens = primitiveTokens,
                lightSemantic = lightSemantic,
                darkSemantic = darkSemantic,
                typographyTokens = typographyTokens,
            )
        }

        logger.lifecycle("Theme generated in: ${outputDirectory.get().asFile.absolutePath}")
    }
}
