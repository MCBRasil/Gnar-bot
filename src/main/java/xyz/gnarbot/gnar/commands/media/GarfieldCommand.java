package xyz.gnarbot.gnar.commands.media;

import com.google.inject.Inject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.handlers.servers.Host;
import xyz.gnarbot.gnar.utils.Note;

@Command(aliases = "garfield")
public class GarfieldCommand extends CommandExecutor
{
    @Inject
    public Host host;
    
    @Override
    public void execute(Note note, String label, String[] args)
    {
        try
        {
            Document document;
            
            document = Jsoup.connect("https://garfield.com/comic/random").followRedirects(true).get();
            
            String link = document.getElementsByClass("img-responsive").get(0).absUrl("src");
            
            String builder = "Garfield" + "\n" + "Date: **" + link.substring(link.lastIndexOf("/") + 1, link
                    .lastIndexOf(".")) + "**\n" + "Link: " + link;
            note.replyRaw(builder);
            
        }
        catch (Exception e)
        {
            note.reply("Unable to grab Garfield comic.");
            e.printStackTrace();
        }
    }
}
