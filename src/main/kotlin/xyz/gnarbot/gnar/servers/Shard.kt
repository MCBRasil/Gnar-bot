package xyz.gnarbot.gnar.servers

import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.Guild
import xyz.gnarbot.gnar.api.data.ShardInfo
import xyz.gnarbot.gnar.commands.handlers.CommandRegistry
import xyz.gnarbot.gnar.servers.listeners.GuildCountListener
import xyz.gnarbot.gnar.servers.listeners.ShardListener
import xyz.gnarbot.gnar.servers.listeners.UserListener
import java.util.*

/**
 * Individual shard instances of [JDA] of the bot that contains all the [Servlet] for each guild.
 */
class Shard(val id: Int, private val jda: JDA) : JDA by jda {
    val servlets: MutableMap<String, Servlet> = WeakHashMap()

    val commandRegistry = CommandRegistry()

    init {
        jda.addEventListener(ShardListener(this))
        jda.addEventListener(UserListener())
        jda.addEventListener(GuildCountListener.INSTANCE)

        //Logger.getLogger("org.apache.http.client.protocol.ResponseProcessCookies").level = Level.OFF
    }

    fun getHost(id: String?) : Servlet? {
        return getHost(getGuildById(id))
    }

    /**
     * Lazily get a Host instance from a Guild instance.
     *
     * @param guild JDA Guild.
     *
     * @return Host instance of Guild.
     *
     * @see Servlet
     */
    fun getHost(guild: Guild?): Servlet? {
        if (guild == null) return null
        return servlets.getOrPut(guild.id) { Servlet(this, guild) }
    }

    /**
     * @return The string representation of the shard.
     */
    override fun toString(): String {
        return "Shard(id=$id, guilds=${jda.guilds.size})"
    }

    /**
     * @return JSON data on the shard.
     */
    val info: ShardInfo get() = ShardInfo(this)



    /**
     * Shuts down the shard.
     */
    override fun shutdown() {
        jda.shutdown(false)
        clearServlets()
        commandRegistry.commandMap.clear()
    }

    fun clearServlets() {
        servlets.values.forEach {
            if(it.shutdown(false)) {
                servlets.remove(it.id)
            }
        }
    }

    fun reset(id: String?) = reset(getGuildById(id))

    fun reset(guild: Guild?) {
        if (guild == null) return
        servlets[guild.id]?.shutdown(true)
        servlets[guild.id] = getHost(guild.id)!!
    }

}

