package xyz.gnarbot.gnar.commands.general;

import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

@Command(
        aliases = {"invite", "invitebot"},
        description = "Get a link to invite GN4R to your server."
)
public class InviteBotCommand extends CommandExecutor
{
    @Override
    public void execute(Note note, String label, String[] args)
    {
        //message.replyRaw("**" + GnarQuotes.getRandomQuote() + "** Want some GN4R on your server?!\n**0Auth Link:** " + message.getJDA().getSelfInfo().getAuthUrl(Permission.ADMINISTRATOR));
    }
}
