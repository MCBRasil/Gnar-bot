package xyz.gnarbot.gnar.commands.executors.general

import b
import link
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor

@Command(aliases = arrayOf("donate"), description = "Show the getBot's uptime.")
class DonateCommand : CommandExecutor() {
    override fun execute(message: Message, args: Array<String>) {
        message.respond().embed("Donations") {
            color = Constants.COLOR
            description {
                appendln("Want to donate to support Gnar?")
                appendln(b(link("PayPal", "https://gnarbot.xyz/donate")))
                appendln(b(link("Patreon", "https://www.patreon.com/gnarbot")))
            }
        }.rest().queue()
    }
}
