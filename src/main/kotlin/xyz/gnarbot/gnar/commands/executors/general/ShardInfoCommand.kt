package xyz.gnarbot.gnar.commands.executors.general

import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.utils.Note

@Command(
        aliases = arrayOf("shards", "shard", "shardinfo"),
        description = "Get shard information.",
        showInHelp = false
)
class ShardInfoCommand : CommandExecutor() {
    override fun execute(note: Note, args: List<String>) {
        note.embed("Shard Information") {
            Bot.shards.forEach {
                field("Shard ${it.id}", true) {
                    append("Status: ").appendln(b(it.status))
                    append("Guilds: ").appendln(b(it.guilds.size))
                    append("Users: ").appendln(b(it.users.size))
                    append("Requests: ").appendln(b(it.servlets.values.sumBy { it.commandHandler.requests }))
                }
            }
        }.rest().queue()
    }
}