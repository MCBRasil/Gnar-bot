package xyz.gnarbot.gnar.commands.executors.general;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;

//TODO REMOVE THE EDITZ
@Command(aliases = "react",
        usage = "(message-id) (emoji...)",
        description = "Make GNAR react to something, against it's " + "will. You evil prick.")
public class ReactCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
        if (args.size() < 2) {
            note.respond().error("Insufficient arguments. `" + meta().usage() + "`").queue();
            return;
        }

        String msg_id = args.get(0);
        Message msg = note.getChannel().getMessageById(msg_id).complete();

        if (note.getEmotes().size() > 0) {
            for (Emote em : note.getEmotes()) {
                msg.addReaction(em).queue();
            }

        } else {
            boolean suc = false;

            List<String> reactions = args.subList(1, args.size());

            for (String r : reactions) {
                msg.addReaction(r).queue();
                suc = true;
            }

//            if (suc) {
////                note.info("Reacted to the message with " + (args.size() - 1) + " emotes. :smile:")
////                        .complete()
////                        .optDelete(5);
////                return;
//            }

            if (!suc) {
                note.respond().error("No reactions detected, robot.").queue();
            }
        }
    }
}



