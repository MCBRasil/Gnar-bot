package xyz.gnarbot.gnar.servers

import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.Region
import net.dv8tion.jda.core.entities.*
import net.dv8tion.jda.core.exceptions.PermissionException
import net.dv8tion.jda.core.managers.AudioManager
import net.dv8tion.jda.core.managers.GuildController
import net.dv8tion.jda.core.managers.GuildManager
import net.dv8tion.jda.core.managers.GuildManagerUpdatable
import net.dv8tion.jda.core.requests.RestAction
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.handlers.CommandHandler
import xyz.gnarbot.gnar.members.Person
import xyz.gnarbot.gnar.members.PersonHandler
import xyz.gnarbot.gnar.servers.music.MusicManager
import java.time.OffsetDateTime

/**
 * Represents a bot on each [Guild].
 * @see Guild
 */
class Host(val shard: Shard, private var guild : Guild) : Guild by guild {
    val personHandler: PersonHandler
    val commandHandler: CommandHandler

    var musicManager: MusicManager? = null
        get() {
            if (field == null) {
                this.musicManager = MusicManager(this, Bot.playerManager)
                field!!.player.volume = 35
            }
            return field
        }

    fun resetMusicManager() {
        musicManager!!.scheduler.queue.clear()
        musicManager!!.player.destroy()
        audioManager.closeAudioConnection()
        audioManager.sendingHandler = null
        musicManager = null
    }

    init {
        this.personHandler = PersonHandler(this)
        this.commandHandler = CommandHandler(this)

        audioManager.closeAudioConnection()
    }

    fun ensure() : Host {
        val _guild = shard.getGuildById(id)
        if (_guild != guild) {
            guild = _guild
        }
        return this
    }

    //    @Deprecated("Useless")
    //    /** Load JSON instance from the Host's storage. */
    //    public boolean loadJSON()
    //    {
    //        file = Bot.files.hosts.child("$id.json")
    //        file.createNewFile()
    //
    //        val content = file.readText()
    ////        if (content.isEmpty()) jsonObject = NullableJSON()
    ////        else jsonObject = NullableJSON(content)
    //    }
    //
    //    @Deprecated("Useless")
    //    /** Save the JSON instance of the Host. */
    //    public boolean saveJSON() = file.writeText(jsonObject.toString(4))

    /**
     * Attempt to ban the member from the guild.
     * @return If the bot had permission.
     */
    fun ban(person: Person): Boolean {
        try {
            controller.ban(person as User, 2).queue()
            return true
        } catch (e: PermissionException) {
            return false
        }

    }

    /**
     * Attempt to un-ban the member from the guild.
     * @return If the bot had permission.
     */
    fun unban(person: Person): Boolean {
        try {
            controller.unban(person).queue()
            return true
        } catch (e: PermissionException) {
            return false
        }

    }

    /**
     * Attempt to kick the member from the guild.
     * @return If the bot had permission.
     */
    fun kick(person: Person): Boolean {
        try {
            controller.kick(person).queue()
            return true
        } catch (e: PermissionException) {
            return false
        }

    }

    /**
     * Attempt to mute the member in the guild.
     * @return If the bot had permission.
     */
    fun mute(person: Person): Boolean {
        try {
            controller.setMute(person, true).queue()
            return true
        } catch (e: PermissionException) {
            return false
        }

    }

    /**
     * Attempt to unmute the member in the guild.
     * @return If the bot had permission.
     */
    fun unmute(person: Person): Boolean {
        try {
            controller.setMute(person, false).queue()
            return true
        } catch (e: PermissionException) {
            return false
        }
    }

    /**
     * @return String representation of the Host.
     */
    override fun toString(): String = "Host(id=${guild.id}, shard=${shard.id}, guild=${guild.name})"

    fun handleMessage(message: Message) {
        val person = personHandler.asPerson(message.author)
        commandHandler.callCommand(message, message.content, person)
    }



