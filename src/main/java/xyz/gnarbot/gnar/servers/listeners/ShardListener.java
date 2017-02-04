package xyz.gnarbot.gnar.servers.listeners;


import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.servers.Host;
import xyz.gnarbot.gnar.servers.Shard;

public class ShardListener extends ListenerAdapter {
    private final Shard shard;

    public ShardListener(Shard shard) {
        this.shard = shard;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (Bot.INSTANCE.getBlocked().contains(event.getAuthor().getId())) {
            event.getChannel().sendMessage("You are not allowed to use this bot.").queue();
            return;
        }

        if (event.getMessage().getContent().startsWith(Bot.getToken())) {
            Host host = shard.getHost(event.getGuild());
            if (host != null) host.handleMessage(event.getMessage());
        }
    }
}
