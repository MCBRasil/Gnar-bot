package xyz.gnarbot.gnar.textadventure

import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor

@Command(aliases = arrayOf("startadventure"), usage = "[command]", description = "Start a text-based aventure!")
class StartAdventureCommand : CommandExecutor() {
    override fun execute(message: Message, args: MutableList<String>) {
        Adventure.getAdventure(message.author, message, bot)
        message.delete().queue()
    }
}