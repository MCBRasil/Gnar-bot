package xyz.gnarbot.gnar.commands.executors.general

import com.google.inject.Inject
import net.dv8tion.jda.core.OnlineStatus
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.servers.Shard
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("info", "botinfo"), description = "Show information about GN4R-BOT.")
class BotInfoCommand : CommandExecutor() {

    @Inject lateinit var shard : Shard

    override fun execute(note: Note, args: List<String>) {
        val registry = shard.commandRegistry

        var uptimeMinutes = Bot.uptime / 1000 / 60
        if (uptimeMinutes == 0L) uptimeMinutes = 1L

        var textChannels = 0
        var voiceChannels = 0
        var activeHosts = 0 // wrapper
        var guilds = 0

        var users = 0
        var activePersons = 0 // wrapper
        var offline = 0
        var online = 0
        var inactive = 0

        for (shard in Bot.shards) {
            guilds += shard.guilds.size

            for (g in shard.guilds) {
                for (u in g.members) {
                    when (u.onlineStatus) {
                        OnlineStatus.ONLINE -> online++
                        OnlineStatus.OFFLINE -> offline++
                        OnlineStatus.IDLE -> inactive++
                        else -> {}
                    }
                }
            }

            shard.hosts.values.forEach { activePersons += it.peopleHandler.registry.size }

            users += shard.users.size
            textChannels += shard.textChannels.size
            voiceChannels += shard.voiceChannels.size
            activeHosts += shard.hosts.size
        }

        val commandSize = registry.uniqueExecutors.count { it -> it.isShownInHelp }

        val requests = Bot.shards
                .flatMap { it.hosts.values }
                .sumBy { it.commandHandler.requests }

        note.embed("Bot Information") {
            setColor(Bot.color)

            field("Requests", true, requests)
            field("Requests Per Minutes", true, requests / uptimeMinutes)
            field("Text Channels", true, textChannels)
            field("Voice Channels", true, voiceChannels)
            field("Guilds", true, guilds)
            field("Host Wrappers", true, activeHosts)

            field("Users", true) {
                append("Total: ").appendln(highlight(users))
                append("Online: ").appendln(highlight(online))
                append("Offline: ").appendln(highlight(offline))
                append("Inactive: ").appendln(highlight(inactive))
                append("**Wrappers:** ").appendln(highlight(activePersons))
            }

            field("Others", true) {
                appendln("Creators: **[Avarel](https://github.com/Avarel)** and **[Maeyrl](https://github.com/maeyrl)**")
                appendln("Contributor: **[Gatt](https://github.com/RealGatt)**")
                appendln("Website: **[gnarbot.xyz](https://gnarbot.xyz)**")
                appendln("Commands: **[$commandSize]()**")
                appendln("Library: **[JDA 3](https://github.com/DV8FromTheWorld/JDA)**")
            }
        }
    }
}
