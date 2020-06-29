package ru.task.helpers

import ru.task.models.Accounts
import ru.task.models.ErrorModel
import ru.task.tests.Accounts.BaseTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ErrorHelper(val app: BaseTest) {

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
                          errorExpected: ErrorModel) {
        assertErrorStatus(response)
        val errorActual = ErrorModel(error!!["field"]!!, error["message"]!!, error["code"]!!, error["value"]!!)
        assertEquals(errorExpected, errorActual,
                "Ожидаемое сообщение об ошибке: $errorExpected. Фактическое сообщение: $errorActual")
    }

    private fun assertErrorStatus(response: Accounts) {
        assertEquals("error", response.status, "Ожидался status error. Фактически он равен ${response.status}")
    }
}