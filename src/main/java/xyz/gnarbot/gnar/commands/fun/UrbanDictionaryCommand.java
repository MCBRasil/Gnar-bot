package xyz.gnarbot.gnar.commands.fun;

import com.google.inject.Inject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.handlers.servers.Host;
import xyz.gnarbot.gnar.utils.Note;

@Command(aliases = "ub")
public class UrbanDictionaryCommand extends CommandExecutor {

    @Inject
    public Host host;

    @Override
    public void execute(Note msg, String label, String[] args) {
        try {
            HttpResponse<String> s = Unirest.get("https://mashape-community-urban-dictionary.p.mashape.com/define?term=" + StringUtils.join(args, "+"))
                    .header("X-Mashape-Key", Bot.INSTANCE.getAuthTokens().getProperty("mashape"))
                    .header("Accept", "text/plain")
                    .asString();

            JSONObject json = new JSONObject(s.getBody());

            JSONArray words = json.getJSONArray("list");

            msg.reply("\n\n**Definition:** " + words.getJSONObject(0).get("definition") + "\n\n**Example:** " + words.getJSONObject(0).get("example"));
        } catch (Exception e) { msg.reply("Could not find that word, rip u"); }
    }

}
