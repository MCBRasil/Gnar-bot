package xyz.gnarbot.gnar.utils

import net.dv8tion.jda.entities.Message
import xyz.gnarbot.gnar.Host
import xyz.gnarbot.gnar.handlers.Member

/**
 * Gnar's wrapper class for JDA's [Message].
 *
 * @see Message
 */
class Note(val host : Host, private val message : Message) : Message by message
{
    /**
     * The author of this Message as a [Member] instance.
     *
     * @return Message author as Member.
     */
    override fun getAuthor() : Member = host.memberHandler.asMember(message.author)
    
    /**
     * Get mentioned users of this Message as [Member] instances.
     *
     * @return Immutable list of mentioned [Member] instances.
     */
    override fun getMentionedUsers() : List<Member> = message.mentionedUsers.map { host.memberHandler.asMember(it) }
    
    /**
     * Stylized quick-reply to a message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    fun reply(msg : String) : Message = message.channel.sendMessage("__**${message.author.username}**__ \u279c $msg")
    
    /**
     * Quick-reply to a message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    fun replyRaw(msg : String) : Message = message.channel.sendMessage(msg)
    
    /**
     * Append and update a message.
     *
     * @param append The text to append.
     * @return The updated Message.
     */
    fun updateAppend(append : String?) : Message = updateMessage(content + append)
    
    /**
     * @return String representation of the note.
     */
    override fun toString() = "Note(id=$id, author=${author.username}, content=\"$content\")"
}
