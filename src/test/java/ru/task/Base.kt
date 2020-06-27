package ru.task

import io.restassured.RestAssured.*

open class BaseTest {
    val baseUrl = "https://api.worldoftanks.ru/wot/"

    fun getResponse(pathRequest: String) : String{
        return get("$baseUrl$pathRequest").getBody().asString()
    }
}