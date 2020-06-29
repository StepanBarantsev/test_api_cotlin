package ru.task

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import ru.task.models.Accounts

class TestsLocalization {

    private val app = BaseTest()
    private val invalidLang = "jf"
    private val validLangs = arrayOf("en", "ru", "pl", "de", "fr", "es", "zh-cn", "zh-tw", "tr", "cs", "th", "vi", "ko")
    private val partOfName = "blo"


    @ParameterizedTest
    @ValueSource(strings=["en", "ru", "pl", "de", "fr", "es", "zh-cn", "zh-tw", "tr", "cs", "th", "vi", "ko"])
    fun testInvalidLang(lang: String) {
        val response = app.accountsHelper.sendAccountsRequest(search=partOfName, language=lang)

        app.accountsHelper.assertOkStatus(response)
    }

    @Test
    fun testValidLang() {
        val response = app.accountsHelper.sendAccountsRequest(search=partOfName, language=invalidLang)

        app.accountsHelper.assertErrorFieldsNotNull(response, response.error)
        app.accountsHelper.assertErrorFields(response, response.error, "language", "INVALID_LANGUAGE", "407", invalidLang)
    }
}