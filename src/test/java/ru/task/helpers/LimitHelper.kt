package ru.task.helpers

import org.junit.jupiter.api.Assertions
import ru.task.BaseTest
import ru.task.models.Accounts
import kotlin.test.assertNotNull

class LimitHelper(val app: BaseTest) {

    val localBasePath = "account/list/?"

    // Предположим, что по этой части находится 100+ игроков (это действительно так)
    // Проверки будут построены на основе этого предположения
    val partOfName = "blo"
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

    fun sendLimitRequest(limit: Int) : Accounts{
        // С учетом перегрузки, я так понимаю, все хорошо
        return sendLimitRequest(limit.toString())
    }

    fun sendLimitRequest(limit: String): Accounts{
        return app.getResponse("${app.limitHelper.localBasePath}application_id=${app.key}&search=${app.limitHelper.partOfName}&limit=${limit}")
    }

    fun sendRequestWithoutLimit() : Accounts{
        return app.getResponse("${app.limitHelper.localBasePath}application_id=${app.key}&search=${app.limitHelper.partOfName}")
    }
}