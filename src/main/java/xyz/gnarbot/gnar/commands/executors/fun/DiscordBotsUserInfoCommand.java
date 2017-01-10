package xyz.gnarbot.gnar.commands.executors.fun;

import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.DiscordBotUserInfo;
import xyz.gnarbot.gnar.utils.Note;

@Deprecated // Dis really needed?
@Command(aliases = "dui", showInHelp = false)
public class DiscordBotsUserInfoCommand extends CommandExecutor
{
    @Override
    public void execute(Note note, String label, String[] args)
    {
        if (args.length > 0)
        {
            if (note.getMentionedUsers().size() == 0)
            {
                String data = DiscordBotUserInfo.getUserInfo(args[0]);
                note.replyEmbed("DiscordBots User Info", data);
            }
            else
            {
                String data = DiscordBotUserInfo.getUserInfo(note.getMentionedUsers().get(0).getId());
                note.replyEmbed("DiscordBots User Info", data);
            }
        }
        else
        {
            note.error("You must supply a bot ID or mention.");
        }
    }
    
}
