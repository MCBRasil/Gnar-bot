package xyz.gnarbot.gnar.commands.executors.general

import link
import net.dv8tion.jda.core.OnlineStatus
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.Command
import xyz.gnarbot.gnar.commands.CommandExecutor

@Command(
        aliases = arrayOf("info", "botinfo"),
        description = "Show information about the bot."
)
class BotInfoCommand : CommandExecutor() {
    override fun execute(message: Message, args: Array<String>) {
        val registry = bot.commandRegistry

        // Uptime
        val s = bot.uptime / 1000
        val m = s / 60
        val h = m / 60
        val d = h / 24

        var voiceConnections = 0

        var textChannels = 0
        var voiceChannels = 0
        var guildData = 0 // wrapper
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
            guildData += shard.guildData.size
        }

        val commandSize = registry.entries.count { it.meta.category.show }

        val requests = bot.shards
                .flatMap { it.guildData.values }
                .sumBy { it.commandHandler.requests }

        message.respond().embed("Bot Information") {
            color = Constants.COLOR

            inline {
                field("Requests", requests)
                field("Requests Per Hour", requests / if (h == 0L) 1 else h)
                field("Website", link("gnarbot.xyz", "https://gnarbot.xyz"))

                field("Text Channels", textChannels)
                field("Voice Channels", voiceChannels)
                field("Voice Connections", voiceConnections)

                field("Guilds", guilds)
                field("Guild Data", guildData)
                field("Uptime", "${d}d ${h}h ${m}m ${s}s")

                field("Users") {
                    buildString {
                        append("Total: ").appendln(users)
                        append("Online: ").appendln(online)
                        append("Offline: ").appendln(offline)
                        append("Inactive: ").appendln(inactive)
                    }
                }

                field("Others") {
                    buildString {
                        appendln("Creators: **[Avarel](https://github.com/Avarel)** and **[Xevryll](https://github.com/xevryll)**")
                        appendln("Contributor: **[Gatt](https://github.com/RealGatt)**")
                        appendln("Commands: **$commandSize**")
                        appendln("Library: Java **[JDA 3](https://github.com/DV8FromTheWorld/JDA)**")
                    }
                }
            }
        }.rest().queue()
    }
}
