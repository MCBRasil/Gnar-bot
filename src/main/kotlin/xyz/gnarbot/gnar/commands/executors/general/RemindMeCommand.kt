package xyz.gnarbot.gnar.commands.executors.general

import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.utils.schedule
import java.util.*
import java.util.concurrent.TimeUnit

@Command(aliases = arrayOf("remindme", "remind"), usage = "# -time_unit -msg")
class RemindMeCommand : CommandExecutor() {
    override fun execute(message: Message, args: List<String>) {
        if (args.size >= 3) {
            val string = args.subList(2, args.size).joinToString(" ")

            val time = try {
                args[0].toInt()
            } catch (e: NumberFormatException) {
                message.respond().error("The time number was not an integer.").queue()
                return
            }

            val timeUnit = try {
                TimeUnit.valueOf(args[1].toUpperCase())
            } catch (e: IllegalArgumentException) {
                message.respond().error("The specified time unit was invalid. \n`${Arrays.toString(TimeUnit.values())}`").queue()
                return
            }

            // todo change to new embed
            if (time > 0) {
                message.respond().embed("Reminder Scheduled") {
                    color = Constants.COLOR
                    description = "I'll be reminding you in __$time ${timeUnit.toString().toLowerCase()}__."
                }.rest().queue()

                bot.scheduler.schedule(time.toLong(), timeUnit) {
                    message.author.openPrivateChannel().queue {
                        it.send().embed("Reminder from $time ${timeUnit.toString().toLowerCase()} ago.") {
                            color = Constants.COLOR
                            description = string
                        }.rest().queue()
                    }
                }
            } else {
                message.respond().error("Number must be more than 0.").queue()
            }
        } else {
            message.respond().error("Insufficient amount of arguments. `(#) (unit) (msg)`").queue()
        }
    }
}
