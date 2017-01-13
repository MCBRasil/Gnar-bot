package xyz.gnarbot.gnar.commands.executors.general;

import com.mashape.unirest.http.Unirest;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.BotData;
import xyz.gnarbot.gnar.utils.Note;

import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

//TODO REMOVE THE EDITZ
@Command(aliases = "react",
         usage = "(messageid) (emoji)",
         description = "Make GNAR react to something, against it's " + "will. You evil prick.")
public class ReactCommand extends CommandExecutor
{
    public static boolean sendReaction(Message message, String encodedEmoji)
    {
        try
        {
            Unirest.put("https://discordapp.com/api/v6/channels/" + message.getChannel()
                    .getId() + "/messages/" + message.getId() + "/reactions/" + encodedEmoji + "/@me")
                    .header("Authorization", message.getJDA().getToken())
                    .asJsonAsync();
            
            return true;
        }
        catch (Exception ignore)
        {
            return false;
        }
    }
    
    public static boolean sendReactionEncode(Message message, String encodedEmoji)
    {
        try
        {
            return sendReaction(message, URLEncoder.encode(encodedEmoji, "UTF-8"));
        }
        catch (Exception ignore)
        {
            return false;
        }
    }
    
    public static void sendReaction(Message message, Emote emote)
    {
        try
        {
            Unirest.put("https://discordapp.com/api/v6/channels/" + message.getChannel()
                    .getId() + "/messages/" + message.getId() + "/reactions/" + emote.getName() + ":" + emote.getId()
                    + "/@me")
                    .header("Authorization", message.getJDA().getToken())
                    .asJsonAsync();
        }
        catch (Exception ignore) {}
    }
    
    @Override
    public void execute(Note note, String label, String[] args)
    {
        if (args.length < 2)
        {
            note.error("**" + BotData.randomQuote() + "** Well that sounds fantastic! I'll just react to th-... " +
                    "You didn't give me anything to work with. *(I need two arguments, the first being a Message's " +
                    "ID" + " and the second being the emoji.)*");
            return;
        }
        String msgid = args[0];
        String reaction = args[1];
        
        Message msg = note.getChannel().getMessageById(msgid).complete();
        
        if (note.getEmotes().size() > 0)
        {
            Emote em = note.getEmotes().get(0);
            sendReaction(msg, em);
    
            try
            {
                note.info("Reacted to the message.").get().delete(5);
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



