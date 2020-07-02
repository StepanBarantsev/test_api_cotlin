package ru.task.tests.Accounts.SearchType

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import ru.task.models.ErrorModel
import ru.task.Application
import io.qameta.allure.Description


@Tag("SearchType")
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
    @Description("Отправка запроса с параметром type=startswith и параметром search по которому находится 100+ ников")
    fun testExistingNamesMoreThan100() {
        val response = app.accountsHelper.sendAccountsRequest(search=existingPartOfNamesMoreThan100, searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=existingPartOfNamesMoreThan100, expectedNum=100)
    }

    @Test
    @Description("Отправка запроса с параметром type=startswith и параметром search по которому находится меньше 100 ников")
    fun testExistingNamesLessThan100() {
        val response = app.accountsHelper.sendAccountsRequest(search=existingPartOfNamesLessThan100, searchType=searchType)

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
    @Description("Отправка запроса с параметром type=startswith и параметром search по которому находится ровно 1 ник (ник отпавляем точно соответствующий существующему)")
    fun testUniqueExistingNameFull() {
        val response = app.accountsHelper.sendAccountsRequest(search=uniqueName, searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=uniqueName, expectedNum=1)
    }

    @Test
    @Description("Отправка запроса с параметром type=startswith и параметром search по которому находится ровно 1 ник (отправляем ник без двух последних сивмволов)")
    fun testUniqueExistingNamePart() {
        val partOfUniqueName = uniqueName.substring(0, uniqueName.length - 2)
        val response = app.accountsHelper.sendAccountsRequest(search=partOfUniqueName, searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=partOfUniqueName, expectedNum=1)
    }

    @Test
    @Description("Отправка запроса с параметром type=startswith и параметром search на который ник заканчивается, но не начинается")
    fun testNameEndwith() {
        val partOfUniqueName = uniqueName.substring(1, uniqueName.length-1)
        val response = app.accountsHelper.sendAccountsRequest(search=partOfUniqueName, searchType=searchType)

        app.accountsHelper.assertOkStatus(response)
        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertEmptyData(response)
    }

    @Test
    @Description("Отправка запроса с параметром type=startswith и параметром search по которому ничего не должно находиться")
    fun testNotExistingName() {
        val response = app.accountsHelper.sendAccountsRequest(search=notExistingName, searchType=searchType)

        app.accountsHelper.assertOkStatus(response)
        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertEmptyData(response)
    }

    @Test
    @Description("Отправка запроса с параметром type=startswith и параметром search меньше минимальной длины")
    fun testNameLessMinLength() {
        val response = app.accountsHelper.sendAccountsRequest(search=nameOneSymbol, searchType=searchType)

        app.errorHelper.assertErrorFieldsNotNull(response, response.error)
        app.errorHelper.assertErrorFields(response, response.error, ErrorModel("search", "NOT_ENOUGH_SEARCH_LENGTH", "407", nameOneSymbol))
    }

    @Test
    @Description("Отправка запроса с параметром type=startswith и параметром search больше максимальной длины")
    fun testNameMoreMaxLength() {
        val response = app.accountsHelper.sendAccountsRequest(search=name25Symbols, searchType=searchType)

        app.errorHelper.assertErrorFieldsNotNull(response, response.error)
        app.errorHelper.assertErrorFields(response, response.error, ErrorModel("search", "INVALID_SEARCH", "407", name25Symbols))
    }

    // Это гарантированно уникальное имя
    @Test
    @Description("Отправка запроса с параметром type=startswith и параметром search длиной в 24 символа (существующий ник)")
    fun testName24Symbols() {
        val response = app.accountsHelper.sendAccountsRequest(search=name24Symbols, searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=name24Symbols, expectedNum=1)
    }

    @Test
    @Description("Отправка запроса с параметром type=startswith и параметром search в котором указан существующий ник, но целиком заглавными буквами")
    fun testNameWithUpperLetters() {
        val response = app.accountsHelper.sendAccountsRequest(search=uniqueName.toLowerCase(), searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=uniqueName, expectedNum=1)
    }

    @Test
    @Description("Отправка запроса с параметром type=startswith и параметром search в котором указан существующий ник, но целиком строчными буквами")
    fun testNameWithLowerLetters() {
        val response = app.accountsHelper.sendAccountsRequest(search=uniqueName.toUpperCase(), searchType=searchType)

        app.accountsHelper.assertValidFieldsNotNull(response)
        app.accountsHelper.assertDefaultSearchType(response, expectedSearch=uniqueName, expectedNum=1)
    }
}