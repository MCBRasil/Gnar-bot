package xyz.gnarbot.gnar.commands.executors.mod;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.commands.Category;
import xyz.gnarbot.gnar.commands.Command;
import xyz.gnarbot.gnar.commands.CommandExecutor;
import xyz.gnarbot.gnar.commands.Scope;

@Command(aliases = "kick",
        usage = "-user",
        category = Category.MODERATION,
        scope = Scope.TEXT,
        permissions = Permission.KICK_MEMBERS)
public class KickCommand extends CommandExecutor {
    @Override
    public void execute(Message message, String[] args) {
        Member author = message.getMember();
        Member target = null;

        if (message.getMentionedChannels().size() >= 1) {
            target = getGuild().getMember(message.getMentionedUsers().get(0));
        } else if (args.length >= 1) {
            target = getGuildData().getMemberByName(args[0], false);
        }

        if (target == null) {
            message.respond().error("Could not find user.").queue();
            return;
        }

        if (!author.canInteract(target)) {
            message.respond().error("Sorry, that user has an equal or higher role.").queue();
            return;
        }

        getGuild().getController().kick(target).queue();
        message.respond().info(target.getEffectiveName() + " has been kicked.").queue();
    }
}
