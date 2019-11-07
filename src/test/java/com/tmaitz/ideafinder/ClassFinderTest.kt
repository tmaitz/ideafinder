package com.tmaitz.ideafinder

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource


internal class ClassFinderTest {

    @ParameterizedTest
    @ValueSource(strings = ["FB", "FoBa", "FBar", "fb"])
    fun `find both classes with simple search`(pattern: String) {
        val finder = ClassFinder(pattern)
        Assertions.assertTrue(finder.isMatched("a.b.FooBarBaz"))
        Assertions.assertTrue(finder.isMatched("c.d.FooBar"))
    }

    @ParameterizedTest
    @ValueSource(strings = ["BF", "bf"])
    fun `find nothing in case of wrong order`(pattern: String) {
        val finder = ClassFinder(pattern)
        Assertions.assertFalse(finder.isMatched("a.b.FooBarBaz"))
        Assertions.assertFalse(finder.isMatched("c.d.FooBar"))
    }

    @ParameterizedTest
    @ValueSource(strings = ["fbb"])
    fun `find one className by full camel case pattern`(pattern: String) {
        val finder = ClassFinder(pattern)
        Assertions.assertTrue(finder.isMatched("a.b.FooBarBaz"))
        Assertions.assertFalse(finder.isMatched("c.d.FooBar"))
    }

    @ParameterizedTest
    @ValueSource(strings = ["fBb"])
    fun `find nothing in case of mixed case (upper and lower) in pattern`(pattern: String) {
        val finder = ClassFinder(pattern)
        Assertions.assertFalse(finder.isMatched("a.b.FooBarBaz"))
        Assertions.assertFalse(finder.isMatched("c.d.FooBar"))
    }

    @ParameterizedTest
    @ValueSource(strings = ["FBar "])
    fun `find one className in case of whitespace in pattern end`(pattern: String) {
        val finder = ClassFinder(pattern)
        Assertions.assertFalse(finder.isMatched("a.b.FooBarBaz"))
        Assertions.assertTrue(finder.isMatched("c.d.FooBar"))
    }

    @ParameterizedTest
    @ValueSource(strings = ["B*rBaz", "oB*rBaz"])
    fun `find one className by pattern with mask '*'`(pattern: String) {
        val finder = ClassFinder(pattern)
        Assertions.assertTrue(finder.isMatched("a.b.FooBarBaz"))
        Assertions.assertFalse(finder.isMatched("c.d.FooBar"))
    }

    @ParameterizedTest
    @ValueSource(strings = ["BrBaz", "BarBz*"])
    fun `find nothing by pattern with mask '*'`(pattern: String) {
        val finder = ClassFinder(pattern)
        Assertions.assertFalse(finder.isMatched("a.b.FooBarBaz"))
        Assertions.assertFalse(finder.isMatched("c.d.FooBar"))
    }

    @ParameterizedTest
    @ValueSource(strings = ["HaMa", "HashMap", "HM", "HashM", "*HashM", "*HashM*", "*H*a*s*h*M*a*p*", "anM", "*", "", "* "])
    fun `find className matched by all patterns`(pattern: String) {
        val className = "HashMapManMen"
        val finder = ClassFinder(pattern)
        Assertions.assertTrue(finder.isMatched(className)) { "Pattern '$pattern' doesn't match '$className'" }

    }

}