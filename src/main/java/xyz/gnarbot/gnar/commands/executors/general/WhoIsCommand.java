package xyz.gnarbot.gnar.commands.executors.general;

import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Member;
import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.members.Person;
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
        Person person = null;
        if (note.getMentionedUsers().size() > 0) {
            person = note.getMentionedUsers().get(0);
        } else { // Real Name > Nick Name > Contains
            String query = StringUtils.join(args, " ");

            for (Member m : note.getGuild().getMembersByName(query, true)) {
                person = new Person(note.getHost(), m);
            }
            if (person == null) {
                for (Member m : note.getGuild().getMembersByNickname(query, true)) {
                    person = new Person(note.getHost(), m);
                }
            }
            if (person == null) { // JUST IN CASE
                for (Member m : note.getGuild().getMembers()) {
                    if (m.getUser().getName().toLowerCase().contains(query.toLowerCase())) {
                        person = new Person(note.getHost(), m);
                    }
                }
            }
        }

        if (person == null) {
            note.error("You did not mention a valid user.");
            return;
        }

        StringBuilder mainBuilder = new StringBuilder();


        String nickname = note.getGuild().getMember(person).getNickname();
        Game game = note.getGuild().getMember(person).getGame();

        StringJoiner metaBuilder = new StringJoiner("\n");
        metaBuilder.add("Name: **[" + person.getName() + "#" + person.getDiscriminator() + "]()**");
        metaBuilder.add("ID: **[" + person.getId() + "]()**");
        metaBuilder.add("");
        metaBuilder.add("__**General**                                                      __");
        metaBuilder.add("  Nick: **[" + (nickname != null ? nickname : "None") + "]()**");
        metaBuilder.add("  Game: **[" + (game != null ? game.getName() : "None") + "]()**");
        metaBuilder.add("  Bot: **[" + String.valueOf(person.isBot()).toUpperCase() + "]()**");
        metaBuilder.add("  Clearance: **[" + person.getClearance().toString().replaceAll("_", " ") + "]()**");
        metaBuilder.add("\n");

        mainBuilder.append(metaBuilder.toString());

        mainBuilder.append("__**Roles**                                                           __").append('\n');

        person.getRoles()
                .stream()
                .filter(role -> !mainBuilder.toString().contains(role.getId()))
                .forEach(role -> mainBuilder.append("  - **[").append(role.getName()).append("]()**").append('\n'));

        note.replyEmbedRaw("Who is " + person.getName() + "?", mainBuilder.toString()
                .replaceAll("null", "None"), Bot.getColor(), person.getAvatarUrl());
    }
}
