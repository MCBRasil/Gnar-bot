package xyz.gnarbot.gnar.commands.executors.mod;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.members.Level;
import xyz.gnarbot.gnar.utils.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Command(aliases = "deletemsg",
        usage = "-msg_id...",
        description = "Delete those messages..",
        level = Level.BOT_COMMANDER)
public class DeleteMessageCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
        if (!note.getAuthor().hasPermission(Permission.MESSAGE_MANAGE)) {
            note.respond().error("You don't have the `Manage Messages` permission!").queue();
            return;
        }

        if (args.isEmpty()) {
            note.respond().error("Please input message ID(s) to queue them for deletion.").queue();
            return;
        }

        List<String> ids = args.subList(1, args.size());
        List<Message> list = new ArrayList<>();

        for (String id : ids) {
            list.add(note.respond().getChannel().getMessageById(id).complete());
        }
        if (list.size() < 2) {
            for (Message msg : list) {
                msg.delete().queue();
            }

            Message msg = note.respond().info("Deleted the message.\nDeleting this message in **5** seconds.")
                    .complete();

            Bot.INSTANCE.getScheduler().schedule(() -> msg.delete().queue(), 5, TimeUnit.SECONDS);
        } else {
            note.getTextChannel().deleteMessages(list).queue();

            Message msg = note.respond().info("Attempted to delete **[" + list.size()
                    + "]()** messages.\nDeleting this message in **5** seconds.")
                    .complete();

            Bot.INSTANCE.getScheduler().schedule(() -> msg.delete().queue(), 5, TimeUnit.SECONDS);
        }
    }
}



