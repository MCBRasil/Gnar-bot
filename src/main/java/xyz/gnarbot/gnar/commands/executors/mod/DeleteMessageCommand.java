package xyz.gnarbot.gnar.commands.executors.mod;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.members.Clearance;
import xyz.gnarbot.gnar.utils.Note;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Command(aliases = "deletemsg",
        usage = "-msg_id...",
        description = "Delete those messages..",
        clearance = Clearance.BOT_COMMANDER)
public class DeleteMessageCommand extends CommandExecutor {
    @Override
    public void execute(Note note, String[] args) {
        if (!note.getAuthor().hasPermission(Permission.MESSAGE_MANAGE)) {
            note.error("You don't have the `Manage Messages` permission!");
            return;
        }

        if (args.length < 1) {
            note.error("Please input message ID(s) to queue them for deletion.");
            return;
        }

        String[] ids = Arrays.copyOfRange(args, 1, args.length);
        List<Message> list = new ArrayList<>();

        for (String id : ids) {
            list.add(note.getChannel().getMessageById(id).complete());
        }
        try {
            if (list.size() < 2) {
                for (Message msg : list) {
                    msg.deleteMessage().queue();
                }

                Note info = note.info("Deleted the message.\nDeleting this message in **5** seconds.").get();
                info.delete(5);
            } else {
                note.getTextChannel().deleteMessages(list).queue();

                Note info = note.info("Attempted to delete **[" + list.size()
                        + "]()** messages.\nDeleting this message in **5** seconds.").get();
                info.delete(5);
            }
        } catch (InterruptedException | ExecutionException e) {
            note.error("Delete queue was interrupted.");
        }
    }
}



