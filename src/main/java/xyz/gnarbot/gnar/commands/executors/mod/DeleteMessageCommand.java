package xyz.gnarbot.gnar.commands.executors.mod;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.commands.handlers.Category;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Command(aliases = "deletemsg",
        usage = "-msg_id...",
        description = "Delete those messages..",
        category = Category.MODERATION,
        channelPermissions = Permission.MESSAGE_MANAGE)
public class DeleteMessageCommand extends CommandExecutor {
    @Override
    public void execute(Message message, List<String> args) {
        if (args.isEmpty()) {
            message.respond().error("Please input message ID(s) to queue them for deletion.").queue();
            return;
        }

        List<String> ids = args.subList(1, args.size());
        List<Message> list = new ArrayList<>();

        for (String id : ids) {
            list.add(message.respond().getChannel().getMessageById(id).complete());
        }
        if (list.size() < 2) {
            for (Message msg : list) {
                msg.delete().queue();
            }

            message.respond().info("Deleted the message.\nDeleting this message in **5** seconds.")
                    .queue(msg -> getBot().getScheduler().schedule(
                            () -> msg.delete().queue(), 5, TimeUnit.SECONDS));
        } else {
            message.getTextChannel().deleteMessages(list).queue();

            message.respond().info("Attempted to delete **[" + list.size()
                    + "]()** messages.\nDeleting this message in **5** seconds.")
                    .queue(it -> getBot().getScheduler().schedule(
                            () -> it.delete().queue(), 5, TimeUnit.SECONDS));
        }
    }
}



