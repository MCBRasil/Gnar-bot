package xyz.gnarbot.gnar.commands.mod;

import com.google.inject.Inject;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.utils.PermissionUtil;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.handlers.members.Clearance;
import xyz.gnarbot.gnar.handlers.servers.Host;
import xyz.gnarbot.gnar.utils.Note;

import java.util.ArrayList;
import java.util.List;

@Command(aliases = "unban", clearance = Clearance.BOT_COMMANDER)
public class UnbanCommand extends CommandExecutor
{
    @Inject
    public Host host;

    @Override
    public void execute(Note msg, String label, String[] args)
    {

        Member author = msg.getAuthor();
        User target = null;

        try { //Checks if they mentioned someone
            List<User> banned =  msg.getGuild().getController().getBans().block();
            ArrayList<User> confirmed = new ArrayList<>();
            banned.stream().filter(user -> user.getName().contains(args[0])).forEach(user -> confirmed.add(user));
            target = confirmed.get(0);
        } catch (Exception e) { }

        if(target == null) {
            try {
                List<User> banned = msg.getGuild().getController().getBans().block();
                ArrayList<User> confirmed = new ArrayList<>();
                banned.stream().filter(user -> user.getId().equals(args[0])).forEach(user -> confirmed.add(user));
                target = confirmed.get(0);
            } catch (Exception e) {}
        }

        if(target != null) {
            if (PermissionUtil.checkPermission(msg.getTextChannel(), author, Permission.BAN_MEMBERS)) {
                if(host.unban(target.getId())) {
                    msg.reply(target.getName() + " has been un-bannerino'd");
                } else {
                    msg.reply("GNAR does not have permission to unban");
                }
            } else {
                msg.reply("You do not have permission to unban");
            }
        } else {
            msg.reply("Could not find user, maybe you made a typo?");
        }
    }
}

