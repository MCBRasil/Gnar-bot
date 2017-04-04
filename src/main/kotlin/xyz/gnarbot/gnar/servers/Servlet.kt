package xyz.gnarbot.gnar.servers

import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.*
import net.dv8tion.jda.core.entities.impl.MessageImpl
import net.dv8tion.jda.core.requests.Request
import net.dv8tion.jda.core.requests.Response
import net.dv8tion.jda.core.requests.RestAction
import net.dv8tion.jda.core.requests.Route
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.handlers.CommandHandler
import xyz.gnarbot.gnar.members.Client
import xyz.gnarbot.gnar.members.ClientHandler
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note

/**
 * Represents a bot that serves on each [Guild] and wraps around it.
 * @see Guild
 */
class Servlet(val shard: Shard, private val guild: Guild, val bot: Bot) : Guild by guild {
    val clientHandler = ClientHandler(this)
    val commandHandler = CommandHandler(this, bot)

    val selfClient get() = clientHandler.selfClient

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

    fun getClientByName(name: String, searchNickname: Boolean): Client? {
        return clientHandler.getClientByName(name, searchNickname)
    }

    /**
     * Lazily wrap users in a Member instance.
     *
     * @param member JDA member.
     *
     * @return User instance.
     */
    fun getClient(member: Member): Client? {
        return clientHandler.getClient(member)
    }

    /**
     * Lazily wrap users in a Member instance.
     *
     * @param user JDA user.
     *
     * @return User instance.
     */
    fun getClient(user: User): Client? {
        return user.let { clientHandler.getClient(user) }
    }

    /**
     * @return String representation of the Host.
     */
    override fun toString(): String = "Servlet(id=${guild.id}, shard=${shard.id}, guild=${guild.name})"

    fun shutdown(interrupt: Boolean) : Boolean {
        clientHandler.registry.clear()

        musicManager.let {
            if (!interrupt && it.player.playingTrack != null) {
                return false
            }
        }

        resetMusicManager()
        return true
    }

    fun handleMessage(message: Message) {
        val person = clientHandler.getClient(message.author)
        commandHandler.callCommand(message, message.content, person)
    }

    fun MessageChannel.sendNote(embed: MessageEmbed) : RestAction<Note> {
        val message = MessageBuilder().setEmbed(embed).build()
        return sendNote(message)
    }

    fun MessageChannel.sendNote(message: Message) : RestAction<Note> {
        val route = Route.Messages.SEND_MESSAGE.compile(id)
        val json = (message as MessageImpl).toJSONObject()
        return object : RestAction<Note>(jda, route, json) {
            override fun handleResponse(response: Response, request: Request<Any?>?) {
                if (response.isOk) {
                    val msg = EntityBuilder.get(jda).createMessage(response.`object`, this@sendNote, false)
                    request?.onSuccess(Note(this@Servlet, msg))
                } else {
                    request?.onFailure(response)
                }
            }
        }
    }
}
