package xyz.gnarbot.gnar.commands.executors.admin

import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.members.Level
import xyz.gnarbot.gnar.utils.Note

@Command(
        aliases = arrayOf("throwError"),
        level = Level.BOT_CREATOR,
        showInHelp = false
)
class ThrowError : CommandExecutor() {
    override fun execute(message: Note, args: List<String>) {
        throw RuntimeException("Requested to throw an error, so here you go.")
    }
}