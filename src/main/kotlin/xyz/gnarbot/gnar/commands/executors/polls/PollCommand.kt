package xyz.gnarbot.gnar.commands.executors.polls

import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.commands.Command
import xyz.gnarbot.gnar.commands.CommandExecutor
import java.util.concurrent.TimeUnit

@Command(aliases = arrayOf("poll"),
        usage = "(option 1);(option 2);...",
        description = "Create a poll.")
class PollCommand : CommandExecutor() {
    override fun execute(message: Message, args: Array<String>) {
        val options = args.joinToString(" ").split(',').map(String::trim)

        message.respond().embed("Poll") {
            description = "Vote through clicking the reactions on the choices below! Results will be final in 1 minute!"
            field("Options") {
                buildString {
                    options.forEachIndexed { index, option ->
                        appendln("${'\u0030' + index}\u20E3 **$option**")
                    }
                }
            }
        }.rest().queue {
            for (index in 0..options.size - 1) {
                it.addReaction("${'\u0030' + index}\u20E3").queue()
            }

            it.editMessage(it.embeds[0].edit().apply {
                description = "Voting has ended! Check the results in the newer messages!"
                clearFields()
            }.build()).queueAfter(1, TimeUnit.MINUTES) {
                it.respond().embed("Poll Results") {
                    description {
                        "Voting has ended! Here are the results!"
                    }

                    var topVotes = 0
                    val winners = mutableListOf<Int>()

                    field("Results") {
                        buildString {
                            it.reactions.forEach { reaction ->
                                val value = reaction.emote.name[0] - '\u0030'
                                if (value !in 0..options.size - 1) return@forEach

                                options[value].let {
                                    appendln("${reaction.emote.name} **$it** — __${reaction.count - 1} Votes__")

                                    if (reaction.count - 1 > topVotes) {
                                        winners.clear()
                                        topVotes = reaction.count - 1
                                        winners += value
                                    } else if (reaction.count - 1 == topVotes) {
                                        winners += value
                                    }
                                }
                            }
                        }
                    }

                    field("Winner") {
                        winners.joinToString(prefix = "**", postfix = "**") { "**${options[it]}**" }
                    }
                }.rest().queue()
            }
        }
    }
}
