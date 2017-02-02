package xyz.gnarbot.gnar.servers;


import net.dv8tion.jda.client.events.relationship.FriendRequestReceivedEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import xyz.gnarbot.gnar.Bot;

class ShardListener extends ListenerAdapter {
    private final Shard shard;

    private int changes = 0;

    public ShardListener(Shard shard) {
        this.shard = shard;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (Bot.INSTANCE.getBlockedUsers().contains(event.getAuthor().getId())) {
            event.getChannel().sendMessage("You are not allowed to use this bot.").queue();
            return;
        }

        for (String label : shard.getCommandRegistry().getCommandMap().keySet()) {
            if (event.getMessage().getContent().startsWith(Bot.getToken() + label)) {
                Host host = shard.getHost(event.getGuild());
                if (host != null) host.handleMessage(event.getMessage());
                return;
            }
        }
    }

    @Override
    public void onFriendRequestReceived(FriendRequestReceivedEvent event) {
        event.getFriendRequest().accept().queue();
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        updateQueue();
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        updateQueue();
    }

    private void updateQueue() {
        changes++;

        if (changes > 20) {
            shard.update();
            changes = 0;
        }
    }
}
