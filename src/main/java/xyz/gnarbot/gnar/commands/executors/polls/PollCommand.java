package xyz.gnarbot.gnar.commands.executors.polls;

import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.commands.handlers.Category;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;

// TODO recode command.
@Command(aliases = "poll",
        usage = "(argument)",
        description = "Do poll-y stuff!",
        category = Category.NONE)
public class PollCommand extends CommandExecutor {
    @Override
    public void execute(Message message, String[] args) {

        //TODO rewrite polls
        message.respond().error("This command is going to be rewritten soon.").queue();

//        if (args.length == 0 || (!args.length == 0 && args[0].equalsIgnoreCase("help"))) {
//            String reply = "Poll System Help!~\n {} = Required Arguments  |  () = Optional Arguments```ini\n" +
//                    "[_poll help] This list\n" + "[_poll startyesno {time} {question}] Start a Yes/No Poll for " +
//                    "\"time\" minutes." + "\n```";
//            message.respond().info(reply).queue();
//        } else if (args.length > 0) {
//            if (args[0].equalsIgnoreCase("startyesno") && args.length > 1) {
//                int time = 15;
//
//                try {
//                    time = Integer.parseInt(args[1].trim());
//                } catch (NumberFormatException ignore) {}
//
//                args.set(0, "");
//                args.set(1, "");
//                StringBuilder q = new StringBuilder();
//                for (String s : args) {
//                    if (!s.equalsIgnoreCase("")) {
//                        q.append(s).append(" ");
//                    }
//                }
//                q = new StringBuilder(q.toString().trim());
//                PollManager.registerPoll(new YesNoPoll(message, q.toString(), time, getBot()));
//            }
//        }
    }
}
