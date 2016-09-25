package xyz.gnarbot.gnar.handlers

import net.dv8tion.jda.entities.Role
import net.dv8tion.jda.entities.User
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.Host

/**
 * Gnar's wrapper class for JDA's [User].
 *
 * @see User
 */
class Member(private val host : Host, private val user : User) : User by user
{
    val isBotMaster : Boolean
        get() = Bot.admins.contains(user)
    
    var clearance = Clearance.USER
    
    init
    {
        // Automatically assign permission.
        clearance = when
        {
            isBot ->                            Clearance.BOT
            isBotMaster ->        Clearance.BOT_MASTER
            user == host.owner ->              Clearance.SERVER_OWNER
            hasRole("Bot Commander") ->         Clearance.BOT_COMMANDER
            else ->                             Clearance.USER
        }
    }
    
    /**
     * Check if the member have a role named [name].
     *
     * @param name Role name.
     * @return If the member have a role named [name].
     */
    fun hasRole(name : String) : Boolean
    {
        host.getRolesForUser(user)?.forEach {
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
    fun hasRole(role : Role) : Boolean
    {
        host.getRolesForUser(user)?.forEach {
            if (it == role)
            {
                return true
            }
        }
        return false
    }
    
    /**
     * @return String representation of the member.
     */
    override fun toString() : String
    {
        return "Member(id=${user.id}, name=${user.username}, guild=${host.name}, clearance=$clearance)"
    }
}
