package xyz.gnarbot.gnar.commands.executors.admin

import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.members.Level
import xyz.gnarbot.gnar.utils.Note

@Command(
        aliases = arrayOf("gc"),
        description = "Run JavaScript commands.",
        level = Level.BOT_CREATOR,
        showInHelp = false
)
class GarbageCollectCommand : CommandExecutor() {
    override fun execute(note: Note, args: List<String>) {
        System.gc()

        note.info("Garbage collection request sent to JVM.")
        Bot.LOGGER.info("Garbage collection request sent to JVM.")
    }
}