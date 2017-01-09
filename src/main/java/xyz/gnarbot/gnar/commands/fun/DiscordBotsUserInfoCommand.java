package xyz.gnarbot.gnar.commands.fun;

import com.google.inject.Inject;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.handlers.servers.Host;
import xyz.gnarbot.gnar.utils.DiscordBotUserInfo;
import xyz.gnarbot.gnar.utils.Note;

@Deprecated // Dis really needed?
@Command(aliases = "dui", showInHelp = false)
public class DiscordBotsUserInfoCommand extends CommandExecutor
{
    @Inject
    public Host host;
    
    @Override
    public void execute(Note note, String label, String[] args)
    {
        if (args.length > 0)
        {
            if (note.getMentionedUsers().size() == 0)
            {
                String data = DiscordBotUserInfo.getUserInfo(args[0]);
                note.replyEmbed("DiscordBots User Info",data);
            }
            else
            {
                String data = DiscordBotUserInfo.getUserInfo(note.getMentionedUsers().get(0).getId());
                note.replyEmbed("DiscordBots User Info",data);
            }
        }
        else
        {
            note.error("You must supply a bot ID or mention.");
        }
    }
    
}
