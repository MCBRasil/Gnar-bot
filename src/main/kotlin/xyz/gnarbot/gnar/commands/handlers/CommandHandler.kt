package xyz.gnarbot.gnar.commands.handlers

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.exceptions.PermissionException
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.members.Client
import xyz.gnarbot.gnar.servers.Servlet
import xyz.gnarbot.gnar.utils.Note
import xyz.gnarbot.gnar.utils.Utils

class CommandHandler(private val servlet: Servlet, private val bot: Bot) {
    val disabled: MutableList<CommandRegistry.CommandEntry> = mutableListOf()

    val commandRegistry get() = ArrayList(bot.commandRegistry.entries).apply { removeAll(disabled) }

    /**
     * @return the amount of successful requests on this command handler.
     */
    var requests = 0
        private set

    /**
     * Call the command based on the message content.
     *
     * @param message Message object.
     * @param content String content of the message.
     * @param author  Author of the message.
     */
    fun callCommand(message: Message, content: String, author: Client) {
        if (!content.startsWith(bot.token)) return

        // Tokenize the message.
        val tokens = Utils.fastSplit(content, ' ')

        val label = tokens[0].substring(bot.token.length).toLowerCase()

        val args = tokens.subList(1, tokens.size)

        val note = Note(servlet, message)

        val entry = bot.commandRegistry.getEntry(label) ?: return

        if (disabled.contains(entry)) return

        val cls = entry.cls

        val meta = entry.meta

        if (meta.channelPermissions.isNotEmpty()) {
            if (note.author.hasPermission(note.textChannel, *meta.channelPermissions)) {
                val requirement = meta.channelPermissions.map(Permission::name)
                note.respond().error("You lack the following permissions `$requirement`.")
                return
            }
        }
        if (meta.guildPermissions.isNotEmpty()) {
            if (note.author.hasPermission(*meta.guildPermissions)) {
                val requirement = meta.guildPermissions.map(Permission::name)
                note.respond().error("You lack the following permissions `$requirement`.")
                return
            }
        }

        if (meta.level.value > author.level.value) {
            note.respond().error("Insufficient bot level.\n${meta.level.requirement}")
            return
        }

        try {
            requests++
            val cmd = cls.newInstance()

            cmd.jda = servlet.jda
            cmd.shard = servlet.shard
            cmd.servlet = servlet
            cmd.bot = bot
            cmd.commandMeta = meta

            cmd.execute(note, args)
        } catch (e: PermissionException) {
            note.respond().error("The bot lacks the permission `"
                    + e.permission.getName() + "` required to perform this command.").queue()
        } catch (e: RuntimeException) {
            note.respond().error("**Exception**: " + e.message).queue()
            e.printStackTrace()
        }
    }

    /**
     * Enable the command [cmd].
     *
     * @param cmd Command entry.
     */
    fun enableCommand(cmd: CommandRegistry.CommandEntry) {
        disabled -= cmd
    }

    /**
     * Enable the command named [label].
     *
     * @param label Command label.
     */
    fun enableCommand(label: String) {
        enableCommand(bot.commandRegistry.getEntry(label))
    }

    /**
     * Disable the command [cmd].
     *
     * @param cmd Command entry.
     */
    fun disableCommand(cmd: CommandRegistry.CommandEntry) {
        disabled += cmd
    }

    /**
     * Enable the command named [label].
     *
     * @param label Command label.
     */
    fun disableCommand(label: String) {
        disableCommand(bot.commandRegistry.getEntry(label))
    }
}
