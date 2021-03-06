package xyz.gnarbot.gnar.commands.executors.general;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.Constants;
import xyz.gnarbot.gnar.commands.Command;
import xyz.gnarbot.gnar.commands.CommandExecutor;

import java.util.List;

@Command(aliases = {"whois", "infoof", "infoon", "user"},
        usage = "-@user",
        description = "Get information on a user."
)
public class WhoIsCommand extends CommandExecutor {
    @Override
    public void execute(Message message, String[] args) {
        if (args.length == 0) {
            message.respond().error("You did not mention a user.").queue();
            return;
        }

        // SEARCH USERS
        final Member client;

        List<User> mentioned = message.getMentionedUsers();
        if (mentioned.size() > 0) {
            client = getGuild().getMember(mentioned.get(0));
        } else {
            client = getGuildData().getMemberByName(StringUtils.join(args, " "), true);
        }

        if (client == null) {
            message.respond().error("You did not mention a valid user.").queue();
            return;
        }


        message.respond().embed("Who is " + client.getName() + "?")
                .setColor(Constants.COLOR)
                .setThumbnail(client.getAvatarUrl())
                .field("Name", true, client.getName())
                .field("Nickname", true, client.getNickname() != null ? client.getNickname() : "No nickname.")

                .field("ID", true, client.getId())
                .field("Game", true, client.getGame() != null ? client.getGame().getName() : "No game.")

                .field("Discriminator", true, client.getDiscriminator())

                .field("Roles", false, () -> {
                    StringBuilder sb = new StringBuilder();
                    for (Role role : client.getRoles()) {
                        sb.append("• ").append(role.getName()).append('\n');
                    }
                    return sb.toString();
                })
                .rest().queue();
    }
}
