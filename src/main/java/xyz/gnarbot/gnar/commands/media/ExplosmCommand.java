package xyz.gnarbot.gnar.commands.media;

import com.google.inject.Inject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.handlers.servers.Host;
import xyz.gnarbot.gnar.utils.Note;

import java.util.Random;

@Command(aliases = {"c&h", "cah"})
public class ExplosmCommand extends CommandExecutor
{
    @Inject
    public Host host;
    
    @Override
    public void execute(Note msg, String label, String[] args)
    {
        try
        {
            Document document;
            
            int min = 1500;
            int max = 4300;
            
            String rand;
            
            if (args.length >= 1)
            {
                int input;
                try
                {
                    input = Integer.valueOf(args[0]);
                    
                    if (input > max || input < 100)
                    {
                        msg.reply("Explosm does not have a comic for that number.");
                    }
                    
                    rand = String.valueOf(input);
                }
                catch (NumberFormatException e)
                {
                    if (args[0].equalsIgnoreCase("latest"))
                    {
                        rand = "latest";
                    }
                    else
                    {
                        msg.reply("You didn't enter a proper number.");
                        return;
                    }
                }
            }
            else
            {
                rand = String.valueOf(min + new Random().nextInt(max - min));
            }
            
            document = Jsoup.connect(String.format("http://explosm.net/comics/%s/", rand)).get();
            
            String builder = "Cyanide and Happiness" + "\n" + "No: **" + rand + "**\n" + "Link: " + document
                    .getElementById("main-comic").absUrl("src");
            
            msg.getChannel().sendMessage(builder).queue();
        }
        catch (Exception e)
        {
            msg.reply("Unable to grab Cyanide and Happiness comic.");
            e.printStackTrace();
        }
    }
}
