package xyz.gnarbot.gnar.commands.executors.admin

import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.members.Level
import xyz.gnarbot.gnar.utils.Note
import javax.script.ScriptEngineManager
import javax.script.ScriptException

@Command(
        aliases = arrayOf("js", "runjs"),
        description = "Run JavaScript commands.",
        level = Level.BOT_CREATOR,
        showInHelp = false
)
class JavascriptCommand : CommandExecutor() {
    val blocked = arrayListOf("leave", "delete", "Guilds", "Token", "Channels", "voice",
            "remove", "ByName", "ById", "Controller", "Manager", "Permissions")

    override fun execute(note: Note, args: List<String>) {
        val engine = ScriptEngineManager().getEngineByName("javascript")

        engine.put("jda", note.jda)
        engine.put("message", note)
        engine.put("host", note.servlet)
        engine.put("channel", note.channel)

        val script = args.joinToString(" ")

        if (blocked.any { script.contains(it, true) }) {
            note.error("JavaScript eval Expression may be malicious, canceling.").queue()
            return
        }

        val result: Any? = try {
            engine.eval(script)
        } catch (e: ScriptException) {
            note.error("The error `$e` occurred while executing the JavaScript statement.").queue()
            return
        }

        if (result != null) {
            note.embed("JavaScript") {
                field("Running", false, script)
                field("Result", false, result)
            }.rest().queue()
        }
    }
}