package xyz.gnarbot.gnar.servers

import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.handlers.CommandHandler
import xyz.gnarbot.gnar.servers.music.MusicManager

/**
 * Represents a bot that serves on each [Guild] and wraps around it.
 * @see Guild
 */
class Servlet(val shard: Shard, private val guild: Guild, val bot: Bot) : Guild by guild {
    val commandHandler = CommandHandler(this, bot)

    private var musicManager_delegate: MusicManager? = null
        get() {
            if (field == null) {
                field = MusicManager(this, bot.playerManager)
                field!!.player.volume = 35
            }
            return field
        }

    var musicManager: MusicManager
        get() = musicManager_delegate!!
        set(value) {
            musicManager_delegate = value
        }

    fun resetMusicManager() {
        musicManager.scheduler.queue.clear()
        musicManager.player.destroy()
        audioManager.closeAudioConnection()
        audioManager.sendingHandler = null
        musicManager_delegate = null
    }

    fun getMemberByName(name: String, searchNickname: Boolean = false): Member? {
        for (member in getMembersByName(name, true)) {
            return member
        }
        if (searchNickname) {
            for (member in getMembersByNickname(name, true)) {
                return member
            }
        }
        return null
    }

    /**
     * @return String representation of the Host.
     */
    override fun toString(): String = "Servlet(id=${guild.id}, shard=${shard.id}, guild=${guild.name})"

    fun shutdown(interrupt: Boolean) : Boolean {
        musicManager.let {
            if (!interrupt && it.player.playingTrack != null) {
                return false
            }
        }

        resetMusicManager()
        return true
    }

    fun handleMessage(message: Message) {
        commandHandler.callCommand(message, message.content)
    }
}
