package xyz.gnarbot.gnar.commands.fun;

import net.dv8tion.jda.core.entities.Guild;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.handlers.servers.Shard;
import xyz.gnarbot.gnar.utils.Note;

@Command(aliases = {"shared", "serversshared"}, description = "Shows servers you share with the bot.")
public class ServersSharedCommand extends CommandExecutor
{
    @Override
    public void execute(Note message, String label, String[] args)
    {
        int total = 0;
        String servers = "";
        
        for (Shard shard : Bot.INSTANCE.getShards())
        {
            for (Guild g : shard.getJDA().getGuilds())
            {
                if (g.getMembers().contains(message.getAuthor()))
                {
                    total++;
                    servers += "    **Server:** " + g.getName() + "\n";
                }
            }
        }
        
        message.replyRaw("**Total Servers:** " + total + "\n**Servers:** \n" + servers);
    }
}