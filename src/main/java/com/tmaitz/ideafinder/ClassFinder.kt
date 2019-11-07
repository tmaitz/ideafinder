package com.tmaitz.ideafinder

class ClassFinder(originalPattern: String) {

    private val pattern: String

    init {
        if (originalPattern == originalPattern.toLowerCase()) {
            this.pattern = originalPattern.toUpperCase()
        } else {
            this.pattern = originalPattern
        }
    }

    fun match(classNameWithPackage: String): Boolean {
        return match(ClassName(classNameWithPackage))
    }

    fun match(className: ClassName): Boolean {
        var simpleName: String = className.simpleName
        var masked = true
        for (ch in pattern) {
            if (ch == '*') {
                // if current char is '*' mean that next chars in className may be different than the next character pattern
                // set masked flag true and iterate to next char in pattern
                masked = true
                continue
            }
            simpleName = processClassName(ch, simpleName, masked) ?: return false
            masked = false
        }

        return true
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
