package xyz.gnarbot.gnar.injection

import com.google.inject.AbstractModule
import net.dv8tion.jda.entities.Guild
import xyz.gnarbot.gnar.GuildHandler
import xyz.gnarbot.gnar.handlers.MemberHandler
import xyz.gnarbot.gnar.handlers.commands.CommandHandler

class GuildInjectorModule(private val guildHandler : GuildHandler) : AbstractModule()
{
    public override fun configure()
    {
        bind(Guild::class.java).toInstance(guildHandler.guild)
        bind(CommandHandler::class.java).toInstance(guildHandler.commandHandler)
        bind(MemberHandler::class.java).toInstance(guildHandler.memberHandler)
        bind(GuildHandler::class.java).toInstance(guildHandler)
    }
}