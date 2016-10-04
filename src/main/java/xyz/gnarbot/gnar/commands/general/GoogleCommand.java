package xyz.gnarbot.gnar.commands.general;

import net.dv8tion.jda.entities.Message;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

@Command(
        aliases = "google",
        usage = "(query)",
        description = "Who needs browsers!?"
)
public class GoogleCommand extends CommandExecutor
{
    @Override
    public void execute(Note note, String label, String[] args)
    {
        if (args.length == 0)
        {
            note.reply("Gotta have a query to Google.");
            return;
        }
        
        try
        {
            String query = StringUtils.join(args, " ");
            Message msg = note.reply("Searching `" + query + "`.");
            
            String userAgent = "GN4R-Bot";
            
            Elements links = Jsoup.connect(
                    String.format("http://www.google.com/search?q=%s", URLEncoder.encode(query, StandardCharsets.UTF_8.displayName())))
                    .userAgent(userAgent).get().select(".g>.r>a");
            
            StringJoiner joiner = new StringJoiner("\n");
            
            for (Element link : links)
            {
                String title = link.text();
                String url = link.absUrl("href");
                url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), StandardCharsets.UTF_8.displayName());
                
                if (!url.startsWith("http"))
                {
                    continue;
                }
                
                joiner.add("Title: **" + title + "**");
                joiner.add("URL: " + url);
                
                break;
            }
            
            
            if (!links.isEmpty()) msg.updateMessage(joiner.toString());
            else
                msg.updateMessage(String.format("%s ➜ No results for `%s`.", note.getAuthor().getAsMention(), query));
        }
        catch (IOException e)
        {
            note.reply("Caught an exception while trying to Google stuff.");
            e.printStackTrace();
        }
    }
}



