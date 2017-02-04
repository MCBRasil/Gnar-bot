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
import java.util.StringJoiner;

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

            JSONObject word = words.getJSONObject(0);

            StringJoiner sj = new StringJoiner("\n");

            sj.add("Word: **[" + word.get("word") + "](" + word.get("permalink") + ")" + "**\n\n");
            sj.add("Definition: \n**[" + word.get("definition") + "]()**\n\n");
            sj.add("Example: \n**[" + word.get("example") + "]()**");

            String logo = "https://s3.amazonaws.com/mashape-production-logos/apis/53aa4f67e4b0a9b1348da532_medium";

            note.replyEmbedRaw("Urban Dictionary", sj.toString(), Bot.getColor(), logo);
        } catch (Exception e) { note.error("Could not find that word, rip u"); }
    }

}
