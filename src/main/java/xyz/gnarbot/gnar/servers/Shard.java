package xyz.gnarbot.gnar.servers;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
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
    }
    
    /**
     * Updates Server Counts on ad sites
     */
    public void update()
    {
        int count = 0;
        
        for (Shard s : Bot.INSTANCE.getShards())
        {
            count += s.getJDA().getGuilds().size();
        }
        
        DiscordBotsInfo.updateServerCount(count);
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
        
        //Bot.getLOG().info("Creating new Host instance for " + guild.getName() + ".");
        
        return hosts.computeIfAbsent(guild, k -> new Host(this, guild));
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
    
    public void shutdown()
    {
        jda.shutdown();
        hosts.clear();
    }
}

