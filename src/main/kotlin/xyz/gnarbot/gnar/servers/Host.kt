package xyz.gnarbot.gnar.servers

import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.User
import net.dv8tion.jda.core.exceptions.PermissionException
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.handlers.CommandHandler
import xyz.gnarbot.gnar.members.Person
import xyz.gnarbot.gnar.members.PersonHandler
import xyz.gnarbot.gnar.servers.music.MusicManager

/**
 * Represents a bot on each [Guild].
 * @see Guild
 */
class Host(val shard: Shard, private var guild : Guild) : Guild by guild {
    val personHandler: PersonHandler
    val commandHandler: CommandHandler

    var musicManager: MusicManager? = null
        get() {
            if (field == null) {
                this.musicManager = MusicManager(Bot.playerManager)
                field!!.player.volume = 35
            }
            return field
        }

    init {
        this.personHandler = PersonHandler(this)
        this.commandHandler = CommandHandler(this)
    }

    fun check() : Host {
        val _guild = shard.getGuildById(id)
        if (_guild != guild) {
            guild = _guild
        }
        return this
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
    fun ban(person: Person): Boolean {
        try {
            controller.ban(person as User, 2).queue()
            return true
        } catch (e: PermissionException) {
            return false
        }

    }

    /**
     * Attempt to un-ban the member from the guild.
     * @return If the bot had permission.
     */
    fun unban(person: Person): Boolean {
        try {
            controller.unban(person).queue()
            return true
        } catch (e: PermissionException) {
            return false
        }

    }

    /**
     * Attempt to kick the member from the guild.
     * @return If the bot had permission.
     */
    fun kick(person: Person): Boolean {
        try {
            controller.kick(person).queue()
            return true
        } catch (e: PermissionException) {
            return false
        }

    }

    /**
     * Attempt to mute the member in the guild.
     * @return If the bot had permission.
     */
    fun mute(person: Person): Boolean {
        try {
            controller.setMute(person, true).queue()
            return true
        } catch (e: PermissionException) {
            return false
        }

    }

    /**
     * Attempt to unmute the member in the guild.
     * @return If the bot had permission.
     */
    fun unmute(person: Person): Boolean {
        try {
            controller.setMute(person, false).queue()
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
        val person = personHandler.asPerson(message.author)
        commandHandler.callCommand(message, message.content, person)
    }
}
