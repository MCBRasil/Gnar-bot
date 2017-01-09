package xyz.gnarbot.gnar.commands.admin

import org.apache.commons.lang3.StringUtils
import xyz.gnarbot.gnar.handlers.commands.*
import xyz.gnarbot.gnar.handlers.members.Clearance
import xyz.gnarbot.gnar.utils.Note
import javax.script.*

@Command(
        aliases = arrayOf("js", "runjs"),
        description = "Run JavaScript commands.",
        clearance = Clearance.BOT_MASTER,
        showInHelp = false
)
class JavascriptCommand : CommandExecutor()
{
    override fun execute(message : Note, label : String, args : Array<out String>?)
    {
        val engine = ScriptEngineManager().getEngineByName("javascript")
        
        engine.put("jda", message.jda)
        engine.put("message", message)
        engine.put("host", message.host)
        engine.put("channel", message.channel)
        
        val script = StringUtils.join(args, " ")
        
        message.replyEmbed("Java Script","Running `$script`.")
        
        val result : Any? = try { engine.eval(script) }
        catch (e : ScriptException)
        {
            message.error("The error `$e` occurred while executing the JavaScript statement.")
            return
        }
        
        if (result != null)
        {
            if (result.javaClass == Int::class.javaObjectType
                    || result.javaClass == Double::class.javaObjectType
                    || result.javaClass == Float::class.javaObjectType
                    || result.javaClass == String::class.javaObjectType
                    || result.javaClass == Boolean::class.javaObjectType)
            {
                message.replyEmbed("Result", result.toString())
            }
        }
    }
}