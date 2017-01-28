package xyz.gnarbot.gnar.commands.executors.general

import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.utils.Note
import xyz.gnarbot.gnar.utils.getYouTubeResults
import java.awt.Color

@Command(aliases = arrayOf("youtube"), usage = "-query...", description = "Search and get a YouTube video.")
class YoutubeCommand : CommandExecutor()
{
    override fun execute(note : Note, label : String, args : Array<String>)
    {
        var a = getYouTubeResults(args, note, label)

        if(a!=null) {
            note.channel.sendMessage(a);
        } else {
            note.replyEmbed("YouTube Results", "Error: Video not found, did you make a typo?", Color(141, 20, 0), "https://s.ytimg.com/yts/img/favicon_144-vflWmzoXw.png")
        }
    }
}



