package xyz.gnarbot.gnar.commands.executors.media;

import org.w3c.dom.Document;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.List;

@Command(aliases = {"cats", "cat"})
public class CatsCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
        try {
            String apiKey = "MTAyODkw";

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc;

            if (args.size() >= 1 && args.get(0) != null) {
                switch (args.get(0)) {
                    case "png":
                    case "jpg":
                    case "gif":
                        doc = db.parse("http://thecatapi.com/api/images/get?format=xml&type=" + args.get(0) + "&api_key="
                                + apiKey + "&results_per_page=1");

                        break;
                    default:
                        note.error("Not a valid picture type. `[png, jpg, gif]`").queue();
                        return;
                }
            } else {
                doc = db.parse(new URL("http://thecatapi.com/api/images/get?format=xml&api_key=" + apiKey +
                        "&results_per_page=1")
                        .openStream());
            }

            String url = doc.getElementsByTagName("url").item(0).getTextContent();

            note.embed("Random Cat Pictures")
                    .image(url)
                    .rest().queue();

        } catch (Exception e) {
            note.error("Unable to find cats to sooth the darkness of your soul.").queue();
            e.printStackTrace();
        }
    }
}
