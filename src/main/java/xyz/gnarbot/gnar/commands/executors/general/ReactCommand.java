package xyz.gnarbot.gnar.commands.executors.general;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;
import xyz.gnarbot.gnar.utils.Utils;

import java.util.List;

//TODO REMOVE THE EDITZ
@Command(aliases = "react",
        usage = "(messageid) (emoji)",
        description = "Make GNAR react to something, against it's " + "will. You evil prick.")
public class ReactCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
        if (args.size() < 2) {
            note.error("Insufficient arguments. `" + this.getUsage() + "`").queue();
            return;
        }
        String msgid = args.get(0);
        Message msg = note.getChannel().getMessageById(msgid).complete();
        if (note.getEmotes().size() > 0) {
            for (Emote em : note.getEmotes()) {
                msg.addReaction(em).queue();
            }

            note.info("Reacted to the message with " + note.getEmotes().size() + " emotes. :smile:")
                    .complete()
                    .optDelete(5);
        } else {
            args.set(0, "");
            boolean suc = false;
            for (String r : args) {
                if (!r.equalsIgnoreCase("")) {
                    if (Utils.sendReactionAutoEncode(msg, r)) {
                        suc = true;
                    }
                }
            }
            if (suc) {
                note.info("Reacted to the message with " + (args.size() - 1) + " emotes. :smile:")
                        .complete()
                        .optDelete(5);
                return;
            }

            note.error("No reactions detected, robot.").queue();
        }
    }
}



