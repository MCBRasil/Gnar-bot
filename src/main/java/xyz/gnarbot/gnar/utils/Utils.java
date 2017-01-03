package xyz.gnarbot.gnar.utils;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;

public class Utils
{
    
    public static JSONObject information;
    
    static
    {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager()
        {
            public X509Certificate[] getAcceptedIssuers()
            {
                return null;
            }
            
            public void checkClientTrusted(X509Certificate[] certs, String authType) {}
            
            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
        }};
        
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
        try
        {
            Unirest.put("https://discordapp.com/api/v6/channels/" + message.getChannel().getId() + "/messages/" +
                    message.getId() + "/reactions/" + encodedEmoji + "/@me").header("Authorization", message.getJDA()
                    .getToken()).asJsonAsync();
        }
        catch (Exception ignore) {}
    }
    
    public static boolean sendReactionAutoEncode(Message message, String encodedEmoji)
    {
        try
        {
            Unirest.put("https://discordapp.com/api/v6/channels/" + message.getChannel().getId() + "/messages/" +
                    message.getId() + "/reactions/" + URLEncoder.encode(encodedEmoji, "UTF-8") + "/@me").header
                    ("Authorization", message.getJDA().getToken()).asJsonAsync();
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
            Unirest.put("https://discordapp.com/api/v6/channels/" + message.getChannel().getId() + "/messages/" +
                    message.getId() + "/reactions/" + emote.getName() + ":" + emote.getId() + "/@me").header
                    ("Authorization", message.getJDA().getToken()).asJsonAsync();
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
    
    public String getTinyURL(String link)
    {
        try
        {
            return Unirest.get("http://tinyurl.com/api-create.php?url=" + link)
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
