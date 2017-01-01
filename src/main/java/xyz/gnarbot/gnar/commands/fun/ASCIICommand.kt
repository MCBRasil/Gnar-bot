package xyz.gnarbot.gnar.commands.`fun`

import org.apache.commons.lang3.StringUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import xyz.gnarbot.gnar.handlers.commands.Command
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("ascii"), usage = "(string)", description = "ASCII text art!", showInHelp = false)
class ASCIICommand : CommandExecutor()
{
    override fun execute(message : Note, label : String, args : Array<out String>)
    {
        if (args.size == 0)
        {
            message.reply("Please provide a query.")
            return
        }
        
        try
        {
            val query = StringUtils.join(args, "+")
            
            if (query.length > 15)
            {
                message.reply("The query has too many characters. `15 at most.`")
                return
            }
            
            val document = Jsoup.connect("http://artii.herokuapp.com/make?text=$query").get()
            
            val element = document.getElementsByTag("body")[0]
            
            val builder = "```\n${getText(element)}```"
            
            message.replyRaw(builder)
        }
        catch (e : Exception)
        {
            message.reply("Unable to generate ASCII art. `Developer Note: Our ASCII API is down for the moment.`")
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
        return text
    }
}

