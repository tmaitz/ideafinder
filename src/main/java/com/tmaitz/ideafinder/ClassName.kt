package com.tmaitz.ideafinder

fun extractSimpleClassName(classWithPackage: String): String {
    return classWithPackage.substring(classWithPackage.lastIndexOf('.') + 1)
}

data class ClassName(val fullName: String): Comparable<ClassName> {
    val simpleName: String = extractSimpleClassName(fullName)

    override fun compareTo(other: ClassName): Int {
        return simpleName.compareTo(other.simpleName)
    }
}
