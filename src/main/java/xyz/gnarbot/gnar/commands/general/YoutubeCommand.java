package xyz.gnarbot.gnar.commands.general;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;
import xyz.gnarbot.gnar.utils.Utils;

@Command(
        aliases = "youtube",
        usage = "(query)",
        description = "Search and get a YouTube video."
)
public class YoutubeCommand extends CommandExecutor
{
    @Override
    public void execute(Note message, String label, String[] args)
    {
        if (args.length == 0)
        {
            message.reply("Gotta have a query to YouTube.");
            return;
        }
        
        try
        {
            String query = StringUtils.join(args, " ");
            Note msg = message.reply("Searching `" + query + "`.");
            
            query = query.replace(" ", "+");
            
            String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&type=video&q=" + query + "&key=" + Bot.INSTANCE.getAuthTokens().get("youtube");
            
            JSONObject jsonObject = Utils.jsonFromUrl(url);
            
            String videoID = jsonObject.getJSONArray("items").getJSONObject(0).getJSONObject("id").getString("videoId");
            message.getChannel().sendMessage("https://www.youtube.com/watch?v=" + videoID).queue();
            msg.deleteMessage();
        }
        catch (JSONException | NullPointerException e)
        {
            message.reply("Unable to get YouTube results.");
            e.printStackTrace();
        }
    }
}



