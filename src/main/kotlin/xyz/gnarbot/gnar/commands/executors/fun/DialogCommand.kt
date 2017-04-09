package xyz.gnarbot.gnar.commands.executors.`fun`

import net.dv8tion.jda.core.entities.Message
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.text.WordUtils
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import java.util.*

@Command(aliases = arrayOf("dialog"),
        usage = "-words...",
        description = "Make some of that Windows ASCII art!")
class DialogCommand : CommandExecutor() {
    override fun execute(message: Message, args: Array<String>) {
        val joiner = StringJoiner("\n", "```", "```")
        joiner.add("﻿ ___________________________ ")
        joiner.add("| Dialog          [_][☐][✕]|")
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

        when (Random().nextInt(5)) {
            0 -> {
                joiner.add("|   _________    ________   |")
                joiner.add("|  |   Yes   |  |   No   |  |")
                joiner.add("|   ‾‾‾‾‾‾‾‾‾    ‾‾‾‾‾‾‾‾   |")
            }
            1 -> {
                joiner.add("|  _____    ______    ____  |")
                joiner.add("| | Yes |  | What |  | No | |")
                joiner.add("|  ‾‾‾‾‾    ‾‾‾‾‾‾    ‾‾‾‾  |")
            }
            2 -> {
                joiner.add("|   _________    ________   |")
                joiner.add("|  |  Maybe  |  |( ͡° ͜ʖ ͡°)|  |")
                joiner.add("|   ‾‾‾‾‾‾‾‾‾    ‾‾‾‾‾‾‾‾   |")
            }
            3 -> {
                joiner.add("|   _____________________   |")
                joiner.add("|  |     Confirm     | X |  |")
                joiner.add("|   ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾   |")
            }
            4 -> {
                joiner.add("|   ___________   _______   |")
                joiner.add("|  | HELLA YES | | PUSSY |  |")
                joiner.add("|   ‾‾‾‾‾‾‾‾‾‾‾   ‾‾‾‾‾‾‾   |")
            }
        }

        joiner.add(" ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾ ")

        try {
            message.respond().embed {
                color = Constants.COLOR
                description = joiner.toString()
            }.rest().queue()
        } catch (e: UnsupportedOperationException) {
            message.respond().error("Message was too long or something... no memes for you.").queue()
        }
    }
}