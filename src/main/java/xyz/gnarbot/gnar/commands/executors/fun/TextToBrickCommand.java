package xyz.gnarbot.gnar.commands.executors.fun;

import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

@Command(aliases = "ttb", usage = "(string)", description = "Text to bricks fun.")
public class TextToBrickCommand extends CommandExecutor {
    @Override
    public void execute(Note note, String[] args) {
        if (args.length == 0) {
            note.error("Please provide a query.");
            return;
        }
        StringBuilder sb = new StringBuilder();
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
        note.replyEmbedRaw("Text to Brick", sb.toString(), Bot.getColor());
    }
}
