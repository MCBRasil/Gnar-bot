package xyz.gnarbot.gnar.commands.executors.`fun`

import net.dv8tion.jda.core.entities.Message
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.text.WordUtils
import xyz.gnarbot.gnar.commands.handlers.Category
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import java.util.*

@Command(
        aliases = arrayOf("progress"),
        administrator = true,
        category = Category.NONE
)
class ProgressionCommand : CommandExecutor() {
    override fun execute(message: Message, args: List<String>) {
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
//            message.respond().text(list[0]).queue { msg ->
//                list.forEachIndexed { i, _ ->
//                    bot.scheduler.schedule(i + 1L, TimeUnit.SECONDS) {
//                        msg.editMessage(list[i]).queue()
//                    }
//                }
//            }
        } catch (e: UnsupportedOperationException) {
            message.respond().error("Message was too long or something... no memes for you.").queue()
        }
    }
}