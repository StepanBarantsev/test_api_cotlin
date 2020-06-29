package ru.task.models

data class ErrorModel(val field: String, val message: String, val code: String, val value: String){
}