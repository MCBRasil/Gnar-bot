package xyz.gnarbot.gnar.commands.executors.admin

import net.dv8tion.jda.core.EmbedBuilder
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.members.Level
import xyz.gnarbot.gnar.utils.Note
import java.util.*

@Command(
        aliases = arrayOf("shards", "shard", "shardinfo"),
        description = "Get shard information.",
        level = Level.BOT_MASTER,
        showInHelp = false
)
class ShardInfoCommand : CommandExecutor() {
    override fun execute(note: Note, args: MutableList<String>) {
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

        note.channel.sendMessage(eb.build()).queue()
    }
}