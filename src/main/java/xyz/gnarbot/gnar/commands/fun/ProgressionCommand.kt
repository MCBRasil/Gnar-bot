package xyz.gnarbot.gnar.commands.`fun`

import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.text.WordUtils
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.handlers.commands.Command
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor
import xyz.gnarbot.gnar.handlers.members.Clearance
import xyz.gnarbot.gnar.utils.Note
import java.util.*
import java.util.concurrent.TimeUnit

@Command(aliases = arrayOf("progress"), clearance = Clearance.BOT_MASTER, showInHelp = false)
class ProgressionCommand : CommandExecutor()
{
    override fun execute(message : Note, label : String, args : Array<out String>)
    {
        val joiner = StringJoiner("\n", "```", "```")
        joiner.add("﻿ ___________________________ ")
        joiner.add("| Progression     [_][☐][✕]|")
        joiner.add("|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|")
        
        val lines = WordUtils
                .wrap(StringUtils.join(args, ' ').replace("```",""), 25)
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
        
        for (i in 0..20)
        {
            val builder = StringBuilder()
            repeat(20) {
                if (it < i+1) builder.append('█')
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
            val msg = message.replyRaw(list[0])
    
            list.forEachIndexed { i, s ->
                Bot.scheduler.schedule({
                    msg.editMessage(list[i]).queue()
                }, i + 1L, TimeUnit.SECONDS)
            }
        }
        catch (e : UnsupportedOperationException)
        {
            message.reply("Message was too long or something... no memes for you.")
        }
    }
}