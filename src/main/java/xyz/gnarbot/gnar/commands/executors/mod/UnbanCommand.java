package xyz.gnarbot.gnar.commands.executors.mod;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.utils.PermissionUtil;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.members.Level;
import xyz.gnarbot.gnar.members.Person;
import xyz.gnarbot.gnar.servers.Host;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;

@Command(aliases = "unban", level = Level.BOT_COMMANDER)
public class UnbanCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
        Host host = note.getHost();

        Person author = note.getAuthor();
        Person target = null;

        if (!PermissionUtil.checkPermission(note.getTextChannel(), author, Permission.BAN_MEMBERS)) {
            note.error("You do not have permission to manage bans.");
            return;
        }

        List<User> bans = note.getGuild().getController().getBans().complete();

        for (User user : bans) {
            if (user.getId().equals(args.get(0))) {
                target = host.getPersonHandler().asPerson(user);
                break;
            }
        }

        if (args.size() >= 1) {
            target = note.getHost().getPersonHandler().getUser(args.get(0));
        }
        if (target == null) {
            note.error("Could not find user.");
            return;
        }

        if (!host.unban(target)) {
            note.error("Gnar does not have permission to manage bans.");
            return;
        }
        note.info(target.getEffectiveName() + " has been unbanned.");
    }
}

