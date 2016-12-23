package xyz.gnarbot.gnar.commands.fun;

import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

@Command(
        aliases = "discordgold",
        description = "Tilts dumb people."
)
public class DiscordGoldCommand extends CommandExecutor
{
    @Override
    public void execute(Note message, String label, String[] args)
    {
        message.delete();
        message.replyRaw("```xl\nDiscord Gold is required to view this message.\n```");
    }
}
