package xyz.gnarbot.gnar.commands.fun;

import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.util.StringJoiner;

@Command(aliases = {"poop"}, usage = "[string]", description = "Shit your heart out.")
public class PoopCommand extends CommandExecutor
{
    @Override
    public void execute(Note message, String label, String[] args)
    {
        String poop = StringUtils.join(args, " ");
        
        StringJoiner joiner = new StringJoiner("\n", "```\n", "```");
        
        joiner.add("░░░░░░░░░░░█▀▀░░█░░░░░░");
        joiner.add("░░░░░░▄▀▀▀▀░░░░░█▄▄░░░░");
        joiner.add("░░░░░░█░█░░░░░░░░░░▐░░░");
        joiner.add("░░░░░░▐▐░░░░░░░░░▄░▐░░░");
        joiner.add("░░░░░░█░░░░░░░░▄▀▀░▐░░░");
        joiner.add("░░░░▄▀░░░░░░░░▐░▄▄▀░░░░");
        joiner.add("░░▄▀░░░▐░░░░░█▄▀░▐░░░░░");
        joiner.add("░░█░░░▐░░░░░░░░▄░▌░░░░░");
        joiner.add("░░░█▄░░▀▄░░░░▄▀█░▌░░░░░");
        joiner.add("░░░▌▐▀▀▀░▀▀▀▀░░█░▌░░░░░");
        joiner.add("░░▐▌▐▄░░▀▄░░░░░█░█▄▄░░░");
        
        
        StringBuilder poopArt = new StringBuilder("░░░▀▀░▄███▄▄░░░▀▄▄▄▀░░░");
        
        for (int i = 0; i < poop.length(); i++)
        {
            try
            {
                poopArt.setCharAt(7 + i, poop.charAt(i));
            }
            catch (IndexOutOfBoundsException e)
            {
                message.reply("Poop is too big. Constipation occurred.");
                return;
            }
        }
        
        joiner.add(poopArt);
        joiner.add("░░░░░░░░░░░░░░░░░░░░░░░");
        
        message.replyEmbedRaw("Pooping Memes", joiner.toString(), Bot.getColor());
    }
}