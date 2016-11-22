package xyz.gnarbot.gnar.commands.fun;

import com.google.inject.Inject;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.handlers.servers.Host;
import xyz.gnarbot.gnar.utils.DiscordBotUserInfo;
import xyz.gnarbot.gnar.utils.Note;

@Command(aliases = "dui")
public class DiscordBotsUserInfoCommand extends CommandExecutor {

    @Inject
    public Host host;

    @Override
    public void execute(Note msg, String label, String[] args) {
        if (args.length > 0)
        {
            if (msg.getMentionedUsers().size() == 0)
            {
                String data = DiscordBotUserInfo.getUserInfo(args[0]);
                msg.reply(data);
            }
            else
            {
                String data = DiscordBotUserInfo.getUserInfo(msg.getMentionedUsers().get(0).getId());
                msg.reply(data);

            }
        }
        else
        {
            msg.reply("You must supply a bot ID or mention.");
        }
    }

}
