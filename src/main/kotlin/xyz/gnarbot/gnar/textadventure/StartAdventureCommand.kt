package xyz.gnarbot.gnar.textadventure

import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.commands.Category
import xyz.gnarbot.gnar.commands.Command
import xyz.gnarbot.gnar.commands.CommandExecutor

@Command(
        aliases = arrayOf("startadventure"),
        usage = "[command]",
        description = "Start a text-based aventure!",
        category = Category.NONE
)
class StartAdventureCommand : CommandExecutor() {
    override fun execute(message: Message, args: Array<String>) {
        Adventure.getAdventure(message.author, message, bot)
        message.delete().queue()
    }
}