package ru.task

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestsLimit: BaseTest() {

    private val localBasePath = "account/list/?"

    @Test
    fun testFirst() {
        assertEquals("ok",
                getResponse("${localBasePath}application_id=${key}&search=blo&limit=1")["status"])
    }
}