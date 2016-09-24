package xyz.gnarbot.gnar;

import net.dv8tion.jda.JDA;
import xyz.gnarbot.gnar.handlers.commands.CommandDistributor;

import java.util.ArrayList;
import java.util.List;

/**
 * Individual sharded instances of the bot.
 */
public class Shard
{
    private final int id;
    
    private final CommandDistributor distributor = new CommandDistributor();
    private final List<GuildHandler> guildHandlers = new ArrayList<>(1000);
    private final JDA jda;
    
    public Shard(int id, JDA jda)
    {
        this.id = id;
        this.jda = jda;
    }
    
    public int getId()
    {
        return id;
    }
    
    public CommandDistributor getDistributor()
    {
        return distributor;
    }
    
    @Override
    public String toString()
    {
        return "Shard(id="+id+")";
    }
}
