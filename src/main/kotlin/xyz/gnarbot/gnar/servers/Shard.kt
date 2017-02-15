package xyz.gnarbot.gnar.servers

import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.Guild
import xyz.gnarbot.gnar.commands.handlers.CommandRegistry
import xyz.gnarbot.gnar.servers.listeners.GuildCountListener
import xyz.gnarbot.gnar.servers.listeners.ShardListener
import xyz.gnarbot.gnar.servers.listeners.UserListener
import java.util.*

/**
 * Individual shard instances of the bot.
 */
class Shard (val id: Int, private val jda: JDA) : JDA by jda {
    val hosts : MutableMap<String, Host> = WeakHashMap()

    val commandRegistry = CommandRegistry()

    init {
        jda.addEventListener(ShardListener(this))
        jda.addEventListener(UserListener())
        jda.addEventListener(GuildCountListener.INSTANCE)

        //Logger.getLogger("org.apache.http.client.protocol.ResponseProcessCookies").level = Level.OFF
    }

    /**
     * Lazily get a Host instance from a Guild instance.

     * @param guild JDA Guild.
     * *
     * @return Host instance of Guild.
     * *
     * @see Host
     */
    fun getHost(guild: Guild?): Host? {
        if (guild == null) return null
        return hosts.getOrPut(guild.id) { Host(this, guild) }.ensure()
    }

    /**
     * @return The string representation of the shard.
     */
    override fun toString(): String {
        return "Shard(id=" + id + ", guilds=" + jda.guilds.size + ")"
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
        hosts.clear()
    }

    class ShardInfo(shard: Shard) {
        val requests: Int = shard.hosts.values.map { it.commandHandler.requests }.sum()
        val id: Int = shard.id
        val status: JDA.Status = shard.status
        val guilds: Int = shard.guilds.size
        val users: Int = shard.users.size
        val textChannels: Int = shard.textChannels.size
        val voiceChannels: Int = shard.voiceChannels.size
    }
}

