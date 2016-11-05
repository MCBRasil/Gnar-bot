package xyz.gnarbot.gnar.commands.fun;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

@Command(
        aliases = {"yodatalk"},
        usage = "(sentence)",
        description = "Learn to speak like Yoda, you will."
)
public class YodaTalkCommand extends CommandExecutor
{
    @Override
    public void execute(Note message, String label, String[] args)
    {
        if (args.length == 0)
        {
            message.reply("At least put something. `:[`");
            return;
        }
        
        try
        {
            String query = StringUtils.join(args, "+");
            
            HttpResponse<String> response = Unirest.get("https://yoda.p.mashape.com/yoda?sentence=" + query)
                    .header("X-Mashape-Key", "dw1mYrC2ssmsh2WkFGHaherCtl48p1wtuHWjsnYbP3Y7q8y6M5")
                    .header("Accept", "text/plain")
                    .asString();
            
            String result = response.getBody();
            
            message.reply("`" + result + "`.");
        }
        catch (UnirestException e)
        {
            e.printStackTrace();
        }
    }
}