package ru.task

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import ru.task.models.Accounts
import kotlin.random.Random
import kotlin.test.assertNotNull

class TestsLimit {

    private val app = BaseTest()

    // Предположим, что по этой части находится 100+ игроков (это действительно так)
    // Проверки будут построены на основе этого предположения
    val partOfName = "blo"

    @Test
    fun testRandomValidLimit() {
        val randNum = Random.nextInt(1,app.accountsHelper.defaultLimit)
        val response = app.accountsHelper.sendAccountsRequest(search=partOfName, limit=randNum)

        app.accountsHelper.assertValidFieldsNotNull(response)

        assertEquals("ok", response.status)
        assertEquals(randNum, response.meta!!["count"]!!.toInt())
        assertEquals(response.meta["count"]!!.toInt(), response.data!!.size)
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 100])
    fun testValidBorderLimits(num: Int) {
        val response = app.accountsHelper.sendAccountsRequest(search=partOfName, limit=num)

        app.accountsHelper.assertValidFieldsNotNull(response)

        assertEquals("ok", response.status)
        assertEquals(num, response.meta!!["count"]!!.toInt())
        assertEquals(response.meta["count"]!!.toInt(), response.data!!.size)
    }

    // При вводе любого некорректного числового лимита (больше/меньше) возвращается default
    @ParameterizedTest
    @ValueSource(ints = [0, 101])
    fun testInvalidBorderLimits(num: Int) {
        val response = app.accountsHelper.sendAccountsRequest(search=partOfName, limit=num)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultLimit(response)
    }

    @Test
    fun testRandomNumberMoreThenLimit() {
        val randNum = Random.nextInt(app.accountsHelper.defaultLimit + 1,app.accountsHelper.defaultLimit * 10)
        val response = app.accountsHelper.sendAccountsRequest(search=partOfName, limit=randNum)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultLimit(response)
    }

    @Test
    fun testWithoutLimit() {
        val response = app.accountsHelper.sendAccountsRequest(search=partOfName)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultLimit(response)
    }

    @Test
    fun testEmptyLimit() {
        val response = app.accountsHelper.sendAccountsRequest(search=partOfName, limit="")

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultLimit(response)
    }

    @Test
    fun testNegativeLimit() {
        val randNum = Random.nextInt(-200,-1)
        val response = app.accountsHelper.sendAccountsRequest(search=partOfName, limit=randNum)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultLimit(response)
    }

    @Test
    fun testStingLimit() {
        val str = "fdkdfkdfksdksdksdj"
        val response: Accounts = app.accountsHelper.sendAccountsRequest(search=partOfName, limit=str)

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