package ru.task

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.kohsuke.rngom.parse.host.Base
import ru.task.models.Accounts
import kotlin.random.Random
import kotlin.test.assertNotNull

class TestsLimit {

    private val app = BaseTest()

    @Test
    fun testRandomValidLimit() {
        val randNum = Random.nextInt(1,app.limitHelper.defaultLimit)
        val response = app.limitHelper.sendLimitRequest(randNum)

        app.limitHelper.assertValidFieldsNotNull(response)

        assertEquals("ok", response.status)
        assertEquals(randNum, response.meta!!["count"]!!.toInt())
        assertEquals(response.meta["count"]!!.toInt(), response.data!!.size)
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 100])
    fun testValidBorderLimits(num: Int) {
        val response = app.limitHelper.sendLimitRequest(num)

        app.limitHelper.assertValidFieldsNotNull(response)

        assertEquals("ok", response.status)
        assertEquals(num, response.meta!!["count"]!!.toInt())
        assertEquals(response.meta["count"]!!.toInt(), response.data!!.size)
    }

    // При вводе любого некорректного числового лимита (больше/меньше) возвращается default
    @ParameterizedTest
    @ValueSource(ints = [0, 101])
    fun testInvalidBorderLimits(num: Int) {
        val response = app.limitHelper.sendLimitRequest(num)

        app.limitHelper.assertValidFieldsNotNull(response)
        app.limitHelper.assertDefaultLimit(response)
    }

    @Test
    fun testRandomNumberMoreThenLimit() {
        val randNum = Random.nextInt(app.limitHelper.defaultLimit + 1,app.limitHelper.defaultLimit * 10)
        val response = app.limitHelper.sendLimitRequest(randNum)

        app.limitHelper.assertValidFieldsNotNull(response)
        app.limitHelper.assertDefaultLimit(response)
    }

    @Test
    fun testWithoutLimit() {
        val response = app.limitHelper.sendRequestWithoutLimit()

        app.limitHelper.assertValidFieldsNotNull(response)
        app.limitHelper.assertDefaultLimit(response)
    }

    @Test
    fun testEmptyLimit() {
        val response = app.limitHelper.sendLimitRequest("")

        app.limitHelper.assertValidFieldsNotNull(response)
        app.limitHelper.assertDefaultLimit(response)
    }

    @Test
    fun testNegativeLimit() {
        val randNum = Random.nextInt(-200,-1)
        val response = app.limitHelper.sendLimitRequest(randNum)

        app.limitHelper.assertValidFieldsNotNull(response)
        app.limitHelper.assertDefaultLimit(response)
    }

    @Test
    fun testStingLimit() {
        val str = "fdkdfkdfksdksdksdj"
        val response: Accounts = app.limitHelper.sendLimitRequest(str)

        assertNotNull(response.status)
        assertNotNull(response.error)
        assertNotNull(response.error["field"])
        assertNotNull(response.error["message"])
        assertNotNull(response.error["code"])
        assertNotNull(response.error["value"])

        assertEquals("error", response.status)
        assertEquals("limit", response.error["field"])
        assertEquals("INVALID_LIMIT", response.error["message"])
        assertEquals("407", response.error["code"])
        assertEquals(str, response.error["value"])
    }

}