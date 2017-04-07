package xyz.gnarbot.gnar.commands.executors.mod

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.handlers.Category
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor

@Command(
        aliases = arrayOf("enable"),
        usage = "[labels...]",
        description = "Enable commands.",
        category = Category.BETA,
        guildPermissions = arrayOf(Permission.ADMINISTRATOR)
)
class EnableCommand : CommandExecutor() {
    override fun execute(message: Message, args: List<String>) {
        args.forEach(guildData.commandHandler::enableCommand)

        message.respond().embed("Enabling Commands") {
            description = "Enabled `$args`"
            color = Constants.COLOR
        }.rest().queue()
    }
}