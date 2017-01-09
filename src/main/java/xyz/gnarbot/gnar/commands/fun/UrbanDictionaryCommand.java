package xyz.gnarbot.gnar.commands.fun;

import com.google.inject.Inject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.handlers.servers.Host;
import xyz.gnarbot.gnar.utils.Note;

@Command(aliases = "ub")
public class UrbanDictionaryCommand extends CommandExecutor
{
    
    @Inject
    public Host host;
    
    @Override
    public void execute(Note note, String label, String[] args)
    {
        try
        {
            HttpResponse<String> s = Unirest.get("https://mashape-community-urban-dictionary.p.mashape" +
                    ".com/define?term=" + StringUtils.join(args, "+")).header("X-Mashape-Key", Bot.INSTANCE
                    .getAuthTokens().getProperty("mashape")).header("Accept", "text/plain").asString();
            
            JSONObject json = new JSONObject(s.getBody());
            
            JSONArray words = json.getJSONArray("list");
            
            JSONObject word = words.getJSONObject(0);
            
            note.replyEmbedRaw("Urban Dictionary",
                    "Word: **[" + word.get("word") + "](" + word.get("permalink") + ")**\n\n"
                    +"Definition: \n**[" + word.get("definition") + "]()**\n\n"
                    + "Example: \n**[" + word.get("example") + "]()**",
                    
                    Bot.getColor(), "https://s3.amazonaws.com/mashape-production-logos/apis/53aa4f67e4b0a9b1348da532_medium");
        }
        catch (Exception e) { note.error("Could not find that word, rip u"); }
    }
    
}
