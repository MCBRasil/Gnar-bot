package xyz.gnarbot.gnar.commands.executors.mod

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.Category
import xyz.gnarbot.gnar.commands.Command
import xyz.gnarbot.gnar.commands.CommandExecutor

@Command(
        aliases = arrayOf("enable"),
        usage = "[labels...]",
        description = "Enable commands.",
        category = Category.BETA,
        permissions = arrayOf(Permission.ADMINISTRATOR)
)
class EnableCommand : CommandExecutor() {
    override fun execute(message: Message, args: Array<String>) {
        val enabled = args.map {
            bot.commandRegistry.getEntry(it)?.let(guildData.commandHandler::enableCommand)
        }.filterNotNull()

        message.respond().embed("Enabling Commands") {
            color = Constants.COLOR
            description = if (enabled.isNotEmpty()) {
                "Enabled `$enabled`"
            } else {
                "You didn't enter any valid commands."
            }
            footer = "This command is not completed yet."
        }.rest().queue()
    }
}