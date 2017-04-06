package xyz.gnarbot.gnar.commands.executors.mod;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import xyz.gnarbot.gnar.commands.handlers.Category;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;

import java.util.List;

@Command(aliases = "unban",
        category = Category.MODERATION,
        guildPermissions = Permission.BAN_MEMBERS)
public class UnbanCommand extends CommandExecutor {
    @Override
    public void execute(Message message, List<String> args) {
        Member author = message.getMember();
        Member target = null;

        List<User> bans = message.getGuild().getController().getBans().complete();

        for (User user : bans) {
            if (user.getId().equals(args.get(0))) {
                target = getServlet().getMember(user);
                break;
            }
        }

        if (args.size() >= 1) {
            target = getServlet().getMemberByName(args.get(0), true);
        }
        if (target == null) {
            message.respond().error("Could not find user.").queue();
            return;
        }

        getServlet().getController().unban(target).queue();
        message.respond().info(target.getEffectiveName() + " has been unbanned.").queue();
    }
}

