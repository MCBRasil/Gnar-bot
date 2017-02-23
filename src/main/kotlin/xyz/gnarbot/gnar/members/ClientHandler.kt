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

    val selfClient: Client get() = asPerson(servlet.jda.selfUser)

    fun getUser(name: String): Client? {
        val list = servlet.getMembersByName(name, true)
        if (list.isEmpty()) return null
        return asPerson(list.first())
    }

    /**
     * Lazily wrap users in a Member instance.
     *
     * @param member JDA member.
     *
     * @return User instance.
     */
    fun asPerson(member: Member): Client {
        return asPerson(member.user)
    }

    /**
     * Lazily wrap users in a Member instance.
     *
     * @param user JDA user.
     *
     * @return User instance.
     */
    fun asPerson(user: User): Client {
        return registry.getOrPut(user.id) { Client(servlet, servlet.getMember(user)) }
    }

    /**
     * Remove the user from the registry.
     */
    fun removeUser(member: Member) {
        registry.remove(member.user.id)
    }
}

