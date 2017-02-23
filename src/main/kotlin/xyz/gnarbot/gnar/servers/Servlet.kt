package xyz.gnarbot.gnar.servers

import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.User
import net.dv8tion.jda.core.exceptions.PermissionException
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.handlers.CommandHandler
import xyz.gnarbot.gnar.members.Client
import xyz.gnarbot.gnar.members.ClientHandler
import xyz.gnarbot.gnar.servers.music.MusicManager

/**
 * Represents a bot that serves on each [Guild] and wraps around it.
 * @see Guild
 */
class Servlet(val shard: Shard, private var guild: Guild) : Guild by guild {
    val clientHandler: ClientHandler = ClientHandler(this)
    val commandHandler: CommandHandler = CommandHandler(this)

    val selfClient: Client get() = clientHandler.selfClient

    var musicManager: MusicManager? = null
        get() {
            if (field == null) {
                this.musicManager = MusicManager(this, Bot.playerManager)
                field!!.player.volume = 35
            }
            return field
        }

    fun resetMusicManager() {
        musicManager!!.scheduler.queue.clear()
        musicManager!!.player.destroy()
        audioManager.closeAudioConnection()
        audioManager.sendingHandler = null
        musicManager = null
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

    fun getPersonByName(name: String, searchNickname: Boolean = false): Client? {
        return getMemberByName(name, searchNickname)?.let { clientHandler.asPerson(it) }
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

    fun shutdown() {
        clientHandler.registry.clear()
        resetMusicManager()
    }

    fun handleMessage(message: Message) {
        val person = clientHandler.asPerson(message.author)
        commandHandler.callCommand(message, message.content, person)
    }

}
