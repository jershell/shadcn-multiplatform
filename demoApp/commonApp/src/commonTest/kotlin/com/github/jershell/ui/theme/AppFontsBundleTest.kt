package com.github.jershell.ui.theme

import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.inter_black
import com.github.jershell.shadcn.demoapp.generated.resources.inter_bold
import com.github.jershell.shadcn.demoapp.generated.resources.inter_extrabold
import com.github.jershell.shadcn.demoapp.generated.resources.inter_extralight
import com.github.jershell.shadcn.demoapp.generated.resources.inter_light
import com.github.jershell.shadcn.demoapp.generated.resources.inter_medium
import com.github.jershell.shadcn.demoapp.generated.resources.inter_regular
import com.github.jershell.shadcn.demoapp.generated.resources.inter_semibold
import com.github.jershell.shadcn.demoapp.generated.resources.inter_thin
import com.github.jershell.shadcn.demoapp.generated.resources.notosans_black
import com.github.jershell.shadcn.demoapp.generated.resources.notosans_bold
import com.github.jershell.shadcn.demoapp.generated.resources.notosans_extrabold
import com.github.jershell.shadcn.demoapp.generated.resources.notosans_extralight
import com.github.jershell.shadcn.demoapp.generated.resources.notosans_light
import com.github.jershell.shadcn.demoapp.generated.resources.notosans_medium
import com.github.jershell.shadcn.demoapp.generated.resources.notosans_regular
import com.github.jershell.shadcn.demoapp.generated.resources.notosans_semibold
import com.github.jershell.shadcn.demoapp.generated.resources.notosans_thin
import com.github.jershell.shadcn.demoapp.generated.resources.roboto_black
import com.github.jershell.shadcn.demoapp.generated.resources.roboto_bold
import com.github.jershell.shadcn.demoapp.generated.resources.roboto_extrabold
import com.github.jershell.shadcn.demoapp.generated.resources.roboto_extralight
import com.github.jershell.shadcn.demoapp.generated.resources.roboto_light
import com.github.jershell.shadcn.demoapp.generated.resources.roboto_medium
import com.github.jershell.shadcn.demoapp.generated.resources.roboto_regular
import com.github.jershell.shadcn.demoapp.generated.resources.roboto_semibold
import com.github.jershell.shadcn.demoapp.generated.resources.roboto_thin
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import org.jetbrains.compose.resources.FontResource
import org.jetbrains.compose.resources.getFontResourceBytes
import org.jetbrains.compose.resources.getSystemResourceEnvironment

/**
 * Verifies that every bundled font of the theme builder is really in the
 * compose resources bundle (all nine upright weights per family) and each file
 * is a valid TrueType font (`00 01 00 00` sfnt magic).
 */
class AppFontsBundleTest {

    private val families: Map<String, List<FontResource>> = mapOf(
        "Inter" to listOf(
            Res.font.inter_thin,
            Res.font.inter_extralight,
            Res.font.inter_light,
            Res.font.inter_regular,
            Res.font.inter_medium,
            Res.font.inter_semibold,
            Res.font.inter_bold,
            Res.font.inter_extrabold,
            Res.font.inter_black,
        ),
        "Noto Sans" to listOf(
            Res.font.notosans_thin,
            Res.font.notosans_extralight,
            Res.font.notosans_light,
            Res.font.notosans_regular,
            Res.font.notosans_medium,
            Res.font.notosans_semibold,
            Res.font.notosans_bold,
            Res.font.notosans_extrabold,
            Res.font.notosans_black,
        ),
        "Roboto" to listOf(
            Res.font.roboto_thin,
            Res.font.roboto_extralight,
            Res.font.roboto_light,
            Res.font.roboto_regular,
            Res.font.roboto_medium,
            Res.font.roboto_semibold,
            Res.font.roboto_bold,
            Res.font.roboto_extrabold,
            Res.font.roboto_black,
        ),
    )

    @Test
    fun allFontResourcesAreBundledAsValidTtf() = runTest {
        val environment = getSystemResourceEnvironment()
        families.forEach { (family, resources) ->
            resources.forEach { resource ->
                val bytes = getFontResourceBytes(environment, resource)
                assertTrue(bytes.size > 1_000, "$family/$resource: size=${bytes.size}")
                val sfntMagic = bytes[0] == 0.toByte() && bytes[1] == 1.toByte() &&
                    bytes[2] == 0.toByte() && bytes[3] == 0.toByte()
                val ottoMagic = bytes[0] == 'O'.code.toByte() && bytes[1] == 'T'.code.toByte() &&
                    bytes[2] == 'T'.code.toByte() && bytes[3] == 'O'.code.toByte()
                assertTrue(sfntMagic || ottoMagic, "$family/$resource: not a TTF/OTF font file")
            }
        }
    }
}
