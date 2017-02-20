package xyz.gnarbot.gnar.servers

import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.Guild
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

    fun getHost(id: String?) {

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
        servlets.clear()
    }

    fun reset(id: String?) = reset(getGuildById(id))

    fun reset(guild: Guild?) {
        if (guild == null) return
        if (!servlets.contains(guild.id)) return

        servlets[guild.id] = Servlet(this, guild)
    }

    class ShardInfo(shard: Shard) {
        val requests: Int = shard.servlets.values.map { it.commandHandler.requests }.sum()
        val id: Int = shard.id
        val status: JDA.Status = shard.status
        val guilds: Int = shard.guilds.size
        val users: Int = shard.users.size
        val textChannels: Int = shard.textChannels.size
        val voiceChannels: Int = shard.voiceChannels.size
    }
}

