package ru.task.models

import com.fasterxml.jackson.annotation.JsonProperty

class Accounts (@JsonProperty val status: String? = null,
               @JsonProperty val meta: Map<String, Int>? = null,
               @JsonProperty val data: Array<Map<String, String>>? = null){
}