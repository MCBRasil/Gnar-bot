package xyz.gnarbot.gnar.commands.executors.general

import link
import net.dv8tion.jda.core.OnlineStatus
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor


@Command(
        aliases = arrayOf("info", "botinfo"),
        description = "Show information about the bot."
)
class BotInfoCommand : CommandExecutor() {
    override fun execute(message: Message, args: List<String>) {
        val registry = bot.commandRegistry

        var uptime_hour = bot.uptime / 1000 / 60 / 60
        if (uptime_hour == 0L) uptime_hour = 1L

        var voiceConnections = 0

        var textChannels = 0
        var voiceChannels = 0
        var servlets = 0 // wrapper
        var guilds = 0

        var users = 0
        var offline = 0
        var online = 0
        var inactive = 0

        for (shard in bot.shards) {
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

            users += shard.users.size
            textChannels += shard.textChannels.size
            voiceChannels += shard.voiceChannels.size
            servlets += shard.servlets.size
        }

        val commandSize = registry.entries.count { it.meta.category.show }

        val requests = bot.shards
                .flatMap { it.servlets.values }
                .sumBy { it.commandHandler.requests }

        message.respond().embed("Bot Information") {
            color = Constants.COLOR

            field("Requests", true, requests)
            field("Requests Per Hour", true, requests / uptime_hour)
            field("Website", true, link("gnarbot.xyz", "https://gnarbot.xyz"))

            field("Text Channels", true, textChannels)
            field("Voice Channels", true, voiceChannels)
            field("Voice Connections", true, voiceConnections)

            field("Guilds", true, guilds)
            field("Guild#Servlets", true, servlets)
            field(true)

            field("Users", true) {
                append("Total: ").appendln(users)
                append("Online: ").appendln(online)
                append("Offline: ").appendln(offline)
                append("Inactive: ").appendln(inactive)
            }

            field("Others", true) {
                appendln("Creators: **[Avarel](https://github.com/Avarel)** and **[Xevryll](https://github.com/xevryll)**")
                appendln("Contributor: **[Gatt](https://github.com/RealGatt)**")
                appendln("Commands: **$commandSize**")
                appendln("Library: Java **[JDA 3](https://github.com/DV8FromTheWorld/JDA)**")
            }
        }.rest().queue()
    }
}
