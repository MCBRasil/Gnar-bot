package xyz.gnarbot.gnar.commands.executors.general

import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.handlers.Category
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor

@Command(
        aliases = arrayOf("shards", "shard", "shardinfo"),
        description = "Get shard information.",
        category = Category.NONE
)
class ShardInfoCommand : CommandExecutor() {
    override fun execute(message: Message, args: List<String>) {
        message.respond().embed("Shard Information") {
            color = Constants.COLOR
            bot.shards.forEach {
                field("Shard ${it.id}", true) {
                    append("Status: ").appendln(it.status)
                    append("Guilds: ").appendln(it.guilds.size)
                    append("Users: ").appendln(it.users.size)
                    append("Requests: ").appendln(it.guildData.values.sumBy { it.commandHandler.requests })
                }
            }
        }.rest().queue()
    }
}