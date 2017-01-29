package xyz.gnarbot.gnar.members

import net.dv8tion.jda.core.entities.Member
import xyz.gnarbot.gnar.servers.Host
import java.util.*
import net.dv8tion.jda.core.entities.User as JDAUser

/**
 * Handle JDA's [Member] & [Person] instances.
 */
class PersonHandler(private val host: Host) {
    /**
     * Returns the wrapper mapping registry.
     *
     * @return The wrapper mapping registry.
     */
    val registry: MutableMap<Member, Person> = WeakHashMap()

    fun getUser(name: String) : Person? {
        val list = host.getMembersByName(name, true)

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
    fun asPerson(member: Member): Person {
        var user = registry[member]

        if (user == null) {
            registry[member] = Person(host, member)
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
    fun asPerson(user: JDAUser): Person {
        val member = host.getMember(user)

        return asPerson(member)
    }

    /**
     * Remove the user from the registry.
     */
    fun removeUser(member: Member) {
        registry.remove(member)
    }
}

