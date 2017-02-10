package xyz.gnarbot.gnar.api

import org.json.JSONArray
import org.json.JSONObject
import ro.pippo.core.Application
import ro.pippo.core.Pippo
import xyz.gnarbot.gnar.Bot

class APIPortal : Application()
{
    fun start() {
        val pippo = Pippo(this)
        pippo.server.port = 3001
        pippo.start()
    }

    override fun onInit()
    {
        GET("/api/shards(/)?") {
            val jso = JSONObject()

            jso.put("all", Bot.jsonResponse())

            val jsa = JSONArray()
            Bot.shards.forEach { jsa.put(it.jsonResponse()) }
            jso.put("shards", jsa)

            it.text().send(jso)
        }

        GET("/api/shards/{id}(/)?") {
            val id = it.getParameter("id").toInt(0)

            val jso = if (id >= Bot.shards.size || id < 0) {
                JSONObject().put("message", "Shard not found.")
            } else {
                Bot.shards[id].jsonResponse()
            }

            it.text().send(jso)
        }
    }
}
