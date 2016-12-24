package xyz.gnarbot.gnar.handlers.servers;


import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import xyz.gnarbot.gnar.handlers.commands.CommandDistributor;

class ShardListener extends ListenerAdapter
{
    private final Shard shard;
    
    public ShardListener(Shard shard)
    {
        this.shard = shard;
    }
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (!event.getAuthor().isBot()
                && CommandDistributor.INSTANCE.checkValid(event.getMessage().getContent()))
        {
            Host host = shard.getHost(event.getGuild());
    
            if (host != null) host.handleMessageEvent(event);
        }
    }
}
