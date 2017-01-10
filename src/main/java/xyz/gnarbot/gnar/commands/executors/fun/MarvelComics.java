package xyz.gnarbot.gnar.commands.executors.fun;

import com.mashape.unirest.http.Unirest;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.awt.*;

@Command(aliases = {"marvel"}, usage = "(hero/villain name)", description = "Look up info on a Marvel character.")
public class MarvelComics extends CommandExecutor
{
    @Override
    public void execute(Note note, String label, String[] args)
    {
        if (args.length == 0)
        {
            note.error("Please provide a name.");
            return;
        }
        
        try
        {
            long timeStamp = System.currentTimeMillis();
            String ts = String.valueOf(timeStamp);
            String apiKeyPu = Bot.INSTANCE.getAuthTokens().getProperty("marvelPuToken");
            String apiKeyPr = Bot.INSTANCE.getAuthTokens().getProperty("marvelPrToken");
            String preHash = timeStamp + apiKeyPr + apiKeyPu;
            String hash = DigestUtils.md5Hex(preHash);
            
            String s = StringUtils.join(args, "+");
            
            JSONObject jso = Unirest.get("http://gateway.marvel.com:80/v1/public/characters")
                    .queryString("apikey", apiKeyPu)
                    .queryString("hash", hash)
                    .queryString("name", s)
                    .queryString("ts", ts)
                    .asJson()
                    .getBody()
                    .getObject();
            
            JSONObject je = jso.getJSONObject("data");
            JSONArray j2 = je.getJSONArray("results");
            JSONObject j = j2.getJSONObject(0);
            
            JSONObject thumb = (JSONObject) j.get("thumbnail");
            
            note.replyEmbedRaw("Marvel Characters",
                    StringUtils.capitalize(s.toLowerCase().replaceAll("\\+", " ")),
                    Color.RED, null, thumb.optString("path") + "." + thumb.optString("extension"));
        }
        catch (Exception e)
        {
            note.error("Couldn't find that Marvel character.");
        }
    }
}
