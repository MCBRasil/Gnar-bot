package xyz.gnarbot.gnar.commands.executors.general

import net.dv8tion.jda.core.OnlineStatus
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.utils.Note
import java.util.*

@Command(aliases = arrayOf("info", "botinfo"), description = "Show information about GN4R-BOT.")
class BotInfoCommand : CommandExecutor() {
    override fun execute(note: Note, args: Array<String>) {
        val commandHandler = note.host.commandHandler

        var textChannels = 0
        var voiceChannels = 0
        var servers = 0

        var users = 0
        var offline = 0
        var online = 0
        var inactive = 0
        var others = 0

        for (shard in Bot.shards) {
            val jda = shard.jda

            servers += jda.guilds.size

            for (g in jda.guilds) {
                for (u in g.members) {
                    when (u.onlineStatus) {
                        OnlineStatus.ONLINE -> online++
                        OnlineStatus.OFFLINE -> offline++
                        OnlineStatus.IDLE -> inactive++
                        else -> others++
                    }
                }
            }

            users += jda.users.size
            textChannels += jda.textChannels.size
            voiceChannels += jda.voiceChannels.size
        }

        val commandSize = commandHandler.uniqueExecutors.count { it -> it.isShownInHelp }

        val requests = Bot.shards
                .flatMap { it.hosts }
                .map { it.commandHandler.requests }
                .sum()

        val joiner = StringJoiner("\n")

        //         "__**General**                                                      __"

        joiner.add("Requests: **[$requests]()**")
        joiner.add("Servers: **[$servers]()**")
        joiner.add("Shards: **[" + Bot.shards.size + "]()**")
        joiner.add("")
        joiner.add("__**Channels**                                                     __")
        joiner.add("  Text: **[$textChannels]()**")
        joiner.add("  Voice: **[$voiceChannels]()**")
        joiner.add("")
        joiner.add("__**Users**                                                          __")
        joiner.add("  Total: **[$users]()**")
        joiner.add("  Online: **[$online]()**")
        joiner.add("  Offline: **[$offline]()**")
        joiner.add("  Inactive: **[$inactive]()**")
        joiner.add("  Others: **[$others]()**")
        joiner.add("")
        joiner.add("__**Others**                                                         __")
        joiner.add("  Creators: **[Avarel](https://github.com/Avarel)** and **[Maeyrl](https://github.com/maeyrl)**")
        joiner.add("  Contributor: **[Gatt](https://github.com/RealGatt)**")
        joiner.add("  Website: **[gnarbot.xyz](https://gnarbot.xyz)**")
        joiner.add("  Commands: **[$commandSize]()**")
        joiner.add("  Library: **[JDA 3" + "](https://github.com/DV8FromTheWorld/JDA)**")

        note.replyEmbedRaw("Bot Information", joiner.toString(), Bot.color, note.jda.selfUser.avatarUrl)
    }
}
