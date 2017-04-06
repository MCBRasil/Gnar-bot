package xyz.gnarbot.gnar.commands.executors.general;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;

import java.util.List;

//TODO REMOVE THE EDITZ
@Command(aliases = "react",
        usage = "(message-id) (emoji...)",
        description = "Make GNAR react to something, against it's " + "will. You evil prick.")
public class ReactCommand extends CommandExecutor {
    @Override
    public void execute(Message message, List<String> args) {
        if (args.size() < 2) {
            message.respond().error("Insufficient arguments. `" + getMeta().usage() + "`").queue();
            return;
        }

        String msg_id = args.get(0);
        Message msg = message.getChannel().getMessageById(msg_id).complete();

        if (message.getEmotes().size() > 0) {
            for (Emote em : message.getEmotes()) {
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
                message.respond().error("No reactions detected, robot.").queue();
            }
        }
    }
}



