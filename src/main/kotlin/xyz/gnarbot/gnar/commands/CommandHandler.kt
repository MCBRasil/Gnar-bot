package xyz.gnarbot.gnar.commands

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.exceptions.PermissionException
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.guilds.GuildData
import xyz.gnarbot.gnar.utils.Utils
import java.util.*

class CommandHandler(private val guildData: GuildData, private val bot: Bot) {

    /** @returns Enabled command entries. */
    val enabled get() = bot.commandRegistry.entries.apply { removeAll(disabled) }

    /** @returns Disabled command entries. */
    val disabled: MutableList<CommandRegistry.CommandEntry> = mutableListOf()

    /**
     * @return the amount of successful requests on this command handler.
     */
    var requests = 0
        private set

    /**
     * Call the command based on the message content.
     *
     * @param message Message object.
     * @return If the call was successful.
     */
    fun callCommand(message: Message) : Boolean {
        val content = message.content
        if (!content.startsWith(bot.prefix)) return false

        // Tokenize the message.
        val tokens = Utils.stringSplit(content, ' ')

        val label = tokens[0].substring(bot.prefix.length).toLowerCase()

        val args = Arrays.copyOfRange(tokens, 1, tokens.size) //tokens.subList(1, tokens.size)
        
        val entry = bot.commandRegistry.getEntry(label) ?: return false

        if (disabled.contains(entry)) {
            message.respond().error("This command is disabled by the server owner.").queue()
            return false
        }

        val cls = entry.cls

        val meta = entry.meta

        val member = message.member

        if (meta.administrator) {
            if (!bot.admins.contains(member.id)) {
                message.respond().error("This command is for bot administrators only.").queue()
                return false
            }
        }

        if (meta.permissions.isNotEmpty()) {
            if (meta.scope == Scope.VOICE) {
                if (member.voiceState.channel == null) {
                    message.respond().error("This command requires you to be in a voice channel.").queue()
                    return false
                }
            }
            if (meta.scope.checkPermission(message, member, *meta.permissions)) {
                val requirements = entry.meta.permissions.map(Permission::getName)
                message.respond().error("You lack the following permissions: `$requirements` in " + when (meta.scope) {
                    Scope.GUILD -> "the guild `${message.guild.name}`."
                    Scope.TEXT -> "the text channel `${message.textChannel.name}`."
                    Scope.VOICE -> "the voice channel `${member.voiceState.channel.name}`."
                }).queue()
                return false
            }
        }

        try {
            requests++
            val cmd = cls.newInstance()

            cmd.jda = guildData.shard
            cmd.shard = guildData.shard
            cmd.guild = guildData.guild
            cmd.guildData = guildData
            cmd.commandHandler = this
            cmd.bot = bot
            cmd.commandMeta = meta

            cmd.execute(message, args)
            return true
        } catch (e: PermissionException) {
            message.respond().error("The bot lacks the permission `${e.permission.getName()}` required to perform this command.").queue()
        } catch (e: RuntimeException) {
            message.respond().error("**Exception**: " + e.message).queue()
            e.printStackTrace()
        }
        return false
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
