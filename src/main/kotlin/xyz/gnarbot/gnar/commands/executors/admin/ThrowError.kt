package xyz.gnarbot.gnar.commands.executors.admin

import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.commands.Category
import xyz.gnarbot.gnar.commands.Command
import xyz.gnarbot.gnar.commands.CommandExecutor

@Command(
        aliases = arrayOf("throwError"),
        administrator = true,
        category = Category.NONE
)
class ThrowError : CommandExecutor() {
    override fun execute(message: Message, args: Array<String>) {
        throw RuntimeException("Requested to throw an error, so here you go.")
    }
}