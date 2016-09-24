package xyz.gnarbot.gnar

import com.google.inject.Guice
import com.google.inject.Injector
import net.dv8tion.jda.entities.Guild
import net.dv8tion.jda.events.message.MessageReceivedEvent
import net.dv8tion.jda.managers.GuildManager
import org.json.JSONObject
import xyz.gnarbot.gnar.handlers.MemberHandler
import xyz.gnarbot.gnar.handlers.commands.CommandHandler
import xyz.gnarbot.gnar.injection.GuildInjectorModule
import xyz.gnarbot.gnar.utils.NullableJSONObject
import java.io.File

class Host(val shard : Shard, guild : Guild) : GuildManager(guild), Guild by guild
{
    lateinit var file : File
        private set
    lateinit var jsonObject : JSONObject
        private set
    
    val memberHandler = MemberHandler(this)
    val commandHandler = CommandHandler(this)
    
    /** Dependency injection instance from Guice. */
    private val injector : Injector = Guice.createInjector(GuildInjectorModule(this))
    
    init
    {
        loadJSON()
        saveJSON()
        
        commandHandler.inheritFrom(shard.distributor)
        commandHandler.registry.values.forEach { injector.injectMembers(it) }
    }
    
    fun handleMessageEvent(event : MessageReceivedEvent)
    {
        if (event.isPrivate) return
        commandHandler.callCommand(event)
    }
    
    /** Load JSON instance from the Host's storage. */
    fun loadJSON()
    {
        file = File("_DATA/servers/$id.json")
        file.createNewFile()
    
        val content = file.readText()
        if (content.length == 0) jsonObject = NullableJSONObject()
        else jsonObject = NullableJSONObject(content)
    }
    
    /** Save the JSON instance of the Host. */
    fun saveJSON() = file.writeText(jsonObject.toString(4))
    
    /**
     * @return String representation of the Host.
     */
    override fun toString() : String = "Host(id=$id, shard=${shard.id}, guild=${guild.name})"
    
}

