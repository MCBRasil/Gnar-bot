package xyz.gnarbot.gnar.members

import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.User
import xyz.gnarbot.gnar.servers.Host
import java.util.*

/**
 * Handle JDA's [Member] & [Person] instances.
 */
class PersonHandler(private val host: Host) {
    /**
     * Returns the wrapper mapping registry.
     *
     * @return The wrapper mapping registry.
     */
    val registry : MutableMap<String, Person> = WeakHashMap()

    val me : Person get() = asPerson(host.jda.selfUser)

    fun getUser(name: String): Person? {
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
        return asPerson(member.user)
    }

    /**
     * Lazily wrap users in a Member instance.
     *
     * @param user JDA user.
     *
     * @return User instance.
     */
    fun asPerson(user: User): Person {
        var person = registry[user.id]

        if (person == null) {
            registry.put(user.id, Person(host, host.getMember(user)))
            person = registry[user.id]
        }

        return person!!.ensure()
    }

    /**
     * Remove the user from the registry.
     */
    fun removeUser(member: Member) {
        registry.remove(member.user.id)
    }
}

