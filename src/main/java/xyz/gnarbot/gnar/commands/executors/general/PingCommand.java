package xyz.gnarbot.gnar.commands.executors.general;

import xyz.gnarbot.gnar.Constants;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Command(aliases = "ping", description = "Show the bot's current response time.")
public class PingCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
        OffsetDateTime sentTime = note.getCreationTime();

        note.respond().embed("Response Time")
                .setColor(Constants.COLOR)
                .setDescription("Checking ping...")
                .rest().queue(message -> (message.editMessage(Math.abs(sentTime.until(message.getCreationTime(), ChronoUnit.MILLIS)) + " ms\"")).queue());
    }
}
