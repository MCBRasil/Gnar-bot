package xyz.gnarbot.gnar.commands.general;

import com.google.inject.Inject;
import net.dv8tion.jda.core.entities.Game;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.handlers.members.User;
import xyz.gnarbot.gnar.handlers.servers.Host;
import xyz.gnarbot.gnar.utils.Note;

import java.util.StringJoiner;

@Command(aliases = {"whois", "infoof", "infoon"}, usage = "(@user)", description = "Get information on a user.")
public class WhoIsCommand extends CommandExecutor
{
    @Inject
    private Host host;
    
    @Override
    public void execute(Note message, String label, String[] args)
    {
        
        if (args.length == 0)
        {
            message.reply("You did not mention a valid user.");
            return;
        }
        
        User user = message.getMentionedUsers().get(0);
        
        if (user == null)
        {
            message.reply("You did not mention a valid user.");
            return;
        }
        
        StringBuilder mainBuilder = new StringBuilder();
        
        
        String nickname = message.getGuild().getMember(user).getNickname();
        Game game = message.getGuild().getMember(user).getGame();
        String avatarID = user.getAvatarId();
        
        StringJoiner metaBuilder = new StringJoiner("\n");
        metaBuilder.add("\u258C ID _________ " + user.getId());
        metaBuilder.add("\u258C Name _______ " + user.getName());
        metaBuilder.add("\u258C Nickname ___ " + (nickname != null ? nickname : "None"));
        metaBuilder.add("\u258C Game _______ " + (game != null ? game.getName() : "None"));
        metaBuilder.add("\u258C Avatar _____ " + (avatarID != null ? avatarID : "Error"));
        metaBuilder.add("\u258C Disc. ______ " + user.getDiscriminator());
        metaBuilder.add("\u258C Bot ________ " + String.valueOf(user.isBot()).toUpperCase());
        metaBuilder.add("\u258C Gn4r Perm __ " + user.getClearance().toString().replaceAll("_", " "));
        metaBuilder.add("\u258C \n");
        
        mainBuilder.append(metaBuilder.toString());
        
        mainBuilder.append("\u258C Roles ______ ").append(user.getRoles().size()).append('\n');
        
        user.getRoles().stream().filter(role -> !mainBuilder.toString().contains(role.getId())).forEach(role ->
                mainBuilder.append("\u258C  - ").append(role.getName()).append('\n'));
        
        message.replyRaw("```xl\n" + mainBuilder.toString().replaceAll("null", "None") + "```");
    }
}
