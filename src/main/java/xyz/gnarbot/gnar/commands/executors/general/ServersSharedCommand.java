package xyz.gnarbot.gnar.commands.executors.general;

import net.dv8tion.jda.core.entities.Guild;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.servers.Shard;
import xyz.gnarbot.gnar.utils.Note;

@Command(aliases = {"shared", "serversshared"}, description = "Shows servers you share with the bot.")
public class ServersSharedCommand extends CommandExecutor {
    @Override
    public void execute(Note note, String[] args) {
        int total = 0;
        String servers = "";

        for (Shard shard : Bot.INSTANCE.getShards()) {
            for (Guild g : shard.getJDA().getGuilds()) {
                if (g.getMembers().contains(note.getAuthor())) {
                    total++;
                    servers += "    **Server:** " + g.getName() + "\n";
                }
            }
        }

        note.replyRaw("**Total Servers:** " + total + "\n**Servers:** \n" + servers);
    }
}