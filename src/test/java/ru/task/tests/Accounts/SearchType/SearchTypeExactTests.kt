package ru.task.tests.Accounts.SearchType

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import ru.task.Application
import io.qameta.allure.Description
import org.junit.jupiter.api.DisplayName


@Tag("SearchType")
@DisplayName("Проверка параметра type=exact")
class TestsSearchTypeExact{

    private val app = Application()
    private val uniqueName = "BloodestLotus"
    private val searchType = "exact"
    private val secondExistingName = "Step08"
    private val notExistingName = "BloodestLotu"
    private val secondNotExistingName = "uuuuuuuuuuuuuuuuuuuuk"

    @Test
    @DisplayName("Отправка запроса с type=exact и search, полностью совпадающим с ником игрока")
    @Description("Отправка запроса с type=exact и search, полностью совпадающим с ником игрока. Ожидается 1 найденный игрок")
    fun testUniqueExistingNameFull() {
        val response = app.accountsHelper.sendAccountsRequest(search=uniqueName, type=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertExactSearchType(response, expectedNames=arrayOf(uniqueName), expectedNum=1)
    }

    @Test
    @DisplayName("Отправка запроса с type=exact и search, частично совпадающим с ником игрока (ник игрока начинается с переданного параметра)")
    @Description("Отправка запроса с type=exact и search, частично совпадающим с ником игрока (ник игрока начинается с переданного параметра). Ожидается 0 найденных игроков")
    fun testUniqueExistingNamePart() {
        val partOfUniqueName = uniqueName.substring(0, uniqueName.length - 2)
        val response = app.accountsHelper.sendAccountsRequest(search=partOfUniqueName, type=searchType)

        app.accountsHelper.assertOkStatus(response)
        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertEmptyData(response)
    }

    @Test
    @DisplayName("Отправка запроса с type=exact и search, полностью совпадающим с ником игрока, но написанным строчными буквами")
    @Description("Отправка запроса с type=exact и search, полностью совпадающим с ником игрока, но написанным строчными буквами. Ожидается 1 найденный игрок")
    fun testNameWithLowerLetters() {
        val response = app.accountsHelper.sendAccountsRequest(search=uniqueName.toLowerCase(), type=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertExactSearchType(response, expectedNames=arrayOf(uniqueName), expectedNum=1)
    }

    @Test
    @DisplayName("Отправка запроса с type=exact и search, полностью совпадающим с ником игрока, но написанным заглавными буквами")
    @Description("Отправка запроса с type=exact и search, полностью совпадающим с ником игрока, но написанным заглавными буквами. Ожидается 1 найденный игрок")
    fun testNameWithUpperLetters() {
        val response = app.accountsHelper.sendAccountsRequest(search=uniqueName.toUpperCase(), type=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertExactSearchType(response, expectedNames=arrayOf(uniqueName), expectedNum=1)
    }

    @Test
    @DisplayName("Отправка запроса с type=exact и search с двумя существующими никами")
    @Description("Отправка запроса с type=exact и search с двумя существующими никами. Ожидается 2 найденных игрока")
    fun testTwoExistingNames() {
        val response = app.accountsHelper.sendAccountsRequest(search="$uniqueName,$secondExistingName", type=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertExactSearchType(response, expectedNames=arrayOf(uniqueName, secondExistingName), expectedNum=2)
    }

    @Test
    @DisplayName("Отправка запроса с type=exact и search с несуществующим ником")
    @Description("Отправка запроса с type=exact и search с несуществующим ником. Ожидается 0 найденных игроков")
    fun testNotExistingName() {
        val response = app.accountsHelper.sendAccountsRequest(search=notExistingName, type=searchType)

        app.accountsHelper.assertOkStatus(response)
        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertEmptyData(response)
    }

    @Test
    @DisplayName("Отправка запроса с type=exact и search с двумя несуществующими никами")
    @Description("Отправка запроса с type=exact и search с двумя несуществующими никами. Ожидается 0 найденных игроков")
    fun testTwoNotExistingNames() {
        val response = app.accountsHelper.sendAccountsRequest(search="$notExistingName,$secondNotExistingName", type=searchType)

        app.accountsHelper.assertOkStatus(response)
        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertEmptyData(response)
    }

    @Test
    @DisplayName("Отправка запроса с type=exact и search с одним существующим и одним несуществующим ником")
    @Description("Отправка запроса с type=exact и search с одним существующим и одним несуществующим ником. Ожидается 1 найденный игрок")
    fun testOneExistingAndOneNotExistingNames() {
        val response = app.accountsHelper.sendAccountsRequest(search="$notExistingName,$uniqueName", type=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertExactSearchType(response, expectedNames=arrayOf(uniqueName), expectedNum=1)
    }

    @Test
    @DisplayName("Отправка запроса с type=exact и search с двумя совпадающими никами")
    @Description("Отправка запроса с type=exact и search с двумя совпадающими никами. Ожидается 1 найденный игрок")
    fun testRepeatedNames() {
        val response = app.accountsHelper.sendAccountsRequest(search="$uniqueName,$uniqueName", type=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertExactSearchType(response, expectedNames=arrayOf(uniqueName), expectedNum=1)
    }
}