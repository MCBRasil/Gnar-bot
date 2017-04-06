package xyz.gnarbot.gnar

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import net.dv8tion.jda.core.entities.Game
import net.dv8tion.jda.core.jda
import org.json.JSONArray
import org.json.JSONTokener
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import xyz.gnarbot.gnar.api.APIPortal
import xyz.gnarbot.gnar.api.data.BotInfo
import xyz.gnarbot.gnar.commands.handlers.CommandRegistry
import xyz.gnarbot.gnar.servers.Shard
import xyz.gnarbot.gnar.servers.listeners.GuildCountListener
import java.util.concurrent.Executors
import kotlin.jvm.JvmStatic as static

/**
 * Main class of the bot. Implemented as a singleton.
 */
class Bot {
    /** @returns The SLF4J logger instance of the bot. */
    val log: Logger = LoggerFactory.getLogger("Bot")

    /** @returns The global token of the bot. */
    val token = "_" //default token

    /** @returns If the bot is initialized. */
    var initialized = false
        private set

    val files = BotFiles()

    val guildCountListener = GuildCountListener(this)

    val commandRegistry = CommandRegistry(this)



    val playerManager: AudioPlayerManager = DefaultAudioPlayerManager().apply {
        registerSourceManager(YoutubeAudioSourceManager())
        registerSourceManager(SoundCloudAudioSourceManager())
        registerSourceManager(VimeoAudioSourceManager())
        registerSourceManager(BandcampAudioSourceManager())
        //registerSourceManager(TwitchStreamAudioSourceManager())
    }

    /** @return Sharded JDA instances of the bot.*/
    val shards = mutableListOf<Shard>()

    /** @return Administrator users of the bot. */
    val admins = hashSetOf<String>().apply {
        JSONArray(JSONTokener(files.admins.reader())).forEach {
            add(it as String)
        }
    }

    val blocked = hashSetOf<String>().apply {
        JSONArray(JSONTokener(files.blocked.reader())).forEach {
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
        val api = APIPortal(this).apply { start() }

        if (initialized) throw IllegalStateException("Bot instance have already been initialized.")
        initialized = true

        log.info("Initializing the Discord bot.")
        log.info("Requesting $numShards shards.")

        log.info("There are ${admins.size} administrators registered for the bot.")
        log.info("There are ${blocked.size} blocked users registered for the bot.")

        shards += jda(token, numShards) { id ->
            setAutoReconnect(true)
            setGame(Game.of("$id | _help"))
            setAudioEnabled(true)
            setEnableShutdownHook(true)
        }.mapIndexed { id, jda ->
            jda.selfUser.manager.setName("Gnar").queue()
            log.info("Shard [$id] is initialized.")

//            jda.apply {
//                addEventListener(ShardListener(jda, bot))
//                addEventListener(UserListener())
//                addEventListener(guildCountListener)
//            }

            Shard(id, jda, this)
        }

        log.info("Bot is now connected to Discord.")

        api.registerRoutes()
    }

    /**
     * Stop the bot.
     */
    fun stop() {
        shards.forEach(Shard::shutdown)
        shards.clear()
        initialized = false
        System.gc()

        log.info("Bot is now disconnected from Discord.")
    }

    val info: BotInfo get() = BotInfo(this)

}