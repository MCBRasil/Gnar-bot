package xyz.gnarbot.gnar.members

import net.dv8tion.jda.core.entities.Member
import xyz.gnarbot.gnar.servers.Host
import java.util.WeakHashMap
import net.dv8tion.jda.core.entities.User as JDAUser

/**
 * Handle JDA's [Member] & [User] instances.
 */
class UserHandler(private val host : Host)
{
    /**
     * Returns the wrapper mapping registry.
     *
     * @return The wrapper mapping registry.
     */
    val registry : MutableMap<Member, User> = WeakHashMap()
    
    /**
     * Lazily wrap users in a Member instance.
     *
     * @param member JDA member.
     *
     * @return User instance.
     */
    fun asUser(member : Member) : User
    {
        var user = registry[member]
        
        if (user == null)
        {
            registry[member] = User(host, member)
            user = registry[member]
        }
        
        return user!!
    }
    
    /**
     * Lazily wrap users in a Member instance.
     *
     * @param user JDA user.
     *
     * @return User instance.
     */
    fun asUser(user : JDAUser) : User
    {
        val member = host.getMember(user)
        
        return asUser(member)
    }
    
    /**
     * Remove the user from the registry.
     */
    fun removeUser(member : Member)
    {
        registry.remove(member)
    }
}

