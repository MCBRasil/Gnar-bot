package xyz.gnarbot.gnar.commands.executors.fun;

import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;

@Command(aliases = "ttb", usage = "(string)", description = "Text to bricks fun.")
public class TextToBrickCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
        if (args.isEmpty()) {
            note.error("Please provide a query.").queue();
            return;
        }

        note.embed("Text to Brick")
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
