package xyz.gnarbot.gnar.commands.executors.mod;

import com.google.inject.Inject;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.members.Level;
import xyz.gnarbot.gnar.servers.Servlet;
import xyz.gnarbot.gnar.utils.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Command(aliases = {"prune", "delmessages", "delmsgs"},
        usage = "-amount -words...",
        description = "Delete up to 100 messages.",
        level = Level.BOT_COMMANDER)
public class PruneCommand extends CommandExecutor {
    @Inject
    public Servlet servlet;

    @Override
    public void execute(Note note, List<String> args) {
        if (!note.getAuthor().hasPermission(Permission.MESSAGE_MANAGE)) {
            note.respond().error("You don't have the `Manage Messages` permission!").queue();
            return;
        }

        if (args.isEmpty()) {
            note.respond().error("Insufficient amount of arguments.").queue();
            return;
        }

        try {
            note.delete().queue();

            MessageHistory history = note.respond().getChannel().getHistory();

            int amount = Integer.parseInt(args.get(0));
            amount = Math.min(amount, 100);

            if (amount < 2) {
                note.respond().error("You need to delete 2 or more messages to use this command.").queue();
                return;
            }

            List<Message> msgs = history.retrievePast(amount).complete();

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

            note.getTextChannel().deleteMessages(msgs).queue();

            Message msg = note.respond().info("Attempted to delete **[" + msgs.size() + "]()** messages.\nDeleting this message in **5** seconds.")
                    .complete();

            Bot.INSTANCE.getScheduler().schedule(() -> msg.delete().queue(), 5, TimeUnit.SECONDS);
        } catch (NumberFormatException e) {
            note.respond().error("Improper arguments supplies, must be a number.").queue();
        }
    }
}
