package xyz.gnarbot.gnar

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.entities.Game
import org.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import xyz.gnarbot.gnar.api.APIPortal
import xyz.gnarbot.gnar.api.data.BotInfo
import xyz.gnarbot.gnar.commands.handlers.CommandRegistry
import xyz.gnarbot.gnar.servers.Shard
import xyz.gnarbot.gnar.utils.Utils
import java.awt.Color
import java.util.concurrent.Executors
import kotlin.jvm.JvmStatic as static

/**
 * Main class of the bot. Implemented as a singleton.
 */
object Bot {
    @static val color = Color(0, 80, 175)

    @static val LOGGER: Logger = LoggerFactory.getLogger("Bot")

    @static val token = "_" //default token

    @static val files = BotFiles()

    val commandRegistry = CommandRegistry(this)

    /** @returns If the bot is initialized. */
    var initialized = false
        private set

    val playerManager: AudioPlayerManager = DefaultAudioPlayerManager().apply {
        registerSourceManager(YoutubeAudioSourceManager())
        registerSourceManager(SoundCloudAudioSourceManager())
        registerSourceManager(VimeoAudioSourceManager())
        //registerSourceManager(TwitchStreamAudioSourceManager())
    }

    /** @return Sharded JDA instances of the bot.*/
    val shards = mutableListOf<Shard>()

    /** @return Administrator users of the bot. */
    val admins = hashSetOf<String>().apply {
        JSONArray(files.admins.readText()).forEach {
            add(it as String)
        }
    }

    val blocked = hashSetOf<String>().apply {
        JSONArray(files.blocked.readText()).forEach {
            add(it as String)
        }
    }

    val startTime = System.currentTimeMillis()
    /** Returns how many milliseconds since the bot have been up. */
    val uptime: Long get() = System.currentTimeMillis() - startTime

    val scheduler = Executors.newSingleThreadScheduledExecutor()!!

    /**
     * Start the bot.
     *
     * @param token Discord token.
     * @param numShards Number of shards to request.
     */
    fun start(token: String, numShards: Int) {
        val api = APIPortal().apply { start() }

        if (initialized) throw IllegalStateException("Bot instance have already been initialized.")
        initialized = true

        LOGGER.info("Initializing the Discord bot.")
        LOGGER.info("Requesting $numShards shards.")

        LOGGER.info("There are ${admins.size} administrators registered for the bot.")
        LOGGER.info("There are ${blocked.size} blocked users registered for the bot.")

        for (id in 0..numShards - 1) {
            val jda = makeJDA(token, numShards, id)

            jda.selfUser.manager.setName("Gnar").queue()

            shards.add(Shard(id, this, jda))

            LOGGER.info("Shard [$id] is initialized.")
        }

        LOGGER.info("Bot is now connected to Discord.")

        api.registerRoutes()

        Utils.setLeagueInfo()
    }

    fun makeJDA(token: String, numShards: Int, id: Int): JDA {
        return JDABuilder(AccountType.BOT).apply {
            if (numShards > 1) useSharding(id, numShards)
            setToken(token)
            setAutoReconnect(true)
            setGame(Game.of("$id | _help"))
            setAudioEnabled(true)
            setEnableShutdownHook(true)
        }.buildBlocking()
    }

    /**
     * Stop the bot.
     */
    fun stop() {
        shards.forEach(Shard::shutdown)
        shards.clear()
        initialized = false
        System.gc()

        LOGGER.info("Bot is now disconnected from Discord.")
    }

    val info: BotInfo get() = BotInfo(this)

}