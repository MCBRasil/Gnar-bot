package xyz.gnarbot.gnar.commands.executors.media;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;
import java.util.Random;

@Command(aliases = {"c&h", "cah"}, description = "Get Cyanide and Happiness comics.", usage = "~id")
public class ExplosmCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
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
                        note.error("Explosm does not have a comic for that number.").queue();
                    }

                    rand = String.valueOf(input);
                } catch (NumberFormatException e) {
                    if (args.get(0).equalsIgnoreCase("latest")) {
                        rand = "latest";
                    } else {
                        note.error("You didn't enter a proper ID number.").queue();
                        return;
                    }
                }
            } else {
                rand = String.valueOf(min + new Random().nextInt(max - min));
            }

            document = Jsoup.connect("http://explosm.net/comics/" + rand + "/").get();

            String url = document.getElementById("main-comic").absUrl("src");

            String logo = "http://explosm.net/img/logo.png";

            note.embed("Cyanide and Happiness")
                    .description("No: **" + rand + "**\n")
                    .thumbnail(logo)
                    .image(url)
                    .rest().queue();

        } catch (Exception e) {
            note.error("Unable to grab Cyanide and Happiness comic.").queue();
            e.printStackTrace();
        }
    }
}
