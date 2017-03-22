package xyz.gnarbot.gnar.commands.executors.general

import com.google.common.collect.Lists
import com.google.inject.Inject
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.Constants
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

            val entry = registry.getEntry(target)

            if (entry == null) {
                note.respond().error("There is no command named `$target`. :cry:").queue()
                return
            }

            note.respond().embed("Command Information") {
                field("Aliases", true, entry.meta.aliases.joinToString(separator = ", ${Bot.token}", prefix = Bot.token))
                field("Usage", true, "${Bot.token}${entry.meta.aliases[0].toLowerCase()} ${entry.meta.usage}")
                field("Level", true, entry.meta.level.title)
                field("Description", false, entry.meta.description)
            }.rest().queue()

            return
        }

        val cmds = registry.entries

        note.author.requestPrivateChannel().send().embed("Documentation") {
            color = Constants.COLOR
            description = "This is all of Gnar's currently registered commands."

            for (level in Level.values()) {
                val filtered = cmds.filter {
                   it.meta.level == level && it.meta.showInHelp
                }
                if (filtered.isEmpty()) continue

                val pages = Lists.partition(filtered, filtered.size / 3 + 1)

                blankField(true)
                field("${level.title} — ${filtered.size}", false, level.requireText)

                for (page in pages) {
                    field("", true) {
                        page.forEach {
                            if (it.meta.symbol.isNotBlank()) {
                                append("**").append(it.meta.symbol).append("** ")
                            }
                            append("**[").append(Bot.token).append(it.meta.aliases[0]).appendln("]()**")
                        }
                    }
                }
            }

            blankField(true)
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
        }.rest().queue()

        note.respond().info("Gnar's guide has been directly messaged to you.").queue()
    }
}