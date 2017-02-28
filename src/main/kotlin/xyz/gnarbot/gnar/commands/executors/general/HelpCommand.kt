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

            val cmd = registry.getCommand(target)

            if (cmd == null) {
                note.error("There is no command named `$target`. :cry:").queue()
                return
            }

            val meta = cmd.getAnnotation(Command::class.java)

            note.embed("Command Information") {
                field("Aliases", true, meta.aliases.joinToString(separator = ", ${Bot.token}", prefix = Bot.token))
                field("Usage", true, "${Bot.token}${meta.aliases[0].toLowerCase()} ${meta.usage}")
                field("Level", true, meta.level.title)
                field("Description", false, meta.description)
            }.rest().queue()

            return
        }

        val executors = registry.uniqueExecutors

        val embed = note.embed("Documentation") {
            description("This is all of Gnar's currently registered commands.")

            for (level in Level.values()) {
                val filtered = executors.filter {
                    val meta = it.getAnnotation(Command::class.java)
                    meta.level == level && meta.showInHelp
                }
                val sectionCount = filtered.size
                if (sectionCount < 1) continue

                val pages = Lists.partition(filtered, sectionCount / 3 + 1)

                addBlankField(true)
                field("${level.title} — $sectionCount", false, level.requireText)

                for (page in pages) {
                    field("", true) {
                        page.map {
                            it.getAnnotation(Command::class.java)
                                    //meta.symbol?.let { append(it).append(' ') }
                        }.forEach { append("**[").append(Bot.token).append(it.aliases[0]).appendln("]()**") }
                    }
                }
            }

            addBlankField(true)
            field("Additional Information") {
                append("To view a command's description, do `").append(Bot.token).appendln("help [command]`.")
                append("__The commands that requires a named role must be created by you and assigned to a member in your guild.__")
            }

            field("News") {
                appendln("• Music player now running LIVE. Report any bugs to us!")
                appendln("• To try out music, join a channel and type `_play -url|search YT`!")
                appendln("• The website is nearing completion!")
                appendln("• Many general commands got a new layout, check it out!.")
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