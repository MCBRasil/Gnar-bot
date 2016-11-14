package xyz.gnarbot.gnar.commands.media;

import com.google.inject.Inject;
import org.w3c.dom.Document;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.handlers.servers.Host;
import xyz.gnarbot.gnar.utils.Note;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;

@Command(aliases = "cat")
public class CatsCommand extends CommandExecutor
{
    @Inject
    public Host host;

    @Override
    public void execute(Note msg, String label, String[] args)
    {
        try {
            String apiKey = "MTAyODkw";

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc;

            if (args.length >= 1 && args[0] != null)
            {
                switch (args[0])
                {
                    case "png":
                    case "jpg":
                    case "gif":
                        doc = db.parse(new URL(String.format("http://thecatapi.com/api/images/get?format=xml&type=%s&api_key=%s&results_per_page=1", args[0], apiKey)).openStream());
                        break;
                    default:
                        msg.getChannel().sendMessage(String.format("%s ➜ Not a valid picture type. `[png, jpg, gif]`", msg.getAuthor().getAsMention())).queue();
                        return;
                }
            }
            else
            {
                doc = db.parse(new URL(String.format("http://thecatapi.com/api/images/get?format=xml&api_key=%s&results_per_page=1", apiKey)).openStream());
            }

            String catURL = doc.getElementsByTagName("url").item(0).getTextContent();

            msg.replyRaw("Random Cat Pictures\nLink: " + catURL);
        }
        catch (Exception e)
        {
            msg.reply("Unable to find cats to sooth the darkness of your soul.");
            e.printStackTrace();
        }
    }
}
