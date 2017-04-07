package xyz.gnarbot.gnar.commands.executors.mod;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import xyz.gnarbot.gnar.commands.handlers.Category;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Command(aliases = {"prune", "delmessages", "delmsgs"},
        usage = "-amount -words...",
        description = "Delete up to 100 messages.",
        category = Category.MODERATION,
        channelPermissions = Permission.MESSAGE_MANAGE)
public class PruneCommand extends CommandExecutor {

    @Override
    public void execute(Message message, List<String> args) {
        if (args.isEmpty()) {
            message.respond().error("Insufficient amount of arguments.").queue();
            return;
        }
        
        message.delete().queue();

        MessageHistory history = message.respond().getChannel().getHistory();

        int amount;
        try {
            amount = Integer.parseInt(args.get(0));
            amount = Math.min(amount, 100);
        } catch (NumberFormatException e) {
            message.respond().error("Improper arguments supplies, must be a number.").queue();
            return;
        }

        if (amount < 2) {
            message.respond().error("You need to delete 2 or more messages to use this command.").queue();
            return;
        }

        history.retrievePast(amount).queue(msgs -> {
            if (args.size() >= 2) {
                List<String> filter = args.subList(1, args.size());

                List<Message> _msgs = new ArrayList<>();

                for (Message msg : msgs) {
                    for (String word : filter) {
                        if (msg.getContent().contains(word)) {
                            _msgs.add(msg);
                        }
                    }
                }
                msgs = _msgs;
            }

            message.getTextChannel().deleteMessages(msgs).queue();

            message.respond().info("Attempted to delete **[" + msgs.size() + "]()** messages.\nDeleting this message in **5** seconds.")
                    .queue(msg -> getBot().getScheduler().schedule(
                            () -> msg.delete().queue(), 5, TimeUnit.SECONDS));
        });
    }
}
