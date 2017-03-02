package xyz.gnarbot.gnar.commands.executors.music

import com.google.inject.Inject
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
                note.error("You actually have to be listening to the song to start a vote... Tsk tsk...").complete().optDelete(10)
                return
            }
            if (manager.isVotingToSkip) {
                note.error("There is already a vote going on!").complete().optDelete(10)
                return
            }
            if ((System.currentTimeMillis() - manager.lastVoteTime) < 30000) {
                note.error("You must wait 30 seconds before starting a new vote!").queue()
                return
            }
            if ((manager.player.playingTrack.duration - manager.player.playingTrack.position) <= 30) {
                note.error("By the time the vote finishes, the song will be over!").queue()
                return
            }

            manager.lastVoteTime = System.currentTimeMillis()
            manager.isVotingToSkip = true

            val msg = note.embed("Vote Skip") {
                description {
                    color(musicColor)
                    append(b(note.author.name))
                    append(" has voted to **skip** the current track!")
                    appendln("React with :thumbsup: or :thumbsdown:")
                    append("Whichever has the most votes in 30 seconds will win!")
                }
            }.rest().complete()

            msg.optDelete(35)
            msg.addReaction("👍").queue()
            msg.addReaction("👎").queue()

            val hostCopy = servlet
            val managerCopy = manager
            Bot.scheduler.schedule({
                checkVictory(note, msg, hostCopy, managerCopy)
            }, 30, TimeUnit.SECONDS)
        } else {
            note.error("You're not in the Music Channel!\n*or there isn't a song playing...*").queue()
        }
    }

    fun checkVictory(note: Note, msg: Note, servlet: Servlet, manager: MusicManager) {
        val _msg = note.channel.getMessageById(msg.id).complete()

        if (_msg.reactions[0].count > _msg.reactions[1].count) {
            note.embed("Vote Skip") {
                color(musicColor)
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
            note.embed("Vote Skip") {
                color(musicColor)
                description {
                    appendln("The vote has failed! ")
                    append("The song will stay!")
                }
            }.rest().queue()
        }
        manager.isVotingToSkip = false
    }
}
