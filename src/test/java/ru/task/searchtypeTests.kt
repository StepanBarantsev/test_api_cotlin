package ru.task

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestsSearchType: BaseTest(){

    @Test
    fun testFirst() {
        assertEquals("aaa", baseUrl)
        assertEquals(2, 1 + 1, "Optional message")
        assertEquals(2, 1 + 1, { "Assertion message " + "can be lazily evaluated" })
    }
}