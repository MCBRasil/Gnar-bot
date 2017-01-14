package xyz.gnarbot.gnar.commands.executors.general;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.commands.handlers.CommandHandler;
import xyz.gnarbot.gnar.servers.Shard;
import xyz.gnarbot.gnar.utils.Note;

import java.util.StringJoiner;
import java.util.stream.Collectors;

@Command(aliases = {"info", "botinfo"}, description = "Show information about GN4R-BOT.")
public class BotInfoCommand extends CommandExecutor
{
    @Override
    public void execute(Note note, String label, String[] args)
    {
        CommandHandler commandHandler = note.getHost().getCommandHandler();
        
        int textChannels = 0;
        int voiceChannels = 0;
        int servers = 0;
        int users = 0;
        int offline = 0;
        int online = 0;
        int inactive = 0;
        int dnd = 0;
        
        for (Shard shard : Bot.INSTANCE.getShards())
        {
            JDA jda = shard.getJDA();
            
            servers += jda.getGuilds().size();
            for (Guild g : jda.getGuilds())
            {
                for (Member u : g.getMembers())
                {
                    switch (u.getOnlineStatus())
                    {
                        case ONLINE:
                            online++;
                            break;
                        case OFFLINE:
                            offline++;
                            break;
                        case IDLE:
                            inactive++;
                            break;
                        case DO_NOT_DISTURB:
                            dnd++;
                            break;
                    }
                }
                users += g.getMembers().size();
                textChannels += g.getTextChannels().size();
                voiceChannels += g.getVoiceChannels().size();
            }
        }
        
        int commandSize = commandHandler.getUniqueRegistry().values().parallelStream()
                .filter(CommandExecutor::isShownInHelp)
                .collect(Collectors.toList())
                .size();
        
        int requests = Bot.INSTANCE.getShards().stream()
                .flatMap(shard -> shard.getHosts().stream())
                .mapToInt(guild -> guild.getCommandHandler().getRequests())
                .sum();
        
        StringJoiner joiner = new StringJoiner("\n");
        
        //         "__**General**                                                      __"
        
        joiner.add("Requests: **[" + requests + "]()**");
        joiner.add("Servers: **[" + servers + "]()**");
        joiner.add("Shards: **[" + Bot.INSTANCE.getShards().size() + "]()**");
        joiner.add("");
        joiner.add("__**Channels**                                                     __");
        joiner.add("  Text: **[" + textChannels + "]()**");
        joiner.add("  Voice: **[" + voiceChannels + "]()**");
        joiner.add("");
        joiner.add("__**Users**                                                          __");
        joiner.add("  Total: **[" + users + "]()**");
        joiner.add("  Online: **[" + online + "]()**");
        joiner.add("  Offline: **[" + offline + "]()**");
        joiner.add("  Inactive: **[" + inactive + "]()**");
        joiner.add("  DND: **[" + dnd + "]()**");
        joiner.add("");
        joiner.add("__**Others**                                                         __");
        joiner.add("  Creators: **[Avarel](https://github.com/Avarel)** and **[Maeyrl](https://github.com/maeyrl)**");
        joiner.add("  Contributor: **[Gatt](https://github.com/RealGatt)**");
        joiner.add("  Website: **[gnarbot.xyz](http://gnarbot.xyz)**");
        joiner.add("  Commands: **[" + commandSize + "]()**");
        joiner.add("  Library: **[JDA 3" + "](https://github.com/DV8FromTheWorld/JDA)**");
        joiner.add("  Uptime: **[" + Bot.INSTANCE.getSimpleUptime() + "]()**");
        
        note.replyEmbedRaw("Bot Information", joiner.toString(), Bot.getColor(), note.getJDA()
                .getSelfUser()
                .getAvatarUrl());
    }
}
