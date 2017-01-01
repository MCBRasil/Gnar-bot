package xyz.gnarbot.gnar.commands.mod;

import com.google.inject.Inject;
import net.dv8tion.jda.core.MessageHistory;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.exceptions.PermissionException;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.handlers.members.Clearance;
import xyz.gnarbot.gnar.handlers.servers.Host;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Command(aliases = {"delmessages", "prune"}, clearance = Clearance.BOT_COMMANDER)
public class DeleteMessagesCommand extends CommandExecutor
{
    @Inject
    public Host host;
    
    @Override
    public void execute(Note msg, String label, String[] args)
    {
        try
        {
            if (!msg.getAuthor().hasPermission(Permission.MESSAGE_MANAGE))
            {
                msg.reply("You do not have permission to delete messages");
                return;
            }
            
            if (args.length == 0)
            {
                msg.reply("Insufficient amount of arguments.");
                return;
            }
            
            MessageHistory messageHistory = msg.getChannel().getHistory();
            
            int amount = (int) Math.round(Double.parseDouble(args[0]));
            amount = Math.min(amount, 100);
            
            if (amount < 2)
            {
                msg.reply("You need to delete 2 or more messages to use this command.");
                return;
            }
            
            List<Message> messages = messageHistory.retrievePast(amount).complete();
            
            msg.reply(amount + " " + messages.size());
            
            if (args.length == 2)
            {
                if (args[1].contains("-content:"))
                {
                    String targetWord = args[1].replaceFirst("-content:", "");
                    Set<Message> removeSet = messages.stream().filter(mesg -> mesg.getContent().toLowerCase()
                            .contains(targetWord.toLowerCase())).collect(Collectors.toSet());
                    
                    try
                    {
                        ((TextChannel) msg.getChannel()).deleteMessages(removeSet).queue();
                    }
                    catch (PermissionException e)
                    {
                        msg.reply("GN4R does not have sufficient permission to delete messages.");
                    }
                    msg.reply("Attempted to delete `" + removeSet.size() + "` messages with the word `" + targetWord
                            + "`.");
                    return;
                }
            }
            
            ((TextChannel) msg.getChannel()).deleteMessages(messages).queue();
            Message mesg = msg.getChannel().sendMessage(msg.getAuthor().getAsMention() + " ➜ Attempted to delete `" +
                    messages.size() + "` messages.").complete();
            
            Bot.INSTANCE.getScheduler().schedule(mesg::deleteMessage, 5, TimeUnit.SECONDS);
        }
        catch (Exception e)
        {
            msg.reply("Improper arguments supplies, must be a number.");
        }
    }
}
