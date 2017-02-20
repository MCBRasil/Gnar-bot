package xyz.gnarbot.gnar.commands.executors.general

import org.apache.commons.lang3.StringUtils
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.utils.Note
import xyz.gnarbot.gnar.utils.fastSplit
import xyz.hexav.aje.AJEException
import xyz.hexav.aje.ExpressionBuilder
import java.awt.Color
import java.util.*

@Command(aliases = arrayOf("math"), usage = "-expression", description = "Calculate fancy math expressions.")
class MathCommand : CommandExecutor() {
    override fun execute(note: Note, args: List<String>) {
        if (args.isEmpty()) {
            note.respond("Error", "Please provide a math expression.", Color.RED)
            return
        }

        note.embed {
            setTitle("Math", null)
            setColor(Bot.color)

            val exp = ExpressionBuilder()
            val lines = StringUtils.join(args, ' ').fastSplit(';')
            lines.forEach { exp.addLine(it) }

            field("Expressions", true, "**[${lines.map(String::trim).joinToString("\n")}]()**")

            try {
                val results = exp
                        .build()
                        .evalList()

                field("Result", true, "**[${Arrays.toString(results)}]()**")
            } catch (e: AJEException) {
                field("Error", true, "**[${e.message!!}]()**")
                setColor(Color.RED)
            }
        }
    }
}