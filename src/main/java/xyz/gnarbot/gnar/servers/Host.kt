package xyz.gnarbot.gnar.servers

import com.google.inject.*
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.*
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.exceptions.PermissionException
import net.dv8tion.jda.core.managers.GuildManager
import xyz.gnarbot.gnar.commands.handlers.*
import xyz.gnarbot.gnar.members.*
import xyz.gnarbot.gnar.members.User
import xyz.gnarbot.gnar.utils.Note
import net.dv8tion.jda.core.entities.User as JDAUser

/**
 * Represents a bot on each [Guild].
 */
class Host(val shard : Shard, guild : Guild) : GuildManager(guild), Guild by guild
{
//    lateinit var file : File
//        private set
    
    val userHandler = MemberHandler(this)
    val commandHandler = CommandHandler(this)
    
    /** Dependency injection instance from Guice. */
    private val injector : Injector = Guice.createInjector(HostModule())
    
    init
    {
        commandHandler.recieveFrom(CommandTable)
        commandHandler.registry.values.forEach { injector.injectMembers(it) }
    }
    
    /** Hanldles incoming message events.*/
    fun handleMessageEvent(event : MessageReceivedEvent)
    {
        if (event.isFromType(ChannelType.PRIVATE)) return
        commandHandler.callCommand(event)
    }
    
    fun noteOf(msg : Message) : Note
    {
        return Note(this, msg)
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
    override fun toString() : String = "Host(id=$id, shard=${shard.id}, guild=\"${guild.name}\")"
    
    /**
     * Attempt to ban the member from the guild.
     * @return If the bot had permission.
     */
    fun ban(user : String) : Boolean
    {
        try
        {
            guild.controller.ban(user, 2).queue()
            return true
        }
        catch (e : PermissionException)
        {
            return false
        }
    }
    
    /**
     * Attempt to un-ban the member from the guild.
     * @return If the bot had permission.
     */
    fun unban(user : String) : Boolean
    {
        try
        {
            guild.controller.unban(user).queue()
            return true
        }
        catch (e : PermissionException)
        {
            return false
        }
    }
    
    /**
     * Attempt to kick the member from the guild.
     * @return If the bot had permission.
     */
    fun kick(user : Member) : Boolean
    {
        try
        {
            guild.controller.kick(user).queue()
            return true
        }
        catch (e : PermissionException)
        {
            return false
        }
    }
    
    /**
     * Attempt to mute the member in the guild.
     * @return If the bot had permission.
     */
    fun mute(user : User) : Boolean
    {
        try
        {
            guild.controller.setMute(user, true)
            return true
        }
        catch (e : PermissionException)
        {
            return false
        }
    }
    
    /**
     * Attempt to unmute the member in the guild.
     * @return If the bot had permission.
     */
    fun unmute(user : User) : Boolean
    {
        try
        {
            guild.controller.setMute(user, false)
            return true
        }
        catch (e : PermissionException)
        {
            return false
        }
    }
    
    /**
     * Injector module for the Host.
     */
    private inner class HostModule : AbstractModule()
    {
        public override fun configure()
        {
            bind(Guild::class.java).toInstance(guild)
            bind(CommandHandler::class.java).toInstance(commandHandler)
            bind(MemberHandler::class.java).toInstance(userHandler)
            bind(Host::class.java).toInstance(this@Host)
            bind(Shard::class.java).toInstance(shard)
            bind(JDA::class.java).toInstance(jda)
        }
    }
}

