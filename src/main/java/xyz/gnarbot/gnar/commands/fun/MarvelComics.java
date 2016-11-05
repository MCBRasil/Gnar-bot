package xyz.gnarbot.gnar.commands.fun;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;
import xyz.gnarbot.gnar.utils.Utils;

@Command(
        aliases = {"marvel"},
        usage = "(hero/villain name)",
        description = "Look up info on a Marvel character."
)
public class MarvelComics extends CommandExecutor
{
    @Override
    public void execute(Note message, String label, String[] args)
    {
        if (args.length == 0)
        {
            message.reply("Please provide a name.");
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
            
            JSONObject jso = Utils.jsonFromUrl("http://gateway.marvel.com:80/v1/public/characters?apikey=" + apiKeyPu + "&hash=" + hash + "&name=" + s + "&ts=" + ts);
            JSONObject je = jso.getJSONObject("data");
            JSONArray j2 = je.getJSONArray("results");
            JSONObject j = j2.getJSONObject(0);
            
            JSONObject thumb = (JSONObject) j.get("thumbnail");
            
            message.reply("Thumbnail: " + thumb.get("path") + "." + thumb.get("extension"));
        }
        catch (Exception e)
        {
            message.reply("Character not found.");
        }
    }
}
