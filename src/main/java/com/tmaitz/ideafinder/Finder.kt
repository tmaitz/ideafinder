package com.tmaitz.ideafinder

class Finder(originalPattern: String) {

    private val pattern: String

    init {
        if (originalPattern == originalPattern.toLowerCase()) {
            this.pattern = originalPattern.toUpperCase()
        } else {
            this.pattern = originalPattern
        }
    }

    fun match(classNameWithPackage: String): Boolean {
        var className: String = extractClassName(classNameWithPackage);
        var masked = false
        for (ch in pattern) {
            if (ch == '*') {
                // if current char is '*' mean that next chars in classname may be different than the next character pattern
                // set masked flag true and iterate to next char in pattern
                masked = true
                continue
            }
            className = processClassName(ch, className, masked) ?: return false
            masked = false
        }

        return true
    }

    private fun extractSortedPackages(resultMap: Map<String, String?>): List<String> {
        return resultMap.entries
                .filter { entry -> entry.value != null }
                .sortedWith(Comparator { entry1, entry2 ->
                    val cn1 = extractClassName(entry1.key)
                    val cn2 = extractClassName(entry2.key)
                    cn1.compareTo(cn2, ignoreCase = true)
                })
                .map { it.key }
                .toList()
    }

    private fun extractClassName(classWithPackage: String): String {
        return classWithPackage.substring(classWithPackage.lastIndexOf('.') + 1)
    }

    private fun processClassName(ch: Char, className: String?, masked: Boolean): String? {
        // not matched -> skip
        if (className == null) return null
        if (ch == ' ') {
            // in case of whitespace -> skip if word not finished
            // otherwise -> save this word
            return if (className.isEmpty()) className else null
        }
        for (j in className.indices) {
            if (className[j] == ch) {
                return className.substring(j + 1)
            }
            if (Character.isUpperCase(ch) || masked) continue
            return null
        }
        return null
    }

}
