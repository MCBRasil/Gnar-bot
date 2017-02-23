package xyz.gnarbot.gnar.commands.executors.fun;

import net.dv8tion.jda.core.MessageBuilder;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;

@Command(aliases = {"tts"}, usage = "(string)", description = "Text to speech fun.")
public class TextToSpeechCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
        if (args.isEmpty()) {
            note.error("Please provide a query.").queue();
            return;
        }

        MessageBuilder builder = new MessageBuilder();
        builder.setTTS(true);
        builder.append(note.getContent().replaceFirst(Bot.getToken() + "tts ", ""));

        note.getChannel().sendMessage(builder.build()).queue();
    }
}
