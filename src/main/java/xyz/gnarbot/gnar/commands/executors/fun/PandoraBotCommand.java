package xyz.gnarbot.gnar.commands.executors.fun;

import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.servers.Servlet;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;

@Command(aliases = "pbot")
public class PandoraBotCommand extends CommandExecutor {
    @Inject
    private Servlet servlet;

    private ChatterBotFactory factory = new ChatterBotFactory();

    private ChatterBot bot = null;

    private ChatterBotSession session = null;

    @Override
    public void execute(Note note, List<String> args) {
        try {
            if (bot == null) {
                bot = factory.create(ChatterBotType.PANDORABOTS, "b0dafd24ee35a477");
                session = bot.createSession();
                note.info("Pandora-Bot session created for the server.");
            }

            String input = StringUtils.join(args, " ");

            String output = session.think(input);
            note.info("**[PandoraBot]** ─ `" + output + "`");
        } catch (Exception e) {
            note.error("PandoraBot has encountered an exception. Resetting PandoraBot.");
            bot = null;
        }
    }

}
