package com.tmaitz.ideafinder

class Finder {

    fun find(classes: List<String>, originalPattern: String): List<String> {
        var pattern = originalPattern
        val resultMap = HashMap<String, String?>()
        if (pattern == pattern.toLowerCase()) {
            pattern = pattern.toUpperCase()
        }

        for (classWithPackage in classes) {
            val className = extractClassName(classWithPackage)
            resultMap[classWithPackage] = className
        }

        var masked = false
        for (ch in pattern) {
            if (ch == '*') {
                // if current char is '*' mean that next chars in classname may be different than the next character pattern
                // set masked flag true and iterate to next char in pattern
                masked = true
                continue
            }
            for (classNameWithPackage in resultMap.keys) {
                val className = resultMap[classNameWithPackage]
                resultMap[classNameWithPackage] = processClassName(ch, className, masked)
            }
            masked = false
        }

        return extractSortedPackages(resultMap)
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
