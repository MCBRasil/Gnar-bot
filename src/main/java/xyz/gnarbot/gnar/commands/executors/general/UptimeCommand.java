package xyz.gnarbot.gnar.commands.executors.general;

import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

@Command(aliases = "uptime", description = "Show the bot's uptime.")
public class UptimeCommand extends CommandExecutor
{
    @Override
    public void execute(Note note, String label, String[] args)
    {
        note.replyEmbedRaw("Bot Uptime", Bot.INSTANCE.getUptime());
    }
}
