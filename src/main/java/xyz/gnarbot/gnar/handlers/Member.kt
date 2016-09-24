package xyz.gnarbot.gnar.handlers

import net.dv8tion.jda.entities.Guild
import net.dv8tion.jda.entities.User
import xyz.gnarbot.gnar.Bot

/**
 * Gnar's wrapper class for JDA's [User].
 *
 * @see User
 */
class Member(private val guild : Guild, private val user : User) : User by user
{
    var clearance = Clearance.USER
    
    init
    {
        clearance = when
        {
            isBot ->                            Clearance.BOT
            Bot.adminIDs.contains(user.id) ->   Clearance.BOT_MASTER
            user === guild.owner ->            Clearance.SERVER_OWNER
            hasRoleNamed("Bot Commander") ->    Clearance.BOT_COMMANDER
            else ->                             Clearance.USER
        }
    }
    
    fun hasRoleNamed(name : String) : Boolean
    {
        guild.getRolesForUser(user)?.forEach {
            if (it.name == name)
            {
                return true
            }
        }
        return false
    }
    
    override fun toString() : String
    {
        return "Person(id=${user.id}, name=${user.username}, guild=${guild.name})"
    }
}
