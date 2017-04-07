package xyz.gnarbot.gnar.commands.executors.music

import b
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Category
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.servers.GuildData
import xyz.gnarbot.gnar.servers.music.MusicManager
import java.util.concurrent.TimeUnit

@Command(aliases = arrayOf("voteskip"),
        description = "Vote to skip the current music track.",
        category = Category.MUSIC)
class VoteSkipCommand : MusicExecutor() {

    override fun execute(message: Message, args: List<String>) {
        val manager = guildData.musicManager

        val member = guild.getMember(message.author)

        if (member.voiceState.channel !== null && manager.player.playingTrack !== null) {
            if (member.voiceState.isDeafened) {
                message.respond().error("You actually have to be listening to the song to start a vote... Tsk tsk...").queue { msg ->
                    msg.delete().queueAfter(5, TimeUnit.SECONDS)
                }
                return
            }
            if (manager.isVotingToSkip) {
                message.respond().error("There is already a vote going on!").queue { msg ->
                    msg.delete().queueAfter(5, TimeUnit.SECONDS)
                }
                return
            }
            if ((System.currentTimeMillis() - manager.lastVoteTime) < 30000) {
                message.respond().error("You must wait 30 seconds before starting a new vote!").queue()
                return
            }
            if ((manager.player.playingTrack.duration - manager.player.playingTrack.position) <= 30) {
                message.respond().error("By the time the vote finishes, the song will be over!").queue()
                return
            }

            manager.lastVoteTime = System.currentTimeMillis()
            manager.isVotingToSkip = true

            message.respond().embed("Vote Skip") {
                color = musicColor
                description {
                    append(b(message.author.name))
                    append(" has voted to **skip** the current track!")
                    appendln("React with :thumbsup: or :thumbsdown:")
                    append("Whichever has the most votes in 30 seconds will win!")
                }
            }.rest().queue { msg ->
                msg.addReaction("👍").queue()
                msg.addReaction("👎").queue()

                msg.delete().queueAfter(30, TimeUnit.SECONDS) {
                    checkVictory(msg, guildData, manager)
                }
            }
        } else {
            message.respond().error("You're not in the Music Channel!\n*or there isn't a song playing...*").queue()
        }
    }

    fun checkVictory(msg: Message, guildData: GuildData, manager: MusicManager) {
        msg.channel.getMessageById(msg.id).queue { _msg ->
            if (_msg.reactions[0].count > _msg.reactions[1].count) {
                msg.respond().embed("Vote Skip") {
                    color = musicColor
                    description {
                        append("The vote has passed! ")
                        append(_msg.reactions[0].count - 1).append(" to ").appendln(_msg.reactions[1].count - 1)
                        append("The song has been skipped!")
                    }
                }.rest().queue()

                if (manager.scheduler.queue.isEmpty()) {
                    guildData.resetMusicManager()
                } else {
                    manager.scheduler.nextTrack()
                }
            } else {
                msg.respond().embed("Vote Skip") {
                    color = musicColor
                    description {
                        appendln("The vote has failed! ")
                        append("The song will stay!")
                    }
                }.rest().queue()
            }
            manager.isVotingToSkip = false
        }
    }
}
