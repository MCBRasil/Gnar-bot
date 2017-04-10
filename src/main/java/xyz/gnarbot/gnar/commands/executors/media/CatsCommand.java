package xyz.gnarbot.gnar.commands.executors.media;

import net.dv8tion.jda.core.entities.Message;
import org.w3c.dom.Document;
import xyz.gnarbot.gnar.Constants;
import xyz.gnarbot.gnar.commands.Category;
import xyz.gnarbot.gnar.commands.Command;
import xyz.gnarbot.gnar.commands.CommandExecutor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;

@Command(
        aliases = {"cats", "cat"},
        description = "Grab random cats for you.",
        category = Category.FUN
)
public class CatsCommand extends CommandExecutor {
    @Override
    public void execute(Message message, String[] args) {
        try {
            String apiKey = "MTAyODkw";

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc;

            if (args.length >= 1 && args[0] != null) {
                switch (args[0]) {
                    case "png":
                    case "jpg":
                    case "gif":
                        doc = db.parse("http://thecatapi.com/api/images/get?format=xml&type=" + args[0] + "&api_key="
                                + apiKey + "&results_per_page=1");

                        break;
                    default:
                        message.respond().error("Not a valid picture type. `[png, jpg, gif]`").queue();
                        return;
                }
            } else {
                doc = db.parse(new URL("http://thecatapi.com/api/images/get?format=xml&api_key=" + apiKey +
                        "&results_per_page=1")
                        .openStream());
            }

            String url = doc.getElementsByTagName("url").item(0).getTextContent();

            message.respond().embed("Random Cat Pictures")
                    .setColor(Constants.COLOR)
                    .setImage(url)
                    .rest().queue();

        } catch (Exception e) {
            message.respond().error("Unable to find cats to sooth the darkness of your soul.").queue();
            e.printStackTrace();
        }
    }
}
