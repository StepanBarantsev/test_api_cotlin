package ru.task.tests.Accounts.Limit

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import ru.task.models.Accounts
import ru.task.models.ErrorModel
import ru.task.tests.Accounts.BaseTest
import kotlin.random.Random

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
        app.accountsHelper.assertOkStatus(response)
        app.accountsHelper.assertLimitEqualDataAndMeta(randNum, response)
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 100])
    fun testValidBorderLimits(num: Int) {
        val response = app.accountsHelper.sendAccountsRequest(search=partOfName, limit=num)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertOkStatus(response)
        app.accountsHelper.assertLimitEqualDataAndMeta(num, response)
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

        app.errorHelper.assertErrorFieldsNotNull(response, response.error)
        app.errorHelper.assertErrorFields(response, response.error, ErrorModel("limit", "INVALID_LIMIT", "407", str))

    }



}