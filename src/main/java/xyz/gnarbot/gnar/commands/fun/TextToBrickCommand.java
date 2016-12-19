package xyz.gnarbot.gnar.commands.fun;

import net.dv8tion.jda.core.MessageBuilder;
import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.BotData;
import xyz.gnarbot.gnar.utils.Note;

import java.awt.*;

@Command(
        aliases = {"ttb"},
        usage = "(string)",
        description = "Text to bricks fun."
)
public class TextToBrickCommand extends CommandExecutor
{
    @Override
    public void execute(Note message, String label, String[] args)
    {
        if (args.length == 0)
        {
            message.reply("Please provide a query.");
            return;
        }
        String s = "";
        for (String a : StringUtils.join(args, " ").split("")){
            if (Character.isLetter(a.toLowerCase().charAt(0))) {
                s += ":regional_indicator_" + a.toLowerCase() + ":";
            }else{
                if (a == " "){
                    s += " ";
                }
                s += a;
            }
        }
        message.replyEmbed("Text to Brick", s, Color.WHITE);
    }
}
