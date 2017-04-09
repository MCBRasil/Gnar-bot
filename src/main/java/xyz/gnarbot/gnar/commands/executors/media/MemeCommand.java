package xyz.gnarbot.gnar.commands.executors.media;


import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import xyz.gnarbot.gnar.Constants;
import xyz.gnarbot.gnar.Credentials;
import xyz.gnarbot.gnar.commands.handlers.Category;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Command(
        aliases = "meme",
        usage = "-meme_name | _top | _bottom",
        description = "Create the dankest memes ever.",
        category = Category.FUN
)
public class MemeCommand extends CommandExecutor {
    private static final Map<String, String> map = new TreeMap<>();

    static {
        try {
            JSONObject memes = Unirest.get("https://api.imgflip.com/get_memes").asJson().getBody().getObject();

            JSONArray memeList = memes
                    .getJSONObject("data")
                    .getJSONArray("memes");

            for (int i = 0; i < memeList.length(); i++) {
                JSONObject jso = memeList.optJSONObject(i);
                map.put(jso.getString("name"), jso.getString("id"));
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute(Message message, String[] args) {
        try {
            if (args[0].equalsIgnoreCase("list")) {
                int page = 1;

                try {
                    if (args[1] != null) {
                        page = Integer.valueOf(args[1]);
                        if (page <= 0) { page = 1; }
                    }
                } catch (Exception ignore) {}

                Set<String> names = map.keySet();


                int pages;

                if (map.keySet().size() % 10 == 0) {
                    pages = names.size() / 10;
                } else {
                    pages = names.size() / 10 + 1;
                }

                if (page > pages) page = pages;

                int _page = page;
                message.respond().embed("Meme List")
                        .description(sb -> {
                            int i = 0;
                            for (String g : names) {
                                i++;
                                if (i < 10 * _page + 1 && i > 10 * _page - 10) {
                                    sb.append("**#").append(i).append("** [").append(g).append("]()").append('\n');
                                }
                            }
                        })
                        .setFooter("Page [" + page + "/" + pages + "]")
                        .rest().queue();

                return;
            }

            String request = StringUtils.join(args, " ");
            String[] arguments = request.split("\\|");

            int ld = 999;
            String id = null;

            for (Map.Entry<String, String> entry : map.entrySet()) {
                int _d = StringUtils.getLevenshteinDistance(entry.getKey(), arguments[0].trim());

                if (_d < ld) {
                    ld = _d;
                    id = entry.getValue();
                }
            }

            JSONObject response = Unirest.get("https://api.imgflip.com/caption_image")
                    .queryString("template_id", id)
                    .queryString("username", "GNARBot")
                    .queryString("password", Credentials.IMGFLIP)
                    .queryString("text0", arguments[1].trim())
                    .queryString("text1", arguments[2].trim())
                    .asJson()
                    .getBody()
                    .getObject()
                    .getJSONObject("data");

            message.respond().embed("Meme Generator")
                    .setColor(Constants.COLOR)
                    .setImage(response.optString("url"))
                    .rest().queue();

        } catch (Exception e) {
            message.respond().error(
                    "**Please supply more arguments. Example Usage:**\n\n" +
                    "[_meme Spongegar | Top Text | Bottom Text]()\n\n" +
                    "**For a list of memes, type:**\n\n" +
                    "[_meme list (page #)]()").queue();
        }
    }

}
