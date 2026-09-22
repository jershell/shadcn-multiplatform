package com.github.jershell.theme

import com.github.jershell.theme.models.CollectedToken

object TokenDeduplicator {
    fun deduplicateByObject(tokens: List<CollectedToken>): List<CollectedToken> {
        val result = mutableListOf<CollectedToken>()
        val usedByObject = mutableMapOf<String, MutableSet<String>>()

        tokens.forEach { token ->
            val used = usedByObject.getOrPut(token.objectName) { mutableSetOf() }
            var propertyName = token.propertyName
            if (used.contains(propertyName)) {
                val suffix = disambiguationSuffix(token.path)
                propertyName = "$propertyName$suffix"
                var counter = 2
                while (used.contains(propertyName)) {
                    propertyName = "${token.propertyName}$suffix$counter"
                    counter++
                }
            }
            used.add(propertyName)
            result += if (propertyName == token.propertyName) {
                token
            } else {
                token.copy(propertyName = propertyName)
            }
        }
        return result
    }

    private fun disambiguationSuffix(path: String): String {
        val normalized = TokenNaming.normalizeJsonPath(path)
        val hash = normalized.hashCode().toUInt().toString(16)
        return "Ref$hash"
    }
}
