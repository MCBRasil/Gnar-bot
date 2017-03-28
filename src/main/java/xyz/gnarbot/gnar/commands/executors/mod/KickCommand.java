package xyz.gnarbot.gnar.commands.executors.mod;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.utils.PermissionUtil;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.members.Client;
import xyz.gnarbot.gnar.members.Level;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;

@Command(aliases = "kick",
        usage = "-user",
        level = Level.BOT_COMMANDER)
public class KickCommand extends CommandExecutor {

    @Override
    public void execute(Note note, List<String> args) {
        Client author = note.getAuthor();
        Client target = null;

        if (!PermissionUtil.checkPermission(note.getTextChannel(), author, Permission.KICK_MEMBERS)) {
            note.respond().error("You do not have permission to kick.").queue();
            return;
        }

        if (note.getMentionedChannels().size() >= 1) {
            target = note.getMentionedUsers().get(0);
        } else if (args.size() >= 1) {
            target = note.getServlet().getClientHandler().getClientByName(args.get(0), false);
        }

        if (target == null) {
            note.respond().error("Could not find user.").queue();
            return;
        }
        if (!PermissionUtil.canInteract(author, target)) {
            note.respond().error("Sorry, that user has an equal or higher role.").queue();
            return;
        }

        if (!getServlet().kick(target)) {
            note.respond().error("Gnar does not have permission to kick.").queue();
            return;
        }
        note.respond().info(target.getEffectiveName() + " has been kicked.").queue();
    }
}
