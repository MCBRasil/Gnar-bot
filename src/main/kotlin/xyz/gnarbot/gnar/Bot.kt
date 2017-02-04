package xyz.gnarbot.gnar

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.entities.Game
import net.dv8tion.jda.core.utils.SimpleLog
import org.json.JSONArray
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

    @static val LOG = SimpleLog.getLog("Bot")!!

    @static val token = "_" //default token

    @static val files = BotFiles()

    /** @returns If the bot is initialized. */
    var initialized = false
        private set

    val playerManager: AudioPlayerManager = DefaultAudioPlayerManager().apply {
        registerSourceManager(YoutubeAudioSourceManager())
        registerSourceManager(SoundCloudAudioSourceManager())
        registerSourceManager(VimeoAudioSourceManager())
        registerSourceManager(TwitchStreamAudioSourceManager())
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
        if (initialized) throw IllegalStateException("Bot instance have already been initialized.")
        initialized = true

        LOG.info("Initializing Bot.")
        LOG.info("Requesting $numShards shards.")

        LOG.info("There are ${admins.size} administrators registered for the bot.")

        for (id in 0..numShards - 1) {
            val jda = JDABuilder(AccountType.BOT).run {
                if (numShards > 1) useSharding(id, numShards)

                setToken(token)
                setAutoReconnect(true)
                setGame(Game.of("Shard: $id | _help"))

                buildBlocking()
            }

            jda.selfUser.manager.setName("Gnar").queue()

            shards += Shard(id, jda)

            LOG.info("Built shard $id.")
        }

        LOG.info("Bot is now connecting to Discord.")
        Utils.setLeagueInfo()
    }

    /**
     * Stop the bot.
     */
    fun stop() {
        shards.forEach(Shard::shutdown)
        initialized = false

        LOG.info("Bot is now disconnecting from Discord.")
    }

}