package xyz.gnarbot.gnar.commands.general

import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.MessageBuilder
import org.apache.commons.lang3.StringUtils
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.handlers.commands.Command
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor
import xyz.gnarbot.gnar.utils.Note
import xyz.hexav.aje.ExpressionBuilder
import java.awt.Color
import java.util.*

@Command(aliases = arrayOf("math"), usage = "(expression)", description = "Calculate fancy math expressions.")
class MathCommand : CommandExecutor()
{
    override fun execute(msg : Note, label : String, args : Array<out String>)
    {
        val _args = args
        
        if (args.isEmpty())
        {
            msg.replyEmbedRaw("Error", "Please provide a math expression.", Color.RED)
            return
        }
        
        val exp = StringUtils.join(_args, " ")
        val eb = EmbedBuilder()
        eb.setTitle("Expression")
        eb.setDescription(exp)
        eb.setColor(Color.WHITE)
        val embed = eb.build()
        val mb = MessageBuilder()
        mb.setEmbed(embed)
        val m = mb.build()
        msg.channel.sendMessage(m).queue()
        
        try
        {
            val results = ExpressionBuilder(exp)
                    .build()
                    .evalList()
            eb.addField("Result", Arrays.toString(results), false)
            val embed = eb.build()
            val mb = MessageBuilder()
            mb.setEmbed(embed)
            val m = mb.build()
            msg.channel.sendMessage(m).queue()

            m.deleteMessage().queue()
        }
        catch (e : RuntimeException)
        {
            e.printStackTrace()
            msg.replyEmbedRaw("Error", e.message!!, Color.RED)
        }
    }
}