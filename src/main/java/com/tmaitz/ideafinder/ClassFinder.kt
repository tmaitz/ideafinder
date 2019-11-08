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

    fun isMatched(classNameWithPackage: String): Boolean {
        return isMatched(ClassName(classNameWithPackage))
    }

    fun isMatched(className: ClassName): Boolean {
        return isMatchedByPattern(0, className.simpleName, true)
    }

    /**
     * Main idea - starting from the 0 index of the pattern,
     * recursively bypass the incoming analyzedString (char by char) and shorten it
     * if the matching conditions are met according to the current analyzed pattern's position
     */
    private fun isMatchedByPattern(patternCursorPosition: Int, analyzedString: String, wildcardPrevious: Boolean): Boolean {
        // pattern finished => pattern matched
        if (patternCursorPosition >= pattern.length) return true
        val patternSymbol = pattern[patternCursorPosition]
        if (patternSymbol == ' ') {
            return analyzedString.isEmpty()
        }
        val isWildcardSymbol = patternSymbol == '*'
        val isCamelCaseStart = patternSymbol.isUpperCase()
        for (i in analyzedString.indices) {
            if (patternSymbol == analyzedString[i] || isWildcardSymbol) {
                // if current pattern symbol is wildcard => we should check same string with next pattern character
                if (isWildcardSymbol && i == 0) {
                    if (isMatchedByPattern(patternCursorPosition + 1, analyzedString, isWildcardSymbol)) {
                        return true
                    }
                }
                if (isMatchedByPattern(patternCursorPosition + 1, analyzedString.substring(i + 1), isWildcardSymbol)) {
                    return true
                }
            }
            // if previous pattern symbol is wildcard or current pattern symbol is upper case => check next string characters
            if (!wildcardPrevious && !isCamelCaseStart) {
                break
            }
        }
        // incoming string finished => pattern not matched
        return false
    }

}
