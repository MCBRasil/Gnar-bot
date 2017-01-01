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
    fun reply(msg : String) = Note(host, channel.sendMessage("__**${message.author.name}**__ \u279c $msg").complete())
    
    /**
     * Quick-reply to a message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    fun replyRaw(msg : String) = Note(host, channel.sendMessage(msg).complete())
    
    /**
     * Send an embeded message..
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    @Deprecated("Use replyEmbedRaw - embeds don't need stylized pointers")
    @JvmOverloads
    fun replyEmbed(title : String, msg : String, color : Color = Bot.color, thumb : String? = null, img : String? = null)
    {
        val embed = EmbedBuilder().run {
            setDescription("${message.author.name} \u279c" + msg)
            setTitle(title)
            setColor(color)
            
            if (!thumb.isNullOrBlank())
                setThumbnail(thumb)
            
            if (!img.isNullOrBlank())
                setImage(img)
            
            build()
        }
        channel.sendMessage(embed).queue()
    }
    
    @Deprecated("Use replyEmbedRaw - embeds don't need stylized pointers")
    fun replyEmbed(title : String, msg : String, color : Color, image : String, imageNotThumb : Boolean)
    {
        val eb = EmbedBuilder()
        eb.setDescription("__**${message.author.name}**__ \u279c" + msg)
        eb.setTitle(title)
        eb.setColor(color)
        if (imageNotThumb)
        {
            eb.setImage(image)
        }
        else
        {
            eb.setThumbnail(image)
        }
        val embed = eb.build()
        channel.sendMessage(embed).queue()
    }
    
    /**
     * Send an embeded message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    @JvmOverloads
    fun replyEmbedRaw(title : String, msg : String, color : Color = Bot.color, thumb : String? = null, img : String? = null)
    {
        channel.sendMessage(makeEmbed(title, msg, color, thumb, img)).queue()
    }
    
    fun replyEmbedRaw(title : String, msg : String, color : Color, image : String, imageNotThumb : Boolean)
    {
        channel.sendMessage(makeEmbed(title, msg, color, image, imageNotThumb)).queue()
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
