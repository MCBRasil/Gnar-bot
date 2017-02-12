package xyz.gnarbot.gnar.servers.listeners;


import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
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

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        shard.getHosts().remove(event.getGuild().getId());
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        Host host = shard.getHost(event.getGuild());
        if (host != null) {
            host.getPersonHandler().removeUser(event.getMember());
        }
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        super.onGuildVoiceJoin(event);
    }
}
