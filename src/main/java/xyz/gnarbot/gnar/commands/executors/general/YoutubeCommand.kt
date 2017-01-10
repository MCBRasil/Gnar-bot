package xyz.gnarbot.gnar.commands.executors.general

import com.mashape.unirest.http.Unirest
import net.dv8tion.jda.core.EmbedBuilder
import org.json.*
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.handlers.*
import xyz.gnarbot.gnar.utils.Note
import java.awt.Color
import java.util.StringJoiner

@Command(aliases = arrayOf("youtube"), usage = "(query)", description = "Search and get a YouTube video.")
class YoutubeCommand : CommandExecutor()
{
    override fun execute(note : Note, label : String, args : Array<String>)
    {
        if (args.isEmpty())
        {
            note.error("Gotta put something to search YouTube.")
            return
        }
        
        try
        {
            val query = args.joinToString("+")
            
            val jsonObject = Unirest.get("https://www.googleapis.com/youtube/v3/search")
                    .queryString("part", "snippet")
                    .queryString("maxResults", 3)
                    .queryString("type", "video")
                    .queryString("q", query)
                    .queryString("key", Bot.authTokens["youtube"])
                    .asJson()
                    .body
                    .`object`
            
            if (jsonObject.length() == 0)
            {
                note.error("No search results for `$query`.")
                return
            }
            
            val items = jsonObject.getJSONArray("items")
            
            val sj = StringJoiner("\n")
            
            for (obj in items)
            {
                val item = obj as JSONObject
                
                val snippet = item.getJSONObject("snippet")
                
                val title = snippet.getString("title")
                val desc = snippet.getString("description")
                val videoID = item
                        .getJSONObject("id")
                        .getString("videoId")
                val url = "https://www.youtube.com/watch?v=$videoID"
                
                sj.add("\n**[$title]($url)**\n$desc")
            }
            
            val eb = EmbedBuilder()
                    .setAuthor("YouTube Results", "https://www.youtube.com", "https://s.ytimg.com/yts/img/favicon_144-vflWmzoXw.png")
                    .setThumbnail("https://s.ytimg.com/yts/img/favicon_144-vflWmzoXw.png")
                    .setDescription(sj.toString())
                    .setColor(Color(141, 20, 0))
            
            note.channel.sendMessage(eb.build()).queue()
        }
        catch (e : JSONException)
        {
            note.error("Unable to get YouTube results.")
            e.printStackTrace()
        }
        catch (e : NullPointerException)
        {
            note.error("Unable to get YouTube results.")
            e.printStackTrace()
        }
        
    }
}



