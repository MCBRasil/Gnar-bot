package xyz.gnarbot.gnar.commands.executors.mod;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.User;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.members.Client;
import xyz.gnarbot.gnar.members.Level;
import xyz.gnarbot.gnar.servers.Servlet;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;

@Command(aliases = "unban", level = Level.BOT_COMMANDER)
public class UnbanCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
        Servlet servlet = note.getServlet();

        Client author = note.getAuthor();
        Client target = null;

        if (!author.hasPermission(note.getTextChannel(), Permission.BAN_MEMBERS)) {
            note.respond().error("You do not have permission to manage bans.").queue();
            return;
        }

        List<User> bans = note.getGuild().getController().getBans().complete();

        for (User user : bans) {
            if (user.getId().equals(args.get(0))) {
                target = servlet.getClientHandler().getClient(user);
                break;
            }
        }

        if (args.size() >= 1) {
            target = note.getServlet().getClientHandler().getClientByName(args.get(0), true);
        }
        if (target == null) {
            note.respond().error("Could not find user.").queue();
            return;
        }

        if (!servlet.unban(target)) {
            note.respond().error("Gnar does not have permission to manage bans.").queue();
            return;
        }
        note.respond().info(target.getEffectiveName() + " has been unbanned.").queue();
    }
}

