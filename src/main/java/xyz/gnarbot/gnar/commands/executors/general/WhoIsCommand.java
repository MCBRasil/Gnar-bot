package xyz.gnarbot.gnar.commands.executors.general;

import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Member;
import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.members.HostUser;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;
import java.util.StringJoiner;

@Command(aliases = {"whois", "infoof", "infoon", "user"}, usage = "-@user", description = "Get information on a user.")
public class WhoIsCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
        if (args.isEmpty()) {
            note.error("You did not mention a user.");
            return;
        }

        // SEARCH USERS
        HostUser hostUser = null;

        List<HostUser> mentioned = note.getMentionedUsers();
        if (mentioned.size() > 0) {
            hostUser = mentioned.get(0);
        } else { // Real Name > Nick Name > Contains
            String query = StringUtils.join(args, " ");

            for (Member m : note.getGuild().getMembersByName(query, true)) {
                hostUser = new HostUser(note.getHost(), m);
            }
            if (hostUser == null) {
                for (Member m : note.getGuild().getMembersByNickname(query, true)) {
                    hostUser = new HostUser(note.getHost(), m);
                }
            }
            if (hostUser == null) { // JUST IN CASE
                for (Member m : note.getGuild().getMembers()) {
                    if (m.getUser().getName().toLowerCase().contains(query.toLowerCase())) {
                        hostUser = new HostUser(note.getHost(), m);
                    }
                }
            }
        }

        if (hostUser == null) {
            note.error("You did not mention a valid user.");
            return;
        }

        StringBuilder mainBuilder = new StringBuilder();


        String nickname = note.getGuild().getMember(hostUser).getNickname();
        Game game = note.getGuild().getMember(hostUser).getGame();

        StringJoiner metaBuilder = new StringJoiner("\n");
        metaBuilder.add("Name: **[" + hostUser.getName() + "#" + hostUser.getDiscriminator() + "]()**");
        metaBuilder.add("ID: **[" + hostUser.getId() + "]()**");
        metaBuilder.add("");
        metaBuilder.add("__**General**__");
        metaBuilder.add("Nick: **[" + (nickname != null ? nickname : "None") + "]()**");
        metaBuilder.add("Game: **[" + (game != null ? game.getName() : "None") + "]()**");
        metaBuilder.add("Bot: **[" + String.valueOf(hostUser.isBot()).toUpperCase() + "]()**");
        metaBuilder.add("Level: **[" + hostUser.getLevel().toString().replaceAll("_", " ") + "]()**");
        metaBuilder.add("\n");

        mainBuilder.append(metaBuilder.toString());

        mainBuilder.append("__**Roles**__").append('\n');

        hostUser.getRoles()
                .stream()
                .filter(role -> !mainBuilder.toString().contains(role.getId()))
                .forEach(role -> mainBuilder.append("- **[").append(role.getName()).append("]()**").append('\n'));

        note.respond("Who is " + hostUser.getName() + "?", mainBuilder.toString()
                .replaceAll("null", "None"), Bot.getColor(), hostUser.getAvatarUrl());
    }
}
