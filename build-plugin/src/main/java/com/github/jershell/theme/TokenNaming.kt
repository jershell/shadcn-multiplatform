package com.github.jershell.theme

object TokenNaming {
    /**
     * RJPath returns paths prefixed with `$` — strip the prefix to match reference tokens.
     */
    fun normalizeJsonPath(path: String): String {
        return path.trim().removePrefix("$").removePrefix(".").trim()
    }

    fun pathToPropertyName(
        path: String,
        pathPrefixToStrip: String,
        case: ParseColorDesignTokens.TokenCase,
    ): String {
        var stripped = normalizeJsonPath(path)
        if (pathPrefixToStrip.isNotEmpty()) {
            val prefix = normalizeJsonPath(pathPrefixToStrip).removeSuffix(".")
            when {
                stripped == prefix -> stripped = ""
                stripped.startsWith("$prefix.") -> stripped = stripped.removePrefix(prefix).removePrefix(".")
            }
        }
        return finalizePropertyName(segmentsToPropertyName(stripped, case))
    }

    fun semanticPropertyName(
        path: String,
        modePathPrefix: String,
        case: ParseColorDesignTokens.TokenCase,
    ): String {
        val normalized = normalizeJsonPath(path)
        val modePrefix = normalizeJsonPath(modePathPrefix)
        var stripped = normalized
        when {
            normalized == modePrefix -> stripped = ""
            normalized.startsWith("$modePrefix.") -> stripped = normalized.removePrefix(modePrefix).removePrefix(".")
        }
        return finalizePropertyName(segmentsToPropertyName(stripped, case))
    }

    fun referencePathToTokenPath(reference: String): String {
        return reference.removePrefix("{").removeSuffix("}").trim()
    }

    private fun segmentsToPropertyName(path: String, case: ParseColorDesignTokens.TokenCase): String {
        val normalized = path
            .replace("-", ".")
            .replace(" ", ".")
            .split(".")
            .filter { it.isNotEmpty() }
            .joinToString("_") { sanitizeSegment(it) }

        if (normalized.isEmpty()) return ""
        return ThemeUtil.formatPropertyName(normalized, case)
    }

    private fun sanitizeSegment(segment: String): String {
        var cleaned = segment.trim()
            .replace(",", "_")
            .replace(" ", "_")

        if (cleaned.isEmpty()) return "empty"

        var negative = false
        if (cleaned.startsWith("-")) {
            negative = true
            cleaned = cleaned.substring(1)
        }

        cleaned = cleaned.replace("-", "_")

        if (cleaned.isEmpty()) return "negEmpty"

        val body = when {
            cleaned.all { it.isDigit() } -> "token$cleaned"
            cleaned.first().isDigit() -> "n$cleaned"
            else -> cleaned
        }

        return if (negative) "neg$body" else body
    }

  /**
   * Kotlin identifier: must not start with a digit; only letters/digits/_ are allowed.
   */
    private fun finalizePropertyName(name: String): String {
        if (name.isEmpty()) return "empty"
        val sanitized = name.filter { it.isLetterOrDigit() || it == '_' }
        if (sanitized.isEmpty()) return "empty"
        if (!sanitized.first().isLetter() && sanitized.first() != '_') {
            return "token$sanitized"
        }
        return sanitized
    }
}
