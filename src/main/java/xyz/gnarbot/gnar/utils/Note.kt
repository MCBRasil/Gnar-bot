package xyz.gnarbot.gnar.utils

import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.exceptions.PermissionException
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.handlers.members.User
import xyz.gnarbot.gnar.handlers.servers.Host
import java.awt.Color

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
     * @return Message author as User.
     */
    override fun getAuthor() : User? = host.userHandler.asUser(message.author)
    
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
    fun reply(msg : String) = Note(host, channel.sendMessage("__**${message.author.name}__ ›** $msg").complete())
    
    fun edit(msg : String)
    {
        editMessage(msg).queue()
    }
    
    /**
     * Quick-reply to a message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    fun replyRaw(msg : String) = Note(host, channel.sendMessage(msg).complete())
    
    /**
     * Send an embeded message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    @JvmOverloads
    fun replyEmbed(title : String?, msg : String?, color : Color? = Bot.color, thumb : String? = null, img : String? = null)
    {//›
        channel.sendMessage(makeEmbed(title, msg, color, thumb, img, author)).queue()
    }
    
    /**
     * Send an embeded message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    @JvmOverloads
    fun replyEmbedRaw(title : String?, msg : String?, color : Color? = Bot.color, thumb : String? = null, img : String? = null)
    {
        channel.sendMessage(makeEmbed(title, msg, color, thumb, img)).queue()
    }
    
    fun replyError(msg : String)
    {
        val eb = EmbedBuilder()
    
        eb.setAuthor("Error", null, "http://gnarbot.xyz/img/Error.png")
        eb.setDescription(msg)
        eb.setColor(Color.RED)
        
        channel.sendMessage(eb.build()).queue()
    }
    
    fun delete() : Boolean
    {
        try
        {
            deleteMessage().complete()
            return true
        }
        catch(e : PermissionException)
        {
            return false
        }
    }
    
    /**
     * @return String representation of the note.
     */
    override fun toString() = "Note(id=$id, author=${author?.name}, content=\"$content\")"
}
