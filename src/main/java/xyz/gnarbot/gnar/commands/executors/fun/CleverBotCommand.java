package xyz.gnarbot.gnar.commands.executors.fun;

import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;

@Command(aliases = "cbot")
public class CleverBotCommand extends CommandExecutor {
    // TODO put this on again when we fix this
//    @Inject
//    public Host host;

    private ChatterBotFactory factory = new ChatterBotFactory();

    private ChatterBot bot = null;

    private ChatterBotSession session = null;

    @Override
    public void execute(Note note, List<String> args) {
        note.error("Command is being worked on :), use _pbot for a bit");
        
        /*
        try
        {
            if (bot == null)
            {
                bot = factory.create(ChatterBotType.CLEVERBOT);
                session = bot.createSession();
                msg.reply("Clever-Bot session created for the server.");
            }

            String input = StringUtils.join(args, " ");

            String output = session.think(input);
            msg.replyRaw("**[CleverBot]** ─ `" + output + "`");
        }
        catch (Exception e)
        {
            msg.reply("CleverBot has encountered an exception. Resetting CleverBot.");
            bot = null;
        }*/
    }

}
