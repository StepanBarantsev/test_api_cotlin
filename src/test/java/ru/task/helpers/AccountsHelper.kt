package ru.task.helpers

import ru.task.tests.Accounts.BaseTest
import ru.task.models.Accounts
import kotlin.test.assertNotNull
import kotlin.test.assertEquals
import kotlin.test.assert

class AccountsHelper(val app: BaseTest) {

    val localBasePath = "account/list/?"
    val defaultLimit = 100

    fun assertValidFieldsNotNull(response: Accounts) {
        assertNotNull(response.status, app.generateMessageAboutNullError("status"))
        assertNotNull(response.meta, app.generateMessageAboutNullError("meta"))
        assertNotNull(response.meta["count"], app.generateMessageAboutNullError("meta[\"count\"]"))
        assertNotNull(response.data, app.generateMessageAboutNullError("data"))
    }

    fun assertDefaultLimit(response: Accounts) {
        assertOkStatus(response)
        assertLimitEqualDataAndMeta(defaultLimit, response)
    }

    fun assertDefaultSearchType(response: Accounts, expectedNum: Int, expectedSearch: String){
        assertOkStatus(response)
        assertLimitEqualDataAndMeta(expectedNum, response)
        assertAllNicknamesStartsWith(response, expectedSearch)
    }

    fun assertExactSearchType(response: Accounts, expectedNum: Int, expectedNames: Array<String>){
        assertOkStatus(response)
        // Приходит много записей, хотя было введено одно имя
        assertLimitEqualDataAndMeta(expectedNum, response)

        assert(expectedNames.contentEquals(getAllNicknames(response)))
        {"Ожидался список имен $expectedNames, а в результате был получен список ${getAllNicknames(response)}"}
    }

    fun assertEmptyData(response: Accounts){
        assertLimitEqualDataAndMeta(0, response)
    }

    fun assertLimitEqualDataAndMeta(expectedNum: Int, response: Accounts) {
        assertEquals(expectedNum, response.meta!!["count"]!!.toInt(),
                "В поле meta указан count равный ${response.meta["count"]}. Ожидалось $expectedNum")
        assertEquals(expectedNum, response.data!!.size,
                "Размер пришедшего массива data равен ${response.data.size}. Ожидалось $expectedNum")
    }

    fun assertOkStatus(response: Accounts) {
        assertEquals("ok", response.status, "Ожидался status ok. Фактически он равен ${response.status}")
    }

    fun getAllNicknames(response: Accounts) : Array<String>{
        val arr = response.data
        val result = Array(arr!!.size){"empty"}
        for (index in arr.indices){
            result[index] = arr[index]["nickname"]!!
        }
        return result
    }

    fun assertAllNicknamesStartsWith(nicknames: Array<String>, namePart: String){
        for (i in nicknames) if (!(i.startsWith(namePart, ignoreCase=true))) throw
        AssertionError("Не все имена из массива игроков $nicknames начинаются с $namePart")
    }

    fun assertAllNicknamesStartsWith(response: Accounts, namePart: String){
        val nicknames = getAllNicknames(response)
        assertAllNicknamesStartsWith(nicknames, namePart)
    }

    fun sendAccountsRequest(search: String?=null, limit: String?=null, searchType: String?=null, language: String?=null): Accounts
    {
        var request = "${localBasePath}application_id=${app.key}"
        request = addSearchToRequest(request, search)
        request = addLimitToRequest(request, limit)
        request = addSearchTypeToRequest(request, searchType)
        request = addLanguageToRequest(request, language)
        return app.getResponse(request)
    }

    fun sendAccountsRequest(search: String?=null, limit: Int, searchType: String?=null, language: String?=null): Accounts{
        return sendAccountsRequest(search, limit.toString(), searchType)
    }

    fun assertErrorFieldsNotNull(response: Accounts, error: Map<String, String>?) {
        assertNotNull(response.status, app.generateMessageAboutNullError("status"))
        assertNotNull(response.error, app.generateMessageAboutNullError("error"))
        assertNotNull(error!!["field"], app.generateMessageAboutNullError("error[\"field\"]"))
        assertNotNull(error["message"], app.generateMessageAboutNullError("error[\"message\"]"))
        assertNotNull(error["code"], app.generateMessageAboutNullError("error[\"code\"]"))
        assertNotNull(error["value"], app.generateMessageAboutNullError("error[\"value\"]"))
    }

    fun assertErrorFields(response: Accounts,
                          error: Map<String, String>?,
                          field: String,
                          message: String,
                          code: String,
                          value: String) {
        assertErrorStatus(response)
        assertEquals(field, error!!["field"], "Ожидалось значение для error[\"field\"]: $field. Фактическое значение: ${error["field"]}")
        assertEquals(message, error["message"], "Ожидалось значение для error[\"message\"]: $message. Фактическое значение: ${error["message"]}")
        assertEquals(code, error["code"], "Ожидалось значение для error[\"code\"]: $code. Фактическое значение: ${error["code"]}")
        assertEquals(value, error["value"], "Ожидалось значение для error[\"value\"]: $value. Фактическое значение: ${error["value"]}")
    }

    private fun assertErrorStatus(response: Accounts) {
        assertEquals("error", response.status, "Ожидался status error. Фактически он равен ${response.status}")
    }

    private fun addLimitToRequest(request: String, limit: String?): String{
        return if (limit != null) request + "&limit=${limit}" else request
    }

    private fun addLanguageToRequest(request: String, language: String?): String{
        return if (language != null) request + "&language=${language}" else request
    }

    private fun addSearchToRequest(request: String, search: String?): String{
        return if (search != null) request + "&search=${search}" else request
    }

    private fun addSearchTypeToRequest(request: String, searchType: String?): String{
        return if (searchType != null) request + "&type=${searchType}" else request
    }

}