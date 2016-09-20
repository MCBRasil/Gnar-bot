package xyz.gnarbot.handlers

import net.dv8tion.jda.entities.User

/**
 * Wrapper class of [User].
 */
class Person(private val user : User) : User by user
{
    var permission = Permission.USER
    
    enum class Permission(val value : Int)
    {
        BOT_MASTER(3),
        SERVER_OWNER(2),
        BOT_COMMANDER(1),
        USER(0),
        BOT(-1)
    }
}
