package com.tmaitz.ideafinder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


class FinderTest {

    private Finder finder;
    private static List<String> classes = Arrays.asList("a.b.FooBarBaz", "c.d.FooBar");

    @BeforeEach
    void setUp() {
        finder = new Finder();
    }

    @ParameterizedTest
    @MethodSource("findAll_source")
    void findAll(List<String> classes, String pattern, List<String> expected) {
        final List<String> result = finder.find(classes, pattern);
        Assertions.assertIterableEquals(expected, result);
    }

    private static Stream<Arguments> findAll_source() {
        var classes = Arrays.asList("a.b.FooBarBaz", "c.d.FooBar");
        var expected = Arrays.asList("c.d.FooBar", "a.b.FooBarBaz");
        return Stream.of(
                Arguments.of(classes, "FB", expected),
                Arguments.of(classes, "FoBa", expected),
                Arguments.of(classes, "FBar", expected),
                Arguments.of(classes, "fb", expected)
        );
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "BF",
            "bf",
    })
    void wrong_order(String pattern) {
        final List<String> result = finder.find(classes, pattern);
        Assertions.assertIterableEquals(Collections.emptyList(), result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "fbb",
    })
    void lowercase_sequence(String pattern) {
        final List<String> result = finder.find(classes, pattern);
        Assertions.assertIterableEquals(Collections.singletonList("a.b.FooBarBaz"), result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "fBb",
    })
    void mixed_sequence(String pattern) {
        final List<String> result = finder.find(classes, pattern);
        Assertions.assertIterableEquals(Collections.emptyList(), result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "FBar ",
    })
    void with_whitespace(String pattern) {
        final List<String> result = finder.find(classes, pattern);
        Assertions.assertIterableEquals(Collections.singletonList("c.d.FooBar"), result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "B*rBaz",
    })
    void masked_pattern(String pattern) {
        final List<String> result = finder.find(classes, pattern);
        Assertions.assertIterableEquals(Collections.singletonList("a.b.FooBarBaz"), result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "BrBaz",
            "BarBz*",
    })
    void masked_pattern1(String pattern) {
        final List<String> result = finder.find(classes, pattern);
        Assertions.assertIterableEquals(Collections.emptyList(), result);
    }

}