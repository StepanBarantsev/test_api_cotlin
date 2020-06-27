package ru.task

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestsSearchType: BaseTest(){

    @Test
    fun testFirst() {
        val request = "account/list/?application_id=${key}&search=blo&limit=1"
        assertEquals("ok", getResponse(request)["status"])
    }
}