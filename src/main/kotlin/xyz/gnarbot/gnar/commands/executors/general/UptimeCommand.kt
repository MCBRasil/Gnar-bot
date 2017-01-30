package xyz.gnarbot.gnar.commands.executors.general

import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("uptime"), description = "Show the bot's uptime.")
class UptimeCommand : CommandExecutor() {
    override fun execute(note: Note, args: Array<String>) {
        val s = Bot.uptime / 1000
        val m = s / 60
        val h = m / 60
        val d = h / 24

        note.replyEmbedRaw("Bot Uptime", "$d days, ${h % 24} hours, ${m % 60} minutes and ${s % 60} seconds")
    }
}
