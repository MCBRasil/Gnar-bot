package xyz.gnarbot.gnar.commands.executors.general;

import net.dv8tion.jda.core.entities.Role;
import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.Constants;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.members.Client;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;

@Command(aliases = {"whois", "infoof", "infoon", "user"}, usage = "-@user", description = "Get information on a user.")
public class WhoIsCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
        if (args.isEmpty()) {
            note.respond().error("You did not mention a user.").queue();
            return;
        }

        // SEARCH USERS
        final Client client;

        List<Client> mentioned = note.getMentionedUsers();
        if (mentioned.size() > 0) {
            client = mentioned.get(0);
        } else {
            client = getServlet().getClientByName(StringUtils.join(args, " "), true);
        }

        if (client == null) {
            note.respond().error("You did not mention a valid user.").queue();
            return;
        }


        note.respond().embed("Who is " + client.getName() + "?")
                .setColor(Constants.COLOR)
                .setThumbnail(client.getAvatarUrl())
                .field("Name", true, client.getName())
                .field("Nickname", true, client.getNickname() != null ? client.getNickname() : "No nickname.")

                .field("ID", true, client.getId())
                .field("Game", true, client.getGame() != null ? client.getGame().getName() : "No game.")

                .field("Level", true, client.getLevel().getTitle())
                .field("Discriminator", true, client.getDiscriminator())

                .field("Roles", false, sb -> {
                    for (Role role : client.getRoles()) {
                        sb.append("• ").append(role.getName()).append('\n');
                    }
                })
                .rest().queue();
    }
}
