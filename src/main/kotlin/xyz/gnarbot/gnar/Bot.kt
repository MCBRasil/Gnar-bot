package xyz.gnarbot.gnar

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.entities.Game
import org.json.JSONArray
import org.json.JSONTokener
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import xyz.gnarbot.gnar.api.data.BotInfo
import xyz.gnarbot.gnar.commands.CommandRegistry
import xyz.gnarbot.gnar.listeners.GuildCountListener
import java.io.File
import kotlin.jvm.JvmStatic as static

/**
 * Main class of the bot. Implemented as a singleton.
 *
 * @param token Discord token.
 * @param numShards Number of shards to request.
 */
class Bot(val token: String, val numShards: Int) {
    /** @returns The SLF4J logger instance of the bot. */
    val log: Logger = LoggerFactory.getLogger("Bot")

    /** @returns The global token of the bot. */
    val prefix = "_" //default token

    // FILES
    val dataFile = File("data").takeIf(File::exists)
            ?: throw IllegalStateException("`data` folder does not exist.")
    val adminsFile = File(dataFile, "administrators.json").takeIf(File::exists)
            ?: throw IllegalStateException("`administrators.json` does not exist.")
    val blockedFile = File(dataFile, "blocked.json").takeIf(File::exists)
            ?: throw IllegalStateException("`blocked.json` does not exist.")

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
    val admins = mutableSetOf<String>().apply {
        JSONArray(JSONTokener(adminsFile.reader())).forEach {
            add(it as String)
        }
    }

    val blocked = mutableSetOf<String>().apply {
        JSONArray(JSONTokener(blockedFile.reader())).forEach {
            add(it as String)
        }
    }

    val startTime = System.currentTimeMillis()
    /** @return how many milliseconds since the bot have been up. */
    val uptime: Long get() = System.currentTimeMillis() - startTime

    /**
     * Start the bot.
     */
    fun start() {
        log.info("Initializing the Discord bot.")
        log.info("Requesting $numShards shards.")

        log.info("There are ${admins.size} administrators registered for the bot.")
        log.info("There are ${blocked.size} blocked users registered for the bot.")

        for (id in 0 until numShards) {
            val jda = with(JDABuilder(AccountType.BOT)) {
                if (numShards > 1) useSharding(id, numShards)
                setToken(token)
                setAutoReconnect(true)
                setGame(Game.of("$id | _help"))
                setAudioEnabled(true)
            }.buildBlocking()

            log.info("JDA $id is ready.")

            jda.selfUser.manager.setName("Gnarr").queue()

            shards += Shard(id, jda, this)
        }

        log.info("Bot is now connected to Discord.")
    }

    fun restart() {
        for (id in 0 until shards.size) {
            shards[id].shutdown()

            val jda = with(JDABuilder(AccountType.BOT)) {
                if (numShards > 1) useSharding(id, numShards)
                setToken(token)
                setAutoReconnect(true)
                setGame(Game.of("$id | _help"))
                setAudioEnabled(true)
            }.buildBlocking()

            log.info("JDA $id is ready.")

            jda.selfUser.manager.setName("Gnarr").queue()

            shards[id] = Shard(id, jda, this)
        }
    }

    /**
     * Stop the bot.
     */
    fun stop() {
        shards.forEach(Shard::shutdown)
        shards.clear()
        System.gc()

        log.info("Bot is now disconnected from Discord.")
    }

    val info: BotInfo get() = BotInfo(this)
}