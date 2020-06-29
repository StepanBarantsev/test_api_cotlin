package ru.task

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

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

class TestsSearchTypeStartswith{

    private val app = BaseTest()
    private val searchType = "startswith"
    private val minlength = 3
    private val maxlength = 24
    private val existingPartOfNamesMoreThan100 = "blo"
    private val existingPartOfNamesLessThan100 = "bloodes"
    private val uniqueName = "BloodestLotus"
    private val notExistingName = "BloodestLotusORB1313"
    private val nameOneSymbol = "u"
    private val name25Symbols = "uuuuuuuuuuuuuuuuuuuukuuuk"
    private val name24Symbols = "uuuuuuuuuuuuuuuuuuuuuuuu"

    @Test
    fun testExistingNamesMoreThan100() {
        val response = app.accountsHelper.sendAccountsRequest(search=existingPartOfNamesMoreThan100, searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=existingPartOfNamesMoreThan100, expectedNum=100)
    }

    @Test
    fun testExistingNamesLessThan100() {
        val response = app.accountsHelper.sendAccountsRequest(search=existingPartOfNamesLessThan100, searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertOkStatus(response)

        assert(100 > response.meta!!["count"]!!.toInt())
        assert(100 > response.data!!.size)
        app.accountsHelper.assertAllNicknamesStartsWith(response, existingPartOfNamesLessThan100)
    }

    @Test
    fun testUniqueExistingNameFull() {
        val response = app.accountsHelper.sendAccountsRequest(search=uniqueName, searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=uniqueName, expectedNum=1)
    }

    @Test
    fun testUniqueExistingNamePart() {
        val partOfUniqueName = uniqueName.substring(0, uniqueName.length - 2)
        val response = app.accountsHelper.sendAccountsRequest(search=partOfUniqueName, searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=partOfUniqueName, expectedNum=1)
    }

    @Test
    fun testNameEndwith() {
        val partOfUniqueName = uniqueName.substring(1, uniqueName.length-1)
        val response = app.accountsHelper.sendAccountsRequest(search=partOfUniqueName, searchType=searchType)

        app.accountsHelper.assertOkStatus(response)
        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertEmptyData(response)
    }

    @Test
    fun testNotExistingName() {
        val response = app.accountsHelper.sendAccountsRequest(search=notExistingName, searchType=searchType)

        app.accountsHelper.assertOkStatus(response)
        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertEmptyData(response)
    }

    @Test
    fun testNameLessMinLength() {
        val response = app.accountsHelper.sendAccountsRequest(search=nameOneSymbol, searchType=searchType)

        app.accountsHelper.assertErrorFieldsNotNull(response, response.error)
        app.accountsHelper.assertErrorFields(response, response.error, "search", "NOT_ENOUGH_SEARCH_LENGTH", "407", nameOneSymbol)
    }

    @Test
    fun testNameMoreMaxLength() {
        val response = app.accountsHelper.sendAccountsRequest(search=name25Symbols, searchType=searchType)

        app.accountsHelper.assertErrorFieldsNotNull(response, response.error)
        app.accountsHelper.assertErrorFields(response, response.error, "search", "INVALID_SEARCH", "407", name25Symbols)
    }

    // Это гарантированно уникальное имя
    @Test
    fun testName24Symbols() {
        val response = app.accountsHelper.sendAccountsRequest(search=name24Symbols, searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=name24Symbols, expectedNum=1)
    }

    @Test
    fun testNameWithUpperLetters() {
        val response = app.accountsHelper.sendAccountsRequest(search=uniqueName.toLowerCase(), searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=uniqueName, expectedNum=1)
    }

    @Test
    fun testNameWithLowerLetters() {
        val response = app.accountsHelper.sendAccountsRequest(search=uniqueName.toUpperCase(), searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=uniqueName, expectedNum=1)
    }
}

class TestsSearchTypeExact{

    private val app = BaseTest()
    private val uniqueName = "BloodestLotus"
    private val searchType = "exact"
    private val secondExistingName = "Step08"
    private val notExistingName = "BloodestLotu"
    private val secondNotExistingName = "uuuuuuuuuuuuuuuuuuuuk"

    @Test
    fun uniqueExistingNameFull() {
        val response = app.accountsHelper.sendAccountsRequest(search=uniqueName, searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertExactSearchType(response, expectedNames=arrayOf(uniqueName), expectedNum=1)
    }

    @Test
    fun uniqueExistingNamePart() {
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