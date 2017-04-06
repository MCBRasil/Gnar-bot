package xyz.gnarbot.gnar.commands.executors.media;

import net.dv8tion.jda.core.entities.Message;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import xyz.gnarbot.gnar.Constants;
import xyz.gnarbot.gnar.commands.handlers.Category;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;

import java.util.List;
import java.util.Random;

@Command(
        aliases = {"c&h", "cah"},
        description = "Get Cyanide and Happiness comics.",
        usage = "~id",
        category = Category.FUN
)
public class ExplosmCommand extends CommandExecutor {
    @Override
    public void execute(Message message, List<String> args) {
        try {
            Document document;

            int min = 1500;
            int max = 4500;

            String rand;

            if (args.size() >= 1) {
                int input;
                try {
                    input = Integer.valueOf(args.get(0));

                    if (input > max || input < 100) {
                        message.respond().error("Explosm does not have a comic for that number.").queue();
                    }

                    rand = String.valueOf(input);
                } catch (NumberFormatException e) {
                    if (args.get(0).equalsIgnoreCase("latest")) {
                        rand = "latest";
                    } else {
                        message.respond().error("You didn't enter a proper ID number.").queue();
                        return;
                    }
                }
            } else {
                rand = String.valueOf(min + new Random().nextInt(max - min));
            }

            document = Jsoup.connect("http://explosm.net/comics/" + rand + "/").get();

            String url = document.getElementById("main-comic").absUrl("src");

            String logo = "http://explosm.net/img/logo.png";

            message.respond().embed("Cyanide and Happiness")
                    .setColor(Constants.COLOR)
                    .setDescription("No: **" + rand + "**\n")
                    .setThumbnail(logo)
                    .setImage(url)
                    .rest().queue();

        } catch (Exception e) {
            message.respond().error("Unable to grab Cyanide and Happiness comic.").queue();
            e.printStackTrace();
        }
    }
}
