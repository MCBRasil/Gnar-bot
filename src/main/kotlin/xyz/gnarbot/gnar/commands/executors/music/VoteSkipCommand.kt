package xyz.gnarbot.gnar.commands.executors.music

import com.google.inject.Inject
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.servers.Servlet
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note
import java.util.concurrent.TimeUnit

@Command(aliases = arrayOf("voteskip"), description = "Vote to skip the current music track.")
class VoteSkipCommand : MusicExecutor() {
    @Inject lateinit var servlet: Servlet
    @Inject lateinit var manager: MusicManager

    override fun execute(note: Note, args: List<String>) {
        if (note.author.voiceChannel !== null && manager.player.playingTrack !== null) {
            if (note.author.voiceState.isDeafened) {
                val msg = note.respond().error("You actually have to be listening to the song to start a vote... Tsk tsk...").complete()
                Bot.scheduler.schedule({ msg.delete().queue() }, 5, TimeUnit.SECONDS)
                return
            }
            if (manager.isVotingToSkip) {
                val msg = note.respond().error("There is already a vote going on!").complete()
                Bot.scheduler.schedule({ msg.delete().queue() }, 5, TimeUnit.SECONDS)
                return
            }
            if ((System.currentTimeMillis() - manager.lastVoteTime) < 30000) {
                note.respond().error("You must wait 30 seconds before starting a new vote!").queue()
                return
            }
            if ((manager.player.playingTrack.duration - manager.player.playingTrack.position) <= 30) {
                note.respond().error("By the time the vote finishes, the song will be over!").queue()
                return
            }

            manager.lastVoteTime = System.currentTimeMillis()
            manager.isVotingToSkip = true

            val msg = note.respond().embed("Vote Skip") {
                color = musicColor
                description {
                    append(b(note.author.name))
                    append(" has voted to **skip** the current track!")
                    appendln("React with :thumbsup: or :thumbsdown:")
                    append("Whichever has the most votes in 30 seconds will win!")
                }
            }.rest().complete()
            msg.addReaction("👍").queue()
            msg.addReaction("👎").queue()

            val hostCopy = servlet
            val managerCopy = manager
            Bot.scheduler.schedule({
                msg.delete()
                checkVictory(msg, hostCopy, managerCopy)
            }, 30, TimeUnit.SECONDS)
        } else {
            note.respond().error("You're not in the Music Channel!\n*or there isn't a song playing...*").queue()
        }
    }

    fun checkVictory(msg: Message, servlet: Servlet, manager: MusicManager) {
        val _msg = msg.channel.getMessageById(msg.id).complete()

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
                servlet.resetMusicManager()
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
