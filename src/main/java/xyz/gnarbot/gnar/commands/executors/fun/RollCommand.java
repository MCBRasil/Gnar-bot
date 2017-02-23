package xyz.gnarbot.gnar.commands.executors.fun;

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
                    note.error("Number need to be > 0.");
                    return;
                }

                note.respond("Roll a Number",
                        "You rolled __**[" + new Random().nextInt(Integer.valueOf(args.get(0))) +
                                "]()**__ from range **[0 to " + args.get(0) + "]()**.");
            } else {
                note.error("Insufficient amount of arguments.");
            }
        } catch (Exception e) {
            note.error("Only numbers are allowed.\n\n**Example:**\n\n[_roll 10]()").queue();
        }
    }
}
