package xyz.gnarbot.gnar.commands.general

import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.handlers.commands.Command
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor
import xyz.gnarbot.gnar.utils.Note
import xyz.gnarbot.gnar.utils.makeEmbed
import java.util.Arrays
import java.util.concurrent.TimeUnit

@Command(aliases = arrayOf("remindme", "remind"), usage = "(#) (unit) (msg)")
class RemindMeCommand : CommandExecutor()
{
    override fun execute(note : Note, label : String, args : Array<String>)
    {
        if (args.size >= 3)
        {
            val string = args.copyOfRange(2, args.size).joinToString(" ")
            
            val time = try
            {
                args[0].trim().toInt()
            }
            catch (e : NumberFormatException)
            {
                note.replyError("The time number was not an integer.")
                return
            }
            
            val timeUnit = try
            {
                TimeUnit.valueOf(args[1].trim().toUpperCase())
            }
            catch (e : IllegalArgumentException)
            {
                note.replyError("The specified time unit was invalid. \n`${Arrays.toString(TimeUnit.values())}`")
                return
            }
            
            if (time > 0)
            {
                note.replyEmbedRaw("Reminder Scheduled", "I'll be reminding you in __$time ${timeUnit.toString().toLowerCase()}__.")
                
                Bot.scheduler.schedule({
                    note.author?.requestPrivateChannel()?.sendMessage(
                            makeEmbed("Reminder from $time ${timeUnit.toString().toLowerCase()} ago.", string))
                            ?.queue()
                }, time.toLong(), timeUnit)
                
            }
            else
            {
                note.replyError("Number must be more than 0.")
            }
        }
        else
        {
            note.replyError("Insufficient amount of arguments. `(#) (unit) (msg)`")
        }
    }
}
