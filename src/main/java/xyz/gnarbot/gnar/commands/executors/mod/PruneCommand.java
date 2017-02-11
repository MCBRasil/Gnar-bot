package xyz.gnarbot.gnar.commands.executors.mod;

import com.google.inject.Inject;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.members.Level;
import xyz.gnarbot.gnar.servers.Host;
import xyz.gnarbot.gnar.utils.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Command(aliases = {"prune", "delmessages", "delmsgs"},
        usage = "-amount -words...",
        description = "Delete up to 100 messages.",
        level = Level.BOT_COMMANDER)
public class PruneCommand extends CommandExecutor {
    @Inject
    public Host host;

    @Override
    public void execute(Note note, List<String> args) {
        if (!note.getAuthor().hasPermission(Permission.MESSAGE_MANAGE)) {
            note.error("You don't have the `Manage Messages` permission!");
            return;
        }

        if (args.isEmpty()) {
            note.error("Insufficient amount of arguments.");
            return;
        }

        try {
            note.optDelete();

            MessageHistory history = note.getChannel().getHistory();

            int amount = Integer.parseInt(args.get(0));
            amount = Math.min(amount, 100);

            if (amount < 2) {
                note.error("You need to delete 2 or more messages to use this command.");
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

            Note info = note.info("Attempted to delete **[" + msgs.size() + "]()** messages.\nDeleting this message in **5** seconds.")
                    .get();

            info.optDelete(5);
        } catch (NumberFormatException e) {
            note.error("Improper arguments supplies, must be a number.");
        } catch (InterruptedException | ExecutionException e) {
            note.error("Delete queue was interrupted.");
        }
    }
}
