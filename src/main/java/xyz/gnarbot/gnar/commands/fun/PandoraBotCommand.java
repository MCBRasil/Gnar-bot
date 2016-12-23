package xyz.gnarbot.gnar.commands.fun;

import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.handlers.servers.Host;
import xyz.gnarbot.gnar.utils.Note;

@Command(aliases = "pbot")
public class PandoraBotCommand extends CommandExecutor
{
    @Inject
    public Host host;
    
    private ChatterBotFactory factory = new ChatterBotFactory();
    
    private ChatterBot bot = null;
    
    private ChatterBotSession session = null;
    
    @Override
    public void execute(Note msg, String label, String[] args)
    {
        try
        {
            if (bot == null)
            {
                bot = factory.create(ChatterBotType.PANDORABOTS, "b0dafd24ee35a477");
                session = bot.createSession();
                msg.reply("Pandora-Bot session created for the server.");
            }
            
            String input = StringUtils.join(args, " ");
            
            String output = session.think(input);
            msg.replyRaw("**[PandoraBot]** ─ `" + output + "`");
        }
        catch (Exception e)
        {
            msg.reply("PandoraBot has encountered an exception. Resetting PandoraBot.");
            bot = null;
        }
    }
    
}
