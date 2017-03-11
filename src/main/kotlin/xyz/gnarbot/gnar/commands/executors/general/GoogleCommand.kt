package xyz.gnarbot.gnar.commands.executors.general

import org.jsoup.Jsoup
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.utils.Note
import java.io.IOException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Command(aliases = arrayOf("google"), usage = "-query...", description = "Who needs browsers!?")
class GoogleCommand : CommandExecutor() {
    override fun execute(note: Note, args: List<String>) {
        if (args.isEmpty()) {
            note.respond().error("Gotta have something to search Google.").queue()
            return
        }

        try {
            val query = args.joinToString(" ")

            val blocks = Jsoup.connect("http://www.google.com/search?q=${URLEncoder.encode(query, StandardCharsets.UTF_8.displayName())}")
                    .userAgent("Gnar")
                    .get()
                    .select(".g")

            if (blocks.isEmpty()) {
                note.respond().error("No search results for `$query`.").queue()
                return
            }

            note.respond().embed {
                color = Constants.COLOR
                setAuthor("Google Results", "https://www.google.com/", "https://www.google.com/favicon.ico")
                thumbnail = "https://gnarbot.xyz/assets/img/google.png"

                description {
                    var count = 0

                    for (block in blocks) {
                        if (count >= 3) break

                        val list = block.select(".r>a")
                        if (list.isEmpty()) break

                        val entry = list[0]
                        val title = entry.text()
                        val url = entry.absUrl("href").replace(")", "\\)")
                        var desc: String? = null

                        val st = block.select(".st")
                        if (!st.isEmpty()) desc = st[0].text()

                        appendln(b(link(title, url))).appendln(desc)

                        count++
                    }
                }
            }.rest().queue()
        } catch (e: IOException) {
            note.respond().error("Caught an exception while trying to Google stuff.").queue()
            e.printStackTrace()
        }
    }
}
