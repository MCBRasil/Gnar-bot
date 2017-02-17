package xyz.gnarbot.gnar.utils.reference

import net.dv8tion.jda.client.entities.Group
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.*
import net.dv8tion.jda.core.requests.RestAction
import java.time.OffsetDateTime

class MessageReference(message : Message) : Message {
    var delegate: Message = message
        get() {
            val _new = field.channel.getMessageById(field.id).complete()
            if (_new != field) {
                field = _new
            }
            return field
        }
    
    override fun addReaction(p0: String): RestAction<Void> = delegate.addReaction(p0)
    override fun addReaction(p0: Emote): RestAction<Void> = delegate.addReaction(p0)
    override fun clearReactions(): RestAction<Void> = delegate.clearReactions()
    override fun delete(): RestAction<Void> = delegate.delete()
    @Deprecated(message = "Deprecated in Java", replaceWith = ReplaceWith("delete"))
    override fun deleteMessage(): RestAction<Void> = delegate.deleteMessage()
    override fun editMessage(p0: String): RestAction<Message> = delegate.editMessage(p0)
    override fun editMessage(p0: Message): RestAction<Message> = delegate.editMessage(p0)
    override fun getAttachments(): List<Message.Attachment> = delegate.attachments
    override fun getAuthor(): User = delegate.author
    override fun getChannel(): MessageChannel = delegate.channel
    override fun getChannelType(): ChannelType = delegate.channelType
    override fun getContent(): String = delegate.content
    override fun getCreationTime(): OffsetDateTime = delegate.creationTime
    override fun getEditedTime(): OffsetDateTime = delegate.editedTime
    override fun getEmbeds(): List<MessageEmbed> = delegate.embeds
    override fun getEmotes(): List<Emote> = delegate.emotes
    override fun getGroup(): Group = delegate.group
    override fun getGuild(): Guild = delegate.guild
    override fun getId(): String = delegate.id
    override fun getJDA(): JDA = delegate.jda
    override fun getMentionedChannels(): List<TextChannel> = delegate.mentionedChannels
    override fun getMentionedRoles(): List<Role> = delegate.mentionedRoles
    override fun getMentionedUsers(): List<User> = delegate.mentionedUsers
    override fun getPrivateChannel(): PrivateChannel = delegate.privateChannel
    override fun getRawContent(): String = delegate.rawContent
    override fun getReactions(): List<MessageReaction> = delegate.reactions
    override fun getStrippedContent(): String = delegate.strippedContent
    override fun getTextChannel(): TextChannel = delegate.textChannel
    override fun getType(): MessageType = delegate.type
    override fun isEdited(): Boolean = delegate.isEdited
    override fun isFromType(p0: ChannelType): Boolean = delegate.isFromType(p0)
    override fun isMentioned(p0: User): Boolean = delegate.isMentioned(p0)
    override fun isPinned(): Boolean = delegate.isPinned
    override fun isTTS(): Boolean = delegate.isTTS
    override fun isWebhookMessage(): Boolean = delegate.isWebhookMessage
    override fun mentionsEveryone(): Boolean = delegate.mentionsEveryone()
    override fun pin(): RestAction<Void> = delegate.pin()
    override fun unpin(): RestAction<Void> = delegate.unpin()
}
