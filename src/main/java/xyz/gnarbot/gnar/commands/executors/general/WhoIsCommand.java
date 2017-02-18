package xyz.gnarbot.gnar.commands.executors.general;

import com.google.inject.Inject;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Role;
import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.members.Person;
import xyz.gnarbot.gnar.servers.Host;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;

@Command(aliases = {"whois", "infoof", "infoon", "user"}, usage = "-@user", description = "Get information on a user.")
public class WhoIsCommand extends CommandExecutor {
    @Inject
    private Host host;

    @Override
    public void execute(Note note, List<String> args) {
        if (args.isEmpty()) {
            note.error("You did not mention a user.");
            return;
        }

        // SEARCH USERS
        final Person person;

        List<Person> mentioned = note.getMentionedUsers();
        if (mentioned.size() > 0) {
            person = mentioned.get(0);
        } else {
            person = host.getPerson(StringUtils.join(args, " "), true);
        }

        if (person == null) {
            note.error("You did not mention a valid user.");
            return;
        }

        String nickname = note.getGuild().getMember(person).getNickname();
        Game game = note.getGuild().getMember(person).getGame();

        note.embed()
                .title("Who is " + person.getName() + "?")
                .setThumbnail(person.getAvatarUrl())
                .setColor(Bot.getColor())
                .description(sb -> {
                    sb.append("Name: **[");
                    sb.append(person.getName()).append("#").append(person.getDiscriminator()).append("]()**\n");
                    sb.append("ID: **[").append(person.getId()).append("]()**\n\n");
                    sb.append("__**General**__\n");
                    sb.append("Nick: **[").append(nickname != null ? nickname : "None").append("]()**\n");
                    sb.append("Game: **[").append(game != null ? game.getName() : "None").append("]()**\n");
                    sb.append("Bot: **[").append(String.valueOf(person.isBot()).toUpperCase()).append("]()**\n");
                    sb.append("Level: **[").append(person.getLevel().toString().replaceAll("_", " ")).append("]()**\n\n");
                    sb.append("__**Roles**__").append('\n');

                    for (Role role : person.getRoles()) {
                        sb.append("• **[").append(role.getName()).append("]()**").append('\n');
                    }
                })
                .respond();
    }
}
