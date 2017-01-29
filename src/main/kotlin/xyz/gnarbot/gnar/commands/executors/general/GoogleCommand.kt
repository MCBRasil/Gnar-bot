package xyz.gnarbot.gnar.commands.executors.general

import net.dv8tion.jda.core.EmbedBuilder
import org.jsoup.Jsoup
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.utils.Note
import java.io.IOException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*

@Command(aliases = arrayOf("google"), usage = "-query...", description = "Who needs browsers!?")
class GoogleCommand : CommandExecutor() {
    override fun execute(note: Note, args: Array<String>) {
        if (args.isEmpty()) {
            note.error("Gotta have something to search Google.")
            return
        }

        try {
            val query = args.joinToString(" ")

            val blocks = Jsoup.connect("http://www.google.com/search?q=%s${URLEncoder.encode(query, StandardCharsets.UTF_8.displayName())}")
                    .userAgent("Gnar")
                    .get()
                    .select(".g")

            if (blocks.isEmpty()) {
                note.error("No search results for `$query`.")
                return
            }

            val sj = StringJoiner("\n")

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

                sj.add("\n**[$title]($url)**\n$desc")

                count++
            }

            val eb = EmbedBuilder().setAuthor("Google Results", "https://www.google.com/", "https://www.google.com/favicon.ico")
                    .setThumbnail("http://gnarbot.xyz/img/Google.jpg")
                    .setDescription(sj.toString())
                    .setColor(Bot.color)

            note.channel.sendMessage(eb.build()).queue()
        } catch (e: IOException) {
            note.error("Caught an exception while trying to Google stuff.")
            e.printStackTrace()
        }
    }
}


/*
package xyz.gnarbot.gnar.commands.executors.general

import net.dv8tion.jda.core.EmbedBuilder
import org.jsoup.Jsoup
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.handlers.commands.Command
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor
import xyz.gnarbot.gnar.utils.Note
import java.io.IOException
import java.net.URISyntaxException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Command(aliases = arrayOf("google"), usage = "(query)", description = "Who needs browsers!?")
class GoogleCommand : CommandExecutor()
{
    override fun execute(note : Note, label : String, args : Array<String>)
    {
        if (args.isEmpty())
        {
            note.replyError("Gotta have something to search Google.")
            return
        }
        
        try
        {
            val query = args.joinToString(" ")
            
            val userAgent = "GN4R-Bot"
            
            val blocks = Jsoup.connect("http://www.google.com/search?q=%s${URLEncoder.encode(query, StandardCharsets.UTF_8.displayName())}")
                    .userAgent(userAgent)
                    .get()
                    .select(".g")
            
            if (blocks.isEmpty())
            {
                note.replyError("No search results for `$query`.")
                return
            }
    
            val eb = EmbedBuilder()
            
            var count = 0
            
            for (block in blocks)
            {
                if (count >= 3) break
                
                val list = block.select(".r>a")
                if (list.isEmpty()) break
                
                val entry = list[0]
                val title = entry.text()
                val url = entry.absUrl("href")
                var desc : String? = null
                
                val st = block.select(".st")
                if (!st.isEmpty()) desc = st[0].text()
                
                eb.addField(title, "$desc\n**[Google Link]($url)**", false)
                
                count++
            }
    
            eb.setAuthor("Google Results", "https://www.google.com/", "https://www.google.com/favicon.ico")
                .setThumbnail("https://www.google.com/favicon.ico")
                .setColor(Bot.color)
            
            note.channel.sendMessage(eb.build()).queue()
        }
        catch (e : IOException)
        {
            note.replyError("Caught an exception while trying to Google stuff.")
            e.printStackTrace()
        }
        catch (e : URISyntaxException)
        {
            e.printStackTrace()
        }
        
    }
}
 */
