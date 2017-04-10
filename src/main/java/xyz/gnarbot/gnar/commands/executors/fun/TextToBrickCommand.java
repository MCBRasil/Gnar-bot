package xyz.gnarbot.gnar.commands.executors.fun;

import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.Constants;
import xyz.gnarbot.gnar.commands.Command;
import xyz.gnarbot.gnar.commands.CommandExecutor;

@Command(
        aliases = "ttb",
        usage = "(string)",
        description = "Text to bricks fun."
)
public class TextToBrickCommand extends CommandExecutor {
    @Override
    public void execute(Message message, String[] args) {
        if (args.length == 0) {
            message.respond().error("Please provide a query.").queue();
            return;
        }

        message.respond().embed("Text to Brick")
                .setColor(Constants.COLOR)
                .description(sb -> {
                    for (String a : StringUtils.join(args, " ").split("")) {
                        if (Character.isLetter(a.toLowerCase().charAt(0))) {
                            sb.append(":regional_indicator_").append(a.toLowerCase()).append(":");
                        } else {
                            if (a.equals(" ")) {
                                sb.append(" ");
                            }
                            sb.append(a);
                        }
                    }
                })
                .rest().queue();
    }
}
