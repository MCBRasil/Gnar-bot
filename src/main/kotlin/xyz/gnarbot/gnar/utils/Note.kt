package xyz.gnarbot.gnar.utils

import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.exceptions.PermissionException
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.members.Person
import xyz.gnarbot.gnar.servers.Host
import java.awt.Color
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

/**
 * The bot's wrapper class for JDA's [Message].
 *
 * @see Message
 * @see Host
 */
class Note(val host: Host, private val message: Message) : Message by message {
    /**
     * The author of this Message as a [Person] instance.
     *
     * @return Message author as User.
     */
    override fun getAuthor(): Person = host.personHandler.asPerson(message.author)

    /**
     * Get mentioned users of this Message as [Person] instances.
     *
     * @return Immutable list of mentioned [Person] instances.
     */
    override fun getMentionedUsers(): List<Person> = message.mentionedUsers.map { host.personHandler.asPerson(it) }

    /**
     * Stylized quick-reply to a message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    @Deprecated("Use one of the embeds, it has style you fucks.")
    fun reply(msg: String) = channel.sendMessage("__**${message.author.name}__ ›** $msg").submit().toNote()

    /**
     * Quick-reply to a message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    fun replyRaw(msg: String) = channel.sendMessage(msg).submit().toNote()

    /**
     * Send an embeded message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    @JvmOverloads
    fun replyEmbed(title: String? = null,
                   msg: String?,
                   color: Color? = Bot.color,
                   thumb: String? = null,
                   img: String? = null): Future<Note> {
        return channel.sendMessage(makeEmbed(title, msg, color, thumb, img, author))
                .submit()
                .toNote()
    }

    /**
     * Send an embeded message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    @JvmOverloads
    fun replyEmbedRaw(title: String? = null,
                      msg: String?,
                      color: Color? = Bot.color,
                      thumb: String? = null,
                      img: String? = null): Future<Note> {
        return channel.sendMessage(makeEmbed(title, msg, color, thumb, img))
                .submit()
                .toNote()
    }

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

    fun delete(): Boolean {
        try {
            deleteMessage().queue()
            return true
        } catch(e: PermissionException) {
            return false
        }
    }

    fun delete(seconds: Long) {
        Bot.scheduler.schedule({ delete() }, seconds, TimeUnit.SECONDS)
    }

    /**
     * @return String representation of the note.
     */
    override fun toString() = "Note(id=$id, author=${author.name}, content=\"$content\")"

    private fun Future<Message>.toNote(): Future<Note> = object : CompletableFuture<Note>() {
        override fun get(timeout: Long, unit: TimeUnit) = Note(host, this@toNote.get(timeout, unit))
        override fun get(): Note = Note(host, this@toNote.get())
    }
}

