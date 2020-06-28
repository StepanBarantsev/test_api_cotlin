package ru.task

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestsSearchType{
    private val app = BaseTest()
    private val partOfName = "blo"

    @Test
    fun invalidSearchType() {
        val type = "something_else"
        val response = app.accountsHelper.sendAccountsRequest(search=partOfName, searchType=type)

        app.accountsHelper.assertErrorFieldsNotNull(response, response.error)
        app.accountsHelper.assertErrorFields(response, response.error, "type", "INVALID_TYPE", "407", type)

    }

    @Test
    fun emptySearchType(){
        val response = app.accountsHelper.sendAccountsRequest(search=partOfName, searchType="")

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=partOfName, expectedNum=100)
    }

    @Test
    fun withoutSearchType(){
        val response = app.accountsHelper.sendAccountsRequest(search=partOfName)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=partOfName, expectedNum=100)
    }
}

class TestsSearchTypeStartswith{

    private val app = BaseTest()
    private val minlength = 3
    private val maxlength = 24
    private val existingPartOfNamesMoreThan100 = "blo"
    private val existingPartOfNamesLessThan100 = "blo"
    private val uniqueName = "blo"
    private val notExistingName = "blo"

    @Test
    fun existingNamesMoreThan100() {
        assert(false)
    }

    @Test
    fun existingNamesLessThan100() {
        assert(false)
    }

    @Test
    fun oneExistingName() {
        assert(false)
    }

    @Test
    fun notExistingName() {
        assert(false)
    }

    @Test
    fun nameLessMinLength() {
        assert(false)
    }

    @Test
    fun nameMoreMaxLength() {
        assert(false)
    }

    @Test
    fun nameWithUpperLetters() {
        assert(false)
    }

    @Test
    fun nameWithLowerLetters() {
        assert(false)
    }
}

class TestsSearchTypeExact{

    private val app = BaseTest()

    @Test
    fun testFirst() {
        assert(false)
    }
}