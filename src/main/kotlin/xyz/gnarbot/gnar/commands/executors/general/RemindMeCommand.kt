package xyz.gnarbot.gnar.commands.executors.general

import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.utils.Note
import java.util.*
import java.util.concurrent.TimeUnit

@Command(aliases = arrayOf("remindme", "remind"), usage = "# -time_unit -msg")
class RemindMeCommand : CommandExecutor() {
    override fun execute(note: Note, args: List<String>) {
        if (args.size >= 3) {
            val string = args.subList(2, args.size).joinToString(" ")

            val time = try {
                args[0].toInt()
            } catch (e: NumberFormatException) {
                note.respond().error("The time number was not an integer.").queue()
                return
            }

            val timeUnit = try {
                TimeUnit.valueOf(args[1].toUpperCase())
            } catch (e: IllegalArgumentException) {
                note.respond().error("The specified time unit was invalid. \n`${Arrays.toString(TimeUnit.values())}`").queue()
                return
            }

            // todo change to new embed
            if (time > 0) {
                note.respond().embed("Reminder Scheduled") {
                    color = Constants.COLOR
                    description = "I'll be reminding you in __$time ${timeUnit.toString().toLowerCase()}__."
                }.rest().queue()

                Bot.scheduler.schedule({
                    note.author.requestPrivateChannel().send().embed("Reminder from $time ${timeUnit.toString().toLowerCase()} ago.") {
                        color = Constants.COLOR
                        description = string
                    }.rest().queue()

                }, time.toLong(), timeUnit)
            } else {
                note.respond().error("Number must be more than 0.").queue()
            }
        } else {
            note.respond().error("Insufficient amount of arguments. `(#) (unit) (msg)`").queue()
        }
    }
}
