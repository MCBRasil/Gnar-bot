package xyz.gnarbot.gnar.commands.executors.media;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;

@Deprecated
@Command(aliases = "garfield", showInHelp = false)
public class GarfieldCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
        try {
            Document document;

            document = Jsoup.connect("https://garfield.com/comic/random").followRedirects(true).get();

            String link = document.getElementsByClass("img-responsive").get(0).absUrl("src");

            String builder = "Garfield" + "\n" + "Date: **" + link.substring(link.lastIndexOf("/") + 1, link
                    .lastIndexOf(".")) + "**\n" + "Link: " + link;

            //note.replyRaw(builder);
            note.error("Garfield command is broken right now.");
        } catch (Exception e) {
            note.error("Unable to grab Garfield comic.");
            e.printStackTrace();
        }
    }
}
