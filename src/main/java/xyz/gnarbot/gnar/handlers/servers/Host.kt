package xyz.gnarbot.gnar.handlers.servers

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.ChannelType
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.exceptions.PermissionException
import net.dv8tion.jda.core.managers.GuildManager
import org.json.JSONObject
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.handlers.commands.CommandDistributor
import xyz.gnarbot.gnar.handlers.commands.CommandHandler
import xyz.gnarbot.gnar.handlers.members.MemberHandler
import xyz.gnarbot.gnar.handlers.members.User
import xyz.gnarbot.gnar.utils.NullableJSON
import xyz.gnarbot.gnar.utils.child
import java.io.File
import net.dv8tion.jda.core.entities.User as JDAUser

/**
 * Represents a bot on each [Guild].
 */
class Host(val shard : Shard, guild : Guild) : GuildManager(guild), Guild by guild
{
    lateinit var file : File
        private set
    lateinit var jsonObject : JSONObject
        private set
    
    val userHandler = MemberHandler(this)
    val commandHandler = CommandHandler(this)
    
    /** Dependency injection instance from Guice. */
    private val injector : Injector = Guice.createInjector(HostModule())
    
    init
    {
//        loadJSON()
//        saveJSON()
        
        commandHandler.recieveFrom(CommandDistributor)
        commandHandler.registry.values.forEach { injector.injectMembers(it) }
    }
    
    /** Hanldles incoming message events.*/
    fun handleMessageEvent(event : MessageReceivedEvent)
    {
        if (event.isFromType(ChannelType.PRIVATE)) return
        commandHandler.callCommand(event)
    }
    
    /** Load JSON instance from the Host's storage. */
    fun loadJSON()
    {
        file = Bot.files.hosts.child("$id.json")
        file.createNewFile()
        
        val content = file.readText()
        if (content.isEmpty()) jsonObject = NullableJSON()
        else jsonObject = NullableJSON(content)
    }
    
    /** Save the JSON instance of the Host. */
    fun saveJSON() = file.writeText(jsonObject.toString(4))
    
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

