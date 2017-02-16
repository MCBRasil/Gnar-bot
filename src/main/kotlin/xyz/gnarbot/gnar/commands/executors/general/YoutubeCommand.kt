package xyz.gnarbot.gnar.commands.executors.general

import net.dv8tion.jda.core.EmbedBuilder
import org.json.JSONException
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.utils.Note
import xyz.gnarbot.gnar.utils.YouTube
import java.awt.Color
import java.util.*

@Command(aliases = arrayOf("youtube"), usage = "-query...", description = "Search and get a YouTube video.")
class YoutubeCommand : CommandExecutor() {
    override fun execute(note: Note, args: List<String>) {
        if (args.isEmpty()) {
            note.error("Gotta put something to search YouTube.")
            return
        }

        try {
            val query = args.joinToString("+")

            val results = YouTube.search(query, 3)

            if (results.isEmpty()) {
                note.error("No search results for `$query`.")
                return
            }

            val sj = StringJoiner("\n")

            var firstUrl: String? = null

            for (result in results) {

                val title = result.title
                val desc = result.description
                val videoID = result.id
                val url = "https://www.youtube.com/watch?v=$videoID"

                if (firstUrl == null) {
                    firstUrl = url
                }

                sj.add("\n**[$title]($url)**\n$desc")
            }

            val eb = EmbedBuilder()
                    .setAuthor("YouTube Results", "https://www.youtube.com", "https://s.ytimg.com/yts/img/favicon_144-vflWmzoXw.png")
                    .setThumbnail("https://s.ytimg.com/yts/img/favicon_144-vflWmzoXw.png")
                    .setDescription(sj.toString())
                    .setColor(Color(141, 20, 0))

            note.channel.sendMessage(eb.build()).queue()
            note.reply("**First Video:** $firstUrl")
        } catch (e: JSONException) {
            note.error("Unable to get YouTube results.")
            e.printStackTrace()
        } catch (e: NullPointerException) {
            note.error("Unable to get YouTube results.")
            e.printStackTrace()
        }

    }
}



