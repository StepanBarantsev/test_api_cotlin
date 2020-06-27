package ru.task

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectReader
import com.fasterxml.jackson.databind.deser.std.ObjectArrayDeserializer
import io.restassured.RestAssured.get


open class BaseTest {
    val baseUrl = "https://api.worldoftanks.ru/wot/"
    // Пока не разобрался, как спрятать ключ, верну его, он не очень секретный)
    val key : String = System.getenv("key") ?: "e004ffa1bf971bf49c8a752024e47f82"

    fun getResponse(pathRequest: String) : Map<String, String>{
        return stringToJsonMap(get("$baseUrl$pathRequest").body.asString())
    }

    
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
}