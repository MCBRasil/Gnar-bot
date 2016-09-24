package xyz.gnarbot.gnar

import net.dv8tion.jda.JDABuilder
import java.io.File
import java.util.Date
import java.util.Properties
import java.util.concurrent.Executors

fun main(args : Array<String>)
{
    Bot(Bot.authTokens.getProperty("beta-bot"), 1)
}

/**
 * Main class of the bot.
 */
class Bot(token :  String, shardsNum : Int)
{
    companion object
    {
        @JvmStatic val shards      = mutableListOf<Shard>()
    
        @JvmStatic val startTime   = System.currentTimeMillis()
        @JvmStatic val adminIDs    = File("_DATA/administrators").readLines()
        @JvmStatic val authTokens  = Properties().apply { load(File("_DATA/tokens.properties").inputStream()) }
        @JvmStatic val scheduler   = Executors.newSingleThreadScheduledExecutor()
    
        @JvmStatic
        fun getUptimeStamp(compact: Boolean = false) : String
        {
            val seconds = (Date().time - startTime) / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24
            return when
            {
                compact -> "$days days, ${hours % 24} hours, ${minutes % 60} minutes and ${seconds % 60} seconds"
                else    -> "${days}d ${hours % 24}h ${minutes % 60}m ${seconds % 60}s"
            }
        }
    }
    
    init
    {
        println("Initializing Gnar-Bot with token='$token', shards=$shardsNum.")
    
        val jda = JDABuilder()
                //.useSharding(id, shardsNum)
                .setBotToken(token)
                .buildBlocking()
    
        jda.accountManager.setUsername("GNAR")
        jda.accountManager.setGame("_help | _invite")
        jda.accountManager.update()
    
        jda.isAutoReconnect = true
    
        shards.add(Shard(0, jda))
        
//        for (id in 0 .. shardsNum - 1)
//        {
//            val jda = JDABuilder()
//                    .useSharding(id, shardsNum)
//                    .setBotToken(token)
//                    .buildBlocking()
//
//            jda.accountManager.setUsername("GNAR")
//            jda.accountManager.setGame("_help | _invite")
//            jda.accountManager.update()
//
//            jda.isAutoReconnect = true
//
//            shards.add(Shard(id, jda))
//        }
    }
}

//
//class Bot(val token : String, shardsNum : Int)
//{
//    companion object Data
//    {
//        @JvmStatic val shards      = arrayListOf<Shard>()
//
//        @JvmStatic val startTime   = System.currentTimeMillis()
//        @JvmStatic val adminIDs    = File("_DATA/administrators").readLines()
//        @JvmStatic val authTokens  = PropertiesManager().load(File("_DATA/tokens.properties"))!!
//        @JvmStatic val scheduler   = Executors.newSingleThreadScheduledExecutor()!!
//
//        @JvmStatic
//        fun getUptimeStamp(compact: Boolean = false) : String
//        {
//            val seconds = (Date().time - startTime) / 1000
//            val minutes = seconds / 60
//            val hours = minutes / 60
//            val days = hours / 24
//            return when
//            {
//                compact -> "$days days, ${hours % 24} hours, ${minutes % 60} minutes and ${seconds % 60} seconds"
//                else    -> "${days}d ${hours % 24}h ${minutes % 60}m ${seconds % 60}s"
//            }
//        }
//
//        @JvmStatic
//        fun getGuildCount() : Int
//        {
//            return shards.map { it.jda.guilds.size }.sum()
//        }
//
//        @JvmStatic
//        fun getGuildManagers() : List<GuildManager>
//        {
//            return shards.flatMap { it.guildManagers }
//        }
//    }
//
//    init
//    {
//        var servers = 0
//
//        for (id in 0 .. shardsNum - 1)
//        {
//            val jda = JDABuilder()
//                    .useSharding(id, shardsNum)
//                    .setBotToken(token)
//                    .buildBlocking()
//
//            jda.accountManager.setUsername("GNAR")
//            jda.accountManager.setGame("_help | _invite")
//            jda.accountManager.update()
//
//            jda.isAutoReconnect = true
//
//            servers += jda.guilds.size
//
//            shards.add(GnarShard(jda, id))
//        }
//
//        DiscordBotsInfo.updateServerCount(servers)
//    }
//}