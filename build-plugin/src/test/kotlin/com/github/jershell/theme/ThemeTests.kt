package com.github.jershell.theme

import com.github.jershell.theme.models.ThemeColor
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlin.test.assertNull
import org.gradle.testfixtures.ProjectBuilder
import kotlinx.serialization.json.*


class ThemeTests {
    @Test
    fun testValueExtract() {
        assertEquals("#ffffff", "#ffffff")
    }

/*    @Test
    fun testThemeColorsGeneration() {
        val project = ProjectBuilder.builder().build()
        val task = project.tasks.create("testTask", ParseColorDesignTokens::class.java)
        
        val colors = mapOf(
            "primary" to ThemeColor("primary", "0xFF0000", "" , false),
            "secondary" to ThemeColor("secondary", "0x00FF00", false)
        )

        val themePalettes = mapOf(
            "light" to mapOf(
                "onContainer" to ThemeColor("primary", "0xFF0000", "" , false),
                "surface" to ThemeColor("secondary", "0x00FF00", "" , false)
            ),
            "dark" to mapOf(
                "onContainer" to ThemeColor("secondary", "0x00FF00", "" , false),
                "surface" to ThemeColor("primary", "0xFF0000", "" , false)
            )
        )

        task.baseColors.putAll(colors)
        task.processedThemeNames.addAll(themePalettes.keys)
        task.processedThemePalette.addAll(themePalettes.values.first().keys)
        task.themeColors.putAll(themePalettes)

        val generatedCode = task.generateCode()

        // Assert the presence of the ThemeColors data class
        assertTrue(generatedCode.contains("data class ThemeColors("))
        assertTrue(generatedCode.contains("val onContainer: Color"))
        assertTrue(generatedCode.contains("val surface: Color"))

        // Assert the presence of the BaseColors object
        assertTrue(generatedCode.contains("object BaseColors {"))
        assertTrue(generatedCode.contains("val primary = Color(0xFF0000)"))
        assertTrue(generatedCode.contains("val secondary = Color(0x00FF00)"))

        // Assert the presence of the theme objects
        assertTrue(generatedCode.contains("val themeLightColors = ThemeColors("))
        assertTrue(generatedCode.contains("val themeDarkColors = ThemeColors("))
    }*/
}

class ThemePathUtilTest {
    @Test
    fun `test extractColorName removes prefix correctly`() {
        val path = "new color styles.theme.light.palette.primary"
        val result = ThemePathUtil.extractColorName(
            path,
            ParseColorDesignTokens.TokenCase.CAMEL_CASE,
            emptyList()
        )
        assertEquals("themeLightPalettePrimary", result)
    }

    @Test
    fun `test extractColorName handles basic colors`() {
        val path = "new color styles.black"
        val result = ThemePathUtil.extractColorName(
            path,
            ParseColorDesignTokens.TokenCase.CAMEL_CASE,
            emptyList()
        )
        assertEquals("black", result)
    }

    @Test
    fun `test extractColorName handles theme colors`() {
        val path = "new color styles.theme.dark.key-colors.primary"
        val result = ThemePathUtil.extractColorName(
            path,
            ParseColorDesignTokens.TokenCase.CAMEL_CASE,
            emptyList()
        )
        assertEquals("themeDarkKeyColorsPrimary", result)
    }

    @Test
    fun `test extractReferencePath handles references correctly`() {
        val value = "{new color styles.theme.light.key-colors.primary}"
        val result = ThemePathUtil.extractReferencePath(value)
        assertEquals("theme.light.key-colors.primary", result)
    }

    @Test
    fun `test extractReferencePath returns null for non-references`() {
        val value = "#FF0000"
        val result = ThemePathUtil.extractReferencePath(value)
        assertNull(result)
    }
}

class ValueExtractorTest {
    @Test
    fun `test extractColor handles hex colors`() {
        val jsonObject = buildJsonObject {
            put("value", "#FF0000")
            put("type", "color")
        }
        val result = ValueExtractor.extractColor(jsonObject)
        assertEquals("#FF0000", result?.value)
    }

