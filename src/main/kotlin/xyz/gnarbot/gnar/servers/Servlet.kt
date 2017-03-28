package xyz.gnarbot.gnar.servers

import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.*
import net.dv8tion.jda.core.entities.impl.MessageImpl
import net.dv8tion.jda.core.exceptions.PermissionException
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

    private var _musicManager: MusicManager? = null
        get() {
            if (field == null) {
                field = MusicManager(this, bot.playerManager)
                field!!.player.volume = 35
            }
            return field
        }

    var musicManager: MusicManager
        get() = _musicManager!!
        set(value) {
            _musicManager = value
        }

    fun resetMusicManager() {
        _musicManager!!.scheduler.queue.clear()
        _musicManager!!.player.destroy()
        audioManager.closeAudioConnection()
        audioManager.sendingHandler = null
        _musicManager = null
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
        return getClient(getMemberByName(name, searchNickname))
    }

    /**
     * Lazily wrap users in a Member instance.
     *
     * @param member JDA member.
     *
     * @return User instance.
     */
    fun getClient(member: Member?): Client? {
        return clientHandler.getClient(member)
    }

    /**
     * Lazily wrap users in a Member instance.
     *
     * @param user JDA user.
     *
     * @return User instance.
     */
    fun getClient(user: User?): Client? {
        return clientHandler.getClient(user)
    }

    //    @Deprecated("Useless")
    //    /** Load JSON instance from the Host's storage. */
    //    public boolean loadJSON()
    //    {
    //        file = Bot.files.hosts.child("$id.json")
    //        file.createNewFile()
    //
    //        val content = file.readText()
    ////        if (content.isEmpty()) jsonObject = NullableJSON()
    ////        else jsonObject = NullableJSON(content)
    //    }
    //
    //    @Deprecated("Useless")
    //    /** Save the JSON instance of the Host. */
    //    public boolean saveJSON() = file.writeText(jsonObject.toString(4))

    /**
     * Attempt to ban the member from the guild.
     * @return If the bot had permission.
     */
    fun ban(client: Client): Boolean {
        try {
            controller.ban(client as User, 2).queue()
            return true
        } catch (e: PermissionException) {
            return false
        }

    }

    /**
     * Attempt to un-ban the member from the guild.
     * @return If the bot had permission.
     */
    fun unban(client: Client): Boolean {
        try {
            controller.unban(client).queue()
            return true
        } catch (e: PermissionException) {
            return false
        }

    }

    /**
     * Attempt to kick the member from the guild.
     * @return If the bot had permission.
     */
    fun kick(client: Client): Boolean {
        try {
            controller.kick(client).queue()
            return true
        } catch (e: PermissionException) {
            return false
        }

    }

    /**
     * Attempt to mute the member in the guild.
     * @return If the bot had permission.
     */
    fun mute(client: Client): Boolean {
        try {
            controller.setMute(client, true).queue()
            return true
        } catch (e: PermissionException) {
            return false
        }

    }

    /**
     * Attempt to unmute the member in the guild.
     * @return If the bot had permission.
     */
    fun unmute(client: Client): Boolean {
        try {
            controller.setMute(client, false).queue()
            return true
        } catch (e: PermissionException) {
            return false
        }
    }

    /**
     * @return String representation of the Host.
     */
    override fun toString(): String = "Host(id=${guild.id}, shard=${shard.id}, guild=${guild.name})"

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
