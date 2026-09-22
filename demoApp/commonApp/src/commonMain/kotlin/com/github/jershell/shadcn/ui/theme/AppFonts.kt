package com.github.jershell.shadcn.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.inter_regular
import com.github.jershell.shadcn.demoapp.generated.resources.inter_medium
import com.github.jershell.shadcn.demoapp.generated.resources.inter_semibold
import com.github.jershell.shadcn.demoapp.generated.resources.inter_bold
import com.github.jershell.shadcn.demoapp.generated.resources.inter_thin
import com.github.jershell.shadcn.demoapp.generated.resources.inter_extralight
import com.github.jershell.shadcn.demoapp.generated.resources.inter_light
import com.github.jershell.shadcn.demoapp.generated.resources.inter_extrabold
import com.github.jershell.shadcn.demoapp.generated.resources.inter_black
import com.github.jershell.shadcn.demoapp.generated.resources.notosans_regular
import com.github.jershell.shadcn.demoapp.generated.resources.notosans_medium
import com.github.jershell.shadcn.demoapp.generated.resources.notosans_semibold
import com.github.jershell.shadcn.demoapp.generated.resources.notosans_bold
import com.github.jershell.shadcn.demoapp.generated.resources.notosans_thin
import com.github.jershell.shadcn.demoapp.generated.resources.notosans_extralight
import com.github.jershell.shadcn.demoapp.generated.resources.notosans_light
import com.github.jershell.shadcn.demoapp.generated.resources.notosans_extrabold
import com.github.jershell.shadcn.demoapp.generated.resources.notosans_black
import com.github.jershell.shadcn.demoapp.generated.resources.roboto_regular
import com.github.jershell.shadcn.demoapp.generated.resources.roboto_medium
import com.github.jershell.shadcn.demoapp.generated.resources.roboto_semibold
import com.github.jershell.shadcn.demoapp.generated.resources.roboto_bold
import com.github.jershell.shadcn.demoapp.generated.resources.roboto_thin
import com.github.jershell.shadcn.demoapp.generated.resources.roboto_extralight
import com.github.jershell.shadcn.demoapp.generated.resources.roboto_light
import com.github.jershell.shadcn.demoapp.generated.resources.roboto_extrabold
import com.github.jershell.shadcn.demoapp.generated.resources.roboto_black

/**
 * Font registry of the theme builder (`Heading` / `Font` pickers). Keys are the
 * preset font names; `"System"` is the fallback of the generated theme.
 *
 * Bundled fonts live in `composeResources/font` (sources in `imports/source/fonts`):
 * all nine upright weights W100..W900 that the generated typography uses —
 * otherwise light/extrabold styles would be synthesized from the wrong weight.
 */
@Composable
public fun rememberAppFontFamilies(): Map<String, FontFamily> {
    val inter = FontFamily(
        Font(Res.font.inter_thin, FontWeight.Thin),
        Font(Res.font.inter_extralight, FontWeight.ExtraLight),
        Font(Res.font.inter_light, FontWeight.Light),
        Font(Res.font.inter_regular, FontWeight.Normal),
        Font(Res.font.inter_medium, FontWeight.Medium),
        Font(Res.font.inter_semibold, FontWeight.SemiBold),
        Font(Res.font.inter_bold, FontWeight.Bold),
        Font(Res.font.inter_extrabold, FontWeight.ExtraBold),
        Font(Res.font.inter_black, FontWeight.Black),
    )
    val notoSans = FontFamily(
        Font(Res.font.notosans_thin, FontWeight.Thin),
        Font(Res.font.notosans_extralight, FontWeight.ExtraLight),
        Font(Res.font.notosans_light, FontWeight.Light),
        Font(Res.font.notosans_regular, FontWeight.Normal),
        Font(Res.font.notosans_medium, FontWeight.Medium),
        Font(Res.font.notosans_semibold, FontWeight.SemiBold),
        Font(Res.font.notosans_bold, FontWeight.Bold),
        Font(Res.font.notosans_extrabold, FontWeight.ExtraBold),
        Font(Res.font.notosans_black, FontWeight.Black),
    )
    val roboto = FontFamily(
        Font(Res.font.roboto_thin, FontWeight.Thin),
        Font(Res.font.roboto_extralight, FontWeight.ExtraLight),
        Font(Res.font.roboto_light, FontWeight.Light),
        Font(Res.font.roboto_regular, FontWeight.Normal),
        Font(Res.font.roboto_medium, FontWeight.Medium),
        Font(Res.font.roboto_semibold, FontWeight.SemiBold),
        Font(Res.font.roboto_bold, FontWeight.Bold),
        Font(Res.font.roboto_extrabold, FontWeight.ExtraBold),
        Font(Res.font.roboto_black, FontWeight.Black),
    )
    return mapOf(
        "Inter" to inter,
        "Noto Sans" to notoSans,
        "Roboto" to roboto,
        "System" to FontFamily.Default,
    )
}

/**
 * Resolves the preset font name against [rememberAppFontFamilies]; an unknown or
 * `null` name falls back to `null` (= `FontFamily.Default` of the generated theme).
 */
@Composable
public fun resolveAppFont(name: String?): FontFamily? {
    val families = rememberAppFontFamilies()
    return name?.let { families[it] ?: FontFamily.Default }
}
