package xyz.gnarbot.gnar.commands.executors.`fun`

import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.text.WordUtils
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.members.Level
import xyz.gnarbot.gnar.utils.Note
import xyz.gnarbot.gnar.utils.schedule
import java.util.*
import java.util.concurrent.TimeUnit

@Command(
        aliases = arrayOf("progress"),
        level = Level.BOT_CREATOR,
        showInHelp = false
)
class ProgressionCommand : CommandExecutor() {
    override fun execute(note: Note, args: List<String>) {
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

            if (str.length > 25) {
                str = "${str.substring(0, 22)}..."
            }

            joiner.add("| $str$builder |")
        }


        joiner.add("| ____________________      |")
        joiner.add("||var-A| var-B |")
        joiner.add("| ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾      |")
        joiner.add(" ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾ ")

        val list = arrayListOf<String>()

        for (i in 0..20) {
            val builder = StringBuilder()
            repeat(20) {
                if (it < i + 1) builder.append('█')
                else builder.append(' ')
            }

            var percent = (i * 5).toString()
            while (percent.length < 3) {
                percent = " $percent"
            }

            list.add(joiner
                    .toString()
                    .replace("var-A", builder.toString())
                    .replace("var-B", percent))
        }

        try {
            val msg = note.respond().text(list[0]).complete()

            list.forEachIndexed { i, _ ->
                bot.scheduler.schedule(i + 1L, TimeUnit.SECONDS) {
                    msg.editMessage(list[i]).queue()
                }
            }
        } catch (e: UnsupportedOperationException) {
            note.respond().error("Message was too long or something... no memes for you.").queue()
        }
    }
}