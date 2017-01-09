package xyz.gnarbot.gnar.tests;

import com.mashape.unirest.http.Unirest;
import org.json.JSONObject;
import org.junit.Test;
import xyz.gnarbot.gnar.Bot;

public class UnirestPosting
{
    @Test
    public void unirestUpdate() throws Exception
    {
        int i = 7000;
    
        String auth = Bot.INSTANCE.getAuthTokens().getProperty("authToken");
        String key = Bot.INSTANCE.getAuthTokens().getProperty("serverKey");
    
        JSONObject json = new JSONObject()
                .put("key", key)
                .put("servercount", i);
    
        String response = Unirest.post("https://www.carbonitex.net/discord/data/botdata.php")
                .header("User-Agent", "Gnar Bot")
                .header("Authorization", auth)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(json)
                .asString().getStatusText();
    
        System.out.println(response);
    }
    
    @Test
    public void unirestUpdate2() throws Exception
    {
        int i = 7000;
        
        String auth = Bot.INSTANCE.getAuthTokens().getProperty("authToken");
        
        JSONObject json = new JSONObject()
                .put("server_count", i);
        
        String response = Unirest.post(Bot.INSTANCE.getAuthTokens().getProperty("url"))
                .header("User-Agent", "Gnar Bot")
                .header("Authorization", auth)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(json)
                .asString().getStatusText();
        
        System.out.println(response);
    }
}
