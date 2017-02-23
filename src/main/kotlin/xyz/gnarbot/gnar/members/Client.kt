package xyz.gnarbot.gnar.members

import net.dv8tion.jda.core.entities.*
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.servers.Servlet

/**
 * Bot's wrapper class for JDA's [Member].
 *
 * @see Member
 */
class Client(var servlet: Servlet, private var member: Member) : Member by member, User by member.user {
    val isBotMaster = Bot.admins.contains(id)

    val level: Level
        get() = when {
            // BOT
            isBot -> Level.BOT

            // BOT MASTER
            isBotMaster -> Level.BOT_CREATOR

            // SERVER OWNER
            member == servlet.owner -> Level.SERVER_OWNER

            // BOT COMMANDER
            hasRole("Bot Commander") -> Level.BOT_COMMANDER

            // DJ
            hasRole("DJ") -> Level.DJ

            // USERS
            else -> Level.USER
        }

    val voiceChannel: VoiceChannel?
        get() {
            return servlet.voiceChannels.firstOrNull { it.members.contains(member) }
        }

    /** @return The current JDA instance. */
    override fun getJDA() = member.jda!!

    /** @return A resolvable mention. */
    override fun getAsMention() = member.asMention!!

    fun requestPrivateChannel(): PrivateChannel {
        if (!user.hasPrivateChannel()) {
            openPrivateChannel().complete()
        }
        return privateChannel
    }

    /**
     * Check if the member have a role named [name].
     *
     * @param name Role name.
     * @return If the member have a role named [name].
     */
    fun hasRole(name: String): Boolean {
        roles.forEach {
            if (it.name == name) {
                return true
            }
        }
        return false
    }

    /**
     * Check if the member have the role [role].
     *
     * @param role Role.
     * @return If the member have the role [role].
     */
    fun hasRole(role: Role): Boolean = roles.contains(role)

    /**
     * @return String representation of the member.
     */
    override fun toString(): String {
        return "Member(id=$id, name=\"$name\", guild=\"${servlet.name}\", level=$level)"
    }
}
