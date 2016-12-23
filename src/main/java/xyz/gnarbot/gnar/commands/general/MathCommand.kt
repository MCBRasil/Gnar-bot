package xyz.gnarbot.gnar.commands.general

import org.apache.commons.lang3.StringUtils
import xyz.gnarbot.gnar.handlers.commands.Command
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor
import xyz.gnarbot.gnar.utils.Note
import xyz.hexav.aje.ExpressionBuilder
import java.util.Arrays

@Command(aliases = arrayOf("math"), usage = "(expression)", description = "Calculate fancy math expressions.")
class MathCommand : CommandExecutor()
{
    override fun execute(msg : Note, label : String, args : Array<out String>)
    {
        val _args = args
        
        if (args.isEmpty())
        {
            msg.reply("Please provide a math expression.")
            return
        }
    
        val exp = StringUtils.join(_args, " ")
        msg.reply("Evaluating expression: `$exp` ...")
    
        try
        {
            val results = ExpressionBuilder(exp)
                    .build()
                    .evalList()
            
            msg.reply("Result: `${Arrays.toString(results)}`.")
        }
        catch (e : RuntimeException)
        {
            msg.reply("Encountered error: **${e.message}**.")
        }
    }
}