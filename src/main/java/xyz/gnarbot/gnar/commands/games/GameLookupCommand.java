package xyz.gnarbot.gnar.commands.games;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.util.StringJoiner;

@Command(
        aliases = {"game", "gamelookup"},
        usage = "(query)",
        description = "Look up information about a game."
)
public class GameLookupCommand extends CommandExecutor
{
    @Override
    public void execute(Note message, String label, String[] args)
    {
        try
        {
            String query = StringUtils.join(args, "+");
            
            HttpResponse<JsonNode> response = Unirest.get("https://videogamesrating.p.mashape.com/get.php?count=5&game=" + query)
                    .header("X-Mashape-Key", Bot.INSTANCE.getAuthTokens().getProperty("mashape"))
                    .header("Accept", "application/json")
                    .asJson();
            
            JSONArray jsa = response.getBody().getArray();
            
            if (jsa.length() == 0)
            {
                message.reply("No game found with that title.");
                return;
            }
            
            JSONObject jso = new JSONObject(jsa.getJSONObject(0).toString())
            {
                @Override
                public String getString(String key) throws JSONException
                {
                    String s = super.getString(key);
                    return s == null || s.isEmpty() ? "Not found." : s;
                }
            };
            
            StringJoiner joiner = new StringJoiner("\n");
            
            joiner.add("Title: **" + jso.getString("title") + "**");
            joiner.add("Publisher: **" + jso.getString("publisher") + "**");
            joiner.add("Score: **" + jso.getString("score") + "**");
            joiner.add("Thumbnail: " + jso.getString("thumb"));
            joiner.add("Description: **" + jso.getString("short_description") + "**");
            
            message.getChannel().sendMessage(joiner.toString()).queue();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
