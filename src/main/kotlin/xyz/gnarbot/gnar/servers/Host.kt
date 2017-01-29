package xyz.gnarbot.gnar.servers

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.ChannelType
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.exceptions.PermissionException
import net.dv8tion.jda.core.managers.GuildManager
import xyz.gnarbot.gnar.commands.handlers.CommandHandler
import xyz.gnarbot.gnar.commands.handlers.CommandTable
import xyz.gnarbot.gnar.members.Person
import xyz.gnarbot.gnar.members.PersonHandler
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note
import net.dv8tion.jda.core.entities.User as JDAUser

/**
 * Represents a bot on each [Guild].
 */
class Host(val shard: Shard, guild: Guild) : GuildManager(guild), Guild by guild {
//    lateinit var file : File
//        private set

    val personHandler = PersonHandler(this)
    val commandHandler = CommandHandler(this)

    private var musicManager : MusicManager? = null

    /** Dependency injection instance from Guice. */
    private val injector: Injector = Guice.createInjector(HostModule())

    init {
        commandHandler.receiveFrom(CommandTable)
        commandHandler.registry.values
                .filter { it.isInject }
                .forEach { injector.injectMembers(it) }
    }

    /** Hanldles incoming message events.*/
    fun handleMessageEvent(event: MessageReceivedEvent) {
        if (event.isFromType(ChannelType.PRIVATE)) return
        commandHandler.callCommand(event)
    }

    fun noteOf(msg: Message): Note {
        return Note(this, msg)
    }

    fun setMusicManager(musicManager : MusicManager?) {
        this.musicManager = musicManager
    }

    fun getMusicManager() : MusicManager {
        if (musicManager == null) {
            musicManager = MusicManager(shard.playerManager).apply { player.volume = 35 }
        }
        return musicManager!!
    }

//    @Deprecated("Useless")
//    /** Load JSON instance from the Host's storage. */
//    fun loadJSON()
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
//    fun saveJSON() = file.writeText(jsonObject.toString(4))

    /**
     * @return String representation of the Host.
     */
    override fun toString(): String = "Host(id=$id, shard=${shard.id}, guild=\"${guild.name}\")"

    /**
     * Attempt to ban the member from the guild.
     * @return If the bot had permission.
     */
    fun ban(person: Person): Boolean {
        try {
            guild.controller.ban(person as JDAUser, 2).queue()
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
            guild.controller.unban(person).queue()
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
            guild.controller.kick(person).queue()
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
            guild.controller.setMute(person, true).queue()
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
            guild.controller.setMute(person, false).queue()
            return true
        } catch (e: PermissionException) {
            return false
        }
    }

    /**
     * Injector module for the Host.
     */
    private inner class HostModule : AbstractModule() {
        public override fun configure() {
            bind(Guild::class.java).toInstance(guild)
            bind(CommandHandler::class.java).toInstance(commandHandler)
            bind(PersonHandler::class.java).toInstance(personHandler)
            bind(Host::class.java).toInstance(this@Host)
            bind(Shard::class.java).toInstance(shard)
            bind(JDA::class.java).toInstance(jda)
        }
    }
}

