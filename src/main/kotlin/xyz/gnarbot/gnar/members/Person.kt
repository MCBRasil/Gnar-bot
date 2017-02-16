package xyz.gnarbot.gnar.members

import net.dv8tion.jda.core.entities.*
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.servers.Host

/**
 * Bot's wrapper class for JDA's [Member].
 *
 * @see Member
 */
class Person(var host: Host, private var member: Member) : Member by member, User by member.user {
    val isBotMaster = Bot.admins.contains(id)

    val level: Level
        get() = when {
            isBot -> Level.BOT

            isBotMaster -> Level.BOT_CREATOR
            member == host.owner -> Level.SERVER_OWNER
            hasRole("Bot Commander") -> Level.BOT_COMMANDER
            hasRole("DJ") -> Level.DJ
            else -> Level.USER
        }

    fun ensure() : Person {
        val _member = host.getMemberById(id)
        if (_member != member) {
            member = _member
        }
        return this
    }

    val voiceChannel : VoiceChannel?
        get() {
            return host.voiceChannels.firstOrNull { it.members.contains(member) }
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
        return "Member(id=$id, name=\"$name\", guild=\"${host.name}\", level=$level)"
    }
}
