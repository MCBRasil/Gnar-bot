package xyz.gnarbot.gnar.utils

import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.exceptions.PermissionException
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.members.User
import xyz.gnarbot.gnar.servers.Host
import java.awt.Color
import java.util.concurrent.*

/**
 * The bot's wrapper class for JDA's [Message].
 *
 * @see Message
 * @see Host
 */
class Note(val host : Host, private val message : Message) : Message by message
{
    /**
     * The author of this Message as a [User] instance.
     *
     * @return Message author as User.
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
    @Deprecated("Use one of the embeds, it has style you fucks.")
    fun reply(msg : String) : Note
    {
        return Note(host, channel.sendMessage("__**${message.author.name}__ ›** $msg").complete())
    }
    
    //TODO Add reactions here
    
    /**
     * Quick-reply to a message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    fun replyRaw(msg : String) = channel.sendMessage(msg).submit().toNote()
    
    /**
     * Send an embeded message.
     *
     * @param msg The text to send.
     * @return The Message created by this function.
     */
    @JvmOverloads
    fun replyEmbed(title : String?,
                   msg : String?,
                   color : Color? = Bot.color,
                   thumb : String? = null,
                   img : String? = null) : Future<Note>
    {
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
    fun replyEmbedRaw(title : String?,
                      msg : String?,
                      color : Color? = Bot.color,
                      thumb : String? = null,
                      img : String? = null) : Future<Note>
    {
        return channel.sendMessage(makeEmbed(title, msg, color, thumb, img))
                .submit()
                .toNote()
    }
    
    fun info(msg : String) : Future<Note>
    {
        val eb = EmbedBuilder()
        
        eb.setAuthor("Info", null, "http://gnarbot.xyz/img/Info.png")
        eb.setDescription(msg)
        eb.setColor(Bot.color)
        
        return channel.sendMessage(eb.build()).submit().toNote()
    }
    
    fun error(msg : String) : Future<Note>
    {
        val eb = EmbedBuilder()
        
        eb.setAuthor("Error", null, "http://gnarbot.xyz/img/Error.png")
        eb.setDescription(msg)
        eb.setColor(Color.RED)
        
        return channel.sendMessage(eb.build()).submit().toNote()
    }
    
    fun edit(msg : String) = editMessage(msg).submit().toNote()
    
    fun delete() : Boolean
    {
        try
        {
            deleteMessage().queue()
            return true
        }
        catch(e : PermissionException)
        {
            return false
        }
    }
    
    fun delete(seconds : Long)
    {
        Bot.scheduler.schedule({ delete() }, seconds, TimeUnit.SECONDS)
    }
    
    /**
     * @return String representation of the note.
     */
    override fun toString() = "Note(id=$id, author=${author.name}, content=\"$content\")"
    
    private fun Future<Message>.toNote() : Future<Note> = object : CompletableFuture<Note>()
    {
        override fun get(timeout : Long, unit : TimeUnit) = Note(host, this@toNote.get(timeout, unit))
        override fun get() : Note = Note(host, this@toNote.get())
    }
}

