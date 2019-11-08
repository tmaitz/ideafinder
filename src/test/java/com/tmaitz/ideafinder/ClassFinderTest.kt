package com.tmaitz.ideafinder

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.util.function.Supplier


internal class ClassFinderTest {

    private fun assertMatchTrue(finder: ClassFinder, className: String, pattern: String) {
        Assertions.assertTrue(finder.isMatched(className)) { "Pattern '$pattern' doesn't match '$className" }
    }

    private fun assertMatchFalse(finder: ClassFinder, className: String, pattern: String) {
        Assertions.assertFalse(finder.isMatched(className)) { "Pattern '$pattern' shouldn't match '$className" }
    }

    @ParameterizedTest
    @ValueSource(strings = ["FB", "FoBa", "FBar", "fb"])
    fun `find both classes with simple search`(pattern: String) {
        val finder = ClassFinder(pattern)
        assertMatchTrue(finder, "a.b.FooBarBaz", pattern)
        assertMatchTrue(finder, "c.d.FooBar", pattern)
    }

    @ParameterizedTest
    @ValueSource(strings = ["BF", "bf"])
    fun `find nothing in case of wrong order`(pattern: String) {
        val finder = ClassFinder(pattern)
        assertMatchFalse(finder, "a.b.FooBarBaz", pattern)
        assertMatchFalse(finder, "c.d.FooBar", pattern)
    }

    @ParameterizedTest
    @ValueSource(strings = ["fbb"])
    fun `find one className by full camel case pattern`(pattern: String) {
        val finder = ClassFinder(pattern)
        assertMatchTrue(finder, "a.b.FooBarBaz", pattern)
        assertMatchFalse(finder, "c.d.FooBar", pattern)
    }

    @ParameterizedTest
    @ValueSource(strings = ["fBb"])
    fun `find nothing in case of mixed case (upper and lower) in pattern`(pattern: String) {
        val finder = ClassFinder(pattern)
        assertMatchFalse(finder, "a.b.FooBarBaz", pattern)
        assertMatchFalse(finder, "c.d.FooBar", pattern)
    }

    @ParameterizedTest
    @ValueSource(strings = ["FBar "])
    fun `find one className in case of whitespace in pattern end`(pattern: String) {
        val finder = ClassFinder(pattern)
        assertMatchFalse(finder, "a.b.FooBarBaz", pattern)
        assertMatchTrue(finder, "c.d.FooBar", pattern)
    }

    @ParameterizedTest
    @ValueSource(strings = ["B*rBaz", "oB*rBaz", "*B*rBaz"])
    fun `find one className by pattern with mask '*'`(pattern: String) {
        val finder = ClassFinder(pattern)
        assertMatchTrue(finder, "a.b.FooBarBaz", pattern)
        assertMatchFalse(finder, "c.d.FooBar", pattern)
    }

    @ParameterizedTest
    @ValueSource(strings = ["BrBaz", "BarBz*"])
    fun `find nothing by pattern with mask '*'`(pattern: String) {
        val finder = ClassFinder(pattern)
        assertMatchFalse(finder, "a.b.FooBarBaz", pattern)
        assertMatchFalse(finder, "c.d.FooBar", pattern)
    }

    @ParameterizedTest
    @ValueSource(strings = ["HaMa", "HashMap", "HM", "HashM", "*HashM", "*HashM*", "*H*a*s*h*M*a*p*", "anM", "*", "", "* "])
    fun `find className 'HashMapManMen' matched by all patterns`(pattern: String) {
        val finder = ClassFinder(pattern)
        assertMatchTrue(finder, "HashMapManMen", pattern)
    }

}