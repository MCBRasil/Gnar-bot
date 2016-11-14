package xyz.gnarbot.gnar.utils;

import org.json.JSONObject;
import xyz.gnarbot.gnar.Bot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordBotUserInfo
{
    public static String getUserInfo(String id)
    {
        try
        {
            String data = "";

            String authToken = Bot.INSTANCE.getAuthTokens().getProperty("authToken");
            URL url = new URL("https://bots.discord.pw/api/bots/" + id);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.24 Safari/537.36");
            con.setRequestProperty("Authorization", authToken);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while ((line = br.readLine()) != null)
            {
                data += "\n" + line;
            }

            JSONObject j = new JSONObject(data);
            String info = "\n";
            info += "**Bots Name:** " + j.get("name");
            info += "\n\n**Bots Prefix:** " + j.get("prefix");
            info += "\n\n**Bots Library:** " + j.get("library");
            info += "\n\n**Bots Description:**\n      " + j.get("description");
            info += "\n\n**Bots Website:** " + j.get("website");
            info += "\n\n**Bots Invite URL:** " + j.get("invite_url");

            return info;

        }
        catch (Exception e)
        {
            return "Error, bot not found.";
        }
    }

}
