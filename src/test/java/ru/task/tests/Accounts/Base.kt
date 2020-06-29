package ru.task.tests.Accounts

import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured.get
import ru.task.helpers.AccountsHelper

import java.util.Properties;


class BaseTest {

    val baseUrl = "https://api.worldoftanks.ru/wot/"
    // Пока не разобрался, как спрятать ключ, верну его, он не очень секретный)
    val key : String = System.getenv("key") ?: "e004ffa1bf971bf49c8a752024e47f82"

    val accountsHelper = AccountsHelper(this)

    inline fun <reified T> getResponse(pathRequest: String) : T{

        return parseJsonToObject(get("$baseUrl$pathRequest").body.asString())
    }

    inline fun <reified T> parseJsonToObject(response: String) : T {
        val objectMapper = ObjectMapper()
        return objectMapper.readValue(response, T::class.java)
    }

    fun generateMessageAboutNullError(fieldName: String) : String{
        return "Поле $fieldName равно null (то есть отсутствует в json ответе)."
    }


    /*
    fun stringToJsonMap(str: String): Map<String, String>
    {
        val reader: ObjectReader = ObjectMapper().readerFor(MutableMap::class.java)
        val map: Map<String, String> = reader.readValue(str)
        return map
    }

    fun jsonStringToArray(str: String): Array<Any>
    {
        val mapper = ObjectMapper()
        val array: Array<Any> = mapper.readValue(str, Array<Any>::class.java)
        return array
    }
    */
}