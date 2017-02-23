package xyz.gnarbot.gnar.commands.executors.general;

import net.dv8tion.jda.core.entities.Guild;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.servers.Shard;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;

@Deprecated
@Command(aliases = {"shared", "serversshared"}, description = "Shows servers you share with the bot.", showInHelp = false)
public class ServersSharedCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
        int total = 0;
        StringBuilder servers = new StringBuilder();

        for (Shard shard : Bot.INSTANCE.getShards()) {
            for (Guild g : shard.getGuilds()) {
                if (g.getMembers().contains(note.getAuthor())) {
                    total++;
                    servers.append("    **Server:** ").append(g.getName()).append("\n");
                }
            }
        }

        note.reply("**Total Servers:** " + total + "\n**Servers:** \n" + servers).queue();
    }
}