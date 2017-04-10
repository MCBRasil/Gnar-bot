package xyz.gnarbot.gnar.commands.executors.mod

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.Category
import xyz.gnarbot.gnar.commands.Command
import xyz.gnarbot.gnar.commands.CommandExecutor

@Command(
        aliases = arrayOf("disable"),
        usage = "[labels...]",
        description = "Disable commands.",
        category = Category.BETA,
        permissions = arrayOf(Permission.ADMINISTRATOR)
)
class DisableCommand : CommandExecutor() {
    override fun execute(message: Message, args: Array<String>) {
        val disabled = args.map {
            bot.commandRegistry.getEntry(it)?.let(guildData.commandHandler::disableCommand)
        }.filterNotNull()

        message.respond().embed("Disabling Commands") {
            color = Constants.COLOR
            description = if (disabled.isNotEmpty()) {
                "Disabled `$disabled`"
            } else {
                "You didn't enter any valid commands."
            }

            footer = "This command is not completed yet."
        }.rest().queue()
    }
}