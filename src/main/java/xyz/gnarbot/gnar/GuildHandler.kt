package xyz.gnarbot.gnar

import com.google.inject.Guice
import com.google.inject.Injector
import net.dv8tion.jda.entities.Guild
import net.dv8tion.jda.events.message.MessageReceivedEvent
import net.dv8tion.jda.managers.GuildManager
import xyz.gnarbot.gnar.handlers.MemberHandler
import xyz.gnarbot.gnar.handlers.commands.CommandHandler
import xyz.gnarbot.gnar.injection.GuildInjectorModule

class GuildHandler(private val shard : Shard, guild : Guild) : GuildManager(guild), Guild by guild
{
    val injector : Injector = Guice.createInjector(GuildInjectorModule(this))
    
    val memberHandler = MemberHandler(this)
    val commandHandler = CommandHandler(this)
    
    init
    {
        commandHandler.inheritFrom(shard.distributor)
        commandHandler.registry.forEach { injector.injectMembers(it) }
    }
    
    fun callCommand(event : MessageReceivedEvent)
    {
        if (event.isPrivate) return
        commandHandler.callCommand(event)
    }
    
    override fun toString() : String
    {
        return "GuildHandler(shard=${shard.id}, guild=${guild.name})"
    }
}

