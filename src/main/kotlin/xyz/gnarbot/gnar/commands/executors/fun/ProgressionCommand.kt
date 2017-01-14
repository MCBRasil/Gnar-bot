package xyz.gnarbot.gnar.commands.executors.`fun`

import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.text.WordUtils
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.handlers.*
import xyz.gnarbot.gnar.members.Clearance
import xyz.gnarbot.gnar.utils.Note
import java.util.StringJoiner
import java.util.concurrent.TimeUnit

@Command(
        aliases = arrayOf("progress"),
        clearance = Clearance.BOT_MASTER,
        showInHelp = false
)
class ProgressionCommand : CommandExecutor()
{
    override fun execute(note : Note, label : String, args : Array<out String>)
    {
        val joiner = StringJoiner("\n", "```", "```")
        joiner.add("﻿ ___________________________ ")
        joiner.add("| Progression     [_][☐][✕]|")
        joiner.add("|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|")
        
        val lines = WordUtils
                .wrap(StringUtils.join(args, ' ').replace("```", ""), 25)
                .split("\n")
        
        lines.forEach {
            val builder = StringBuilder()
            
            repeat(25 - it.trim().length) { builder.append(' ') }
            
            var str = it.trim()
            
            if (str.length > 25)
            {
                str = "${str.substring(0, 22)}..."
            }
            
            joiner.add("| $str$builder |")
        }
        
        
        joiner.add("| ____________________      |")
        joiner.add("||var-A| var-B |")
        joiner.add("| ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾      |")
        joiner.add(" ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾ ")
        
        val list = arrayListOf<String>()
        
        for (i in 0 .. 20)
        {
            val builder = StringBuilder()
            repeat(20) {
                if (it < i + 1) builder.append('█')
                else builder.append(' ')
            }
            
            var percent = (i * 5).toString()
            while (percent.length < 3)
            {
                percent = " $percent"
            }
            
            list.add(joiner
                    .toString()
                    .replace("var-A", builder.toString())
                    .replace("var-B", percent))
        }
        
        try
        {
            val msg = note.replyRaw(list[0])
            
            list.forEachIndexed { i, s ->
                Bot.scheduler.schedule({
                    msg.get().edit(list[i])
                }, i + 1L, TimeUnit.SECONDS)
            }
        }
        catch (e : UnsupportedOperationException)
        {
            note.error("Message was too long or something... no memes for you.")
        }
    }
}