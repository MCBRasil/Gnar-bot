package xyz.gnarbot.gnar.commands.executors.mod;

import com.google.inject.Inject;
import net.dv8tion.jda.core.MessageHistory;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.members.Clearance;
import xyz.gnarbot.gnar.servers.Host;
import xyz.gnarbot.gnar.utils.Note;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Command(aliases = {"prune", "delmessages", "delmsgs"},
         usage = "-amount -words...",
         description = "Delete up to 100 messages.",
         clearance = Clearance.BOT_COMMANDER)
public class PruneCommand extends CommandExecutor
{
    @Inject
    public Host host;
    
    @Override
    public void execute(Note note, String label, String[] args)
    {
        try
        {
            if (!note.getAuthor().hasPermission(Permission.MESSAGE_MANAGE))
            {
                note.error("You do not have permission to delete messages.");
                return;
            }
            
            if (args.length == 0)
            {
                note.error("Insufficient amount of arguments.");
                return;
            }
            
            note.delete();
            
            MessageHistory history = note.getChannel().getHistory();
            
            int amount = Integer.parseInt(args[0]);
            amount = Math.min(amount, 100);
            
            if (amount < 2)
            {
                note.error("You need to delete 2 or more messages to use this command.");
                return;
            }
            
            List<Message> msgs = history.retrievePast(amount).complete();
            
            if (args.length >= 2)
            {
                String[] filter = Arrays.copyOfRange(args, 1, args.length);
                
                List<Message> _msgs = new ArrayList<>();
                
                for (Message msg : msgs)
                {
                    for (String word : filter)
                    {
                        if (msg.getContent().contains(word))
                        {
                            _msgs.add(msg);
                        }
                    }
                }
                msgs = _msgs;
            }
            
            note.getTextChannel().deleteMessages(msgs).queue();
            
            Note info = note.info("Attempted to delete **[" + msgs.size() + "]()** messages.\nDeleting this message in **5** seconds.").get();
            
            info.delete(5);
        }
        catch (NumberFormatException e)
        {
            note.error("Improper arguments supplies, must be a number.");
        }
        catch (InterruptedException | ExecutionException e)
        {
            note.error("Delete queue was interrupted.");
        }
    }
}
