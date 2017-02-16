package xyz.gnarbot.gnar.commands.executors.music.dj

import com.google.inject.Inject
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.servers.Host
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note
import java.util.concurrent.TimeUnit

@Command(aliases = arrayOf("voteskip"), description = "Vote to skip the current music track.")
class VoteSkipCommand : MusicExecutor() {
    @Inject lateinit var host : Host
    @Inject lateinit var manager : MusicManager

    lateinit var hostCopy : Host
    lateinit var managerCopy : MusicManager

    override fun execute(note: Note, args: List<String>) {
        if (note.author.voiceChannel !== null && manager.player.playingTrack !== null) {
            if (note.author.voiceState.isDeafened){
                note.error("You actually have to be listening to the song to start a vote... Tsk tsk...").get().optDelete(10)
                return
            }
            if (manager.isVotingToSkip){
                note.error("There is already a vote going on!").get().optDelete(10)
                return
            }
            if ((System.currentTimeMillis() - manager.getLastVoteTime()) < 30000){
                note.error("You must wait 30 seconds before starting a new vote!")
                return
            }
            if ((manager.player.playingTrack.duration - manager.player.playingTrack.position) <= 30){
                note.error("By the time the vote finishes, the song will be over!").get().optDelete(10)
                return
            }
            manager.setLastVoteTime(System.currentTimeMillis())
            hostCopy = host
            managerCopy = manager
            manager.votingToSkip = true
            var msg = note.replyMusic("[" + note.author.name +
                    "]() has voted to **skip** the current track! " +
                    "React with :thumbsup: or :thumbsdown:!\n" +
                    "Whichever has the most votes in 30 seconds will win!").get()
            msg.optDelete(35)
            msg.addReaction("👍").queue()
            msg.addReaction("👎").queue()
            Bot.scheduler.schedule({
                checkVictory(note, msg)
            }, 30, TimeUnit.SECONDS)
        }else{
            note.error("You're not in the Music Channel!\n*or there isn't a song playing...*").get().optDelete(5)
        }
    }

    fun checkVictory(note: Note, msg: Note){
        var msg2 = note.channel.getMessageById(msg.id).complete()
        if (msg2.reactions.get(0).count > msg2.reactions.get(1).count) {
            msg.replyMusic("The vote has passed! " + (msg2.reactions.get(0).count - 1)+ " to " + (msg2.reactions.get(1).count - 1)+"!\nThe song has been skipped!").get().optDelete(15)
            if (managerCopy.scheduler.queue.isEmpty()) {
                hostCopy.resetMusicManager()
            } else {
                managerCopy.scheduler.nextTrack()
            }
        } else {
            msg.replyMusic("The vote has failed!\nThe song will stay!").get().optDelete(15)
        }
        managerCopy.votingToSkip = false
    }
}
