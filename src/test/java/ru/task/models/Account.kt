package ru.task.models

import com.fasterxml.jackson.annotation.JsonProperty

class Accounts (val status: String? = null,
                val meta: Map<String, Int>? = null,
                val data: Array<Map<String, String>>? = null,
                val error: Map<String, String>? = null){

    override fun toString() : String {
        if (status == "ok"){
            var dataString = "{"

            for (i in data!!) {
                dataString += "nickname: ${i["nickname"]}, "
            }
            if (data.isNotEmpty()) dataString = dataString.substring(0, dataString.length-2)

            dataString += "}"
            return "Accounts(status=$status, meta=$meta, error=$error, data=$dataString)"
        }
        else return "Accounts(status=$status)"
    }
}