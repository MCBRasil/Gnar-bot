package xyz.gnarbot.gnar.utils

import net.dv8tion.jda.entities.Message
import xyz.gnarbot.gnar.GuildHandler
import xyz.gnarbot.gnar.handlers.Member

/**
 * Gnar's wrapper class for JDA's [Message].
 *
 * @see Message
 */
class Note(private val guildHandler : GuildHandler, private val message : Message) : Message by message
{
    /**
     * The author of this Message as a [Member] instance.
     *
     * @return Message author
     */
    override fun getAuthor() : Member? = guildHandler.memberHandler.asMember(author)
    
    /**
     * Stylized quick-reply to a message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    fun reply(msg : String) = message.channel.sendMessage("${message.author.username} \u279c $msg")
    
    /**
     * Quick-reply to a message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    fun replyRaw(msg : String) = message.channel.sendMessage(msg)
    
    /**
     * Gives string representation.
     */
    override fun toString() = "Note(author=${author?.username}, content=\"$content\")"
}
