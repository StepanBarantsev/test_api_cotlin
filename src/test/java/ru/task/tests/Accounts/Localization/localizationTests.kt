package ru.task.tests.Accounts.Localization

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import ru.task.models.ErrorModel
import ru.task.Application
import io.qameta.allure.Description


@Tag("Localization")
class TestsLocalization {

    private val app = Application()
    private val invalidLang = "jf"
    private val validLangs = arrayOf("en", "ru", "pl", "de", "fr", "es", "zh-cn", "zh-tw", "tr", "cs", "th", "vi", "ko")
    private val partOfName = "blo"


    @ParameterizedTest
    @ValueSource(strings=["en", "ru", "pl", "de", "fr", "es", "zh-cn", "zh-tw", "tr", "cs", "th", "vi", "ko"])
    @Description("Отправка запроса с валидным параметром language")
    fun testValidLang(lang: String) {
        val response = app.accountsHelper.sendAccountsRequest(search=partOfName, language=lang)

        app.accountsHelper.assertOkStatus(response)
    }

    @Test
    @Description("Отправка запроса с невалидным параметром language (непредусмотренный язык)")
    fun testInvalidLang() {
        val response = app.accountsHelper.sendAccountsRequest(search=partOfName, language=invalidLang)

        app.errorHelper.assertErrorFieldsNotNull(response, response.error)
        app.errorHelper.assertErrorFields(response, response.error, ErrorModel("language", "INVALID_LANGUAGE", "407", invalidLang))
    }
}