package xyz.gnarbot.gnar.commands.fun;

import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.util.Random;

@Command(aliases = {"roll"}, usage = "(integer)", description = "Roll a random number from 0 to argument.")
public class RollCommand extends CommandExecutor
{
    @Override
    public void execute(Note note, String label, String[] args)
    {
        if (args.length >= 1)
        {
            if (!(Integer.valueOf(args[0]) > 0))
            {
                note.replyError("Number need to be > 0.");
                
                return;
            }
            note.replyEmbedRaw("Roll a Number", "You rolled __**["
                    + new Random().nextInt(Integer.valueOf(args[0]))
                    + "]()**__ from range **[0 to " + args[0] + "]()**.");
        }
        else
        {
            note.replyError("Insufficient amount of arguments.");
        }
    }
}
