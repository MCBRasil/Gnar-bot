package xyz.gnarbot.gnar;

import com.gmail.hexragon.gn4rBot.command.general.HelpCommand;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.Guild;
import xyz.gnarbot.gnar.commands.games.OverwatchLookupCommand;
import xyz.gnarbot.gnar.commands.general.TestCommand;
import xyz.gnarbot.gnar.handlers.commands.CommandDistributor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
    
        distributor.register(HelpCommand.class);
        distributor.register(TestCommand.class);
        distributor.register(OverwatchLookupCommand.class);
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
     * Returns the Hosts instances of this shard.
     * @return The Hosts instances of this shard.
     */
    public List<Host> getHosts()
    {
        return new ArrayList<>(hosts.values());
    }
    
    /**
     * Returns the JDA API of this shard.
     * @return The JDA API of this shard.
     */
    public JDA getJda()
    {
        return jda;
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

