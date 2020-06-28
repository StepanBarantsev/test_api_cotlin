package ru.task.helpers

import org.junit.jupiter.api.Assertions
import ru.task.BaseTest
import ru.task.models.Accounts
import kotlin.test.assertNotNull

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
        Assertions.assertEquals("ok", response.status)
        Assertions.assertEquals(defaultLimit, response.meta!!["count"]!!.toInt())
        Assertions.assertEquals(defaultLimit, response.data!!.size)
    }

    fun sendAccountsRequest(search: String?=null, limit: String?=null): Accounts{
        var request = "${app.accountsHelper.localBasePath}application_id=${app.key}"
        request = addSearchToRequest(request, search)
        request = addLimitToRequest(request, limit)
        return app.getResponse(request)
    }

    fun sendAccountsRequest(search: String?=null, limit: Int): Accounts{
        return sendAccountsRequest(search, limit.toString())
    }

    private fun addLimitToRequest(request: String, limit: String?): String{
        return if (limit != null) request + "&limit=${limit}" else request
    }

    private fun addSearchToRequest(request: String, search: String?): String{
        return if (search != null) request + "&search=${search}" else request
    }
}