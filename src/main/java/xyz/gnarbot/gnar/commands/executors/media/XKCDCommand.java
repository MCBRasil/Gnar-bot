package xyz.gnarbot.gnar.commands.executors.media;

import com.mashape.unirest.http.Unirest;
import net.dv8tion.jda.core.entities.Message;
import org.json.JSONObject;
import xyz.gnarbot.gnar.Constants;
import xyz.gnarbot.gnar.commands.handlers.Category;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;

import java.util.List;
import java.util.Random;

@Command(
        aliases = "xkcd",
        description = "Grab some XKCD comics.",
        category = Category.FUN
)
public class XKCDCommand extends CommandExecutor {
    @Override
    public void execute(Message message, List<String> args) {
        try {
            JSONObject latestJso = Unirest.get("http://xkcd.com/info.0.json").asJson().getBody().getObject();

            if (latestJso != null) {
                int min = 500;
                int max = latestJso.getInt("num");

                int rand;
                if (args.size() >= 1) {
                    int input;
                    try {
                        input = Integer.valueOf(args.get(0));

                        if (input > max || input < 1) {
                            message.respond().error("xkcd does not have a comic for that number.").queue();
                        }

                        rand = input;
                    } catch (NumberFormatException e) {
                        if (args.get(0).equalsIgnoreCase("latest")) {
                            rand = max;
                        } else {
                            message.respond().error("You didn't enter a proper number.").queue();
                            return;
                        }
                    }
                } else {
                    rand = min + new Random().nextInt(max - min);
                }

                JSONObject jso = Unirest.get("http://xkcd.com/" + rand + "/info.0.json").asJson().getBody().getObject();

                if (jso != null) {
                    String title = jso.getString("title");

                    int num = jso.getInt("num");

                    String url = jso.getString("img").replaceAll("\\\\/", "/");

                    String logo = "http://imgs.xkcd.com/static/terrible_small_logo.png";

                    message.respond().embed(title)
                            .setColor(Constants.COLOR)
                            .setDescription("No: " + num)
                            .setThumbnail(logo)
                            .setImage(url)
                            .rest().queue();

                    return;
                }
            }

            message.respond().error("Unable to grab xkcd comic.").queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}