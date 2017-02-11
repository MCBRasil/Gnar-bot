package xyz.gnarbot.gnar.members

import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.PrivateChannel
import net.dv8tion.jda.core.entities.Role
import net.dv8tion.jda.core.entities.User
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.servers.Host

/**
 * Bot's wrapper class for JDA's [Member].
 *
 * @see Member
 */
class Person(var host: Host, private var member: Member) : Member by member, User by member.user {

    val isBotMaster: Boolean get() = Bot.admins.contains(id)

    val level: Level
        get() = when {
            isBotMaster -> Level.BOT_MASTER
            isBot -> Level.BOT
            member == host.owner -> Level.SERVER_OWNER
            hasRole("DJ") -> Level.DJ
            hasRole("Bot Commander") -> Level.BOT_COMMANDER
            else -> Level.USER
        }

    fun check() : Person {
        val _member = host.getMemberById(id)
        if (_member != member) {
            member = _member
        }
        return this
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

//
//
//    // JDA/User delegation
//    val name : String get() = user.name
//    val discriminator : String get() = user.discriminator
//    val avatarID : String get() = user.avatarId
//    val avatarUrl : String get() = user.avatarUrl
//    val defaultAvatarId : String get() = user.defaultAvatarId
//    val defaultAvatarUrl : String get() = user.defaultAvatarUrl
//    val effectiveAvatarUrl : String get() = user.effectiveAvatarUrl
//    val mutualGuilds : List<Guild> get() = user.mutualGuilds
//    val privateChannel : PrivateChannel get() = user.privateChannel
//    val isBot : Boolean get() = user.isBot
//    fun hasPrivateChannel() : Boolean = user.hasPrivateChannel()
//    fun openPrivateChannel() : RestAction<PrivateChannel> = user.openPrivateChannel()
//
//    // JDA/Member delegation
//    val joinDate : OffsetDateTime get() = member.joinDate
//    val voiceState : GuildVoiceState get() = member.voiceState
//    val game : Game? get() = member.game
//    val onlineStatus : OnlineStatus get() = member.onlineStatus
//    val nickname : String? get() = member.nickname
//    val effectiveName : String get() = member.effectiveName
//    val roles : List<Role> get() = member.roles
//    val color : Color get() = member.color
//    val permissions : List<Permission> get() = member.permissions
//    val isOwner : Boolean get() = member.isOwner
//    fun canInteract(member : Member) = member.canInteract(member)
//    fun canInteract(role : Role) = member.canInteract(role)
//    fun canInteract(emote : Emote) = member.canInteract(emote)
}
