package xyz.gnarbot.gnar.servers

import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.Region
import net.dv8tion.jda.core.entities.*
import net.dv8tion.jda.core.exceptions.PermissionException
import net.dv8tion.jda.core.managers.AudioManager
import net.dv8tion.jda.core.managers.GuildController
import net.dv8tion.jda.core.managers.GuildManager
import net.dv8tion.jda.core.managers.GuildManagerUpdatable
import net.dv8tion.jda.core.requests.RestAction
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.handlers.CommandHandler
import xyz.gnarbot.gnar.members.HostUser
import xyz.gnarbot.gnar.members.UsersHandler
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.reference.GuildReference
import java.time.OffsetDateTime

/**
 * Represents a bot on each [Guild].
 * @see Guild
 */
class Host(val shard: Shard, private var guild : Guild) : Guild by guild {
    val usersHandler: UsersHandler = UsersHandler(this)
    val commandHandler: CommandHandler = CommandHandler(this)

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

//    fun ensure() : Host {
//        val _guild = shard.getGuildById(id)
//        if (_guild != guild) {
//            guild = _guild
//        }
//        afkChannel
//        return this
//    }

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
    fun ban(hostUser: HostUser): Boolean {
        try {
            controller.ban(hostUser as User, 2).queue()
            return true
        } catch (e: PermissionException) {
            return false
        }

    }

    /**
     * Attempt to un-ban the member from the guild.
     * @return If the bot had permission.
     */
    fun unban(hostUser: HostUser): Boolean {
        try {
            controller.unban(hostUser).queue()
            return true
        } catch (e: PermissionException) {
            return false
        }

    }

    /**
     * Attempt to kick the member from the guild.
     * @return If the bot had permission.
     */
    fun kick(hostUser: HostUser): Boolean {
        try {
            controller.kick(hostUser).queue()
            return true
        } catch (e: PermissionException) {
            return false
        }

    }

    /**
     * Attempt to mute the member in the guild.
     * @return If the bot had permission.
     */
    fun mute(hostUser: HostUser): Boolean {
        try {
            controller.setMute(hostUser, true).queue()
            return true
        } catch (e: PermissionException) {
            return false
        }

    }

    /**
     * Attempt to unmute the member in the guild.
     * @return If the bot had permission.
     */
    fun unmute(hostUser: HostUser): Boolean {
        try {
            controller.setMute(hostUser, false).queue()
            return true
        } catch (e: PermissionException) {
            return false
        }
    }

    /**
     * @return String representation of the Host.
     */
    override fun toString(): String = "Host(id=${guild.id}, shard=${shard.id}, guild=${guild.name})"

    fun handleMessage(message: Message) {
        val person = usersHandler.asPerson(message.author)
        commandHandler.callCommand(message, message.content, person)
    }

}
