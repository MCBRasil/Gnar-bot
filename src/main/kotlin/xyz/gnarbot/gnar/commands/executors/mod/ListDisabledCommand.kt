package xyz.gnarbot.gnar.commands.executors.mod

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.Category
import xyz.gnarbot.gnar.commands.Command
import xyz.gnarbot.gnar.commands.CommandExecutor

@Command(
        aliases = arrayOf("listdisabled"),
        description = "List disabled commands.",
        category = Category.BETA,
        permissions = arrayOf(Permission.ADMINISTRATOR)
)
class ListDisabledCommand : CommandExecutor() {
    override fun execute(message: Message, args: Array<String>) {
        message.respond().embed("Disabled Commands") {
            color = Constants.COLOR
            description {
                guildData.commandHandler.disabled.forEach {
                    append("• ")
                    appendln(it.meta.aliases.joinToString())
                }
            }
            footer = "This command is not completed yet."
        }.rest().queue()
    }
}