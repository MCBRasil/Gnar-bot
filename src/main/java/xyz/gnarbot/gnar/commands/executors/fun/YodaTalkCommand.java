package xyz.gnarbot.gnar.commands.executors.fun;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

@Command(aliases = {"yodatalk"}, usage = "(sentence)", description = "Learn to speak like Yoda, you will.")
public class YodaTalkCommand extends CommandExecutor
{
    @Override
    public void execute(Note note, String label, String[] args)
    {
        if (args.length == 0)
        {
            note.error("At least put something. `:[`");
            return;
        }
        
        try
        {
            String query = StringUtils.join(args, "+");
            
            HttpResponse<String> response = Unirest.get("https://yoda.p.mashape.com/yoda?sentence=" + query)
                    //.queryString("sentence", query)
                    .header("X-Mashape-Key", "dw1mYrC2ssmsh2WkFGHaherCtl48p1wtuHWjsnYbP3Y7q8y6M5")
                    .header("Accept", "text/plain")
                    .asString();
            
            String result = response.getBody();
            
            note.replyEmbedRaw("Yoda-Speak", "**[" + result + "]()**", Bot.getColor(), "https://upload.wikimedia" +
                    ".org/wikipedia/en/9/9b/Yoda_Empire_Strikes_Back.png");
        }
        catch (UnirestException e)
        {
            e.printStackTrace();
        }
    }
}