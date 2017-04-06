package xyz.gnarbot.gnar.commands.executors.admin

import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.handlers.Category
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import java.awt.Color
import javax.script.ScriptEngineManager
import javax.script.ScriptException

@Command(
        aliases = arrayOf("js", "runjs"),
        description = "Run JavaScript commands.",
        administrator = true,
        category = Category.NONE
)
class JavascriptCommand : CommandExecutor() {
    val blocked = arrayListOf("leave", "delete", "Guilds", "Token", "Channels", "voice",
            "remove", "ByName", "ById", "Controller", "Manager", "Permissions")

    override fun execute(message: Message, args: List<String>) {
        val engine = ScriptEngineManager().getEngineByName("javascript")

        engine.put("jda", jda)
        engine.put("message", message)
        engine.put("host", servlet)
        engine.put("channel", message.channel)

        val script = args.joinToString(" ")

        if (blocked.any { script.contains(it, true) }) {
            message.respond().error("JavaScript eval Expression may be malicious, canceling.").queue()
            return
        }

        message.respond().embed("JavaScript") {
            color = Constants.COLOR

            field("Running", false, script)
            field("Result", false, try {
                engine.eval(script)
            } catch (e: ScriptException) {
                color = Color.RED
                "The error `$e` occurred while executing the JavaScript statement."
            })
        }.rest().queue()


    }
}