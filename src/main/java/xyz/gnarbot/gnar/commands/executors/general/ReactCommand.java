package xyz.gnarbot.gnar.commands.executors.general;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;

import java.util.Arrays;

//TODO REMOVE THE EDITZ
@Command(aliases = "react",
        usage = "(message-id) (emoji...)",
        description = "Make GNAR react to something, against it's " + "will. You evil prick.")
public class ReactCommand extends CommandExecutor {
    @Override
    public void execute(Message message, String[] args) {
        if (args.length < 2) {
            message.respond().error("Insufficient arguments. `" + getMeta().usage() + "`").queue();
            return;
        }

        message.getChannel().getMessageById(args[0]).queue(msg -> {
            if (message.getEmotes().size() > 0) {
                for (Emote em : message.getEmotes()) {
                    msg.addReaction(em).queue();
                }
            } else {
                String[] reactions = Arrays.copyOfRange(args, 1, args.length);

                if (reactions.length == 0) {
                    message.respond().error("No reactions detected, robot.").queue();
                    return;
                }

                for (String r : reactions) {
                    msg.addReaction(r).queue();
                }
            }
        });
    }
}



