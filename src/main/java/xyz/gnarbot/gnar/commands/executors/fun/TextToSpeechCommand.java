package xyz.gnarbot.gnar.commands.executors.fun;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.commands.handlers.Category;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;

import java.util.List;

@Command(
        aliases = {"tts"},
        usage = "(string)",
        description = "Text to speech fun.",
        category = Category.FUN,
        channelPermissions = Permission.MESSAGE_TTS
)
public class TextToSpeechCommand extends CommandExecutor {
    @Override
    public void execute(Message message, List<String> args) {
        if (args.isEmpty()) {
            message.respond().error("Please provide a query.").queue();
            return;
        }

        MessageBuilder builder = new MessageBuilder();
        builder.setTTS(true);
        builder.append(StringUtils.join(args.subList(1, args.size()), " "));

        message.getChannel().sendMessage(builder.build()).queue();
    }
}
