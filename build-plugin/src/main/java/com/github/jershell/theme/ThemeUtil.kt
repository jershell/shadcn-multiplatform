package com.github.jershell.theme

import java.util.Locale

/**
 * Utility for working with themes
 */
object ThemeUtil {
    /**
     * Legacy Figma collection name that used to prefix token paths in older
     * design-tokens exports (e.g. `new color styles.theme.light...`).
     */
    const val LEGACY_COLLECTION_PREFIX: String = "new color styles"

    /**
     * Formats a property name according to the given style
     * @param name original name (segments joined with `_`; `-`/space are also
     *   treated as removable word separators)
     * @param style naming style
     * @return formatted name
     */
    fun formatPropertyName(name: String, style: ParseColorDesignTokens.TokenCase): String {
        return when (style) {
            ParseColorDesignTokens.TokenCase.CAMEL_CASE -> camelPascal(name, decapitalizeFirst = true)

            ParseColorDesignTokens.TokenCase.SNAKE_CASE -> name.lowercase()

            ParseColorDesignTokens.TokenCase.PASCAL_CASE -> camelPascal(name, decapitalizeFirst = false)

            ParseColorDesignTokens.TokenCase.SCREAMING_SNAKE_CASE ->
                name.replace("-", "_").replace(" ", "_").uppercase(Locale.ROOT)
        }
    }

    /**
     * camelCase/PascalCase conversion.
     *
     * `_` between two letter-bounded words is a separator and is removed;
     * otherwise it is preserved so that disambiguated segments stay distinct
     * (e.g. `n1_5` from the `1,5` key must not collapse into `n15`).
     * `-` and space inside a segment are always removable word separators.
     */
    private fun camelPascal(name: String, decapitalizeFirst: Boolean): String {
        val segments = name.split("_")
        val out = StringBuilder()
        segments.forEachIndexed { index, segment ->
            if (index > 0) {
                val prevEndsLetter = segments[index - 1].lastOrNull()?.isLetter() == true
                val startsLetter = segment.firstOrNull()?.isLetter() == true
                if (!(prevEndsLetter && startsLetter)) {
                    out.append('_')
                }
            }
            segment.split("-", " ")
                .filter { it.isNotEmpty() }
                .forEachIndexed { subIndex, word ->
                    val isFirstWord = index == 0 && subIndex == 0
                    out.append(
                        when {
                            isFirstWord && decapitalizeFirst -> word
                            else -> word.replaceFirstChar { it.uppercaseChar() }
                        },
                    )
                }
        }
        return out.toString()
    }

    fun generateColorValue(
        value: String,
        case: ParseColorDesignTokens.TokenCase,
        transforms: List<ParseColorDesignTokens.Transform>
    ): String {
        // if the value starts with {, it is a reference to another color
        if (value.startsWith("{")) {
            // jsonPath values can be converted into names, so this expression must be brought to the jsonPath form
            val valueAsJsonPath = "$.${value.replace("{", "").replace("}", "")}"
            return ThemePathUtil.extractColorName(valueAsJsonPath, case, transforms)
        }
        // if the value starts with #, it is a color
        if (!value.startsWith("#")) {
            return value
        }


        // TODO the color length must first be normalized to RRGGBBAA and only then converted
        val colorStr = value.substring(1)

        // TODO the function should be split in two
        // one for RRGGBBAA -> AARRGGBB
        //
        return when (colorStr.length) {
            in setOf(3, 4) -> {
                val expanded = colorStr.map { it.toString().repeat(2) }.joinToString("")
                val color = expanded.substring(0, 6)
                // 4-digit hex: the last nibble is the alpha, right-padded to a byte (#f008 -> 0x80...)
                val alpha = if (colorStr.length == 4) "${colorStr[3]}0" else "ff"
                "Color(0x$alpha$color)"
            }

            in setOf(6, 8) -> {
                val color = colorStr.substring(0, 6)
                val alpha = if (colorStr.length == 8) colorStr.substring(6, 8) else "ff"
                "Color(0x$alpha$color)"
            }

            else -> value
        }
    }
}