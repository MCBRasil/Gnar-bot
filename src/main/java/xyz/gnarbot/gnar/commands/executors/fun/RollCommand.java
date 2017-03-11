package xyz.gnarbot.gnar.commands.executors.fun;

import xyz.gnarbot.gnar.Constants;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;
import java.util.Random;

@Command(aliases = {"roll"},
        usage = "-max_value",
        description = "Roll a random number from 0 to argument.")
public class RollCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
        try {
            if (args.size() >= 1) {
                if (!(Integer.valueOf(args.get(0)) > 0)) {
                    note.respond().error("Number need to be > 0.").queue();
                    return;
                }

                note.respond().embed("Roll a Number")
                        .setColor(Constants.COLOR)
                        .description(sb -> {
                            sb.append("You rolled a **")
                                    .append(new Random().nextInt(Integer.valueOf(args.get(0))))
                                    .append("** from range **[0 to ")
                                    .append(args.get(0))
                                    .append("]**.");
                        })
                        .rest().queue();

            } else {
                note.respond().error("Insufficient amount of arguments.").queue();
            }
        } catch (Exception e) {
            note.respond().error("Only numbers are allowed.\n\n**Example:**\n\n[_roll 10]()").queue();
        }
    }
}
