package xyz.gnarbot.gnar.commands.executors.general

import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.handlers.*
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("uptime"), description = "Show the bot's uptime.")
class UptimeCommand : CommandExecutor()
{
    override fun execute(note : Note, label : String, args : Array<String>)
    {
        val s = Bot.uptime
        val m = s / 60
        val h = m / 60
        val d = h / 24
        
        note.replyEmbedRaw("Bot Uptime", "$d days, ${h % 24} hours, ${m % 60} minutes and ${s % 60} seconds")
    }
}
