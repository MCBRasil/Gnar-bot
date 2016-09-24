package xyz.gnarbot.gnar.injection

import com.google.inject.AbstractModule
import net.dv8tion.jda.entities.Guild
import xyz.gnarbot.gnar.Host
import xyz.gnarbot.gnar.Shard
import xyz.gnarbot.gnar.handlers.MemberHandler
import xyz.gnarbot.gnar.handlers.commands.CommandHandler

class GuildInjectorModule(private val host : Host) : AbstractModule()
{
    public override fun configure()
    {
        bind(Guild::class.java).toInstance(host.guild)
        bind(CommandHandler::class.java).toInstance(host.commandHandler)
        bind(MemberHandler::class.java).toInstance(host.memberHandler)
        bind(Host::class.java).toInstance(host)
        bind(Shard::class.java).toInstance(host.shard)
    }
}