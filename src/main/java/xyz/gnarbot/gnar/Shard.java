package xyz.gnarbot.gnar;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.Guild;
import xyz.gnarbot.gnar.commands.general.TestCommand;
import xyz.gnarbot.gnar.handlers.commands.CommandDistributor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Individual shard instances of the bot.
 */
public class Shard
{
    private final int id;
    private final JDA jda;
    
    private final CommandDistributor distributor = new CommandDistributor();
    // LinkedHashMap access is faster. Pre-allocate to waste less CPU cycles.
    private final Map<Guild, Host> hosts = new LinkedHashMap<>(1000);
    
    public Shard(int id, JDA jda)
    {
        this.id = id;
        this.jda = jda;
        
        jda.addEventListener(new ShardListener(this));
        
        distributor.register(TestCommand.class);
    }
    
    /**
     * Returns ID of the shard.
     * @return ID of the shard.
     */
    public int getId()
    {
        return id;
    }
    
    /**
     * Lazily get a Host instance from a Guild instance.
     *
     * @see Host
     * @param guild JDA Guild.
     * @return Host instance of Guild.
     */
    public Host getHost(Guild guild)
    {
        Host host = hosts.get(guild);
        
        if (host == null)
        {
            host = new Host(this, guild);
            hosts.put(guild, host);
        }
        
        return host;
    }
    
    /**
     * Returns the command distribution handler.
     * @return Command distribution handler.
     */
    public CommandDistributor getDistributor()
    {
        return distributor;
    }
    
    /**
     * @return The string representation of the shard.
     */
    @Override
    public String toString()
    {
        return "Shard(id="+id+", guilds="+jda.getGuilds().size()+")";
    }
}