    @Test
    fun `test extractColor handles references`() {
        val jsonObject = buildJsonObject {
            put("value", "{new color styles.theme.light.key-colors.primary}")
            put("type", "color")
        }
        val result = ValueExtractor.extractColor(jsonObject)
        assertEquals("{new color styles.theme.light.key-colors.primary}", result?.value)
    }

    @Test
    fun `test extractColor returns null for invalid objects`() {
        val jsonObject = buildJsonObject {
            put("type", "color")
        }
        val result = ValueExtractor.extractColor(jsonObject)
        assertNull(result)
    }
}

class TokenNamingTest {
    @Test
    fun `normalizeJsonPath strips dollar prefix`() {
        assertEquals("tokens.0", TokenNaming.normalizeJsonPath("$.tokens.0"))
    }

    @Test
    fun `pathToPropertyName without dollar prefix`() {
        assertEquals(
            "token0",
            TokenNaming.pathToPropertyName("$.tokens.0", "tokens.", ParseColorDesignTokens.TokenCase.CAMEL_CASE),
        )
    }

    @Test
    fun `semanticPropertyName strips mode prefix`() {
        assertEquals(
            "background",
            TokenNaming.semanticPropertyName(
                "$.mode.light mode.background",
                "mode.light mode",
                ParseColorDesignTokens.TokenCase.CAMEL_CASE,
            ),
        )
    }

    @Test
    fun `comma in key does not collapse with integer key`() {
        val name15 = TokenNaming.pathToPropertyName("tokens.1,5", "tokens.", ParseColorDesignTokens.TokenCase.CAMEL_CASE)
        val nameToken15 = TokenNaming.pathToPropertyName("tokens.15", "tokens.", ParseColorDesignTokens.TokenCase.CAMEL_CASE)
        assertEquals("n1_5", name15)
        assertEquals("token15", nameToken15)
        assertNotEquals(name15, nameToken15)
    }
}

class JsonTokenValueTest {
    @Test
    fun `readFloat handles nested dimension token`() {
        val jsonObject = buildJsonObject {
            put("type", "dimension")
            put("value", 12)
        }
        assertEquals(12f, JsonTokenValue.readFloat(jsonObject))
    }

    @Test
    fun `readFloat handles flat number`() {
        val jsonObject = buildJsonObject {
            put("fontSize", 14)
        }
        assertEquals(14f, JsonTokenValue.readFloat(jsonObject["fontSize"]))
    }
}

class ThemeUtilTest {
    @Test
    fun `test formatPropertyName handles different styles`() {
        val name = "theme-light-palette-primary"
        
        assertEquals(
            "themeLightPalettePrimary",
            ThemeUtil.formatPropertyName(name, ParseColorDesignTokens.TokenCase.CAMEL_CASE)
        )
        
        assertEquals(
            "theme-light-palette-primary",
            ThemeUtil.formatPropertyName(name, ParseColorDesignTokens.TokenCase.SNAKE_CASE)
        )
        
        assertEquals(
            "ThemeLightPalettePrimary",
            ThemeUtil.formatPropertyName(name, ParseColorDesignTokens.TokenCase.PASCAL_CASE)
        )
        
        assertEquals(
            "THEME_LIGHT_PALETTE_PRIMARY",
            ThemeUtil.formatPropertyName(name, ParseColorDesignTokens.TokenCase.SCREAMING_SNAKE_CASE)
        )
    }

    @Test
    fun `test generateColorValue handles different formats`() {
        assertEquals(
            "Color(0xffff0000)",
            ThemeUtil.generateColorValue("#ff0000", ParseColorDesignTokens.TokenCase.CAMEL_CASE, emptyList())
        )
        
        assertEquals(
            "Color(0x80ff0000)",
            ThemeUtil.generateColorValue("#ff000080", ParseColorDesignTokens.TokenCase.CAMEL_CASE, emptyList())
        )
        
        assertEquals(
            "Color(0xffff0000)",
            ThemeUtil.generateColorValue("#f00", ParseColorDesignTokens.TokenCase.CAMEL_CASE, emptyList())
        )
        
        assertEquals(
            "Color(0x80ff0000)",
            ThemeUtil.generateColorValue("#f008", ParseColorDesignTokens.TokenCase.CAMEL_CASE, emptyList())
        )
    }
}