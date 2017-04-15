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
        disableable = false,
        category = Category.MODERATION,
        permissions = arrayOf(Permission.ADMINISTRATOR)
)
class DisableCommand : CommandExecutor() {
    override fun execute(message: Message, args: Array<String>) {
        val disabled = args
                .map(guildData.commandHandler::disableCommand)
                .filterNotNull()
                .map { it.meta.aliases[0] }

        message.respond().embed("Disabling Commands") {
            color = Constants.COLOR
            description {
                if (disabled.isNotEmpty()) {
                    "Disabled `$disabled` command(s) on this server."
                } else {
                    "You didn't enter any enabled commands or commands that could be disabled."
                }
            }
        }.rest().queue()
    }
}