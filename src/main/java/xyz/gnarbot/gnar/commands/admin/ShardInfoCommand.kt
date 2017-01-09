package xyz.gnarbot.gnar.commands.admin

import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.handlers.commands.*
import xyz.gnarbot.gnar.handlers.members.Clearance
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
        val sj = StringJoiner("\n")
    
        Bot.shards.forEach {
            sj.add("__**Shard ${it.id}                                                       **__")
            sj.add("  Status: **[${it.jda.status}]()**")
            sj.add("  Hosts: **[${it.jda.guilds.size}]()**")
        }
        
        message.replyEmbed("Shard Information", sj.toString())
    }
}