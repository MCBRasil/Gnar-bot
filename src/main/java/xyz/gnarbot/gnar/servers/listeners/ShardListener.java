package xyz.gnarbot.gnar.servers.listeners;


import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.servers.Servlet;
import xyz.gnarbot.gnar.servers.Shard;

public class ShardListener extends ListenerAdapter {
    private final Shard shard;
    private final Bot bot;

    public ShardListener(Shard shard, Bot bot) {
        this.shard = shard;
        this.bot = bot;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (bot.getBlocked().contains(event.getAuthor().getId())) {
            event.getChannel().sendMessage("You are not allowed to use this bot.").queue();
            return;
        }

        if (event.getMessage().getContent().startsWith(bot.getToken())) {
            Servlet servlet = shard.getServlet(event.getGuild());
            if (servlet != null) servlet.handleMessage(event.getMessage());
        }
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        shard.getServlets().remove(event.getGuild().getId());
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        Servlet servlet = shard.getServlet(event.getGuild());
        if (servlet != null) {
            servlet.getClientHandler().removeUser(event.getMember());
        }
    }

    @Override
    public void onGenericGuildVoice(GenericGuildVoiceEvent event) {
        if (event instanceof GuildVoiceLeaveEvent || event instanceof GuildVoiceMoveEvent) {
            if (event.getMember().getUser() == event.getJDA().getSelfUser()) return;

            Servlet servlet = shard.getServlet(event.getGuild());

            if (servlet == null) return;

            Channel botChannel = servlet.getSelfClient().getVoiceState().getChannel();

            Channel channelLeft;

            if (event instanceof GuildVoiceLeaveEvent) {
                channelLeft = ((GuildVoiceLeaveEvent) event).getChannelLeft();
            } else {
                channelLeft = ((GuildVoiceMoveEvent) event).getChannelLeft();
            }

            if (botChannel == null || channelLeft != botChannel) return;

            if (botChannel.getMembers().size() == 1) {
                servlet.resetMusicManager();
            }
        }
    }
}
