package xyz.gnarbot.gnar.commands.executors.general

import com.google.inject.Inject
import link
import net.dv8tion.jda.core.OnlineStatus
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.servers.Shard
import xyz.gnarbot.gnar.utils.Note


@Command(aliases = arrayOf("info", "botinfo"), description = "Show information about GN4R-BOT.")
class BotInfoCommand : CommandExecutor() {

    @Inject lateinit var shard: Shard

    override fun execute(note: Note, args: List<String>) {
        val registry = bot().commandRegistry

        var uptime_hour = Bot.uptime / 1000 / 60 / 60
        if (uptime_hour == 0L) uptime_hour = 1L

        var voiceConnections = 0

        var textChannels = 0
        var voiceChannels = 0
        var servlets = 0 // wrapper
        var guilds = 0

        var users = 0
        var clients = 0 // wrapper
        var offline = 0
        var online = 0
        var inactive = 0

        for (shard in Bot.shards) {
            guilds += shard.guilds.size

            for (guild in shard.guilds) {
                for (member in guild.members) {
                    when (member.onlineStatus) {
                        OnlineStatus.ONLINE -> online++
                        OnlineStatus.OFFLINE -> offline++
                        OnlineStatus.IDLE -> inactive++
                        else -> {}
                    }
                }

                if (guild.selfMember.voiceState.channel != null) {
                    voiceConnections++
                }
            }

            shard.servlets.values.forEach { clients += it.clientHandler.registry.size }

            users += shard.users.size
            textChannels += shard.textChannels.size
            voiceChannels += shard.voiceChannels.size
            servlets += shard.servlets.size
        }

        val commandSize = registry.entries.count { it.meta.showInHelp }

        val requests = Bot.shards
                .flatMap { it.servlets.values }
                .sumBy { it.commandHandler.requests }

        note.respond().embed("Bot Information") {
            color = Constants.COLOR

            field("Requests", true, requests)
            field("Requests Per Hour", true, requests / uptime_hour)
            field("Website", true, link("gnarbot.xyz", "https://gnarbot.xyz"))

            field("Text Channels", true, textChannels)
            field("Voice Channels", true, voiceChannels)
            field("Voice Connections", true, voiceConnections)

            field("Guilds", true, guilds)
            field("Guild#Servlets", true, servlets)
            field("Member#Clients", true, clients)

            field("Users", true) {
                append("Total: ").appendln(users)
                append("Online: ").appendln(online)
                append("Offline: ").appendln(offline)
                append("Inactive: ").appendln(inactive)
            }

            field("Others", true) {
                appendln("Creators: **[Avarel](https://github.com/Avarel)** and **[Maeyrl](https://github.com/maeyrl)**")
                appendln("Contributor: **[Gatt](https://github.com/RealGatt)**")
                appendln("Commands: **$commandSize**")
                appendln("Library: Java **[JDA 3](https://github.com/DV8FromTheWorld/JDA)**")
            }
        }.rest().queue()
    }
}
