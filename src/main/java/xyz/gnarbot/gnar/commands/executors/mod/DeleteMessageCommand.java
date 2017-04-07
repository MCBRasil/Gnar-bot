package xyz.gnarbot.gnar.commands.executors.mod;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.commands.handlers.Category;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Command(
        aliases = "deletemsg",
        usage = "-msg_id...",
        description = "Delete those messages..",
        category = Category.MODERATION,
        channelPermissions = Permission.MESSAGE_MANAGE
)
public class DeleteMessageCommand extends CommandExecutor {
    @Override
    public void execute(Message message, List<String> args) {
        if (args.isEmpty()) {
            message.respond().error("Please input message ID(s) to queue them for deletion.").queue();
            return;
        }

        if (args.size() > 3) {
            message.respond().error("Use `_prune` instead if you're going to delete more than 3 messages.").queue();
            return;
        }

        for (String id : args) {
            message.respond().getChannel().getMessageById(id).queue(msg -> msg.delete().queue());
        }

        message.respond().info("Deleted the message.\nDeleting this message in **5** seconds.")
                .queue(msg -> getBot().getScheduler().schedule(
                        () -> msg.delete().queue(), 5, TimeUnit.SECONDS));
    }
}



