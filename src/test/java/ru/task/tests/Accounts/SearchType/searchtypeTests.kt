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
    @Description("Отправка запроса с непредусмотренным параметром type. Ожидается status==error, error[message]==INVALID_TYPE")
    fun testInvalidSearchType() {
        val type = "something_else"
        val response = app.accountsHelper.sendAccountsRequest(search=partOfName, type=type)

        app.errorHelper.assertErrorFieldsNotNull(response, response.error)
        app.errorHelper.assertErrorFields(response, response.error, ErrorModel("type", "INVALID_TYPE", "407", type))

    }

    @Test
    @DisplayName("Отправка запроса с пустым параметром type")
    @Description("Отправка запроса с пустым параметром type. Ожидается type==startswith")
    fun testEmptySearchType(){
        val response = app.accountsHelper.sendAccountsRequest(search=partOfName, type="")

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=partOfName, expectedNum=100)
    }

    @Test
    @DisplayName("Отправка запроса без параметра type")
    @Description("Отправка запроса без параметра type. Ожидается type==startswith")
    fun testWithoutSearchType(){
        val response = app.accountsHelper.sendAccountsRequest(search=partOfName)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=partOfName, expectedNum=100)
    }
}