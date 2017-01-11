package xyz.gnarbot.gnar.commands.executors.admin

import org.apache.commons.lang3.StringUtils
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.members.Clearance
import xyz.gnarbot.gnar.utils.Note
import javax.script.ScriptEngineManager
import javax.script.ScriptException

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

        if (script.contains("leave") || script.contains("delete") || script.contains("Guilds") || script.contains("Token") || script.contains("Channels") || script.contains("voice") || script.contains("remove") || script.contains("ByName") || script.contains("ById") || script.contains("Controller") || script.contains("Manager") || script.contains("Permissions")) {
            message.error("JavaScript Eval Expression may be malicious, canceling.")
            return
        }
        
        message.replyEmbed("Java Script", "Running `$script`.")
        
        val result : Any? = try
        {
            engine.eval(script)
        }
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