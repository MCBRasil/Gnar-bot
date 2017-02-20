package xyz.gnarbot.gnar.utils

import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.MessageEmbed
import net.dv8tion.jda.core.entities.User
import net.dv8tion.jda.core.exceptions.PermissionException
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.members.Person
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
     * The author of this Message as a [Person] instance.
     *
     * @return Message author as User.
     */
    override fun getAuthor(): Person = servlet.peopleHandler.asPerson(message.author)

    /**
     * Get mentioned users of this Message as [Person] instances.
     *
     * @return Immutable list of mentioned [Person] instances.
     */
    override fun getMentionedUsers(): List<Person> = message.mentionedUsers.map { servlet.peopleHandler.asPerson(it) }

    /**
     * Quick-reply to a message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    fun reply(msg: String) = channel.sendMessage(msg).submit().toNote()

    /**
     * Send an embeded message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    @JvmOverloads
    fun respond(title: String? = null,
                msg: String?,
                color: Color? = Bot.color,
                thumb: String? = null,
                img: String? = null): Future<Note> {
        return channel.sendMessage(makeEmbed(title, msg, color, thumb, img))
                .submit()
                .toNote()
    }

    @JvmOverloads
    fun embed(title: String? = null): EmbedDSL = EmbedDSL(this).setTitle(title)

    inline fun embed(title: String? = null, value: EmbedDSL.() -> Unit): Future<Note> {
        return embed(title).apply { value(this) }.respond().toNote()
    }

    /**
     * Send a standard info message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    fun info(msg: String): Future<Note> {
        val eb = EmbedBuilder()
                .setAuthor("Info", null, "https://gnarbot.xyz/assets/img/info.png")
                .setDescription(msg)
                .setColor(Bot.color)

        return channel.sendMessage(eb.build()).submit().toNote()
    }

    /**
     * Send a standard error message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    fun error(msg: String): Future<Note> {
        val eb = EmbedBuilder()
                .setAuthor("Error", null, "https://gnarbot.xyz/assets/img/error.png")
                .setDescription(msg)
                .setColor(Color.RED)

        return channel.sendMessage(eb.build()).submit().toNote()
    }

    fun edit(msg: String) = editMessage(msg).submit().toNote()

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

    fun Future<Message>.toNote(): Future<Note> = object : CompletableFuture<Note>() {
        override fun get(timeout: Long, unit: TimeUnit) = Note(servlet, this@toNote.get(timeout, unit))
        override fun get(): Note = Note(servlet, this@toNote.get())
    }

    @Suppress("NOTHING_TO_INLINE")
    class EmbedDSL(private val message: Message) : EmbedBuilder() {
        inline fun description(value: StringBuilder.() -> Unit): EmbedDSL {
            val sb = StringBuilder()
            value(sb)
            super.setDescription(sb.toString())
            return this
        }

        // USE FOR JAVA
        fun description(value: Consumer<StringBuilder>): EmbedDSL {
            val sb = StringBuilder()
            value.accept(sb)
            super.setDescription(sb.toString())
            return this
        }

        inline fun field(name: String?, inline: Boolean, value: Any): EmbedDSL {
            super.addField(name, value.toString(), inline)
            return this
        }

        inline fun field(name: String?, inline: Boolean, value: StringBuilder.() -> Unit): EmbedDSL {
            val sb = StringBuilder()
            value(sb)
            super.addField(name, sb.toString(), inline)
            return this
        }

        // USE FOR JAVA
        fun field(name: String?, inline: Boolean, value: Consumer<StringBuilder>): EmbedDSL {
            val sb = StringBuilder()
            value.accept(sb)
            super.addField(name, sb.toString(), inline)
            return this
        }

        fun respond(): Future<Message> {
            return message.channel.sendMessage(build()).submit()
        }

        inline fun highlight(string: String) = b("[$string]()")
        inline fun highlight(any: Any) = highlight(any.toString())

        inline fun b(string: String) = "**$string**"
        inline fun b(any: Any) = b(any.toString())
        inline fun i(string: String) = "*$string*"
        inline fun i(any: Any) = i(any.toString())
        inline fun u(string: String) = "__${string}__"
        inline fun u(any: Any) = u(any.toString())

        inline fun link(string: String, url: String? = null) = "[$string]${if (url != null) "($url)" else "()"}"
        inline fun link(any: Any, url: String? = null) = "[$any]${if (url != null) "($url)" else "()"}"

        fun setTitle(title: String?): EmbedDSL {
            return setTitle(title, null)
        }

        override fun setTitle(title: String?, url: String?): EmbedDSL {
            super.setTitle(title, url)
            return this
        }

        override fun setDescription(desc: String?): EmbedDSL {
            super.setDescription(desc)
            return this
        }

        override fun setTimestamp(temporal: TemporalAccessor?): EmbedDSL {
            super.setTimestamp(temporal)
            return this
        }

        override fun setColor(color: Color?): EmbedDSL {
            super.setColor(color)
            return this
        }

        override fun setThumbnail(url: String?): EmbedDSL {
            super.setThumbnail(url)
            return this
        }

        override fun setImage(url: String?): EmbedDSL {
            super.setImage(url)
            return this
        }

        fun setAuthor(user: User): EmbedDSL {
            super.setAuthor(user.name, null, user.avatarUrl)
            return this
        }

        override fun setAuthor(name: String, url: String, iconUrl: String): EmbedDSL {
            super.setAuthor(name, url, iconUrl)
            return this
        }

        override fun setFooter(text: String, iconUrl: String): EmbedDSL {
            super.setFooter(text, iconUrl)
            return this
        }

        override fun addField(field: MessageEmbed.Field): EmbedDSL {
            super.addField(field)
            return this
        }

        override fun addField(name: String, value: String, inline: Boolean): EmbedDSL {
            super.addField(name, value, inline)
            return this
        }

        override fun addBlankField(inline: Boolean): EmbedDSL {
            super.addBlankField(inline)
            return this
        }
    }
}

