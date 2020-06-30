package ru.task.specifichelpers

import java.io.FileInputStream
import java.io.IOException
import java.lang.Exception
import java.util.*


class PropertyHelper {

    fun getPropertyByName(name: String): String {

        val result = System.getenv(name)
        if (result != null) return result

        try {
            FileInputStream("src/test/resources/env.properties").use { input ->
                val prop = Properties()
                prop.load(input)
                return prop.getProperty(name) ?:
                throw Exception("Отсутствует значение $name в .properties файле и в переменных окружения")
            }
        } catch (ex: IOException) {
            throw Exception("Отсутствует файл .properties и переменные окржения для значения $name")
        }
    }
}