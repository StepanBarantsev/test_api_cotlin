package ru.task.tests.Accounts.SearchType

import org.junit.jupiter.api.Test
import ru.task.tests.Accounts.BaseTest

class TestsSearchType{
    private val app = BaseTest()
    private val partOfName = "blo"

    @Test
    fun testInvalidSearchType() {
        val type = "something_else"
        val response = app.accountsHelper.sendAccountsRequest(search=partOfName, searchType=type)

        app.accountsHelper.assertErrorFieldsNotNull(response, response.error)
        app.accountsHelper.assertErrorFields(response, response.error, "type", "INVALID_TYPE", "407", type)

    }

    @Test
    fun testEmptySearchType(){
        val response = app.accountsHelper.sendAccountsRequest(search=partOfName, searchType="")

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=partOfName, expectedNum=100)
    }

    @Test
    fun testWithoutSearchType(){
        val response = app.accountsHelper.sendAccountsRequest(search=partOfName)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=partOfName, expectedNum=100)
    }
}