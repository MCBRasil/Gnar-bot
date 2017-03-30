package xyz.gnarbot.gnar.commands.executors.fun;

import net.dv8tion.jda.core.MessageBuilder;
import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;

@Command(aliases = {"tts"}, usage = "(string)", description = "Text to speech fun.", showInHelp = false)
public class TextToSpeechCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
        if (args.isEmpty()) {
            note.respond().error("Please provide a query.").queue();
            return;
        }

        MessageBuilder builder = new MessageBuilder();
        builder.setTTS(true);
        builder.append(StringUtils.join(args.subList(1, args.size()), " "));

        note.getChannel().sendMessage(builder.build()).queue();
    }
}
