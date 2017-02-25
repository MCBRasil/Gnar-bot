package xyz.gnarbot.gnar.members

import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.User
import xyz.gnarbot.gnar.servers.Servlet
import java.util.*

/**
 * Handle JDA's [Member] & [Client] instances.
 */
class ClientHandler(private val servlet: Servlet) {
    /**
     * Returns the wrapper mapping registry.
     *
     * @return The wrapper mapping registry.
     */
    val registry: MutableMap<String, Client> = WeakHashMap()

    val selfClient: Client get() = getClient(servlet.jda.selfUser)!!

    fun getClientByName(name: String, searchNickname: Boolean): Client? {
        return getClient(servlet.getMemberByName(name, searchNickname))
    }

    /**
     * Lazily wrap users in a Member instance.
     *
     * @param member JDA member.
     *
     * @return User instance.
     */
    fun getClient(member: Member?): Client? {
        if (member == null) return null
        return registry.getOrPut(member.user.id) { Client(servlet, member) }

    }

    /**
     * Lazily wrap users in a Member instance.
     *
     * @param user JDA user.
     *
     * @return User instance.
     */
    fun getClient(user: User?): Client? {
        return getClient(servlet.getMember(user))
    }

    /**
     * Remove the user from the registry.
     */
    fun removeUser(member: Member) {
        registry.remove(member.user.id)
    }
}

