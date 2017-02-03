package xyz.gnarbot.gnar.servers.listeners;

import net.dv8tion.jda.client.events.relationship.FriendRequestReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class UserListener extends ListenerAdapter {
    @Override
    public void onFriendRequestReceived(FriendRequestReceivedEvent event) {
        event.getFriendRequest().accept().queue();
    }
}
