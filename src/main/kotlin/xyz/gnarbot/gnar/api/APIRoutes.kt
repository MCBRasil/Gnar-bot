package xyz.gnarbot.gnar.api

import org.json.JSONArray
import org.json.JSONObject
import ro.pippo.core.Application
import xyz.gnarbot.gnar.Bot

class APIRoutes : Application()
{
    override fun onInit()
    {
        GET("/api/shards")
        {
            val jso = JSONObject()

            jso.put("requests", Bot.shards.flatMap { it.hosts }.map { it.commandHandler.requests }.sum())
            jso.put("guilds", Bot.shards.flatMap { it.hosts }.size)

            val jsa = JSONArray()
            Bot.shards.forEach { jsa.put(it.jsonResponse()) }
            jso.put("shards", jsa)

            it.text().send(jso)
        }
    }
}
