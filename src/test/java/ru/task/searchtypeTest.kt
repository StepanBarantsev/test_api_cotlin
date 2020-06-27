package ru.task

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SearchTypeTest {

    @Test
    fun testFirst() {
        assertEquals("a", "a")
        assertEquals(2, 1 + 1, "Optional message")
        assertEquals(2, 1 + 1, { "Assertion message " + "can be lazily evaluated" })
    }
}