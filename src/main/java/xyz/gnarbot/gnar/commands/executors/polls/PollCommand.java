package xyz.gnarbot.gnar.commands.executors.polls;

import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

// TODO recode command.
@Command(aliases = "poll",
         usage = "(argument)",
         description = "Do poll-y stuff!",
         showInHelp = false)
public class PollCommand extends CommandExecutor
{
    @Override
    public void execute(Note note, String label, String[] args)
    {
        if (args.length == 0 || (args.length > 0 && args[0].equalsIgnoreCase("help")))
        {
            String reply = "Poll System Help!~\n {} = Required Arguments  |  () = Optional Arguments```ini\n" +
                    "[_poll help] This list\n" + "[_poll startyesno {time} {question}] Start a Yes/No Poll for " +
                    "\"time\" minutes." + "\n```";
            note.info(reply);
        }
        else if (args.length > 0)
        {
            if (args[0].equalsIgnoreCase("startyesno") && args.length > 1)
            {
                int time = 15;
                
                try
                {
                    time = Integer.parseInt(args[1].trim());
                }
                catch (NumberFormatException ignore) {}
                
                args[0] = "";
                args[1] = "";
                String q = "";
                for (String s : args)
                {
                    if (!s.equalsIgnoreCase(""))
                    {
                        q += s + " ";
                    }
                }
                q = q.trim();
                PollManager.registerPoll(new YesNoPoll(note, q, time));
            }
        }
    }
}