    override fun checkVerification(): Boolean = this.guild.checkVerification()
    override fun delete(): RestAction<Void> = this.guild.delete()
    override fun delete(p0: String): RestAction<Void> = this.guild.delete(p0)
    override fun getAfkChannel(): VoiceChannel = this.guild.afkChannel
    override fun getAfkTimeout(): Guild.Timeout = this.guild.afkTimeout
    override fun getAudioManager(): AudioManager = this.guild.audioManager
    override fun getController(): GuildController = this.guild.controller
    override fun getCreationTime(): OffsetDateTime = this.guild.creationTime
    override fun getDefaultNotificationLevel(): Guild.NotificationLevel = this.guild.defaultNotificationLevel
    override fun getEmoteById(p0: String): Emote = this.guild.getEmoteById(p0)
    override fun getEmotes(): List<Emote> = this.guild.emotes
    override fun getEmotesByName(p0: String, p1: Boolean): List<Emote> = this.guild.getEmotesByName(p0, p1)
    override fun getIconId(): String = this.guild.iconId
    override fun getIconUrl(): String = this.guild.iconUrl
    override fun getId(): String = this.guild.id
    override fun getInvites(): RestAction<List<Invite>> = this.guild.invites
    override fun getJDA(): JDA = this.guild.jda
    override fun getManager(): GuildManager = this.guild.manager
    override fun getManagerUpdatable(): GuildManagerUpdatable = this.guild.managerUpdatable
    override fun getMember(p0: User): Member = this.guild.getMember(p0)
    override fun getMemberById(p0: String): Member = this.guild.getMemberById(p0)
    override fun getMembers(): List<Member> = this.guild.members
    override fun getMembersByEffectiveName(p0: String, p1: Boolean): List<Member> = this.guild.getMembersByEffectiveName(p0, p1)
    override fun getMembersByName(p0: String, p1: Boolean): List<Member> = this.guild.getMembersByName(p0, p1)
    override fun getMembersByNickname(p0: String, p1: Boolean): List<Member> = this.guild.getMembersByNickname(p0, p1)
    override fun getMembersWithRoles(vararg p0: Role): List<Member> = this.guild.getMembersWithRoles(*p0)
    override fun getMembersWithRoles(p0: MutableCollection<Role>?): MutableList<Member> = this.guild.getMembersWithRoles(p0)
    override fun getName(): String = this.guild.name
    override fun getOwner(): Member = this.guild.owner
    override fun getPublicChannel(): TextChannel = this.guild.publicChannel
    override fun getPublicRole(): Role = this.guild.publicRole
    override fun getRegion(): Region = this.guild.region
    override fun getRequiredMFALevel(): Guild.MFALevel = this.guild.requiredMFALevel
    override fun getRoleById(p0: String): Role = this.guild.getRoleById(p0)
    override fun getRoles(): List<Role> = this.guild.roles
    override fun getRolesByName(p0: String, p1: Boolean): List<Role> = this.guild.getRolesByName(p0, p1)
    override fun getSelfMember(): Member = this.guild.selfMember
    override fun getSplashId(): String = this.guild.splashId
    override fun getSplashUrl(): String = this.guild.splashUrl
    override fun getTextChannelById(p0: String): TextChannel = this.guild.getTextChannelById(p0)
    override fun getTextChannels(): List<TextChannel> = this.guild.textChannels
    override fun getTextChannelsByName(p0: String, p1: Boolean): List<TextChannel> = this.guild.getTextChannelsByName(p0, p1)
    override fun getVerificationLevel(): Guild.VerificationLevel = this.guild.verificationLevel
    override fun getVoiceChannelById(p0: String): VoiceChannel = this.guild.getVoiceChannelById(p0)
    override fun getVoiceChannels(): List<VoiceChannel> = this.guild.voiceChannels
    override fun getVoiceChannelsByName(p0: String, p1: Boolean): List<VoiceChannel> = this.guild.getVoiceChannelsByName(p0, p1)
    override fun getVoiceStates(): List<GuildVoiceState> = this.guild.voiceStates
    override fun getWebhooks(): RestAction<List<Webhook>> = this.guild.webhooks
    override fun isAvailable(): Boolean = this.guild.isAvailable
    override fun isMember(p0: User): Boolean = this.guild.isMember(p0)
    override fun leave(): RestAction<Void> = this.guild.leave()
}
