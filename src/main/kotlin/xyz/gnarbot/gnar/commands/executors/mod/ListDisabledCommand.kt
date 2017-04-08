package xyz.gnarbot.gnar.commands.executors.mod

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.handlers.Category
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor

@Command(
        aliases = arrayOf("listdisabled"),
        description = "List disabled commands.",
        category = Category.BETA,
        guildPermissions = arrayOf(Permission.ADMINISTRATOR)
)
class ListDisabledCommand : CommandExecutor() {
    override fun execute(message: Message, args: List<String>) {
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