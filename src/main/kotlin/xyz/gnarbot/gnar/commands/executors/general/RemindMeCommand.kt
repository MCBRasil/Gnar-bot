package xyz.gnarbot.gnar.commands.executors.general

import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.utils.Note
import xyz.gnarbot.gnar.utils.makeEmbed
import java.util.*
import java.util.concurrent.TimeUnit

@Command(aliases = arrayOf("remindme", "remind"), usage = "(#) (unit) (msg)")
class RemindMeCommand : CommandExecutor() {
    override fun execute(note: Note, args: List<String>) {
        if (args.size >= 3) {
            val string = args.subList(2, args.size).joinToString(" ")

            val time = try {
                args[0].toInt()
            } catch (e: NumberFormatException) {
                note.error("The time number was not an integer.")
                return
            }

            val timeUnit = try {
                TimeUnit.valueOf(args[1].toUpperCase())
            } catch (e: IllegalArgumentException) {
                note.error("The specified time unit was invalid. \n`${Arrays.toString(TimeUnit.values())}`")
                return
            }

            if (time > 0) {
                note.replyEmbedRaw("Reminder Scheduled", "I'll be reminding you in __$time ${timeUnit.toString().toLowerCase()}__.")

                Bot.scheduler.schedule({
                    note.author.requestPrivateChannel()
                            .sendMessage(makeEmbed("Reminder from $time ${timeUnit.toString().toLowerCase()} ago.", string)).queue()
                }, time.toLong(), timeUnit)

            } else {
                note.error("Number must be more than 0.")
            }
        } else {
            note.error("Insufficient amount of arguments. `(#) (unit) (msg)`")
        }
    }
}
