package ru.task

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestsLimit: BaseTest() {

    private val localBasePath = "account/list/?"

    @Test
    fun testRandomValidLimit() {
        assertEquals("ok",
                getResponse("${localBasePath}application_id=${key}&search=blo&limit=1")["status"])
    }

    @Test
    fun testRandomNumberMoreThenLimit() {
        assertEquals("ok",
                getResponse("${localBasePath}application_id=${key}&search=blo&limit=1")["status"])
    }

    @Test
    fun testZeroLimit() {
        assertEquals("ok",
                getResponse("${localBasePath}application_id=${key}&search=blo&limit=1")["status"])
    }

    @Test
    fun testNegativeLimit() {
        assertEquals("ok",
                getResponse("${localBasePath}application_id=${key}&search=blo&limit=1")["status"])
    }

    @Test
    fun testStingLimit() {
        assertEquals("ok",
                getResponse("${localBasePath}application_id=${key}&search=blo&limit=1")["status"])
    }


    @Test
    fun testEmptyLimit() {
        assertEquals("ok",
                getResponse("${localBasePath}application_id=${key}&search=blo&limit=1")["status"])
    }
}