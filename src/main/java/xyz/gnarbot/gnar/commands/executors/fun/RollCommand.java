package xyz.gnarbot.gnar.commands.executors.fun;

import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.util.Random;

@Command(aliases = {"roll"},
         usage = "(integer)",
         description = "Roll a random number from 0 to argument.")
public class RollCommand extends CommandExecutor
{
    @Override
    public void execute(Note note, String label, String[] args)
    {
        if (args.length >= 1)
        {
            if (!(Integer.valueOf(args[0]) > 0))
            {
                note.error("Number need to be > 0.");
                
                return;
            }
            note.replyEmbedRaw("Roll a Number", "You rolled __**[" + new Random().nextInt(Integer.valueOf(args[0])) +
                    "]()**__ from range **[0 to " + args[0] + "]()**.");
        }
        else
        {
            note.error("Insufficient amount of arguments.");
        }
    }
}
