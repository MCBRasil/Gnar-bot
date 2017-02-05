package xyz.gnarbot.gnar.commands.executors.general

import com.google.inject.Inject
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.OnlineStatus
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.servers.Shard
import xyz.gnarbot.gnar.utils.Note
import java.util.*

@Command(aliases = arrayOf("info", "botinfo"), description = "Show information about GN4R-BOT.")
class BotInfoCommand : CommandExecutor() {

    @Inject lateinit var shard : Shard

    override fun execute(note: Note, args: List<String>) {
        val registry = shard.commandRegistry

        var textChannels = 0
        var guilds = 0

        var users = 0
        var offline = 0
        var online = 0
        var inactive = 0
        var others = 0

        for (shard in Bot.shards) {
            val jda = shard.jda

            guilds += jda.guilds.size

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
        }

        val commandSize = registry.uniqueExecutors.count { it -> it.isShownInHelp }

        val requests = Bot.shards
                .flatMap { it.hosts }
                .map { it.commandHandler.requests }
                .sum()

        val eb = EmbedBuilder()

        eb.setTitle("Bot Information")
        eb.setThumbnail(note.jda.selfUser.avatarUrl)
        eb.setColor(Bot.color)

        var joiner = StringJoiner("\n")

        eb.addField("Usage", "Requests: **[$requests]()**\nShards: **[" + Bot.shards.size + "]()**", true)
        eb.addField("Guilds", "Servers: **[$guilds]()**\nChannels: **[$textChannels]()**", true)

        joiner.add("Total: **[$users]()**")
        joiner.add("Online: **[$online]()**")
        joiner.add("Offline: **[$offline]()**")
        joiner.add("Inactive: **[$inactive]()**")
        joiner.add("Others: **[$others]()**")

        eb.addField("Users", joiner.toString(), true)

        joiner = StringJoiner("\n")

        joiner.add("Creators: **[Avarel](https://github.com/Avarel)** and **[Maeyrl](https://github.com/maeyrl)**")
        joiner.add("Contributor: **[Gatt](https://github.com/RealGatt)**")
        joiner.add("Website: **[gnarbot.xyz](https://gnarbot.xyz)**")
        joiner.add("Commands: **[$commandSize]()**")
        joiner.add("Library: **[JDA 3" + "](https://github.com/DV8FromTheWorld/JDA)**")

        eb.addField("Others", joiner.toString(), true)

        note.channel.sendMessage(eb.build()).queue()
    }
}
