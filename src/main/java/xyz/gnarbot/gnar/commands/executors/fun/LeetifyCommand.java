package xyz.gnarbot.gnar.commands.executors.fun;

import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.Constants;
import xyz.gnarbot.gnar.commands.Category;
import xyz.gnarbot.gnar.commands.Command;
import xyz.gnarbot.gnar.commands.CommandExecutor;

import java.util.HashMap;
import java.util.Map;

@Command(aliases = {"leet"}, usage = "(string)", description = "Leet a string!", category = Category.FUN)
public class LeetifyCommand extends CommandExecutor {
    private static final Map<String, String> substitutions = new HashMap<String, String>() {{
        put("a", "4");
        put("A", "@");
        put("G", "6");
        put("e", "3");
        put("l", "1");
        put("s", "5");
        put("S", "\\$");
        put("o", "0");
        put("t", "7");
        put("i", "!");
        put("I", "1");
        put("B", "|3");
    }};

    @Override
    public void execute(Message message, String[] args) {
        String s = StringUtils.join(args, " ");

        for (Map.Entry<String, String> entry : substitutions.entrySet()) {
            s = s.replaceAll(entry.getKey(), entry.getValue());
        }

        message.respond().embed("Leet it")
                .setColor(Constants.COLOR)
                .setDescription(s)
                .rest().queue();
    }
}
