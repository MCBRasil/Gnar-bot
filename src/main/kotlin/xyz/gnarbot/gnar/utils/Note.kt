package xyz.gnarbot.gnar.utils

import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.exceptions.PermissionException
import xyz.gnarbot.gnar.members.Client
import xyz.gnarbot.gnar.servers.Servlet
import java.util.concurrent.TimeUnit

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

    fun optDelete(): Boolean {
        try {
            message.delete().queue()
            return true
        } catch(e: PermissionException) {
            return false
        }
    }

    fun optDelete(seconds: Long) {
        servlet.bot.scheduler.schedule(seconds, TimeUnit.SECONDS) { optDelete() }
    }

    /**
     * @return String representation of the note.
     */
    override fun toString() = "Note(id=$id, author=${author.name}, content=\"$content\")"
}

