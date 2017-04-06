package xyz.gnarbot.gnar.commands.executors.media

import b
import link
import net.dv8tion.jda.core.entities.Message
import org.json.JSONException
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.utils.YouTube
import java.awt.Color

@Command(
        aliases = arrayOf("youtube"),
        usage = "-query...",
        description = "Search and get a YouTube video."
)
class YoutubeCommand : CommandExecutor() {
    override fun execute(message: Message, args: List<String>) {
        if (args.isEmpty()) {
            message.respond().error("Gotta put something to search YouTube.").queue()
            return
        }

        try {
            val query = args.joinToString("+")

            val results = YouTube.search(query, 3)

            if (results.isEmpty()) {
                message.respond().error("No search results for `$query`.").queue()
                return
            }
            var firstUrl: String? = null

            message.respond().embed {
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

            message.respond().text("**First Video:** $firstUrl").queue()
        } catch (e: JSONException) {
            message.respond().error("Unable to get YouTube results.").queue()
            e.printStackTrace()
        } catch (e: NullPointerException) {
            message.respond().error("Unable to get YouTube results.").queue()
            e.printStackTrace()
        }

    }
}



