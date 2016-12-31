package xyz.gnarbot.gnar.handlers.servers;

import net.dv8tion.jda.client.events.relationship.FriendRequestReceivedEvent;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.utils.DiscordBotsInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Individual shard instances of the bot.
 */
public class Shard
{
    private final int id;
    
    private final JDA jda;
    
    private final Map<Guild, Host> hosts = new WeakHashMap<>(2000);
    
    public Shard(int id, JDA jda)
    {
        this.id = id;
        this.jda = jda;
        
        jda.addEventListener(new ShardListener(this));
        jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onFriendRequestReceived(FriendRequestReceivedEvent event) {
                super.onFriendRequestReceived(event);
                event.getFriendRequest().accept().queue();
            }

            @Override
            public void onGuildJoin(GuildJoinEvent event) {
                super.onGuildJoin(event);
                update();
            }

            @Override
            public void onGuildLeave(GuildLeaveEvent event) {
                super.onGuildLeave(event);
                update();
            }
        });
    }

    /**
     * Updates Server Counts on ad sites
     *
     */
    public void update(){
        int count=0;
        for(Shard s : Bot.INSTANCE.getShards()) {
            count+=s.getJDA().getGuilds().size();
        }
        DiscordBotsInfo.updateAbalCount(count);
        DiscordBotsInfo.updateCarbonitexCount(count);
    }
    
    /**
     * Returns ID of the shard.
     *
     * @return ID of the shard.
     */
    public int getId()
    {
        return id;
    }
    
    /**
     * Lazily get a Host instance from a Guild instance.
     *
     * @param guild JDA Guild.
     *
     * @return Host instance of Guild.
     *
     * @see Host
     */
    public Host getHost(Guild guild)
    {
        if (guild == null) return null;
    
        Host host = hosts.get(guild);
    
        if (host == null)
        {
            //Bot.getLOG().info("Creating new Host instance for " + guild.getName() + ".");
            host = new Host(this, guild);
            hosts.put(guild, host);
        }
    
        return host;
    }
    
    /**
     * Returns the Hosts instances of this shard.
     *
     * @return The Hosts instances of this shard.
     */
    public List<Host> getHosts()
    {
        return new ArrayList<>(hosts.values());
    }
    
    /**
     * Returns the JDA API of this shard.
     *
     * @return The JDA API of this shard.
     */
    public JDA getJDA()
    {
        return jda;
    }
    
    /**
     * @return The string representation of the shard.
     */
    @Override
    public String toString()
    {
        return "Shard(id=" + id + ", guilds=" + jda.getGuilds().size() + ")";
    }
}

