package xyz.gnarbot.utils

import net.dv8tion.jda.entities.Message

/**
 * Gnar's wrapper class for JDA's [Message].
 *
 * @see Message
 */
class Note(private val message : Message) : Message by message
{
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
}
