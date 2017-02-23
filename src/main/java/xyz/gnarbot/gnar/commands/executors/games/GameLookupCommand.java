package xyz.gnarbot.gnar.commands.executors.games;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.Credentials;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;
import java.util.StringJoiner;

@Command(aliases = {"game", "gamelookup"}, usage = "(query)", description = "Look up information about a game.")
public class GameLookupCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
        try {
            String query = StringUtils.join(args, "+");

            HttpResponse<JsonNode> response = Unirest.get("https://videogamesrating.p.mashape.com/get.php")
                    .queryString("count", 5)
                    .queryString("game", query)
                    .header("X-Mashape-Key", Credentials.MASHAPE)
                    .header("Accept", "application/json")
                    .asJson();

            JSONArray jsa = response.getBody().getArray();

            if (jsa.length() == 0) {
                note.error("No game found with that title.").queue();
                return;
            }

            JSONObject jso = jsa.getJSONObject(0);

            String title = jso.optString("title");
            String publisher = jso.optString("publisher");
            String score = jso.optString("score");
            String desc = jso.optString("short_description");

            StringJoiner joiner = new StringJoiner("\n");

            joiner.add("Publisher: **[" + publisher + "]()**");
            joiner.add("Score: **[" + score + "]()**");
            joiner.add("Description: **[" + desc + "]()**");

            note.respond(title, joiner.toString(), Bot.getColor(), jso.optString("thumb"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
