package xyz.gnarbot.gnar.commands.executors.general

import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor

@Command(aliases = arrayOf("donate"), description = "Show the getBot's uptime.")
class DonateCommand : CommandExecutor() {
    override fun execute(message: Message, args: List<String>) {
        message.respond().embed("Donations") {
            color = Constants.COLOR
            description = "Want to donate to support Gnar? __**[Click here to donate.](https://gnarbot.xyz/donate)**__"
        }.rest().queue()
    }
}
