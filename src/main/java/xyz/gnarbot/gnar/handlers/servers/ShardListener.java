package xyz.gnarbot.gnar.handlers.servers;


import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

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
        Host host = shard.getHost(event.getGuild());
                
        if (host != null) host.handleMessageEvent(event);
    }
}
