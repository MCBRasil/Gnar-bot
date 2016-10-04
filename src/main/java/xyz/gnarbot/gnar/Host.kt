package xyz.gnarbot.gnar

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import net.dv8tion.jda.entities.Guild
import net.dv8tion.jda.entities.User
import net.dv8tion.jda.events.message.MessageReceivedEvent
import net.dv8tion.jda.exceptions.PermissionException
import net.dv8tion.jda.managers.GuildManager
import org.json.JSONObject
import xyz.gnarbot.gnar.handlers.MemberHandler
import xyz.gnarbot.gnar.handlers.commands.CommandHandler
import xyz.gnarbot.gnar.utils.NullableJSON
import java.io.File

/**
 * Represents a bot on each [Guild].
 */
class Host(val shard : Shard, guild : Guild) : GuildManager(guild), Guild by guild
{
    lateinit var file : File
        private set
    lateinit var jsonObject : JSONObject
        private set
    
    val memberHandler = MemberHandler(this)
    val commandHandler = CommandHandler(this)
    
    /** Dependency injection instance from Guice. */
    private val injector : Injector = Guice.createInjector(HostModule())
    
    init
    {
        loadJSON()
        saveJSON()
        
        commandHandler.recieveFrom(shard.distributor)
        commandHandler.registry.values.forEach { injector.injectMembers(it) }
    }
    
    /** Hanldles incoming message events.*/
    fun handleMessageEvent(event : MessageReceivedEvent)
    {
        if (event.isPrivate) return
        commandHandler.callCommand(event)
    }
    
    /** Load JSON instance from the Host's storage. */
    fun loadJSON()
    {
        file = File(Bot.files.hostsFolder, "$id.json")
        file.createNewFile()
    
        val content = file.readText()
        if (content.length == 0) jsonObject = NullableJSON()
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
     * @return If the bot had permission to ban.
     */
    fun banUser(user : User) : Boolean
    {
        try
        {
            ban(user, 0)
            return true
        }
        catch (e : PermissionException)
        {
            return false
        }
    }
    
    fun unbanUser(user : User) : Boolean
    {
        try
        {
            unBan(user)
            return true
        }
        catch (e : PermissionException)
        {
            return false
        }
    }
    
    fun kickUser(user : User) : Boolean
    {
        try
        {
            kick(user)
            return true
        }
        catch (e : PermissionException)
        {
            return false
        }
    }
    
    fun muteUser(user : User) : Boolean
    {
        try
        {
            mute(user)
            return true
        }
        catch (e : PermissionException)
        {
            return false
        }
    }
    
    fun unmuteUser(user : User) : Boolean
    {
        try
        {
            unmute(user)
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
            bind(MemberHandler::class.java).toInstance(memberHandler)
            bind(Host::class.java).toInstance(this@Host)
            bind(Shard::class.java).toInstance(shard)
        }
    }
}

