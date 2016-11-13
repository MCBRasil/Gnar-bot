package xyz.gnarbot.gnar.utils;

import com.mashape.unirest.http.Unirest;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;

public class Utils
{
    static
    {
        TrustManager[] trustAllCerts = new TrustManager[]
        {
            new X509TrustManager()
            {
                public X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }
                
                public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                
                public void checkServerTrusted(X509Certificate[] certs, String authType) {}
            }
        };
        
        try
        {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
        catch (Exception ignore) {}
    }

    public static void sendReaction(Message message, String encodedEmoji)
{
    try{
        Unirest.put("https://discordapp.com/api/v6/channels/"+message.getChannel().getId()+"/messages/"+message.getId()+"/reactions/"+encodedEmoji+"/@me")
                .header("Authorization", message.getJDA().getToken())
                .asJsonAsync();
    }catch(Exception e){}
}
    public static boolean sendReactionAutoEncode(Message message, String encodedEmoji)
    {
        try{
            Unirest.put("https://discordapp.com/api/v6/channels/"+message.getChannel().getId()+"/messages/"+message.getId()+"/reactions/"+ URLEncoder.encode(encodedEmoji, "UTF-8")+"/@me")
                    .header("Authorization", message.getJDA().getToken())
                    .asJsonAsync();
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public static void sendReaction(Message message, Emote emote)
    {
        try{
            Unirest.put("https://discordapp.com/api/v6/channels/"+message.getChannel().getId()+"/messages/"+message.getId()+"/reactions/"+emote.getName()+":"+emote.getId()+"/@me")
                    .header("Authorization", message.getJDA().getToken())
                    .asJsonAsync();
        }catch(Exception e){}
    }

    private static String readAll(Reader rd) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1)
        {
            sb.append((char) cp);
        }
        return sb.toString();
    }
    
    public static JSONObject jsonFromUrl(String url) throws JSONException
    {
        URLConnection conn;
        try
        {
            conn = new URL(url).openConnection();
            conn.setRequestProperty("User-Agent", "Gnar");
            conn.connect();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }

        try (InputStream is = conn.getInputStream())
        {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        
//        try
//        {
//            HttpResponse<String> response = Unirest.get(url)
//                    .header("User-Agent", "Discord Bot")
//                    .asString();
//
//            System.out.println(response.toString());
//
//            return new JSONObject(response.getBody().toString());
//        }
//        catch (Exception e)
//        {
//            return null;
//        }
    }
}
