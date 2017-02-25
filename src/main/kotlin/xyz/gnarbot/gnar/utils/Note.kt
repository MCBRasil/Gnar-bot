package xyz.gnarbot.gnar.utils

import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.exceptions.PermissionException
import net.dv8tion.jda.core.requests.RestAction
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.members.Client
import xyz.gnarbot.gnar.servers.Servlet
import java.awt.Color
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

/**
 * The bot's wrapper class for JDA's [Message].
 *
 * @see Message
 * @see Servlet
 */
class Note(val servlet: Servlet, private var message: Message) : Message by message {
    /**
     * The author of this Message as a [Client] instance.
     *
     * @return Message author as User.
     */
    override fun getAuthor(): Client = servlet.clientHandler.getClient(message.author)!!

    /**
     * Get mentioned users of this Message as [Client] instances.
     *
     * @return Immutable list of mentioned [Client] instances.
     */
    override fun getMentionedUsers(): List<Client> = message.mentionedUsers.map { servlet.clientHandler.getClient(it)!! }

    /**
     * Quick-reply to a message.
     *
     * @param text The text to send.
     * @return The Message created by this function.
     */
    fun reply(text: String): RestAction<Note> {
        val message = MessageBuilder().append(text).build()

        return servlet.run {
            channel.sendNote(message)
        }
    }

    /**
     * Send an embeded message.
     *
     * @param text The text to send.
     * @return The Message created by this function.
     */
    @JvmOverloads
    fun respond(title: String? = null,
                text: String?,
                color: Color? = Bot.color,
                thumb: String? = null,
                img: String? = null): RestAction<Note> {
        return embed(title) {
            description(text)
            color(color)
            thumbnail(thumb)
            image(img)
        }.rest()
    }

    @JvmOverloads
    fun embed(title: String? = null): EmbedCreator = EmbedCreator(this).title(title)

    fun embed(title: String? = null, block: Consumer<EmbedCreator>): EmbedCreator {
        return embed(title).apply { block.accept(this) }
    }

    inline fun embed(title: String? = null, value: EmbedCreator.() -> Unit): EmbedCreator {
        return embed(title).apply { value(this) }
    }

    /**
     * Send a standard info message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    fun info(msg: String): RestAction<Note> {
        return embed {
            author("Info", null, "https://gnarbot.xyz/assets/img/info.png")
            description(msg)
        }.rest()
    }

    /**
     * Send a standard error message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    fun error(msg: String): RestAction<Note> {
        return embed {
            author("Error", null, "https://gnarbot.xyz/assets/img/error.png")
            description(msg)
            color(Color.RED)
        }.rest()
    }

    fun optDelete(): Boolean {
        try {
            message.delete().queue()
            return true
        } catch(e: PermissionException) {
            return false
        }
    }

    fun optDelete(seconds: Long) {
        Bot.scheduler.schedule({ optDelete() }, seconds, TimeUnit.SECONDS)
    }

    /**
     * @return String representation of the note.
     */
    override fun toString() = "Note(id=$id, author=${author.name}, content=\"$content\")"
}

