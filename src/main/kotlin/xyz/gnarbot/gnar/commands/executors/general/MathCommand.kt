package xyz.gnarbot.gnar.commands.executors.general

import b
import net.dv8tion.jda.core.entities.Message
import org.apache.commons.lang3.StringUtils
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.utils.Utils
import xyz.hexav.aje.AJEException
import xyz.hexav.aje.ExpressionBuilder
import java.awt.Color
import java.util.*

@Command(aliases = arrayOf("math"), usage = "-expression", description = "Calculate fancy math expressions.")
class MathCommand : CommandExecutor() {
    override fun execute(message: Message, args: List<String>) {
        if (args.isEmpty()) {
            message.respond().error("Please provide a math expression.").queue()
            return
        }

        message.respond().embed("Math") {
            color = Constants.COLOR

            val exp = ExpressionBuilder()
            val lines = Utils.fastSplit(StringUtils.join(args, ' '), ';')
            lines.forEach { exp.addLine(it) }

            field("Expressions", true, b(lines.map(String::trim).joinToString("\n")))

            try {
                val results = exp
                        .build()
                        .evalList()

                field("Result", true, b(Arrays.toString(results)))
            } catch (e: AJEException) {
                field("Error", true, e.message)
                color = Color.RED
            }
        }.rest().queue()
    }
}