package com.example.test.utils

import org.junit.Assert

/**
 * receiver 는 expected 인가?
 *   - ex) `stringValue shouldBe "asdf"`
 */
infix fun <T : Any> T?.shouldBe(expected: T?) = this.apply { Assert.assertEquals(expected, this) }