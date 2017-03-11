package xyz.gnarbot.gnar.commands.executors.admin

import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.members.Level
import xyz.gnarbot.gnar.utils.Note

@Command(
        aliases = arrayOf("restartbot"),
        description = "Restart bot..",
        level = Level.BOT_CREATOR,
        showInHelp = false
)
class RestartBotCommand : CommandExecutor() {
    override fun execute(note: Note, args: List<String>) {
        note.respond().embed("Restarting") {
            color = Constants.COLOR

            Bot.shards.forEach {
                it.clearServlets(true)
                it.shutdown(true)
            }

            description = "Bot is now restarting."
        }.rest().queue()
        System.exit(0)
    }
}