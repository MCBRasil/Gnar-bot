package xyz.gnarbot.gnar.utils;

import com.mashape.unirest.http.Unirest;
import net.dv8tion.jda.core.entities.Message;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URLEncoder;

public class Utils
{
    public static JSONObject information;
    
    public static void setLeagueInfo()
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(new File("_DATA/league/League.txt")));
            
            String info = "";
            
            String line;
            while ((line = br.readLine()) != null)
            {
                info += line;
            }
            
            information = new JSONObject(info);
        }
        catch (Exception ignore) { }
    }

    public static void sendReaction(Message message, String encodedEmoji)
    {
        try
        {
            Unirest.put("https://discordapp.com/api/v6/channels/" + message.getChannel()
                    .getId() + "/messages/" + message.getId() + "/reactions/" + encodedEmoji + "/@me")
                    .header("Authorization", message.getJDA().getToken())
                    .asJsonAsync();
        }
        catch (Exception ignore) {}
    }

    public static boolean sendReactionAutoEncode(Message message, String encodedEmoji)
    {
        try
        {
            Unirest.put("https://discordapp.com/api/v6/channels/" + message.getChannel()
                    .getId() + "/messages/" + message.getId() + "/reactions/" + URLEncoder.encode(encodedEmoji,
                    "UTF-8") + "/@me")
                    .header("Authorization", message.getJDA().getToken())
                    .asJsonAsync();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
