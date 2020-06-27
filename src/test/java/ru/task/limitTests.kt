package ru.task

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import ru.task.models.Accounts
import kotlin.random.Random
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

class TestsLimit: BaseTest() {

    private val localBasePath = "account/list/?"

    @Test
    fun testRandomValidLimit() {
        val randNum = Random.nextInt(1,99)
        val response: Accounts = getResponse("${localBasePath}application_id=${key}&search=blo&limit=${randNum}")
        assertEquals("ok", response.status)
        //assertNotNull(response.meta["count"])
        //assert(randNum >= response.meta["count"]!!.toInt())
        //assertNotNull(response.data)
        //assertEquals(response.meta["count"]!!.toInt(), response.data.size)
    }

    /*
    @Test
    @ParameterizedTest
    @ValueSource(ints = [1, 99])
    fun testBorderLimits(num: Int) {
        val response = getResponse("${localBasePath}application_id=${key}&search=blo&limit=${num}")
        assertEquals("ok", response["status"])
        assertNotNull(response["count"])
        assert(randNum >= response["count"]!!.toInt())
    }
    */
    /*
    @Test
    fun testRandomNumberMoreThenLimit() {
        val randNum = Random.nextInt(100,1000)
        assertEquals("ok",
                getResponse("${localBasePath}application_id=${key}&search=blo&limit=${randNum}"))
    }

    */
    /*

    @Test
    fun testZeroLimit() {
        assertEquals("ok",
                getResponse("${localBasePath}application_id=${key}&search=blo&limit=0"))
    }

    */
    /*

    @Test
    fun testNegativeLimit() {
        val randNum = Random.nextInt(-200,-1)
        assertEquals("ok",
                getResponse("${localBasePath}application_id=${key}&search=blo&limit=${randNum}"))
    }

    */
    /*

    @Test
    fun testStingLimit() {
        val str = "fdkdfkdfksdksdksdj"
        assertEquals("ok",
                getResponse("${localBasePath}application_id=${key}&search=blo&limit=${str}"))
    }

    */
    /*

    @Test
    fun testEmptyLimit() {
        assertEquals("ok",
                getResponse("${localBasePath}application_id=${key}&search=blo&limit="))
    }

    */
}