package xyz.gnarbot.gnar.commands.executors.fun;

import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.Constants;
import xyz.gnarbot.gnar.commands.Category;
import xyz.gnarbot.gnar.commands.Command;
import xyz.gnarbot.gnar.commands.CommandExecutor;

import java.util.Random;

@Command(
        aliases = {"roll"},
        usage = "-max_value",
        description = "Roll a random number from 0 to argument.",
        category = Category.FUN)
public class RollCommand extends CommandExecutor {
    @Override
    public void execute(Message message, String[] args) {
        try {
            if (args.length >= 1) {
                if (!(Integer.valueOf(args[0]) > 0)) {
                    message.respond().error("Number need to be > 0.").queue();
                    return;
                }

                message.respond().embed("Roll a Number")
                        .setColor(Constants.COLOR)
                        .description(sb -> {
                            sb.append("You rolled a **")
                                    .append(new Random().nextInt(Integer.valueOf(args[0])))
                                    .append("** from range **[0 to ")
                                    .append(args[0])
                                    .append("]**.");
                        })
                        .rest().queue();

            } else {
                message.respond().error("Insufficient amount of arguments.").queue();
            }
        } catch (Exception e) {
            message.respond().error("Only numbers are allowed.\n\n**Example:**\n\n[_roll 10]()").queue();
        }
    }
}
