package xyz.gnarbot.gnar.servers.listeners;


import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.ReconnectedEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.servers.GuildData;
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

        if (event.getMessage().getContent().startsWith(bot.getPrefix())) {
            if (event.isFromType(ChannelType.TEXT)) {
                GuildData guild = shard.getGuildData(event.getGuild());
                guild.handleMessage(event.getMessage());
            }
        }
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        shard.getGuildData().remove(event.getGuild().getIdLong());
    }

    @Override
    public void onGenericGuildVoice(GenericGuildVoiceEvent event) {
        if (event instanceof GuildVoiceLeaveEvent || event instanceof GuildVoiceMoveEvent) {
            if (event.getMember().getUser() == event.getJDA().getSelfUser()) return;

            Guild guild = event.getGuild();

            if (guild == null) return;

            VoiceChannel botChannel = guild.getSelfMember().getVoiceState().getChannel();

            VoiceChannel channelLeft;

            if (event instanceof GuildVoiceLeaveEvent) {
                channelLeft = ((GuildVoiceLeaveEvent) event).getChannelLeft();
            } else {
                channelLeft = ((GuildVoiceMoveEvent) event).getChannelLeft();
            }

            if (botChannel == null || channelLeft != botChannel) return;

            if (botChannel.getMembers().size() == 1) {
                GuildData data = shard.getGuildData(event.getGuild());
                data.resetMusicManager();
            }
        }
    }

    @Override
    public void onReconnect(ReconnectedEvent event) {
        shard.clearData(true);
    }
}
