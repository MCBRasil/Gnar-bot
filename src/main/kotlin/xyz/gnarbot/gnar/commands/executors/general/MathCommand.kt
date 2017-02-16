package xyz.gnarbot.gnar.commands.executors.general

import net.dv8tion.jda.core.EmbedBuilder
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

        val exp = ExpressionBuilder()

        val lines = StringUtils.join(args, ' ').fastSplit(';')
        val eb = EmbedBuilder()

        lines.forEach { exp.addLine(it) }

        eb.setTitle("Math", null)
        eb.addField("Expressions", "**[${lines.map(String::trim).joinToString("\n")}]()**", true)
        eb.setColor(Bot.color)

        try {
            val results = exp
                    .build()
                    .evalList()

            eb.addField("Result", "**[${Arrays.toString(results)}]()**", true)
        } catch (e: AJEException) {
            eb.addField("Error", "**[${e.message!!}]()**", true)
            eb.setColor(Color.RED)
        }

        note.channel.sendMessage(eb.build()).queue()
    }
}