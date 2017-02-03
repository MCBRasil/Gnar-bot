package xyz.gnarbot.gnar.commands.executors.general

import net.dv8tion.jda.core.EmbedBuilder
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.members.BotPermission
import xyz.gnarbot.gnar.utils.Note
import java.util.*

@Command(aliases = arrayOf("help", "guide"), usage = "~command", description = "Display GN4R's list of commands.")
class HelpCommand : CommandExecutor() {
    override fun execute(note: Note, args: MutableList<String>) {
        val host = note.host

        val registry = host.shard.commandRegistry

        if (args.isNotEmpty()) {


            val cmd: CommandExecutor? = registry.getCommand(args[0])

            if (cmd == null) {
                note.error("There is no command named `${args[0]}`. :cry:")
                return
            }

            val aliases = registry.commandMap.entries
                    .filter { it.value == cmd }
                    .map { it.key }

            val joiner = StringJoiner("\n").apply {
                add("Description: **[${cmd.description}]()**")
                if (!cmd.usage.isNullOrBlank())
                    add("Usage: **[${Bot.token}${args[0].toLowerCase()} ${cmd.usage}]()**")
                add("Aliases: **[${aliases.joinToString(", ")}]()**")
            }

            note.replyEmbedRaw("Command Information", joiner.toString())

            return
        }

        val cmds = registry.uniqueExecutors

        val eb = EmbedBuilder()
        eb.setTitle("Gnar Documentation")
        eb.setDescription("This is all of GN4R-Bot's currently registered commands on the __**${host.guild.name}**__ guild.\n\n")
        eb.setColor(Bot.color)

        for (perm in BotPermission.values()) {
            val sectionCount = cmds.count { it.botPermission == perm && it.isShownInHelp }

            if (sectionCount < 1) continue

            eb.addField("", "__**${perm.title}** $sectionCount __", false)

            var joiner = StringJoiner("\n")
            var count = 0

            val rows = sectionCount / 3

            for (cmd in cmds) {
                if (cmd.botPermission != perm || !cmd.isShownInHelp) continue

                count++

                if (cmd.symbol != null) {
                    joiner.add("${cmd.symbol} **[${Bot.token}${cmd.aliases.first()}]()**")
                } else {
                    joiner.add("**[${Bot.token}${cmd.aliases.first()}]()**")
                }

                if (count > rows) {
                    eb.addField("", joiner.toString(), true)
                    joiner = StringJoiner("\n")
                    count = 0
                }
            }

            if (count > 0) {
                eb.addField("", joiner.toString(), true)
            }

        }

        val builder = StringBuilder()

        builder.append("To view a command's description, do `${Bot.token}help [command]`.\n\n")
        //builder.append("You can also chat and execute commands with Gnar privately, try it!\n\n")

        builder.append("**DJ** commands requires a role named exactly __[DJ]()__.\n")
        builder.append("**Bot Commander** commands requires a role named exactly __[Bot Commander]()__.\n")
        builder.append("**Server Owner** commands requires you to be the __[server owner]()__ to execute.\n")

        builder.append("\n")

        builder.append("**Latest News:**\n")
        builder.append(" - Music player now running LIVE. Report any bugs to us!\n")

        builder.append("\n")

        builder.append("**[Website](http://gnarbot.xyz)**\n")
        builder.append("**[Discord Server](http://discord.gg/NQRpmr2)** \n")

        eb.addField("", builder.toString(), false)

        //message.reply(builder.toString())

        val embed = eb.build()

        note.author.requestPrivateChannel().sendMessage(embed)?.queue()

        note.info("Gnar's guide has been directly messaged to you.")
    }
}