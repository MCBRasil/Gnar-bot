package xyz.gnarbot.gnar.utils;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URLEncoder;

public class Utils
{
    
    public static JSONObject information;
    
//    static
//    {
//        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager()
//        {
//            public X509Certificate[] getAcceptedIssuers()
//            {
//                return null;
//            }
//
//            public void checkClientTrusted(X509Certificate[] certs, String authType) {}
//
//            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
//        }};
//
//        try
//        {
//            SSLContext sc = SSLContext.getInstance("SSL");
//            sc.init(null, trustAllCerts, new java.security.SecureRandom());
//            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//        }
//        catch (Exception ignore) {}
//    }
    
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
    
    public static void sendReaction(Message message, Emote emote)
    {
        try
        {
            Unirest.put("https://discordapp.com/api/v6/channels/" + message.getChannel()
                    .getId() + "/messages/" + message.getId() + "/reactions/" + emote.getName() + ":" + emote.getId()
                    + "/@me")
                    .header("Authorization", message.getJDA().getToken())
                    .asJsonAsync();
        }
        catch (Exception ignore) {}
    }
    
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
    
    public String getTinyURL(String link)
    {
        try
        {
            return Unirest.get("http://tinyurl.com/api-create.php")
                    .queryString("url", link)
                    .asString()
                    .getBody();
        }
        catch (UnirestException e)
        {
            e.printStackTrace();
            return "https://www.google.com/";
        }
    }
}
