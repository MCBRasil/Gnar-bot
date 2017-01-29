package xyz.gnarbot.gnar.api

import org.json.JSONObject
import xyz.gnarbot.gnar.Bot

class JsonAPI {
    fun get() {
        val jso = JSONObject()

        jso.put("total_shards", Bot.shards.size)
    }
}