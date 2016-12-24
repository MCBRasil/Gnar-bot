package xyz.gnarbot.gnar.commands.media;

import com.google.inject.Inject;
import org.json.JSONObject;
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
    public void execute(Note msg, String label, String[] args)
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
                            msg.reply("xkcd does not have a comic for that number.");
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
                            msg.reply("You didn't enter a proper number.");
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
                    String builder = "XKCD **" + randJSON.getString("title") + "**\n" + "No: **" + randJSON.getInt
                            ("num") + "**\n" + "Link: " + randJSON.getString("img").replaceAll("\\\\/", "/");
                    
                    msg.replyRaw(builder);
                    
                    return;
                }
            }
            
            msg.reply("Unable to grab xkcd comic.");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}