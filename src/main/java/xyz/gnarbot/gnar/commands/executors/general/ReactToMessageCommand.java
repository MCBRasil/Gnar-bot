package xyz.gnarbot.gnar.commands.executors.general;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.BotData;
import xyz.gnarbot.gnar.utils.Note;
import xyz.gnarbot.gnar.utils.Utils;

import java.util.concurrent.TimeUnit;

//TODO REMOVE THE EDITZ
@Command(aliases = "react",
         usage = "(messageid) (emoji)",
         description = "Make GNAR react to something, against it's " + "will. You evil prick.")
public class ReactToMessageCommand extends CommandExecutor
{
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
        final Note msg = note.reply("**" + BotData.randomQuote() + "** Searching for Message with ID `" + msgid + "`.");
        
        Message m = msg.getChannel().getMessageById(msgid).complete();
        if (note.getEmotes().size() > 0)
        {
            Emote em = note.getEmotes().get(0);
            Utils.sendReaction(m, em);
            msg.editMessage("**" + BotData.randomQuote() + "** Oh wow! That's pretty cool").queue();
            Bot.INSTANCE.getScheduler().schedule(() -> msg.deleteMessage().queue(), 10, TimeUnit.SECONDS);
        }
        else
        {
            if (Utils.sendReactionAutoEncode(m, reaction))
            {
                msg.editMessage("**" + BotData.randomQuote() + "** Oh wow! That's pretty cool").queue();
                Bot.INSTANCE.getScheduler().schedule(() -> msg.deleteMessage().queue(), 10, TimeUnit.SECONDS);
            }
            else
            {
                msg.editMessage("**" + BotData.randomQuote() + "** Oops. Something happened when I tried to react" +
                        " to that message. Maybe it wasn't a valid emoji? I'm not sure.")
                        .queue();
                Bot.INSTANCE.getScheduler().schedule(() -> msg.deleteMessage().queue(), 10, TimeUnit.SECONDS);
            }
        }
        
        note.delete();
    }
}



