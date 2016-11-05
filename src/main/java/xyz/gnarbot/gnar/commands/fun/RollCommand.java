package xyz.gnarbot.gnar.commands.fun;

import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.util.Random;

@Command(
        aliases = {"roll"},
        usage = "(integer)",
        description = "Roll a random number from 0 to argument."
)
public class RollCommand extends CommandExecutor
{
    @Override
    public void execute(Note message, String label, String[] args)
    {
        if (args.length >= 1)
        {
            if (!(Integer.valueOf(args[0]) > 0))
            {
                message.reply("Number need to be > 0.");
                
                return;
            }
            message.reply(String.format("You rolled `%d` from range `0 to %s`.", new Random().nextInt(Integer.valueOf(args[0])), args[0]));
        }
        else
        {
            message.reply("Insufficient amount of arguments.");
        }
    }
}
