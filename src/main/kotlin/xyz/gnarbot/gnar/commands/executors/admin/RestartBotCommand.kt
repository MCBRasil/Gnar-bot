package xyz.gnarbot.gnar.commands.executors.admin

import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.handlers.Category
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor

@Command(
        aliases = arrayOf("restartbot"),
        description = "Restart bot.",
        administrator = true,
        category = Category.NONE
)
class RestartBotCommand : CommandExecutor() {
    override fun execute(message: Message, args: List<String>) {
        message.respond().embed("Restarting") {
            color = Constants.COLOR

            bot.shards.forEach {
                it.clearData(true)
                it.shutdown(true)
            }

            description = "Bot is now restarting."
        }.rest().queue()
        System.exit(0)
    }
}