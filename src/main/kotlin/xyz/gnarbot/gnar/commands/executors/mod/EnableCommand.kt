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
        disableable = false,
        category = Category.MODERATION,
        permissions = arrayOf(Permission.ADMINISTRATOR)
)
class EnableCommand : CommandExecutor() {
    override fun execute(message: Message, args: Array<String>) {
        val enabled = args
                .map(guildData.commandHandler::enableCommand)
                .filterNotNull()
                .map { it.meta.aliases[0] }

        message.respond().embed("Enabling Commands") {
            color = Constants.COLOR
            description {
                if (enabled.isNotEmpty()) {
                    "Enabled `$enabled` command(s) on this server."
                } else {
                    "You didn't enter any disabled commands or commands that could be disabled."
                }
            }
        }.rest().queue()
    }
}