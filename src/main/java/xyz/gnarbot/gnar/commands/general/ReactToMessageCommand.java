package xyz.gnarbot.gnar.commands.general;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.BotData;
import xyz.gnarbot.gnar.utils.Note;
import xyz.gnarbot.gnar.utils.Utils;

import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

@Command(
        aliases = "react",
        usage = "(messageid) (emoji)",
        description = "Make GNAR react to something, against it's will. You evil prick."
)
public class ReactToMessageCommand extends CommandExecutor
{
    @Override
    public void execute(Note message, String label, String[] args)
    {
        if (args.length < 2)
        {
            message.reply("**" + BotData.randomQuote() + "** Well that sounds fantastic! I'll just react to th-... You didn't give me anything to work with. *(I need two arguments, the first being a Message's ID and the second being the emoji.)*");
            return;
        }
        String msgid = args[0];
        String reaction = args[1];
        final Note msg = message.reply("**" + BotData.randomQuote() + "** Searching for Message with ID `" + msgid + "`.");
        try {
            Message m = msg.getChannel().getMessageById(msgid).block();
            if (message.getEmotes().size() > 0){
                Emote em = message.getEmotes().get(0);
                Utils.sendReaction(m, em);
                msg.editMessage("**" + BotData.randomQuote() + "** Oh wow! That's pretty cool").queue();
                Bot.INSTANCE.getScheduler().schedule(new Runnable() {
                    @Override
                    public void run() {
                        msg.deleteMessage().queue();
                    }
                }, 10, TimeUnit.SECONDS);
            }else {
                if (Utils.sendReactionAutoEncode(m, reaction)) {
                    msg.editMessage("**" + BotData.randomQuote() + "** Oh wow! That's pretty cool").queue();
                    Bot.INSTANCE.getScheduler().schedule(new Runnable() {
                        @Override
                        public void run() {
                            msg.deleteMessage().queue();
                        }
                    }, 10, TimeUnit.SECONDS);
                } else {
                    msg.editMessage("**" + BotData.randomQuote() + "** Oops. Something happened when I tried to react to that message. Maybe it wasn't a valid emoji? I'm not sure.").queue();
                    Bot.INSTANCE.getScheduler().schedule(new Runnable() {
                        @Override
                        public void run() {
                            msg.deleteMessage().queue();
                        }
                    }, 10, TimeUnit.SECONDS);
                }
            }
        }catch (RateLimitedException e){
            msg.editMessage("**" + BotData.randomQuote() + "** Oops. I couldn't find that message within this channel. You sure you got the right place?").queue();
            Bot.INSTANCE.getScheduler().schedule(new Runnable() {
                @Override
                public void run() {
                    msg.deleteMessage().queue();
                }
            }, 10, TimeUnit.SECONDS);
        }
        try {
            message.deleteMessage().queue();
        }catch (Exception e){
            return;
        }
    }
}



