package xyz.gnarbot.gnar.commands.executors.`fun`

import org.apache.commons.lang3.StringUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.*
import xyz.gnarbot.gnar.commands.handlers.*
import xyz.gnarbot.gnar.utils.Note
import java.util.StringJoiner

@Command(
        aliases = arrayOf("ascii"),
        usage = "(string)",
        description = "ASCII text art!"
)
class ASCIICommand : CommandExecutor()
{
    override fun execute(note : Note, label : String, args : Array<out String>)
    {
        if (args.isEmpty())
        {
            note.error("Please provide a query.")
            return
        }
        
        try
        {
            val query = StringUtils.join(args, "+")
            
            if (query.length > 15)
            {
                note.error("The query has too many characters. `15 at most.`")
                return
            }
            
            val document = Jsoup.connect("http://artii.herokuapp.com/make?text=$query").get()
            
            val element = document.getElementsByTag("body")[0]
            
            val builder = "```\n${getText(element)}```"
            
            note.replyEmbedRaw("ASCII Text", builder)
        }
        catch (e : Exception)
        {
            note.error("Unable to generate ASCII art.")
            e.printStackTrace()
        }
        
    }
    
    private fun getText(cell : Element) : String?
    {
        var text : String? = null
        val childNodes = cell.childNodes()
        if (childNodes.size > 0)
        {
            val childNode = childNodes[0]
            if (childNode is TextNode)
            {
                text = childNode.wholeText
            }
        }
        if (text == null)
        {
            text = cell.text()
        }
        
        text?.split("\n")?.let {
            val b = StringJoiner("\n")
            
            it.filterNot(String::isNullOrBlank)
                    .forEach { b.add(it) }
            
            text = b.toString()
        }
        
        return text
    }
}

