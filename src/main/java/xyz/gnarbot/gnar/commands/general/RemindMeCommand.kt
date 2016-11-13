package xyz.gnarbot.gnar.commands.general

import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.handlers.commands.Command
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor
import xyz.gnarbot.gnar.utils.BotData
import xyz.gnarbot.gnar.utils.Note
import java.util.concurrent.TimeUnit

@Command(aliases = arrayOf("remindme", "remind"), usage = "(#) (unit) (msg)")
class RemindMeCommand : CommandExecutor()
{
    override fun execute(message : Note, label : String, args : Array<String>)
    {
        if (args.size >= 3)
        {
            val string = args.copyOfRange(2, args.size).joinToString(" ")
            
            val time = try
            {
                args[0].toInt()
            }
            catch (e : NumberFormatException)
            {
                message.reply("The time number was not an integer.")
                return
            }
            
            val timeUnit = try
            {
                TimeUnit.valueOf(args[1].toUpperCase())
            }
            catch (e : IllegalArgumentException)
            {
                message.reply("The specified time unit was invalid.")
                return
            }
            
            if (time > 0)
            {
                message.reply("**${BotData.randomQuote()}** I'll be reminding you in __$time ${timeUnit.toString().toLowerCase()}__.")
                
                Bot.scheduler.schedule({
                    message.author?.privateChannel?.sendMessage("**REMINDER:** You requested to be reminded about this __$time ${timeUnit.toString().toLowerCase()}__ ago:\n```\n$string```")
                }, time.toLong(), timeUnit)
                
            }
            else
            {
                message.reply("Number must be more than 0.")
            }
        }
        else
        {
            message.reply("Insufficient amount of arguments.")
        }
    }
}