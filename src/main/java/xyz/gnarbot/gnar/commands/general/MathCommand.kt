package xyz.gnarbot.gnar.commands.general

import org.apache.commons.lang3.StringUtils
import xyz.gnarbot.gnar.handlers.commands.Command
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor
import xyz.gnarbot.gnar.utils.Expression
import xyz.gnarbot.gnar.utils.Note
import java.text.DecimalFormat

@Command(aliases = arrayOf("math"), usage = "(expression)", description = "Calculate fancy math expressions.")
class MathCommand : CommandExecutor()
{
    private val formatter = DecimalFormat()
            .apply { isDecimalSeparatorAlwaysShown = false }
    
    override fun execute(msg : Note, label : String, args : Array<out String>)
    {
        var _args = args
        var flag = false
        
        if (args.size == 0)
        {
            msg.reply("Please provide a math expression.")
            return
        }
        
        if (args.size > 1 && args[0].startsWith("-"))
        {
            flag = true
            _args = args.copyOfRange(1, args.size)
        }
    
        val exp = StringUtils.join(_args, " ")
        val _reply = msg.reply("Evaluating expression `$exp` ...")
    
        try
        {
            val result = Expression(exp).eval()
            
            msg.apply()
            {
                if (flag)
                {
                    when (args[0])
                    {
                        "-bool", "-boolean" ->
                        {
                            //_reply.updateAppend("as a `Boolean`.")
                            reply("Final answer: `${if (result == 0.0) false else true}`")
                            return@apply
                        }
                        "-int", "-integer" ->
                        {
                            //_reply.updateAppend(" as an `Integer`.")
                            reply("Final answer: `${result.toInt()}`")
                            return@apply
                        }
                    }
                }
                reply("Final answer: `${formatter.format(result)}`")
            }
        }
        catch (e : RuntimeException)
        {
            msg.reply("Encountered error: **${e.message}**.")
        }
    }
}