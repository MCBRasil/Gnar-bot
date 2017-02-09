package xyz.gnarbot.gnar.api

import ro.pippo.core.Pippo

object APIPortal {
    fun start() {
        val pippo = Pippo(APIRoutes())
        pippo.server.port = 3001
        pippo.start()
    }
}