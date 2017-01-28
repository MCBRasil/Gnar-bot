@file:JvmName("KUtils")

package xyz.gnarbot.gnar.utils

import com.mashape.unirest.http.Unirest
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.MessageEmbed
import org.json.JSONException
import org.json.JSONObject
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.members.User
import java.awt.Color
import java.io.File
import java.util.*

fun File.readProperties() : Properties
{
    return Properties().apply { load(this@readProperties.inputStream()) }
}

fun File.child(path : String) = File(this, path)

@JvmOverloads
fun makeEmbed(title : String?, msg : String?, color : Color? = Bot.color, thumb : String? = null, img : String? = null, author : User? = null) : MessageEmbed
{
    return EmbedBuilder().run {
        setDescription(msg)
        setTitle(title)
        setColor(color)
        
        if (author != null)
        {
            setAuthor(author.name, null, author.avatarUrl)
        }
        
        setThumbnail(thumb)
        
        setImage(img)
        
        build()
    }
}
fun getYouTubeResults(args : Array<String>, note : Note, label : String) : MessageEmbed?{
    if (args.isEmpty()) {
        note.error("Gotta put something to search YouTube.")
        return null
    }

    try {
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

        if (jsonObject.length() == 0) {
            note.error("No search results for `$query`.")
            return null
        }

        val items = jsonObject.getJSONArray("items")

        val sj = StringJoiner("\n")

        var firstUrl: String? = null

        //Optimize this Avarel, I don't know Kotlin
        for (obj in items) {
            val item = obj as JSONObject

            val snippet = item.getJSONObject("snippet")

            val title = snippet.getString("title")
            val desc = snippet.getString("description")
            val videoID = item
                    .getJSONObject("id")
                    .getString("videoId")
            val url = "https://www.youtube.com/watch?v=$videoID"

            if (firstUrl == null) {
                firstUrl = url
            }

            sj.add("\n**[$title]($url)**\n$desc")
        }

        val eb = EmbedBuilder()
                .setAuthor("YouTube Results", "https://www.youtube.com", "https://s.ytimg.com/yts/img/favicon_144-vflWmzoXw.png")
                .setThumbnail("https://s.ytimg.com/yts/img/favicon_144-vflWmzoXw.png")
                .setDescription(firstUrl)
                .setColor(Color(141, 20, 0))

        return eb.build()
    } catch (e: JSONException) {
        note.error("Unable to get YouTube results.")
        e.printStackTrace()
    } catch (e: NullPointerException) {
        note.error("Unable to get YouTube results.")
        e.printStackTrace()
    }
    return null
}

fun getFirstVideo(args : Array<String>) : String? {
    if (args.isEmpty()) {
        return "Gotta put something in the search"
    }

    try {
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

        if (jsonObject.length() == 0) {
            return "No search results for $query"
        }

        val items = jsonObject.getJSONArray("items")

        //Optimize this Avarel, I don't know Kotlin
        for (obj in items) {
            val item = obj as JSONObject
            val videoID = item
                    .getJSONObject("id")
                    .getString("videoId")

            return "https://www.youtube.com/watch?v=$videoID"
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "wat?"
}