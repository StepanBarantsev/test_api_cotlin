package ru.task

import com.fasterxml.jackson.core.type.TypeReference
import io.restassured.RestAssured.get
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectReader


open class BaseTest {
    val baseUrl = "https://api.worldoftanks.ru/wot/"

    fun getResponse(pathRequest: String) : Map<String, String>{
        return stringToJsonMap(get("$baseUrl$pathRequest").body.asString())
    }

    private fun stringToJsonMap(str: String): Map<String, String>
    {
        val reader: ObjectReader = ObjectMapper().readerFor(MutableMap::class.java)
        val map: Map<String, String> = reader.readValue(str)
        return map
    }
}