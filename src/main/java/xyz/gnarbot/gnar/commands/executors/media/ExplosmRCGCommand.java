package xyz.gnarbot.gnar.commands.executors.media;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import xyz.gnarbot.gnar.Constants;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;

@Command(aliases = "rcg", description = "Generate random Cyanide and Happiness comic.")
public class ExplosmRCGCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
        try {
            Document document;

            document = Jsoup.connect("http://explosm.net/rcg").get();

            Element element = document.getElementById("rcg-comic").getElementsByTag("img").first();

            String url = element.absUrl("src");

            String logo = "http://explosm.net/img/logo.png";

            note.respond().embed("Cyanide and Happiness")
                    .setColor(Constants.COLOR)
                    .setDescription("**Random Comic Generator**")
                    .setImage(url)
                    .setThumbnail(logo)
                    .rest().queue();

        } catch (Exception e) {
            note.respond().error("Unable to grab random Cyanide and Happiness comic.").queue();
            e.printStackTrace();
        }
    }
}

