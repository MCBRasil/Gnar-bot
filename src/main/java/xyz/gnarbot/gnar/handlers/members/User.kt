package xyz.gnarbot.gnar.handlers.members

import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.PrivateChannel
import net.dv8tion.jda.core.entities.Role
import net.dv8tion.jda.core.entities.User
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.handlers.servers.Host

/**
 * Bot's wrapper class for JDA's [Member].
 *
 * @see Member
 */
class User(private val host : Host, private val member : Member) : User by member.user, Member by member
{
    val isBotMaster : Boolean = Bot.admins.contains(member.user.id)
    
    val clearance : Clearance
        get() = when
        {
            isBotMaster -> Clearance.BOT_MASTER
            isBot -> Clearance.BOT
            member == host.owner -> Clearance.SERVER_OWNER
            hasRole("Bot Commander") -> Clearance.BOT_COMMANDER
            else -> Clearance.USER
        }
    
    fun requestPrivateChannel() : PrivateChannel
    {
        if (!hasPrivateChannel())
        {
            openPrivateChannel()?.complete()
        }
        return privateChannel
    }
    
    /**
     * The JDA instance.
     *
     * @return The current JDA instance.
     */
    override fun getJDA() = member.jda!!
    
    /**
     * Retrieve a Mention for this User.
     *
     * @return A resolvable mention.
     */
    override fun getAsMention() = member.asMention!!
    
    /**
     * Check if the member have a role named [name].
     *
     * @param name Role name.
     * @return If the member have a role named [name].
     */
    fun hasRole(name : String) : Boolean
    {
        roles.forEach {
            if (it.name == name)
            {
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
    fun hasRole(role : Role) : Boolean = roles.contains(role)
    
    /**
     * @return String representation of the member.
     */
    override fun toString() : String
    {
        return "Member(id=$id, name=\"$name\", guild=\"${host.name}\", clearance=$clearance)"
    }
}
