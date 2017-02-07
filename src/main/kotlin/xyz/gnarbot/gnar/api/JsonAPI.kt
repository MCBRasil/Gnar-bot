package xyz.gnarbot.gnar.api

import org.json.JSONObject
import xyz.gnarbot.gnar.Bot

class JsonAPI {
    fun get() : JSONObject {
        val jso = JSONObject()

        jso.put("total_shards", Bot.shards.size)

        Bot.shards.forEach {
            jso.put(it.id.toString(), true)
        }

        return jso
    }
}