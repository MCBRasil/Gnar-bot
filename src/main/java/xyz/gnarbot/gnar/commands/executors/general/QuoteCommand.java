package xyz.gnarbot.gnar.commands.executors.general;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import xyz.gnarbot.gnar.Constants;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Command(aliases = {"quote", "quotemsg"},
        usage = "(message id) [#channel]",
        description = "Quote somebody else..")
public class QuoteCommand extends CommandExecutor {
    @Override
    public void execute(Message message, List<String> args) {
        if (args.isEmpty()) {
            message.respond().error("Provide a message id.").queue();
            return;
        }

        TextChannel targetChannel = message.getTextChannel();
        if (message.getMentionedChannels().size() > 0) {
            targetChannel = message.getMentionedChannels().get(0);
        }

        for (String id : args) {
            if (!id.contains("#")) {
                try {
                    final TextChannel _targetChannel = targetChannel;
                    message.getChannel().getMessageById(id).queue(msg -> _targetChannel.send().embed()
                            .setColor(Constants.COLOR)
                            .setAuthor(msg.getAuthor().getName(), null, msg.getAuthor().getAvatarUrl())
                            .setDescription(msg.getContent())
                            .rest().queue());

                } catch (Exception e) {
                    try {
                        message.respond()
                                .error("Could not find a message with the ID " + id + " within this channel.")
                                .queue(msg -> getBot().getScheduler().schedule(
                                        () -> msg.delete().queue(), 5, TimeUnit.SECONDS));
                    } catch (Exception ignore) {}
                }
            }
        }

        message.respond().info("Sent quotes to the " + targetChannel.getName() + " channel!")
                .queue(msg -> getBot().getScheduler().schedule(
                () -> msg.delete().queue(), 5, TimeUnit.SECONDS));
    }
}



