package xyz.gnarbot.gnar.utils

import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.MessageEmbed
import net.dv8tion.jda.core.exceptions.PermissionException
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.members.Person
import xyz.gnarbot.gnar.servers.Host
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
 * @see Host
 */
class Note(val host: Host, private var message: Message) : Message by message {
    /**
     * The author of this Message as a [Person] instance.
     *
     * @return Message author as User.
     */
    override fun getAuthor(): Person = host.peopleHandler.asPerson(message.author)

    /**
     * Get mentioned users of this Message as [Person] instances.
     *
     * @return Immutable list of mentioned [Person] instances.
     */
    override fun getMentionedUsers(): List<Person> = message.mentionedUsers.map { host.peopleHandler.asPerson(it) }

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

    fun embed() : EmbedAction = EmbedAction(this)

    /**
     * Send a standard info message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    fun info(msg: String): Future<Note> {
        val eb = EmbedBuilder()
                .setAuthor("Info", null, "http://gnarbot.xyz/img/Info.png")
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
                .setAuthor("Error", null, "http://gnarbot.xyz/img/Error.png")
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

    private fun Future<Message>.toNote(): Future<Note> = object : CompletableFuture<Note>() {
        override fun get(timeout: Long, unit: TimeUnit) = Note(host, this@toNote.get(timeout, unit))
        override fun get(): Note = Note(host, this@toNote.get())
    }

    class EmbedAction(private val note: Note) : EmbedBuilder() {


        fun title(title: String?) : EmbedAction {
            return setTitle(title, null)
        }

        inline fun description(value: StringBuilder.() -> Unit) : EmbedAction {
            val sb = StringBuilder()
            value(sb)
            super.setDescription(sb.toString())
            return this
        }

        // USE FOR JAVA
        fun description(value: Consumer<StringBuilder>) : EmbedAction {
            val sb = StringBuilder()
            value.accept(sb)
            super.setDescription(sb.toString())
            return this
        }

        inline fun field(name: String?, inline: Boolean, value: StringBuilder.() -> Unit): EmbedAction {
            val sb = StringBuilder()
            value(sb)
            super.addField(name, sb.toString(), inline)
            return this
        }

        // USE FOR JAVA
        fun field(name: String?, inline: Boolean, value: Consumer<StringBuilder>): EmbedAction {
            val sb = StringBuilder()
            value.accept(sb)
            super.addField(name, sb.toString(), inline)
            return this
        }



        fun respond() {
            note.channel.sendMessage(build()).queue()
        }




        override fun setTitle(title: String?, url: String?) : EmbedAction {
            super.setTitle(title, url)
            return this
        }

        override fun setDescription(desc: String?) : EmbedAction {
            super.setDescription(desc)
            return this
        }

        override fun setTimestamp(temporal : TemporalAccessor?) : EmbedAction {
            super.setTimestamp(temporal)
            return this
        }

        override fun setColor(color: Color?) : EmbedAction {
            super.setColor(color)
            return this
        }

        override fun setThumbnail(url: String?) : EmbedAction {
            super.setThumbnail(url)
            return this
        }

        override fun setImage(url: String?) : EmbedAction {
            super.setImage(url)
            return this
        }

        override fun setAuthor(name : String, url : String, iconUrl: String) : EmbedAction {
            super.setAuthor(name, url, iconUrl)
            return this
        }

        override fun setFooter(text : String, iconUrl : String) : EmbedAction {
            super.setFooter(text, iconUrl)
            return this
        }

        override fun addField(field : MessageEmbed.Field) : EmbedAction {
            super.addField(field)
            return this
        }

        override fun addField(name: String, value: String, inline: Boolean) : EmbedAction {
            super.addField(name, value, inline)
            return this
        }

        override fun addBlankField(inline: Boolean) : EmbedAction {
            super.addBlankField(inline)
            return this
        }
    }
}

