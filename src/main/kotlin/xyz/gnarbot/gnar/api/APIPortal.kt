package xyz.gnarbot.gnar.api

import ro.pippo.core.Application
import ro.pippo.core.Pippo
import ro.pippo.gson.GsonEngine
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
        registerContentTypeEngine(GsonEngine::class.java)

        GET("/api/shards(/)?") {
            it.json().send(Bot.info)
        }

        GET("/api/shards/{id}(/)?") {
            val id = it.getParameter("id").toInt(0)

            if (id >= Bot.shards.size || id < 0) {
                it.json().send("""{"message": "Shard id not found."}""")
            } else {
                it.json().send(Bot.shards[id].info)
            }
        }
    }
}