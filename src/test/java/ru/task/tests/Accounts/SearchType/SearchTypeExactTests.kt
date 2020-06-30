package ru.task.tests.Accounts.SearchType

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import ru.task.Application

@Tag("SearchType")
class TestsSearchTypeExact{

    private val app = Application()
    private val uniqueName = "BloodestLotus"
    private val searchType = "exact"
    private val secondExistingName = "Step08"
    private val notExistingName = "BloodestLotu"
    private val secondNotExistingName = "uuuuuuuuuuuuuuuuuuuuk"

    @Test
    fun testUniqueExistingNameFull() {
        val response = app.accountsHelper.sendAccountsRequest(search=uniqueName, searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertExactSearchType(response, expectedNames=arrayOf(uniqueName), expectedNum=1)
    }

    @Test
    fun testUniqueExistingNamePart() {
        val partOfUniqueName = uniqueName.substring(0, uniqueName.length - 2)
        val response = app.accountsHelper.sendAccountsRequest(search=partOfUniqueName, searchType=searchType)

        app.accountsHelper.assertOkStatus(response)
        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertEmptyData(response)
    }

    @Test
    fun testNameWithLowerLetters() {
        val response = app.accountsHelper.sendAccountsRequest(search=uniqueName.toLowerCase(), searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertExactSearchType(response, expectedNames=arrayOf(uniqueName), expectedNum=1)
    }

    @Test
    fun testNameWithUpperLetters() {
        val response = app.accountsHelper.sendAccountsRequest(search=uniqueName.toUpperCase(), searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertExactSearchType(response, expectedNames=arrayOf(uniqueName), expectedNum=1)
    }

    @Test
    fun testTwoExistingNames() {
        val response = app.accountsHelper.sendAccountsRequest(search="$uniqueName,$secondExistingName", searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertExactSearchType(response, expectedNames=arrayOf(uniqueName, secondExistingName), expectedNum=2)
    }

    @Test
    fun testNotExistingName() {
        val response = app.accountsHelper.sendAccountsRequest(search=notExistingName, searchType=searchType)

        app.accountsHelper.assertOkStatus(response)
        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertEmptyData(response)
    }

    @Test
    fun testTwoNotExistingNames() {
        val response = app.accountsHelper.sendAccountsRequest(search="$notExistingName,$secondNotExistingName", searchType=searchType)

        app.accountsHelper.assertOkStatus(response)
        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertEmptyData(response)
    }

    @Test
    fun testOneExistingAndOneNotExistingNames() {
        val response = app.accountsHelper.sendAccountsRequest(search="$notExistingName,$uniqueName", searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertExactSearchType(response, expectedNames=arrayOf(uniqueName), expectedNum=1)
    }

    @Test
    fun testRepeatedNames() {
        val response = app.accountsHelper.sendAccountsRequest(search="$uniqueName,$uniqueName", searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertExactSearchType(response, expectedNames=arrayOf(uniqueName), expectedNum=1)
    }
}