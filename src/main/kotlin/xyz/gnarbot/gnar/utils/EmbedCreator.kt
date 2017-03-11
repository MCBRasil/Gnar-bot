package xyz.gnarbot.gnar.utils

import net.dv8tion.jda.core.AbstractEmbedBuilder
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.requests.RestAction
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.servers.Servlet

@Suppress("NOTHING_TO_INLINE")
class EmbedCreator(private val servlet: Servlet, private val textChannel: TextChannel) : AbstractEmbedBuilder<EmbedCreator>() {
    init {
        color = Bot.color
    }

    fun rest(): RestAction<Note> {
        return servlet.run {
            textChannel.sendNote(build())
        }
    }


}