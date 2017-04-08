package xyz.gnarbot.gnar.commands.executors.general

import b
import com.google.common.collect.Lists
import link
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.handlers.Category
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor

@Command(
        aliases = arrayOf("help", "guide"),
        usage = "(command)",
        description = "Display GN4R's list of commands."
)
class HelpCommand : CommandExecutor() {

    override fun execute(message: Message, args: List<String>) {
        val registry = bot.commandRegistry

        if (args.isNotEmpty()) {
            val target = if (args[0].startsWith('_')) args[0].substring(1) else args[0]

            val entry = registry.getEntry(target)

            if (entry == null) {
                message.respond().error("There is no command named `$target`. :cry:").queue()
                return
            }

            message.respond().embed("Command Information") {
                color = Constants.COLOR

                field("Aliases", true, entry.meta.aliases.joinToString(separator = ", ${bot.prefix}", prefix = bot.prefix))
                field("Usage", true, "${bot.prefix}${entry.meta.aliases[0].toLowerCase()} ${entry.meta.usage}")
                field(true)

                if (entry.meta.channelPermissions.isNotEmpty())
                    field("Channel Permission", true, entry.meta.channelPermissions.map(Permission::getName))

                if (entry.meta.voicePermissions.isNotEmpty())
                    field("Voice Permission", true, entry.meta.voicePermissions.map(Permission::getName))

                if (entry.meta.guildPermissions.isNotEmpty())
                    field("Guild Permission", true, entry.meta.guildPermissions.map(Permission::getName))

                field("Description", false, entry.meta.description)

            }.rest().queue()

            return
        }

        val cmds = registry.entries

        message.author.openPrivateChannel().queue {
            it.send().embed("Documentation") {
                color = Constants.COLOR
                description = "This is all of Gnar's currently registered commands."

                for (category in Category.values()) {
                    if (!category.show) continue

                    val filtered = cmds.filter {
                        it.meta.category == category
                    }
                    if (filtered.isEmpty()) continue

                    val pages = if (filtered.size % 3 == 0) {
                        Lists.partition(filtered, filtered.size / 3)
                    } else {
                        Lists.partition(filtered, filtered.size / 3 + 1)
                    }

                    field(true)
                    field("${category.title} — ${filtered.size}\n", false, category.description)

                    for (page in pages) {
                        field("", true) {
                            page.forEach {
                                append("**[").append(bot.prefix).append(it.meta.aliases[0]).appendln("]()**")
                            }
                        }
                    }
                }

                field(true)
                field("Additional Information") {
                    append("To view a command's description, do `").append(bot.prefix).appendln("help [command]`.")
                    append("__The commands that requires a named role must be created by you and assigned to a member in your guild.__")
                }

                field("News") {
                    appendln("• **Incomplete**: disable commands with `_enable | _disable | _listdisabled`.")
                    appendln("• No more role names! **Commands are now linked with Discord permissions.**")
                    appendln("  - DJ commands for instance, requires you to be in a voice channel and have the `Manage Channel` permission of the channel.")
                    appendln("  - Ban and kick commands requires the `Ban Member` and `Kick Member` permission respectively.")
                    appendln("• `_nowplaying` links to the original songs.")
                    appendln("• Optimizations, basically rewritten the bot's systems.")
                }

                field("Contact", true) {
                    appendln(b(link("Website", "http://gnarbot.xyz")))
                    appendln(b(link("Discord Server", "http://discord.gg/NQRpmr2")))
                }

                field("Donations", true) {
                    appendln(b(link("PayPal", "https://gnarbot.xyz/donate")))
                    appendln(b(link("Patreon", "https://www.patreon.com/gnarbot")))
                }
            }.rest().queue()
        }

        message.respond().info("Gnar's guide has been directly messaged to you.\n\nNeed more support? Reach us on our __**[official support server](https://discord.gg/NQRpmr2)**__.").queue()
    }
}