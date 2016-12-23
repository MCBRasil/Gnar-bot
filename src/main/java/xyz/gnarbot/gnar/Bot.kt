package xyz.gnarbot.gnar

import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.entities.Game
import net.dv8tion.jda.core.entities.User
import net.dv8tion.jda.core.utils.SimpleLog
import xyz.gnarbot.gnar.handlers.servers.Shard
import xyz.gnarbot.gnar.utils.Utils
import xyz.gnarbot.gnar.utils.readProperties
import java.util.*
import java.util.concurrent.Executors
import kotlin.jvm.JvmStatic as static

/**
 * Main class of the bot. Implemented as a singleton.
 */
object Bot
{
    @static val LOG = SimpleLog.getLog("Bot")!!
    
    var initialized = false
        private set
    
    /** Sharded JDA instances of the bot.*/
    val shards = mutableListOf<Shard>()
    
    /** Administrator users of the bot. */
    val admins = mutableSetOf<User>()
    
    @static val files = BotFiles()
    
    val startTime = System.currentTimeMillis()
    val scheduler = Executors.newSingleThreadScheduledExecutor()
    
    val authTokens = files.tokens.readProperties()
    
    fun initBot(token : String, num_shards : Int)
    {
        if (initialized) throw IllegalStateException("Bot instance have already been initialized.")
        initialized = true
        
        LOG.info("Initializing Bot.")
        LOG.info("Requesting $num_shards shards.")
        
        for (id in 0 .. num_shards - 1)
        {
            val jda = JDABuilder(AccountType.BOT).run{
                if (num_shards > 1) useSharding(id, num_shards)
                setToken(token)
                setAutoReconnect(true)
                setGame(Game.of("Shard: " + id + " | _help"))
                buildBlocking()
            }
    
            jda.selfUser.manager.setName("Gnar")

            
            shards += Shard(id, jda)
        }
        
        LOG.info("Bot is now connected to Discord.")
        Utils.setLeagueInfo()

    }
    
    val uptime : String
        get()
        {
            val s = (Date().time - startTime) / 1000
            val m = s / 60
            val h = m / 60
            val d = h / 24
            return "$d days, ${h % 24} hours, ${m % 60} minutes and ${s % 60} seconds"
        }
    
    val simpleUptime : String
        get()
        {
            val s = (Date().time - startTime) / 1000
            val m = s / 60
            val h = m / 60
            val d = h / 24
            return "${d}d ${h % 24}h ${m % 60}m ${s % 60}s"
        }
}