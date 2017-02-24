package xyz.gnarbot.gnar.utils.reference

import net.dv8tion.jda.client.entities.Group
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.*
import net.dv8tion.jda.core.requests.RestAction
import java.time.OffsetDateTime

class MessageReference(message: Message) : Message {

    var reference: Message = message

    fun update() {
        reference = reference.channel.getMessageById(reference.id).complete()
    }

    override fun addReaction(p0: String): RestAction<Void> = reference.addReaction(p0)
    override fun addReaction(p0: Emote): RestAction<Void> = reference.addReaction(p0)
    override fun clearReactions(): RestAction<Void> = reference.clearReactions()
    override fun delete(): RestAction<Void> = reference.delete()
    @Suppress("DEPRECATION")
    @Deprecated(message = "Deprecated in Java", replaceWith = ReplaceWith("delete"))
    override fun deleteMessage(): RestAction<Void> = reference.deleteMessage()

    override fun editMessage(p0: String): RestAction<Message> = reference.editMessage(p0)
    override fun editMessage(p0: Message): RestAction<Message> = reference.editMessage(p0)
    override fun editMessage(p0: MessageEmbed?): RestAction<Message> = reference.editMessage(p0)
    override fun getAttachments(): List<Message.Attachment> = reference.attachments
    override fun getAuthor(): User = reference.author
    override fun getChannel(): MessageChannel = reference.channel
    override fun getChannelType(): ChannelType = reference.channelType
    override fun getContent(): String = reference.content
    override fun getCreationTime(): OffsetDateTime = reference.creationTime
    override fun getEditedTime(): OffsetDateTime = reference.editedTime
    override fun getEmbeds(): List<MessageEmbed> = reference.embeds
    override fun getEmotes(): List<Emote> = reference.emotes
    override fun getGroup(): Group = reference.group
    override fun getGuild(): Guild = reference.guild
    override fun getId(): String = reference.id
    override fun getJDA(): JDA = reference.jda
    override fun getMentionedChannels(): List<TextChannel> = reference.mentionedChannels
    override fun getMentionedRoles(): List<Role> = reference.mentionedRoles
    override fun getMentionedUsers(): List<User> = reference.mentionedUsers
    override fun getPrivateChannel(): PrivateChannel = reference.privateChannel
    override fun getRawContent(): String = reference.rawContent
    override fun getReactions(): List<MessageReaction> = reference.reactions
    override fun getStrippedContent(): String = reference.strippedContent
    override fun getTextChannel(): TextChannel = reference.textChannel
    override fun getType(): MessageType = reference.type
    override fun isEdited(): Boolean = reference.isEdited
    override fun isFromType(p0: ChannelType): Boolean = reference.isFromType(p0)
    override fun isMentioned(p0: User): Boolean = reference.isMentioned(p0)
    override fun isPinned(): Boolean = reference.isPinned
    override fun isTTS(): Boolean = reference.isTTS
    override fun isWebhookMessage(): Boolean = reference.isWebhookMessage
    override fun mentionsEveryone(): Boolean = reference.mentionsEveryone()
    override fun pin(): RestAction<Void> = reference.pin()
    override fun unpin(): RestAction<Void> = reference.unpin()
}
