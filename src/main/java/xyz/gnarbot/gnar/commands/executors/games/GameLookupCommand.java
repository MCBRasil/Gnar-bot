package xyz.gnarbot.gnar.commands.executors.games;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import xyz.gnarbot.gnar.Constants;
import xyz.gnarbot.gnar.Credentials;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;

import java.util.List;

@Command(aliases = {"game", "gamelookup"},
        usage = "(Game name)",
        description = "Look up information about a game.")
public class GameLookupCommand extends CommandExecutor {
    @Override
    public void execute(Message message, List<String> args) {
        try {
            String query = StringUtils.join(args, "+");

            HttpResponse<JsonNode> response = Unirest.get("https://igdbcom-internet-game-database-v1.p.mashape.com/games/")
                    .queryString("fields", "name,summary,rating,cover.url")
                    .queryString("limit", 1)
                    .queryString("search", query)
                    .header("X-Mashape-Key", Credentials.MASHAPE)
                    .header("Accept", "application/json")
                    .asJson();

            JSONArray jsa = response.getBody().getArray();

            if (jsa.length() == 0) {
                message.respond().error("No game found with that title.").queue();
                return;
            }

            JSONObject jso = jsa.getJSONObject(0);

            String title = jso.optString("name");
            //String publisher = jso.optString("publisher");
            String score = jso.optString("rating");
            String desc = jso.optString("summary");
            String thumb = "https:" + jso.optJSONObject("cover").optString("url");

            message.respond().embed(title)
                    .setColor(Constants.COLOR)
                    .setThumbnail(thumb)
                    //.field("Publisher", true, publisher)
                    .field("Score", true, score)
                    .field("Description", false, desc)
                    .rest().queue();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
