package xyz.gnarbot.gnar.utils.reference

import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.Region
import net.dv8tion.jda.core.entities.*
import net.dv8tion.jda.core.managers.AudioManager
import net.dv8tion.jda.core.managers.GuildController
import net.dv8tion.jda.core.managers.GuildManager
import net.dv8tion.jda.core.managers.GuildManagerUpdatable
import net.dv8tion.jda.core.requests.RestAction
import java.time.OffsetDateTime

class GuildReference(guild: Guild) : Guild {
    var delegate: Guild = guild
        get() {
            val _new = field.jda.getGuildById(field.id)
            if (_new != field) {
                field = _new
            }
            return field
        }

    override fun checkVerification(): Boolean = delegate.checkVerification()
    override fun delete(): RestAction<Void> = delegate.delete()
    override fun delete(p0: String): RestAction<Void> = delegate.delete(p0)
    override fun getAfkChannel(): VoiceChannel = delegate.afkChannel
    override fun getAfkTimeout(): Guild.Timeout = delegate.afkTimeout
    override fun getAudioManager(): AudioManager = delegate.audioManager
    override fun getController(): GuildController = delegate.controller
    override fun getCreationTime(): OffsetDateTime = delegate.creationTime
    override fun getDefaultNotificationLevel(): Guild.NotificationLevel = delegate.defaultNotificationLevel
    override fun getEmoteById(p0: String): Emote = delegate.getEmoteById(p0)
    override fun getEmotes(): List<Emote> = delegate.emotes
    override fun getEmotesByName(p0: String, p1: Boolean): List<Emote> = delegate.getEmotesByName(p0, p1)
    override fun getIconId(): String = delegate.iconId
    override fun getIconUrl(): String = delegate.iconUrl
    override fun getId(): String = delegate.id
    override fun getInvites(): RestAction<List<Invite>> = delegate.invites
    override fun getJDA(): JDA = delegate.jda
    override fun getManager(): GuildManager = delegate.manager
    override fun getManagerUpdatable(): GuildManagerUpdatable = delegate.managerUpdatable
    override fun getMember(p0: User): Member = delegate.getMember(p0)
    override fun getMemberById(p0: String): Member = delegate.getMemberById(p0)
    override fun getMembers(): List<Member> = delegate.members
    override fun getMembersByEffectiveName(p0: String, p1: Boolean): List<Member> = delegate.getMembersByEffectiveName(p0, p1)
    override fun getMembersByName(p0: String, p1: Boolean): List<Member> = delegate.getMembersByName(p0, p1)
    override fun getMembersByNickname(p0: String, p1: Boolean): List<Member> = delegate.getMembersByNickname(p0, p1)
    override fun getMembersWithRoles(vararg p0: Role): List<Member> = delegate.getMembersWithRoles(*p0)
    override fun getMembersWithRoles(p0: Collection<Role>): List<Member> = delegate.getMembersWithRoles(p0)
    override fun getName(): String = delegate.name
    override fun getOwner(): Member = delegate.owner
    override fun getPublicChannel(): TextChannel = delegate.publicChannel
    override fun getPublicRole(): Role = delegate.publicRole
    override fun getRegion(): Region = delegate.region
    override fun getRequiredMFALevel(): Guild.MFALevel = delegate.requiredMFALevel
    override fun getRoleById(p0: String): Role = delegate.getRoleById(p0)
    override fun getRoles(): List<Role> = delegate.roles
    override fun getRolesByName(p0: String, p1: Boolean): List<Role> = delegate.getRolesByName(p0, p1)
    override fun getSelfMember(): Member = delegate.selfMember
    override fun getSplashId(): String = delegate.splashId
    override fun getSplashUrl(): String = delegate.splashUrl
    override fun getTextChannelById(p0: String): TextChannel = delegate.getTextChannelById(p0)
    override fun getTextChannels(): List<TextChannel> = delegate.textChannels
    override fun getTextChannelsByName(p0: String, p1: Boolean): List<TextChannel> = delegate.getTextChannelsByName(p0, p1)
    override fun getVerificationLevel(): Guild.VerificationLevel = delegate.verificationLevel
    override fun getVoiceChannelById(p0: String): VoiceChannel = delegate.getVoiceChannelById(p0)
    override fun getVoiceChannels(): List<VoiceChannel> = delegate.voiceChannels
    override fun getVoiceChannelsByName(p0: String, p1: Boolean): List<VoiceChannel> = delegate.getVoiceChannelsByName(p0, p1)
    override fun getVoiceStates(): List<GuildVoiceState> = delegate.voiceStates
    override fun getWebhooks(): RestAction<List<Webhook>> = delegate.webhooks
    override fun isAvailable(): Boolean = delegate.isAvailable
    override fun isMember(p0: User): Boolean = delegate.isMember(p0)
    override fun leave(): RestAction<Void> = delegate.leave()
}