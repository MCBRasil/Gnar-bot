package xyz.gnarbot.gnar.utils;

import org.json.JSONObject;
import xyz.gnarbot.gnar.Bot;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordBotsInfo
{

    public static void updateServerCount(int i)
    {
        try
        {


            String url = Bot.INSTANCE.getAuthTokens().getProperty("url");
            URL object = new URL(url);

            String authToken = Bot.INSTANCE.getAuthTokens().getProperty("authToken");

            JSONObject serverCount = new JSONObject();

            serverCount.put("server_count", i);

            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.24 Safari/537.36");
            con.setRequestProperty("Authorization", authToken);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");

            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(serverCount.toString());
            wr.flush();

            System.out.println("Successfully updated Abal server count to " + i + ", Response Code: " + con.getResponseCode());


            JSONObject serverCount2 = new JSONObject();

            String key = Bot.INSTANCE.getAuthTokens().getProperty("serverKey");

            serverCount2.put("key", key);
            serverCount2.put("servercount", i);

            object = new URL("https://www.carbonitex.net/discord/data/botdata.php");

            HttpURLConnection conn = (HttpURLConnection) object.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.24 Safari/537.36");
            conn.setRequestProperty("Authorization", authToken);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");

            OutputStreamWriter wre = new OutputStreamWriter(conn.getOutputStream());
            wre.write(serverCount2.toString());
            wre.flush();

            System.out.println("Successfully updated Carbonitex server count to " + i + ", Response Code: " + conn.getResponseCode());

        }
        catch (Exception e)
        {
            System.out.println("Error updating server count...");
            e.printStackTrace();
        }
    }
}
