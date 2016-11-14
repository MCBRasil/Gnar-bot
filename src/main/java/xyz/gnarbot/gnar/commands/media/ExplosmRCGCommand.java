package xyz.gnarbot.gnar.commands.media;

import com.google.inject.Inject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.handlers.servers.Host;
import xyz.gnarbot.gnar.utils.Note;

@Command(aliases = "rcg")
public class ExplosmRCGCommand extends CommandExecutor
{
    @Inject
    public Host host;

    @Override
    public void execute(Note msg, String label, String[] args)
    {
        try
        {
            Document document;

            document = Jsoup.connect("http://explosm.net/rcg").get();

            Element element = document.getElementById("rcg-comic").getElementsByTag("img").get(0);

            String builder =
                    "Cyanide and Happiness" + "\n" +
                            "**Random Comic Generator**\n" +
                            "Link: " + element.absUrl("src");

            msg.replyRaw(builder);

        }
        catch (Exception e)
        {
            msg.reply("Unable to grab random Cyanide and Happiness comic.");
            e.printStackTrace();
        }
    }
}

