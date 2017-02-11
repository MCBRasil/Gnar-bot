package xyz.gnarbot.gnar.commands.executors.general;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;
import xyz.gnarbot.gnar.utils.Utils;

import java.util.List;
import java.util.concurrent.ExecutionException;

//TODO REMOVE THE EDITZ
@Command(aliases = "react",
        usage = "(messageid) (emoji)",
        description = "Make GNAR react to something, against it's " + "will. You evil prick.")
public class ReactCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
        if (args.size() < 2) {
            note.error("Insufficient arguments. `" + this.getUsage() + "`");
            return;
        }
        String msgid = args.get(0);
        Message msg = note.getChannel().getMessageById(msgid).complete();
        if (note.getEmotes().size() > 0) {
            for (Emote em : note.getEmotes()) {
                msg.addReaction(em).queue();
            }

            try {
                note.info("Reacted to the message with " + note.getEmotes().size() + " emotes. :smile:")
                        .get()
                        .optDelete(5);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            args.set(0, "");
            boolean suc = false;
            for (String r : args) {
                if (!r.equalsIgnoreCase("")) {
                    if (Utils.sendReactionAutoEncode(note, r)) {
                        suc = true;
                    }
                }
            }
            if (suc) {
                try {
                    note.info("Reacted to the message with " + (args.size() - 1) + " emotes. :smile:").get().optDelete(5);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                return;
            }

            note.error("No reactions detected, robot.");
        }
    }
}



