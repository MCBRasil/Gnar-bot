package xyz.gnarbot.gnar.commands.general

import org.apache.commons.lang3.StringUtils
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.handlers.commands.Command
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor
import xyz.gnarbot.gnar.utils.Note
import xyz.hexav.aje.ExpressionBuilder
import java.awt.Color
import java.util.Arrays

@Command(aliases = arrayOf("math"), usage = "(expression)", description = "Calculate fancy math expressions.")
class MathCommand : CommandExecutor()
{
    override fun execute(msg : Note, label : String, args : Array<out String>)
    {
        val _args = args
        
        if (args.isEmpty())
        {
            msg.replyEmbedRaw("Error", "Please provide a math expression.", Color.RED)
            return
        }
        
        val exp = StringUtils.join(_args, " ")
        msg.replyEmbedRaw("Expression", exp, Bot.color)
        
        try
        {
            val results = ExpressionBuilder(exp)
                    .build()
                    .evalList()
            
            msg.replyEmbedRaw("Result", Arrays.toString(results), Bot.color)
        }
        catch (e : RuntimeException)
        {
            msg.replyEmbedRaw("Error", e.message!!, Color.RED)
        }
    }
}