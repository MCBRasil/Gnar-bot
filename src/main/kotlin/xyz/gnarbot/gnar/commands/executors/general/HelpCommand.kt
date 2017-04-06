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
        usage = "~command",
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

                field("Aliases", true, entry.meta.aliases.joinToString(separator = ", ${bot.token}", prefix = bot.token))
                field("Usage", true, "${bot.token}${entry.meta.aliases[0].toLowerCase()} ${entry.meta.usage}")
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

        val privateChannel = if (!message.author.hasPrivateChannel()) {
            message.author.openPrivateChannel().complete()
        } else {
            message.author.privateChannel
        }

        privateChannel.send().embed("Documentation") {
            color = Constants.COLOR
            description = "This is all of Gnar's currently registered commands."

            for (category in Category.values()) {
                if (!category.show) continue

                val filtered = cmds.filter {
                   it.meta.category == category
                }
                if (filtered.isEmpty()) continue

                val pages = Lists.partition(filtered, filtered.size / 3 + 1)

                field(true)
                field("${category.title} — ${filtered.size}\n", false, category.description)

                for (page in pages) {
                    field("", true) {
                        page.forEach {
                            append("**[").append(bot.token).append(it.meta.aliases[0]).appendln("]()**")
                        }
                    }
                }
            }

            field(true)
            field("Additional Information") {
                append("To view a command's description, do `").append(bot.token).appendln("help [command]`.")
                append("__The commands that requires a named role must be created by you and assigned to a member in your guild.__")
            }

            field("News") {
                appendln("• Music is laggy, please donate for better quality.")
                appendln("• The League command will probably be removed")
                appendln("• Working on our own custom version of JDA")
                appendln("• Donation links fixed along with general buds")
            }

            field("Contact") {
                appendln(b(link("Website", "http://gnarbot.xyz")))
                append(b(link("Discord Server", "http://discord.gg/NQRpmr2")))
            }

            field("Donations") {
                appendln(b(link("Donate", "https://gnarbot.xyz/donate")))
            }
        }.rest().queue()

        message.respond().info("Gnar's guide has been directly messaged to you.\n\nNeed more support? Reach us on our __**[official support server](https://discord.gg/NQRpmr2)**__.").queue()
    }
}