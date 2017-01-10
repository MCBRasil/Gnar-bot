package xyz.gnarbot.gnar.commands.executors.fun;

import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.util.HashMap;
import java.util.Map;

@Command(aliases = {"leet"}, usage = "(string)", description = "Leet it!")
public class LeetifyCommand extends CommandExecutor
{
    private static final Map<String, String> substitutions = new HashMap<String, String>()
    {{
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
    public void execute(Note note, String label, String[] args)
    {
        String s = StringUtils.join(args, " ");
        
        for (Map.Entry<String, String> entry : substitutions.entrySet())
        {
            s = s.replaceAll(entry.getKey(), entry.getValue());
        }
        
        note.replyEmbedRaw("Leet It", s);
    }
}
