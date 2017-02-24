package xyz.gnarbot.gnar.commands.executors.general

import com.google.common.collect.Lists
import com.google.inject.Inject
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.commands.handlers.CommandRegistry
import xyz.gnarbot.gnar.members.Level
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("help", "guide"), usage = "~command", description = "Display GN4R's list of commands.")
class HelpCommand : CommandExecutor() {

    @Inject lateinit var registry: CommandRegistry

    override fun execute(note: Note, args: List<String>) {

        if (args.isNotEmpty()) {
            val target = if (args[0].startsWith('_')) args[0].substring(1) else args[0]

            val cmd: CommandExecutor? = registry.getCommand(target)

            if (cmd == null) {
                note.error("There is no command named `$target`. :cry:").queue()
                return
            }

            note.embed("Command Information") {
                color(Bot.color)

                field("Aliases", true, cmd.aliases.joinToString(separator = ", ${Bot.token}", prefix = Bot.token))
                field("Usage", true, "${Bot.token}${cmd.aliases[0].toLowerCase()} ${cmd.usage}")
                field("Level", true, cmd.level.title)
                field("Description", false, cmd.description)
            }.rest().queue()

            return
        }

        val executors = registry.uniqueExecutors

        val embed = note.embed("Documentation") {
            description("This is all of Gnar's currently registered commands.")

            for (level in Level.values()) {
                val sectionCount = executors.count { it.level == level && it.isShownInHelp }
                if (sectionCount < 1) continue

                val pages = Lists.partition(executors.filter { it.level == level && it.isShownInHelp }, sectionCount / 3 + 1)

                field("", false, "__**${level.title}** — ${sectionCount}__\n${level.requireText}")

                for (page in pages) {
                    field("", true) {
                        for (cmd in page) {
                            cmd.symbol?.let { append(it).append(' ') }
                            append("**[").append(Bot.token).append(cmd.aliases.first()).appendln("]()**")
                        }
                    }
                }
            }



            field("Additional Information") {
                append("To view a command's description, do `").append(Bot.token).appendln("help [command]`.")
                append("__The commands that requires a named role must be created by you and assigned to a member in your guild.__")
            }

            field("News") {
                append("• Music player now running LIVE. Report any bugs to us!\n")
                append("• To try out music, join a channel and type `_play -url|search YT`!\n")
                append("• The website is nearing completion!\n")2
                append("• Many general commands got a new layout, check it out!.\n")
            }

            field("Contact") {
                appendln(b(link("Website", "http://gnarbot.xyz")))
                append(b(link("Discord Server", "http://discord.gg/NQRpmr2")))
            }
        }.build()

        note.author.requestPrivateChannel().sendMessage(embed)?.queue()

        note.info("Gnar's guide has been directly messaged to you.").queue()
    }
}