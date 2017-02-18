package xyz.gnarbot.gnar.commands.executors.admin

import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.members.Level
import xyz.gnarbot.gnar.utils.Note

@Command(
        aliases = arrayOf("shards", "shard", "shardinfo"),
        description = "Get shard information.",
        level = Level.BOT_CREATOR,
        showInHelp = false
)
class ShardInfoCommand : CommandExecutor() {
    override fun execute(note: Note, args: List<String>) {
        note.embed().apply {
            title("Shard Information")
            setColor(Bot.color)

            Bot.shards.forEach {
                field("Shard ${it.id}", true) {
                    "Status: **[${it.status}]()**\n" +
                    "Hosts: **[${it.guilds.size}]()**\n" +
                    "Users: **[${it.users.size}]()**\n" +
                    "Requests: **[${it.hosts.values.sumBy { it.commandHandler.requests }}]()**\n"
                }
            }
        }.respond()
    }
}