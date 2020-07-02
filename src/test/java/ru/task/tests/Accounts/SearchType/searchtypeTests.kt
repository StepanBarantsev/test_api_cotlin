package ru.task.tests.Accounts.SearchType

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import ru.task.models.ErrorModel
import ru.task.Application
import io.qameta.allure.Description
import org.junit.jupiter.api.DisplayName


@Tag("SearchType")
@DisplayName("Проверка параметра type")
class TestsSearchType{
    private val app = Application()
    private val partOfName = "blo"

    @Test
    @DisplayName("Отправка запроса с непредусмотренным параметром type")
    @Description("Отправка запроса с непредусмотренным параметром type")
    fun testInvalidSearchType() {
        val type = "something_else"
        val response = app.accountsHelper.sendAccountsRequest(search=partOfName, searchType=type)

        app.errorHelper.assertErrorFieldsNotNull(response, response.error)
        app.errorHelper.assertErrorFields(response, response.error, ErrorModel("type", "INVALID_TYPE", "407", type))

    }

    @Test
    @DisplayName("Отправка запроса с пустым параметром type")
    @Description("Отправка запроса с пустым параметром type")
    fun testEmptySearchType(){
        val response = app.accountsHelper.sendAccountsRequest(search=partOfName, searchType="")

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=partOfName, expectedNum=100)
    }

    @Test
    @DisplayName("Отправка запроса без параметра type")
    @Description("Отправка запроса без параметра type")
    fun testWithoutSearchType(){
        val response = app.accountsHelper.sendAccountsRequest(search=partOfName)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=partOfName, expectedNum=100)
    }
}