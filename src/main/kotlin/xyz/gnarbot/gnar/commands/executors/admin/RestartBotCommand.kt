package xyz.gnarbot.gnar.commands.executors.admin

import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.Category
import xyz.gnarbot.gnar.commands.Command
import xyz.gnarbot.gnar.commands.CommandExecutor

@Command(
        aliases = arrayOf("restartbot"),
        description = "Restart bot.",
        administrator = true,
        category = Category.NONE
)
class RestartBotCommand : CommandExecutor() {
    override fun execute(message: Message, args: Array<String>) {
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