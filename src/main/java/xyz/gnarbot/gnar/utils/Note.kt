package xyz.gnarbot.gnar.utils

import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.requests.RestAction
import xyz.gnarbot.gnar.handlers.members.User
import xyz.gnarbot.gnar.handlers.servers.Host

/**
 * Gnar's wrapper class for JDA's [Message].
 *
 * @see Message
 */
class Note(val host : Host, private val message : Message) : Message by message
{
    /**
     * The author of this Message as a [User] instance.
     *
     * @return Message author as Member.
     */
    override fun getAuthor() : User = host.userHandler.asUser(message.author)
    
    /**
     * Get mentioned users of this Message as [User] instances.
     *
     * @return Immutable list of mentioned [User] instances.
     */
    override fun getMentionedUsers() : List<User> = message.mentionedUsers.map { host.userHandler.asUser(it) }
    
    /**
     * Stylized quick-reply to a message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    fun reply(msg : String) = Note(host, channel.sendMessage("__**${message.author.name}**__ \u279c $msg").block())
    
    /**
     * Quick-reply to a message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    fun replyRaw(msg : String) = Note(host, channel.sendMessage(msg).block())
    
    override fun editMessage(newContent : Message?) : RestAction<Message>
    {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
    /**
     * @return String representation of the note.
     */
    override fun toString() = "Note(id=$id, author=${author.name}, content=\"$content\")"
}
