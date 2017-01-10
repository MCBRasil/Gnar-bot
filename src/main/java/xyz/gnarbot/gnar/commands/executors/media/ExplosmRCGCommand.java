package xyz.gnarbot.gnar.commands.executors.media;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

@Command(aliases = "rcg", description = "Generate random Cyanide and Happiness comic.")
public class ExplosmRCGCommand extends CommandExecutor
{
    @Override
    public void execute(Note note, String label, String[] args)
    {
        try
        {
            Document document;
            
            document = Jsoup.connect("http://explosm.net/rcg").get();
            
            Element element = document.getElementById("rcg-comic").getElementsByTag("img").get(0);
            
            String url = element.absUrl("src");
            
            String logo = "http://explosm.net/img/logo.png";
            
            note.replyEmbedRaw("Cyanide and Happiness", "**Random Comic Generator**", Bot.getColor(), logo, url);
        }
        catch (Exception e)
        {
            note.error("Unable to grab random Cyanide and Happiness comic.");
            e.printStackTrace();
        }
    }
}

