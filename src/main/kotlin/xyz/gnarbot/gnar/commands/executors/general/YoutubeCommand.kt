package xyz.gnarbot.gnar.commands.executors.general

import b
import link
import org.json.JSONException
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.utils.Note
import xyz.gnarbot.gnar.utils.YouTube
import java.awt.Color

@Command(aliases = arrayOf("youtube"), usage = "-query...", description = "Search and get a YouTube video.")
class YoutubeCommand : CommandExecutor() {
    override fun execute(note: Note, args: List<String>) {
        if (args.isEmpty()) {
            note.respond().error("Gotta put something to search YouTube.").queue()
            return
        }

        try {
            val query = args.joinToString("+")

            val results = YouTube.search(query, 3)

            if (results.isEmpty()) {
                note.respond().error("No search results for `$query`.").queue()
                return
            }
            var firstUrl: String? = null

            note.respond().embed {
                setAuthor("YouTube Results", "https://www.youtube.com", "https://s.ytimg.com/yts/img/favicon_144-vflWmzoXw.png")
                thumbnail = "https://gnarbot.xyz/assets/img/youtube.png"
                color = Color(141, 20, 0)

                description {
                    for (result in results) {

                        val title = result.title
                        val desc = result.description
                        val videoID = result.id
                        val url = "https://www.youtube.com/watch?v=$videoID"

                        if (firstUrl == null) {
                            firstUrl = url
                        }

                        appendln(b(link(title, url))).appendln(desc)
                    }
                }
            }.rest().queue()

            note.respond().text("**First Video:** $firstUrl").queue()
        } catch (e: JSONException) {
            note.respond().error("Unable to get YouTube results.").queue()
            e.printStackTrace()
        } catch (e: NullPointerException) {
            note.respond().error("Unable to get YouTube results.").queue()
            e.printStackTrace()
        }

    }
}



