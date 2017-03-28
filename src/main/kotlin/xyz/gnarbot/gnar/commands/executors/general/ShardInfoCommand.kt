package xyz.gnarbot.gnar.commands.executors.general

import xyz.gnarbot.gnar.Constants
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
        note.respond().embed("Shard Information") {
            color = Constants.COLOR
            bot.shards.forEach {
                field("Shard ${it.id}", true) {
                    append("Status: ").appendln(it.status)
                    append("Guilds: ").appendln(it.guilds.size)
                    append("Users: ").appendln(it.users.size)
                    append("Requests: ").appendln(it.servlets.values.sumBy { it.commandHandler.requests })
                }
            }
        }.rest().queue()
    }
}