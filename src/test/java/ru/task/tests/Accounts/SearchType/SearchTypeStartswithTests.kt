package ru.task.tests.Accounts.SearchType

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import ru.task.models.ErrorModel
import ru.task.Application
import io.qameta.allure.Description
import org.junit.jupiter.api.DisplayName


@Tag("SearchType")
@DisplayName("Проверка параметра type=startswith")
class TestsSearchTypeStartswith{

    private val app = Application()
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
    @DisplayName("Отправка запроса с параметром type=startswith и параметром search по которому находится 100+ ников")
    @Description("Отправка запроса с параметром type=startswith и параметром search по которому находится 100+ ников. Ожидается 100 найденных игроков")
    fun testExistingNamesMoreThan100() {
        val response = app.accountsHelper.sendAccountsRequest(search=existingPartOfNamesMoreThan100, type=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=existingPartOfNamesMoreThan100, expectedNum=100)
    }

    @Test
    @DisplayName("Отправка запроса с параметром type=startswith и параметром search по которому находится меньше 100 ников")
    @Description("Отправка запроса с параметром type=startswith и параметром search по которому находится меньше 100 ников. Ожидается меньше 100 найденных игроков")
    fun testExistingNamesLessThan100() {
        val response = app.accountsHelper.sendAccountsRequest(search=existingPartOfNamesLessThan100, type=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertOkStatus(response)

        assert(100 > response.meta!!["count"]!!.toInt())
        {"Имен, нашедшихся по запросу $existingPartOfNamesLessThan100: ${response.meta["count"]} (поле meta[\"count\"])." +
                "Ожидалось, что их количество будет меньше 100"}
        assert(100 > response.data!!.size)
        {"Имен, нашедшихся по части ника $existingPartOfNamesLessThan100: ${response.data.size} (количество элементов, пришедших в data)." +
                "Ожидалось, что их количество будет меньше 100"}
        app.accountsHelper.assertAllNicknamesStartsWith(response, existingPartOfNamesLessThan100)
    }

    @Test
    @DisplayName("Отправка запроса с параметром type=startswith и параметром search по которому находится ровно 1 ник (ник отпавляем точно соответствующий существующему)")
    @Description("Отправка запроса с параметром type=startswith и параметром search по которому находится ровно 1 ник (ник отпавляем точно соответствующий существующему). Ожидается 1 найденный игрок")
    fun testUniqueExistingNameFull() {
        val response = app.accountsHelper.sendAccountsRequest(search=uniqueName, type=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=uniqueName, expectedNum=1)
    }

    @Test
    @DisplayName("Отправка запроса с параметром type=startswith и параметром search по которому находится ровно 1 ник (отправляем ник без двух последних сивмволов)")
    @Description("Отправка запроса с параметром type=startswith и параметром search по которому находится ровно 1 ник (отправляем ник без двух последних сивмволов). Ожидается 1 найденный игрок")
    fun testUniqueExistingNamePart() {
        val partOfUniqueName = uniqueName.substring(0, uniqueName.length - 2)
        val response = app.accountsHelper.sendAccountsRequest(search=partOfUniqueName, type=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=partOfUniqueName, expectedNum=1)
    }

    @Test
    @DisplayName("Отправка запроса с параметром type=startswith и параметром search на который ник заканчивается, но не начинается")
    @Description("Отправка запроса с параметром type=startswith и параметром search на который ник заканчивается, но не начинается. Ожидается 0 найденных игроков")
    fun testNameEndwith() {
        val partOfUniqueName = uniqueName.substring(1, uniqueName.length)
        val response = app.accountsHelper.sendAccountsRequest(search=partOfUniqueName, type=searchType)

        app.accountsHelper.assertOkStatus(response)
        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertEmptyData(response)
    }

    @Test
    @DisplayName("Отправка запроса с параметром type=startswith и параметром search по которому ничего не должно находиться")
    @Description("Отправка запроса с параметром type=startswith и параметром search по которому ничего не должно находиться. Ожидается 0 найденных игроков")
    fun testNotExistingName() {
        val response = app.accountsHelper.sendAccountsRequest(search=notExistingName, type=searchType)

        app.accountsHelper.assertOkStatus(response)
        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertEmptyData(response)
    }

    @Test
    @DisplayName("Отправка запроса с параметром type=startswith и параметром search меньше минимальной длины")
    @Description("Отправка запроса с параметром type=startswith и параметром search меньше минимальной длины. Ожидается status==error, error[message]==NOT_ENOUGH_SEARCH_LENGTH")
    fun testNameLessMinLength() {
        val response = app.accountsHelper.sendAccountsRequest(search=nameOneSymbol, type=searchType)

        app.errorHelper.assertErrorFieldsNotNull(response, response.error)
        app.errorHelper.assertErrorFields(response, response.error, ErrorModel("search", "NOT_ENOUGH_SEARCH_LENGTH", "407", nameOneSymbol))
    }

    @Test
    @DisplayName("Отправка запроса с параметром type=startswith и параметром search больше максимальной длины")
    @Description("Отправка запроса с параметром type=startswith и параметром search больше максимальной длины. Ожидается status==error, error[message]==INVALID_SEARCH")
    fun testNameMoreMaxLength() {
        val response = app.accountsHelper.sendAccountsRequest(search=name25Symbols, type=searchType)

        app.errorHelper.assertErrorFieldsNotNull(response, response.error)
        app.errorHelper.assertErrorFields(response, response.error, ErrorModel("search", "INVALID_SEARCH", "407", name25Symbols))
    }

    // Это гарантированно уникальное имя
    @Test
    @DisplayName("Отправка запроса с параметром type=startswith и параметром search длиной в 24 символа (существующий ник)")
    @Description("Отправка запроса с параметром type=startswith и параметром search длиной в 24 символа (существующий ник). Ожидается 1 найденный игрок")
    fun testName24Symbols() {
        val response = app.accountsHelper.sendAccountsRequest(search=name24Symbols, type=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=name24Symbols, expectedNum=1)
    }

    @Test
    @DisplayName("Отправка запроса с параметром type=startswith и параметром search в котором указан существующий ник, но целиком заглавными буквами")
    @Description("Отправка запроса с параметром type=startswith и параметром search в котором указан существующий ник, но целиком заглавными буквами. Ожидается 100 найденных игроков")
    fun testNameWithUpperLetters() {
        val response = app.accountsHelper.sendAccountsRequest(search=uniqueName.toLowerCase(), type=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=uniqueName, expectedNum=1)
    }

    @Test
    @DisplayName("Отправка запроса с параметром type=startswith и параметром search в котором указан существующий ник, но целиком строчными буквами")
    @Description("Отправка запроса с параметром type=startswith и параметром search в котором указан существующий ник, но целиком строчными буквами. Ожидается 100 найденных игроков")
    fun testNameWithLowerLetters() {
        val response = app.accountsHelper.sendAccountsRequest(search=uniqueName.toUpperCase(), type=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=uniqueName, expectedNum=1)
    }
}