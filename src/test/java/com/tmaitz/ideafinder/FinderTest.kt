package com.tmaitz.ideafinder

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource


internal class FinderTest {

    private var finder: Finder? = null
    private val classes = listOf("a.b.FooBarBaz", "c.d.FooBar")

    @BeforeEach
    fun setUp() {
        finder = Finder()
    }

    @ParameterizedTest
    @ValueSource(strings = ["FB", "FoBa", "FBar", "fb"])
    fun `find both classes with simple search`(pattern: String) {
        val result = finder!!.find(classes, pattern)
        Assertions.assertIterableEquals(listOf("c.d.FooBar", "a.b.FooBarBaz"), result)
    }

    @ParameterizedTest
    @ValueSource(strings = ["BF", "bf"])
    fun `find nothing in case of wrong order`(pattern: String) {
        val result = finder!!.find(classes, pattern)
        Assertions.assertIterableEquals(emptyList<Any>(), result)
    }

    @ParameterizedTest
    @ValueSource(strings = ["fbb"])
    fun `find one className by full camel case pattern`(pattern: String) {
        val result = finder!!.find(classes, pattern)
        Assertions.assertIterableEquals(listOf("a.b.FooBarBaz"), result)
    }

    @ParameterizedTest
    @ValueSource(strings = ["fBb"])
    fun `find nothing in case of mixed case (upper and lower) in pattern`(pattern: String) {
        val result = finder!!.find(classes, pattern)
        Assertions.assertIterableEquals(emptyList<Any>(), result)
    }

    @ParameterizedTest
    @ValueSource(strings = ["FBar "])
    fun `find one className in case of whitespace in pattern end`(pattern: String) {
        val result = finder!!.find(classes, pattern)
        Assertions.assertIterableEquals(listOf("c.d.FooBar"), result)
    }

    @ParameterizedTest
    @ValueSource(strings = ["B*rBaz"])
    fun `find one className by pattern with mask '*'`(pattern: String) {
        val result = finder!!.find(classes, pattern)
        Assertions.assertIterableEquals(listOf("a.b.FooBarBaz"), result)
    }

    @ParameterizedTest
    @ValueSource(strings = ["BrBaz", "BarBz*"])
    fun `find nothing by pattern with mask '*"`(pattern: String) {
        val result = finder!!.find(classes, pattern)
        Assertions.assertIterableEquals(emptyList<Any>(), result)
    }

}