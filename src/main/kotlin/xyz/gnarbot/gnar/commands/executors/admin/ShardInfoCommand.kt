package xyz.gnarbot.gnar.commands.executors.admin

import net.dv8tion.jda.core.EmbedBuilder
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.handlers.*
import xyz.gnarbot.gnar.members.Clearance
import xyz.gnarbot.gnar.utils.Note
import java.util.StringJoiner

@Command(
        aliases = arrayOf("shards", "shard", "shardinfo"),
        description = "Get shard information.",
        clearance = Clearance.BOT_MASTER,
        showInHelp = false
)
class ShardInfoCommand : CommandExecutor()
{
    override fun execute(message : Note, label : String, args : Array<String>)
    {
        val eb = EmbedBuilder()
        
        Bot.shards.forEach {
            
            val sj = StringJoiner("\n")
            
            sj.add("Status: **[${it.jda.status}]()**")
            sj.add("Hosts: **[${it.jda.guilds.size}]()**")
            sj.add("Users: **[${it.jda.users.size}]()**")
            sj.add("Requests: **[${it.hosts.sumBy { it.commandHandler.requests }}]()**")
            
            eb.addField("Shard ${it.id}", sj.toString(), true)
        }
        
        eb.setTitle("Shard Information")
        eb.setColor(Bot.color)
        
        message.channel.sendMessage(eb.build()).queue()
    }
}