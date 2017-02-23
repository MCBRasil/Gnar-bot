package xyz.gnarbot.gnar.utils

import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.MessageEmbed
import net.dv8tion.jda.core.entities.User
import net.dv8tion.jda.core.exceptions.PermissionException
import net.dv8tion.jda.core.requests.RestAction
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.members.Client
import xyz.gnarbot.gnar.servers.Servlet
import java.awt.Color
import java.time.temporal.TemporalAccessor
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future
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
    override fun getAuthor(): Client = servlet.clientHandler.asPerson(message.author)

    /**
     * Get mentioned users of this Message as [Client] instances.
     *
     * @return Immutable list of mentioned [Client] instances.
     */
    override fun getMentionedUsers(): List<Client> = message.mentionedUsers.map { servlet.clientHandler.asPerson(it) }

    /**
     * Quick-reply to a message.
     *
     * @param yrcy The text to send.
     * @return The Message created by this function.
     */
    fun reply(text: String): RestAction<Note> {
        val message = MessageBuilder().append(text).build()
        return channel.sendNote(servlet, channel, message)
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
        }
    }

    @JvmOverloads
    fun embed(title: String? = null): EmbedDSL = EmbedDSL(this).title(title)

    fun embed(title: String? = null, block: Consumer<EmbedDSL>): RestAction<Note> {
        return channel.sendNote(servlet, channel, embed(title).apply { block.accept(this) }.build())
    }

    inline fun embed(title: String? = null, value: EmbedDSL.() -> Unit): RestAction<Note> {
        return embed(title).apply { value(this) }.rest()
    }

    /**
     * Send a standard info message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    fun info(msg: String): RestAction<Note> {
        return embed("Info") {
            author("Info", null, "https://gnarbot.xyz/assets/img/info.png")
            description(msg)
        }
    }

    /**
     * Send a standard error message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    fun error(msg: String): RestAction<Note> {
        return embed("Info") {
            author("Error", null, "https://gnarbot.xyz/assets/img/error.png")
            description(msg)
            color(Color.RED)
        }
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

    @Deprecated("Switching soon.")
    fun Future<Message>.toNote(): Future<Note> = object : CompletableFuture<Note>() {
        override fun get(timeout: Long, unit: TimeUnit) = Note(servlet, this@toNote.get(timeout, unit))
        override fun get(): Note = Note(servlet, this@toNote.get())
    }

    @Suppress("NOTHING_TO_INLINE")
    class EmbedDSL(private val message: Note) {
        val delegate = EmbedBuilder()

        init {
            color(Bot.color)
        }

        inline fun description(value: StringBuilder.() -> Unit): EmbedDSL {
            val sb = StringBuilder()
            value(sb)
            delegate.setDescription(sb.toString())
            return this
        }

        fun description(desc: String?): EmbedDSL {
            delegate.setDescription(desc)
            return this
        }

        // USE FOR JAVA
        fun description(value: Consumer<StringBuilder>): EmbedDSL {
            val sb = StringBuilder()
            value.accept(sb)
            delegate.setDescription(sb.toString())
            return this
        }

        inline fun field(name: String?, inline: Boolean, value: Any?): EmbedDSL {
            delegate.addField(name, value.toString(), inline)
            return this
        }

        inline fun field(name: String?, inline: Boolean, value: StringBuilder.() -> Unit): EmbedDSL {
            val sb = StringBuilder()
            value(sb)
            delegate.addField(name, sb.toString(), inline)
            return this
        }

        // USE FOR JAVA
        fun field(name: String?, inline: Boolean, value: Consumer<StringBuilder>): EmbedDSL {
            val sb = StringBuilder()
            value.accept(sb)
            delegate.addField(name, sb.toString(), inline)
            return this
        }

        fun rest(): RestAction<Note> {
            return message.channel.sendNote(message.servlet, message.channel, build())
        }

        fun title(title: String?): EmbedDSL {
            return title(title, null)
        }

        fun title(title: String?, url: String?): EmbedDSL {
            delegate.setTitle(title, url)
            return this
        }

        fun timestamp(temporal: TemporalAccessor?): EmbedDSL {
            delegate.setTimestamp(temporal)
            return this
        }

        fun color(color: Color?): EmbedDSL {
            delegate.setColor(color)
            return this
        }

        fun thumbnail(url: String?): EmbedDSL {
            delegate.setThumbnail(url)
            return this
        }

        fun image(url: String?): EmbedDSL {
            delegate.setImage(url)
            return this
        }

        fun author(user: User): EmbedDSL {
            delegate.setAuthor(user.name, user.avatarUrl, user.avatarUrl)
            return this
        }

        fun author(name: String, url: String?, iconUrl: String): EmbedDSL {
            delegate.setAuthor(name, url, iconUrl)
            return this
        }

        fun footer(text: String, iconUrl: String?): EmbedDSL {
            delegate.setFooter(text, iconUrl)
            return this
        }

        fun addBlankField(inline: Boolean): EmbedDSL {
            delegate.addBlankField(inline)
            return this
        }

        fun build(): MessageEmbed {
            return delegate.build()
        }

        inline fun highlight(string: String) = b("[$string]()")
        inline fun highlight(any: Any) = highlight(any.toString())

        inline fun b(string: String) = "**$string**"
        inline fun b(any: Any?) = b(any.toString())
        inline fun i(string: String) = "*$string*"
        inline fun i(any: Any?) = i(any.toString())
        inline fun u(string: String) = "__${string}__"
        inline fun u(any: Any?) = u(any.toString())

        inline fun link(string: String, url: String? = null) = "[$string]${if (url != null) "($url)" else "()"}"
        inline fun link(any: Any, url: String? = null) = "[$any]${if (url != null) "($url)" else "()"}"
    }
}

