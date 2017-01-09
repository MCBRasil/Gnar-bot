package xyz.gnarbot.gnar.commands.general;

import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Member;
import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.handlers.members.User;
import xyz.gnarbot.gnar.utils.Note;

import java.util.StringJoiner;

@Command(aliases = {"whois", "infoof", "infoon", "user"}, usage = "(@user)", description = "Get information on a user.")
public class WhoIsCommand extends CommandExecutor
{
    @Override
    public void execute(Note note, String label, String[] args)
    {
        if (args.length == 0)
        {
            note.error("You did not mention a user.");
            return;
        }
        
        // SEARCH USERS
        User user = null;
        if (note.getMentionedUsers().size() > 0)
        {
            user = note.getMentionedUsers().get(0);
        }
        else
        { // Real Name > Nick Name > Contains
            String query = StringUtils.join(args, " ");
            
            for (Member m : note.getGuild().getMembersByName(query, true))
            {
                user = new User(note.getHost(), m);
            }
            if (user == null)
            {
                for (Member m : note.getGuild().getMembersByNickname(query, true))
                {
                    user = new User(note.getHost(), m);
                }
            }
            if (user == null)
            { // JUST IN CASE
                for (Member m : note.getGuild().getMembers())
                {
                    if (m.getUser().getName().toLowerCase().contains(query.toLowerCase()))
                    {
                        user = new User(note.getHost(), m);
                    }
                }
            }
        }
        
        if (user == null)
        {
            note.error("You did not mention a valid user.");
            return;
        }
        
        StringBuilder mainBuilder = new StringBuilder();
        
        
        String nickname = note.getGuild().getMember(user).getNickname();
        Game game = note.getGuild().getMember(user).getGame();
        
        StringJoiner metaBuilder = new StringJoiner("\n");
        metaBuilder.add("Name: **[" + user.getName() + "#" + user.getDiscriminator() + "]()**");
        metaBuilder.add("ID: **[" + user.getId() + "]()**");
        metaBuilder.add("");
        metaBuilder.add("__**General**                                                      __");
        metaBuilder.add("  Nick: **[" + (nickname != null ? nickname : "None") + "]()**");
        metaBuilder.add("  Game: **[" + (game != null ? game.getName() : "None") + "]()**");
        metaBuilder.add("  Bot: **[" + String.valueOf(user.isBot()).toUpperCase() + "]()**");
        metaBuilder.add("  Clearance: **[" + user.getClearance().toString().replaceAll("_", " ") + "]()**");
        metaBuilder.add("\n");
        
        mainBuilder.append(metaBuilder.toString());
        
        mainBuilder.append("__**Roles**                                                           __").append('\n');
        
        user.getRoles().stream()
                .filter(role -> !mainBuilder.toString().contains(role.getId()))
                .forEach(role -> mainBuilder.append("  - **[").append(role.getName()).append("]()**").append('\n'));
        
        note.replyEmbedRaw("Who is " + user.getName() + "?",
                mainBuilder.toString().replaceAll("null", "None"),
                Bot.getColor(),
                user.getAvatarUrl());
    }
}
