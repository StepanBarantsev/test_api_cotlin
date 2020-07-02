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
    @Description("Отправка запроса с type=exact и search, полностью совпадающим с ником игрока")
    fun testUniqueExistingNameFull() {
        val response = app.accountsHelper.sendAccountsRequest(search=uniqueName, searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertExactSearchType(response, expectedNames=arrayOf(uniqueName), expectedNum=1)
    }

    @Test
    @DisplayName("Отправка запроса с type=exact и search, частично совпадающим с ником игрока (ник игрока начинается с переданного параметра)")
    @Description("Отправка запроса с type=exact и search, частично совпадающим с ником игрока (ник игрока начинается с переданного параметра)")
    fun testUniqueExistingNamePart() {
        val partOfUniqueName = uniqueName.substring(0, uniqueName.length - 2)
        val response = app.accountsHelper.sendAccountsRequest(search=partOfUniqueName, searchType=searchType)

        app.accountsHelper.assertOkStatus(response)
        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertEmptyData(response)
    }

    @Test
    @DisplayName("Отправка запроса с type=exact и search, полностью совпадающим с ником игрока, но написанным строчными буквами")
    @Description("Отправка запроса с type=exact и search, полностью совпадающим с ником игрока, но написанным строчными буквами")
    fun testNameWithLowerLetters() {
        val response = app.accountsHelper.sendAccountsRequest(search=uniqueName.toLowerCase(), searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertExactSearchType(response, expectedNames=arrayOf(uniqueName), expectedNum=1)
    }

    @Test
    @DisplayName("Отправка запроса с type=exact и search, полностью совпадающим с ником игрока, но написанным заглавными буквами")
    @Description("Отправка запроса с type=exact и search, полностью совпадающим с ником игрока, но написанным заглавными буквами")
    fun testNameWithUpperLetters() {
        val response = app.accountsHelper.sendAccountsRequest(search=uniqueName.toUpperCase(), searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertExactSearchType(response, expectedNames=arrayOf(uniqueName), expectedNum=1)
    }

    @Test
    @DisplayName("Отправка запроса с type=exact и search с двумя существующими никами")
    @Description("Отправка запроса с type=exact и search с двумя существующими никами")
    fun testTwoExistingNames() {
        val response = app.accountsHelper.sendAccountsRequest(search="$uniqueName,$secondExistingName", searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertExactSearchType(response, expectedNames=arrayOf(uniqueName, secondExistingName), expectedNum=2)
    }

    @Test
    @DisplayName("Отправка запроса с type=exact и search с несуществующим ником")
    @Description("Отправка запроса с type=exact и search с несуществующим ником")
    fun testNotExistingName() {
        val response = app.accountsHelper.sendAccountsRequest(search=notExistingName, searchType=searchType)

        app.accountsHelper.assertOkStatus(response)
        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertEmptyData(response)
    }

    @Test
    @DisplayName("Отправка запроса с type=exact и search с двумя несуществующими никами")
    @Description("Отправка запроса с type=exact и search с двумя несуществующими никами")
    fun testTwoNotExistingNames() {
        val response = app.accountsHelper.sendAccountsRequest(search="$notExistingName,$secondNotExistingName", searchType=searchType)

        app.accountsHelper.assertOkStatus(response)
        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertEmptyData(response)
    }

    @Test
    @DisplayName("Отправка запроса с type=exact и search с одним существующим и одним несуществующим ником")
    @Description("Отправка запроса с type=exact и search с одним существующим и одним несуществующим ником")
    fun testOneExistingAndOneNotExistingNames() {
        val response = app.accountsHelper.sendAccountsRequest(search="$notExistingName,$uniqueName", searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertExactSearchType(response, expectedNames=arrayOf(uniqueName), expectedNum=1)
    }

    @Test
    @DisplayName("Отправка запроса с type=exact и search с двумя совпадающими никами")
    @Description("Отправка запроса с type=exact и search с двумя совпадающими никами")
    fun testRepeatedNames() {
        val response = app.accountsHelper.sendAccountsRequest(search="$uniqueName,$uniqueName", searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertExactSearchType(response, expectedNames=arrayOf(uniqueName), expectedNum=1)
    }
}