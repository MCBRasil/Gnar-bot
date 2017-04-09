package xyz.gnarbot.gnar.commands.executors.general;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.Constants;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@Command(aliases = "ping",
        description = "Show the bot's current response time.")
public class PingCommand extends CommandExecutor {
    @Override
    public void execute(Message message, String[] args) {
        OffsetDateTime sentTime = message.getCreationTime();

        message.respond().embed("Ping")
                .setColor(Constants.COLOR)
                .setDescription("Checking ping...")
                .rest().queue(msg -> msg.editMessage(new EmbedBuilder().setTitle("Response Time")
                        .setColor(Constants.COLOR)
                        .field("Response Time", true, sb -> {
                            long ping = Math.abs(sentTime.until(msg.getCreationTime(), ChronoUnit.MILLIS));
                            sb.append(ping).append(" ms");
                        })
                        .field("JDA Heartbeat", true, sb -> {
                            sb.append(getJDA().getPing()).append(" ms");
                        })
                        .build()).queue());
    }
}
