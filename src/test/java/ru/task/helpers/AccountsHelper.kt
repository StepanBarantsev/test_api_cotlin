package ru.task.helpers

import org.junit.jupiter.api.Assertions
import ru.task.BaseTest
import ru.task.models.Accounts
import kotlin.test.assertNotNull
import kotlin.test.assertEquals

class AccountsHelper(val app: BaseTest) {

    val localBasePath = "account/list/?"
    val defaultLimit = 100

    fun assertValidFieldsNotNull(response: Accounts) {
        assertNotNull(response.status)
        assertNotNull(response.meta)
        assertNotNull(response.meta["count"])
        assertNotNull(response.data)
    }

    fun assertDefaultLimit(response: Accounts) {
        assertOkStatus(response)
        assertEquals(defaultLimit, response.meta!!["count"]!!.toInt())
        assertEquals(defaultLimit, response.data!!.size)
    }

    fun assertDefaultSearchType(response: Accounts, expectedNum: Int, expectedSearch: String){
        assertOkStatus(response)
        // Приходит много записей, хотя было введено одно имя
        assertEquals(expectedNum, response.meta!!["count"]!!.toInt())
        assertEquals(expectedNum, response.data!!.size)

        assertAllNicknamesStartsWith(response, expectedSearch)
    }

    fun assertEmptyData(response: Accounts){
        assertEquals(0, response.meta!!["count"]!!.toInt())
        assertEquals(0, response.data!!.size)
    }

    fun assertOkStatus(response: Accounts) {
        assertEquals("ok", response.status)
    }

    fun getAllNicknames(response: Accounts) : Array<String>{
        val arr = response.data
        val result = Array<String>(arr!!.size){"empty"}
        for (index in arr.indices){
            result[index] = arr[index]["nickname"]!!
        }
        return result
    }

    fun assertAllNicknamesStartsWith(nicknames: Array<String>, namePart: String){
        var flag = true
        for (i in nicknames){
            if (!(i.startsWith(namePart, ignoreCase=true))) flag = false
        }
        assert(flag)
    }

    fun assertAllNicknamesStartsWith(response: Accounts, namePart: String){
        val nicknames = getAllNicknames(response)
        assertAllNicknamesStartsWith(nicknames, namePart)
    }


    fun sendAccountsRequest(search: String?=null, limit: String?=null, searchType: String?=null): Accounts
    {
        var request = "${app.accountsHelper.localBasePath}application_id=${app.key}"
        request = addSearchToRequest(request, search)
        request = addLimitToRequest(request, limit)
        request = addSearchTypeToRequest(request, searchType)
        return app.getResponse(request)
    }

    fun sendAccountsRequest(search: String?=null, limit: Int, searchType: String?=null): Accounts{
        return sendAccountsRequest(search, limit.toString(), searchType)
    }

    fun assertErrorFieldsNotNull(response: Accounts, error: Map<String, String>?) {
        assertNotNull(response.status)
        assertNotNull(response.error)
        assertNotNull(error!!["field"])
        assertNotNull(error["message"])
        assertNotNull(error["code"])
        assertNotNull(error["value"])
    }

    fun assertErrorFields(response: Accounts,
                          error: Map<String, String>?,
                          field: String,
                          message: String,
                          code: String,
                          value: String) {
        assertEquals("error", response.status)
        assertEquals(field, error!!["field"])
        assertEquals(message, error["message"])
        assertEquals(code, error["code"])
        assertEquals(value, error["value"])
    }

    private fun addLimitToRequest(request: String, limit: String?): String{
        return if (limit != null) request + "&limit=${limit}" else request
    }

    private fun addSearchToRequest(request: String, search: String?): String{
        return if (search != null) request + "&search=${search}" else request
    }

    private fun addSearchTypeToRequest(request: String, searchType: String?): String{
        return if (searchType != null) request + "&type=${searchType}" else request
    }

}