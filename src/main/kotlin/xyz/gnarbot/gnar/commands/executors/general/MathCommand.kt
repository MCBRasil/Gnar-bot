package xyz.gnarbot.gnar.commands.executors.general

import org.apache.commons.lang3.StringUtils
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.utils.Note
import xyz.hexav.aje.ExpressionBuilder
import java.awt.Color
import java.util.*

@Command(aliases = arrayOf("math"), usage = "-expression", description = "Calculate fancy math expressions.")
class MathCommand : CommandExecutor() {
    override fun execute(note: Note, args: Array<String>) {
        val _args = args

        if (args.isEmpty()) {
            note.replyEmbedRaw("Error", "Please provide a math expression.", Color.RED)
            return
        }

        val exp = StringUtils.join(_args, " ")
        note.replyEmbedRaw("Expression", "```$exp```")

        try {
            val results = ExpressionBuilder(exp)
                    .build()
                    .evalList()

            note.replyEmbedRaw("Result", "```${Arrays.toString(results)}```")
        } catch (e: RuntimeException) {
            note.replyEmbedRaw("Error", e.message!!, Color.RED)
        }
    }
}