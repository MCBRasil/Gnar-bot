package xyz.gnarbot.gnar.commands.media;

import com.google.inject.Inject;
import org.json.JSONObject;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.handlers.servers.Host;
import xyz.gnarbot.gnar.utils.Note;
import xyz.gnarbot.gnar.utils.Utils;

import java.util.Random;

@Command(aliases = "xkcd")
public class XKCDCommand extends CommandExecutor
{
    @Inject
    public Host host;
    
    @Override
    public void execute(Note note, String label, String[] args)
    {
        try
        {
            JSONObject latestJSON = Utils.jsonFromUrl("http://xkcd.com/info.0.json");
            
            if (latestJSON != null)
            {
                int min = 500;
                int max = latestJSON.getInt("num");
                
                int rand;
                if (args.length >= 1)
                {
                    int input;
                    try
                    {
                        input = Integer.valueOf(args[0]);
                        
                        if (input > max || input < 1)
                        {
                            note.replyError("xkcd does not have a comic for that number.");
                        }
                        
                        rand = input;
                    }
                    catch (NumberFormatException e)
                    {
                        if (args[0].equalsIgnoreCase("latest"))
                        {
                            rand = max;
                        }
                        else
                        {
                            note.replyError("You didn't enter a proper number.");
                            return;
                        }
                    }
                }
                else
                {
                    rand = min + new Random().nextInt(max - min);
                }
                
                JSONObject randJSON = Utils.jsonFromUrl(String.format("http://xkcd.com/%d/info.0.json", rand));
                
                if (randJSON != null)
                {
                    String title = randJSON.getString("title");
                    
                    int num = randJSON.getInt("num");
                    
                    String url = randJSON.getString("img").replaceAll("\\\\/", "/");
                    
                    String logo = "http://imgs.xkcd.com/static/terrible_small_logo.png";
                    
                    note.replyEmbed(title, "No: " + num, Bot.getColor(), logo, url);
                    
                    return;
                }
            }
            
            note.replyError("Unable to grab xkcd comic.");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}