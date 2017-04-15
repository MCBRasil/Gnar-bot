package xyz.gnarbot.gnar.commands.executors.mod

import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.Category
import xyz.gnarbot.gnar.commands.Command
import xyz.gnarbot.gnar.commands.CommandExecutor

@Command(
        aliases = arrayOf("listdisabled"),
        disableable = false,
        category = Category.MODERATION,
        description = "List disabled commands."
)
class ListDisabledCommand : CommandExecutor() {
    override fun execute(message: Message, args: Array<String>) {
        message.respond().embed("Disabled Commands") {
            color = Constants.COLOR
            description {
                if (guildData.commandHandler.disabled.isEmpty()) {
                    "There isn't any command disabled on this server."
                }
                else buildString {
                    guildData.commandHandler.disabled.forEach {
                        append("• ")
                        appendln(it.meta.aliases.joinToString())
                    }
                }
            }
        }.rest().queue()
    }
}