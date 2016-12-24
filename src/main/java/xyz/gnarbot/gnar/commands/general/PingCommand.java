package xyz.gnarbot.gnar.commands.general;

import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@Command(aliases = "ping", description = "Show the bot's current response time..")
public class PingCommand extends CommandExecutor
{
    @Override
    public void execute(Note message, String label, String[] args)
    {
        OffsetDateTime sentTime = message.getCreationTime();
        OffsetDateTime responseTime = OffsetDateTime.now();
        
        message.reply("Response time: `" + Math.abs(sentTime.until(responseTime, ChronoUnit.MILLIS)) + "ms`.");
    }
}
