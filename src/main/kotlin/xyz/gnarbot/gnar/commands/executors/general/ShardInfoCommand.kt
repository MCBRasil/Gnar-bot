package xyz.gnarbot.gnar.commands.executors.general

import com.google.common.collect.Lists
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
        var page = if (args.isNotEmpty()) {
            args[0].toIntOrNull() ?: 1
        } else {
            1
        }

        message.respond().embed("Shard Information") {
            color = Constants.COLOR

            val pages = Lists.partition(bot.shards, 12)

            if (page >= pages.size) page = pages.size
            else if (page <= 0) page = 1

            val shards = pages[page - 1]

            shards.forEach {
                field("Shard ${it.shardInfo.shardId}", true) {
                    append("Status: ").appendln(it.status)
                    append("Guilds: ").appendln(it.guilds.size)
                    append("Users: ").appendln(it.users.size)
                    append("Requests: ").appendln(it.guildData.values.sumBy { it.commandHandler.requests })
                }
            }

            footer = "Page [$page/${pages.size}]"
        }.rest().queue()
    }
}