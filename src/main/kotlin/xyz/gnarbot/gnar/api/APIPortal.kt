//package xyz.gnarbot.gnar.api
//
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//import ro.pippo.core.Application
//import ro.pippo.core.Pippo
//import ro.pippo.gson.GsonEngine
//import xyz.gnarbot.gnar.Bot
//
//class APIPortal(val bot: Bot) : Application() {
//    val LOGGER: Logger = LoggerFactory.getLogger("APIPortal")
//
//    fun start() {
//        val pippo = Pippo(this)
//        pippo.start(3001)
//        LOGGER.info("Opened API web portal on port ${pippo.server.port}.\n\n\n")
//    }
//
//    fun registerRoutes() {
//        registerContentTypeEngine(GsonEngine::class.java)
//
//        GET("/api/shards(/)?") {
//            it.json().send(bot.info)
//        }
//
//        GET("/api/shards/{id}(/)?") {
//            val id = it.getParameter("id").toInt(0)
//
//            if (id >= bot.shards.size || id < 0) {
//                it.json().send("""{"message": "Shard id not found."}""")
//            } else {
//                it.json().send(bot.shards[id].info)
//            }
//        }
//
//        LOGGER.info("Registered API routes.")
//    }
//}