package xyz.gnarbot.gnar.commands.executors.music.dj

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
                note.error("You actually have to be listening to the song to start a vote... Tsk tsk...").get().optDelete(10)
                return
            }
            if (manager.isVotingToSkip) {
                note.error("There is already a vote going on!").get().optDelete(10)
                return
            }
            if ((System.currentTimeMillis() - manager.getLastVoteTime()) < 30000) {
                note.error("You must wait 30 seconds before starting a new vote!")
                return
            }
            if ((manager.player.playingTrack.duration - manager.player.playingTrack.position) <= 30) {
                note.error("By the time the vote finishes, the song will be over!")
                return
            }
            manager.setLastVoteTime(System.currentTimeMillis())
            manager.isVotingToSkip = true
            val msg = note.replyMusic("[" + note.author.name +
                    "]() has voted to **skip** the current track! " +
                    "React with :thumbsup: or :thumbsdown:!\n" +
                    "Whichever has the most votes in 30 seconds will win!").get()
            msg.optDelete(35)
            msg.addReaction("👍").queue()
            msg.addReaction("👎").queue()

            val hostCopy = servlet
            val managerCopy = manager
            Bot.scheduler.schedule({
                checkVictory(note, msg, hostCopy, managerCopy)
            }, 30, TimeUnit.SECONDS)
        } else {
            note.error("You're not in the Music Channel!\n*or there isn't a song playing...*")
        }
    }

    fun checkVictory(note: Note, msg: Note, servlet: Servlet, manager: MusicManager) {
        val _msg = note.channel.getMessageById(msg.id).complete()

        if (_msg.reactions[0].count > _msg.reactions[1].count) {
            msg.replyMusic("The vote has passed! " + (_msg.reactions[0].count - 1) + " to " + (_msg.reactions[1].count - 1) + "!\nThe song has been skipped!")
            if (manager.scheduler.queue.isEmpty()) {
                servlet.resetMusicManager()
            } else {
                manager.scheduler.nextTrack()
            }
        } else {
            msg.replyMusic("The vote has failed!\nThe song will stay!")
        }
        manager.isVotingToSkip = false
    }
}
