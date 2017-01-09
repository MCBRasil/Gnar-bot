package xyz.gnarbot.gnar.commands.media;

import com.google.inject.Inject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.handlers.servers.Host;
import xyz.gnarbot.gnar.utils.Note;

import java.util.Random;

@Command(aliases = {"c&h", "cah"}, description = "Get Cyanide and Happiness comics.", usage = "~id")
public class ExplosmCommand extends CommandExecutor
{
    @Inject
    public Host host;
    
    @Override
    public void execute(Note note, String label, String[] args)
    {
        try
        {
            Document document;
            
            int min = 1500;
            int max = 4500;
            
            String rand;
            
            if (args.length >= 1)
            {
                int input;
                try
                {
                    input = Integer.valueOf(args[0]);
                    
                    if (input > max || input < 100)
                    {
                        note.error("Explosm does not have a comic for that number.");
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
                        note.error("You didn't enter a proper ID number.");
                        return;
                    }
                }
            }
            else
            {
                rand = String.valueOf(min + new Random().nextInt(max - min));
            }
            
            document = Jsoup.connect("http://explosm.net/comics/" + rand + "/").get();
            
            String url = document.getElementById("main-comic").absUrl("src");
            
            String logo = "http://explosm.net/img/logo.png";
            
            note.replyEmbedRaw("Cyanide and Happiness", "No: **" + rand + "**\n", Bot.getColor(), logo, url);
        }
        catch (Exception e)
        {
            note.error("Unable to grab Cyanide and Happiness comic.");
            e.printStackTrace();
        }
    }
}
