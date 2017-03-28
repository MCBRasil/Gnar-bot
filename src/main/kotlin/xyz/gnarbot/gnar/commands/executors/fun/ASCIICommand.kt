package xyz.gnarbot.gnar.commands.executors.`fun`

import org.apache.commons.lang3.StringUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.utils.Note
import xyz.gnarbot.gnar.utils.Utils
import java.util.*

@Command(
        aliases = arrayOf("ascii"),
        usage = "-string",
        description = "ASCII text art!"
)
class ASCIICommand : CommandExecutor() {
    override fun execute(note: Note, args: List<String>) {
        if (args.isEmpty()) {
            note.respond().error("Please provide a query.").queue()
            return
        }

        try {
            val query = StringUtils.join(args, "+")

            if (query.length > 15) {
                note.respond().error("The query has too many characters. `15 at most.`").queue()
                return
            }

            val document = Jsoup.connect("http://artii.herokuapp.com/make?text=$query").get()

            val element = document.getElementsByTag("body")[0]

            note.respond().embed("ASCII Text") {
                color = Constants.COLOR
                description = "```\n${getText(element)}```"
            }.rest().queue()

        } catch (e: Exception) {
            note.respond().error("Unable to generate ASCII art.").queue()
            e.printStackTrace()
        }

    }

    private fun getText(cell: Element): String? {
        var text: String? = null
        val childNodes = cell.childNodes()
        if (childNodes.size > 0) {
            val childNode = childNodes[0]
            if (childNode is TextNode) {
                text = childNode.wholeText
            }
        }
        if (text == null) {
            text = cell.text()
        }

        text?.let { Utils.fastSplit(it, '\n') }?.let {
            val b = StringJoiner("\n")

            it.filterNot(String::isNullOrBlank)
                    .forEach { b.add(it) }

            text = b.toString()
        }

        return text
    }
}

