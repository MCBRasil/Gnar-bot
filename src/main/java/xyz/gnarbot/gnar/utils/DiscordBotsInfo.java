package xyz.gnarbot.gnar.utils;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import xyz.gnarbot.gnar.Bot;

public class DiscordBotsInfo
{
    public static void updateServerCount(int i)
    {
        updateAbalCount(i);
        updateCarbonitexCount(i);
    }
    
    public static void updateAbalCount(int i)
    {
        try
        {
            String auth = Bot.INSTANCE.getAuthTokens().getProperty("authToken");
            
            JSONObject json = new JSONObject().put("server_count", i);
            
            String response = Unirest.post(Bot.INSTANCE.getAuthTokens().getProperty("url"))
                    .header("User-Agent", "Gnar Bot")
                    .header("Authorization", auth)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .body(json)
                    .asString()
                    .getStatusText();
            
            Bot.getLOG().info("Successfully updated Abal server count to " + i + ".");
            Bot.getLOG().info("Response code: " + response);
        }
        catch (UnirestException e)
        {
            Bot.getLOG().warn("Failed updating Abal server count to " + i + ".");
            e.printStackTrace();
        }
    }
    
    public static void updateCarbonitexCount(int i)
    {
        try
        {
            String auth = Bot.INSTANCE.getAuthTokens().getProperty("authToken");
            String key = Bot.INSTANCE.getAuthTokens().getProperty("serverKey");
            
            JSONObject json = new JSONObject().put("key", key).put("servercount", i);
            
            String response = Unirest.post("https://www.carbonitex.net/discord/data/botdata.php")
                    .header("User-Agent", "Gnar Bot")
                    .header("Authorization", auth)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .body(json)
                    .asString()
                    .getStatusText();
            
            Bot.getLOG().info("Successfully updated Carbonitex server count to " + i + ".");
            Bot.getLOG().info("Response code: " + response);
        }
        catch (UnirestException e)
        {
            Bot.getLOG().warn("Failed updating Carbonitex server count to " + i + ".");
            e.printStackTrace();
        }
    }
}
