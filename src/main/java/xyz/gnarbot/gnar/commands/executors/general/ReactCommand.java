package xyz.gnarbot.gnar.commands.executors.general;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.util.concurrent.ExecutionException;

//TODO REMOVE THE EDITZ
@Command(aliases = "react",
         usage = "(messageid) (emoji)",
         description = "Make GNAR react to something, against it's " + "will. You evil prick.")
public class ReactCommand extends CommandExecutor
{
    @Override
    public void execute(Note note, String label, String[] args)
    {
        if (args.length < 2)
        {
            note.error("Insufficient arguments. `" + this.getUsage() + "`");
            return;
        }
        String msgid = args[0];
        
        Message msg = note.getChannel().getMessageById(msgid).complete();
        
        if (note.getEmotes().size() > 0)
        {
            Emote em = note.getEmotes().get(0);
            msg.addReaction(em).queue();
    
            try
            {
                note.info("Reacted to the message. :smile:").get().delete(5);
            }
            catch (InterruptedException | ExecutionException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            note.error("No reactions detected, robot.");
        }
    }
}



