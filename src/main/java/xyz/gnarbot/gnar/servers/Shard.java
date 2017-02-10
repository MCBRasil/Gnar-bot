package xyz.gnarbot.gnar.servers;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import xyz.gnarbot.gnar.commands.handlers.CommandRegistry;
import xyz.gnarbot.gnar.servers.listeners.GuildCountListener;
import xyz.gnarbot.gnar.servers.listeners.ShardListener;
import xyz.gnarbot.gnar.servers.listeners.UserListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;

/**
 * Individual shard instances of the bot.
 */
public class Shard {
    private final int id;

    private final JDA jda;

    private final Map<String, Host> hosts = new WeakHashMap<>();

    private final CommandRegistry commandRegistry = new CommandRegistry();

    public Shard(int id, JDA jda) {
        this.id = id;
        this.jda = jda;

        jda.addEventListener(new ShardListener(this));
        jda.addEventListener(new UserListener());
        jda.addEventListener(GuildCountListener.INSTANCE);

        java.util.logging.Logger.getLogger("org.apache.http.client.protocol.ResponseProcessCookies")
                .setLevel(Level.OFF);
    }

    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    /**
     * Returns ID of the shard.
     *
     * @return ID of the shard.
     */
    public int getId() {
        return id;
    }

    /**
     * Lazily get a Host instance from a Guild instance.
     *
     * @param guild JDA Guild.
     * @return Host instance of Guild.
     * @see Host
     */
    public Host getHost(Guild guild) {
        if (guild == null) return null;

        //Bot.getLOG().info("Creating new Host instance for " + guild.getName() + ".");

        return hosts.computeIfAbsent(guild.getId(), k -> new Host(this, guild.getId()));
    }

    /**
     * Returns the Hosts instances of this shard.
     *
     * @return The Hosts instances of this shard.
     */
    public List<Host> getHosts() {
        return new ArrayList<>(hosts.values());
    }

    /**
     * Returns the JDA API of this shard.
     *
     * @return The JDA API of this shard.
     */
    public JDA getJDA() {
        return jda;
    }

    /**
     * @return The string representation of the shard.
     */
    @Override
    public String toString() {
        return "Shard(id=" + id + ", guilds=" + jda.getGuilds().size() + ")";
    }

    /**
     * @return JSON data on the shard.
     */
    public ShardInfo getInfo() {
        return new ShardInfo(this);
    }

    /**
     * Shuts down the shard.
     */
    public void shutdown() {
        jda.shutdown(false);
        hosts.clear();
    }

    public static class ShardInfo {
        private int requests;
        private final int id;
        private final JDA.Status status;
        private final int guilds;
        private final int users;
        private final int textChannels;
        private final int voiceChannels;

        public ShardInfo(Shard shard) {
            id = shard.id;
            status = shard.jda.getStatus();
            guilds = shard.jda.getGuilds().size();
            users = shard.jda.getUsers().size();
            textChannels = shard.jda.getTextChannels().size();
            voiceChannels = shard.jda.getVoiceChannels().size();

            for (Host host : shard.hosts.values()) {
                requests += host.getCommandHandler().getRequests();
            }
        }

        public int getRequests() {
            return requests;
        }

        public int getId() {
            return id;
        }

        public JDA.Status getStatus() {
            return status;
        }

        public int getGuilds() {
            return guilds;
        }

        public int getUsers() {
            return users;
        }

        public int getTextChannels() {
            return textChannels;
        }

        public int getVoiceChannels() {
            return voiceChannels;
        }
    }
}

