package xyz.gnarbot.gnar.commands.executors.media;

import com.mashape.unirest.http.Unirest;
import org.json.JSONObject;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;
import java.util.Random;

@Command(aliases = "xkcd")
public class XKCDCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
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
                            note.error("xkcd does not have a comic for that number.").queue();
                        }

                        rand = input;
                    } catch (NumberFormatException e) {
                        if (args.get(0).equalsIgnoreCase("latest")) {
                            rand = max;
                        } else {
                            note.error("You didn't enter a proper number.").queue();
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

                    note.embed(title)
                            .description("No: " + num)
                            .thumbnail(logo)
                            .image(url)
                            .rest().queue();

                    return;
                }
            }

            note.error("Unable to grab xkcd comic.").queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}