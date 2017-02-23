package xyz.gnarbot.gnar.commands.executors.fun;

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

@Command(aliases = "ub")
public class UrbanDictionaryCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
        try {
            String query = StringUtils.join(args, "+");

            JSONObject json = Unirest.get("https://mashape-community-urban-dictionary.p.mashape.com/define")
                    .queryString("term", query)
                    .header("X-Mashape-Key", Credentials.MASHAPE)
                    .header("Accept", "text/plain")
                    .asJson()
                    .getBody()
                    .getObject();

            JSONArray words = json.getJSONArray("list");

            if (words.length() < 1) {
                note.error("Could not find that word, rip u");
                return;
            }

            JSONObject word = words.getJSONObject(0);

            note.embed("Urban Dictionary")
                    .color(Bot.getColor())
                    .thumbnail("https://s3.amazonaws.com/mashape-production-logos/apis/53aa4f67e4b0a9b1348da532_medium")
                    .field("Word", true, "[" + word.getString("word") + "](" + word.getString("permalink") + ")")
                    .field("Definition", true, word.optString("definition"))
                    .field("Example", true, word.optString("example"))
                    .rest().queue();

        } catch (Exception e) {
            note.error("Could not find that word, rip u").queue();
        }
    }

}
