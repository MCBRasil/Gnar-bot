package xyz.gnarbot.gnar.commands.executors.admin

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
class JavascriptCommand : CommandExecutor() {
    val blocked = arrayListOf("leave", "delete", "Guilds", "Token", "Channels", "voice",
            "remove", "ByName", "ById", "Controller", "Manager", "Permissions")

    override fun execute(note: Note, args: MutableList<String>) {
        val engine = ScriptEngineManager().getEngineByName("javascript")

        engine.put("jda", note.jda)
        engine.put("message", note)
        engine.put("host", note.host)
        engine.put("channel", note.channel)

        val script = args.joinToString(" ")

        if (blocked.any { script.contains(it, true) }) {
            note.error("JavaScript Eval Expression may be malicious, canceling.")
            return
        }

        val result: Any? = try {
            engine.eval(script)
        } catch (e: ScriptException) {
            note.error("The error `$e` occurred while executing the JavaScript statement.")
            return
        }

        if (result != null) {
            note.replyEmbed("Java Script", "Running `$script`.\n\n**Result:**\n\n" + result.toString())
        }
    }
}