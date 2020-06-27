package ru.task

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestsSearchType: BaseTest(){

    @Test
    fun testFirst() {
        assertEquals("ok",
                getResponse("account/list/?application_id=500cc04cab30927d633bc4016b4d5d47&search=blo&limit=1")["status"])
    }
}