package xyz.gnarbot.gnar.commands.executors.music.parent

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note
import java.awt.Color
import java.time.Duration
import java.util.concurrent.Future

abstract class MusicExecutor : CommandExecutor() {

    protected val color = Color(0, 221, 88)

    init {
        symbol = "**♬**"
    }

    //abstract fun execute(note: Note, args: List<String>, host: Host, manager: MusicManager)

    protected fun Note.replyMusic(msg: String) : Future<Note> {
        return this.replyEmbedRaw("Music", msg, color)
    }

    protected fun getTimestamp(milliseconds: Long): String {
        val seconds = (milliseconds / 1000).toInt() % 60
        val minutes = (milliseconds / (1000 * 60) % 60).toInt()
        val hours = (milliseconds / (1000 * 60 * 60) % 24).toInt()

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            return String.format("%02d:%02d", minutes, seconds)
        }
    }

    protected fun loadAndPlay(note: Note, mng: MusicManager, trackUrl: String) {
        mng.playerManager.loadItemOrdered(mng, trackUrl, object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {

                if (mng.scheduler.queue.size >= 20) {
                    note.error("The queue can not exceed 20 songs.")
                    return
                }

                if (track.duration > Duration.ofHours(1).toMillis()) {
                    note.error("The track can not exceed 1 hour.")
                    return
                }

                mng.scheduler.queue(track)

                var msg = "Added `${track.info.title}` to queue."

                if (mng.player.playingTrack == null && note.host.guild.audioManager.isConnected) {
                    msg += "\nThe player has started playing."
                }

                note.replyMusic(msg)
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                val tracks = playlist.tracks

                var added = 0
                for (track in tracks) {

                    if (mng.scheduler.queue.size >= 20) {
                        note.info("Ignored ${tracks.size - added} songs as the queue can not exceed 20 songs.")
                        break
                    }

                    mng.scheduler.queue(track)
                    added++
                }

                note.replyMusic("Added `$added` tracks to queue from playlist `${playlist.name}`.")
            }

            override fun noMatches() {
                note.error("Nothing found by `$trackUrl`.")
            }

            override fun loadFailed(e: FriendlyException) {
                note.error("**Exception**: `${e.message}`")
            }
        })
    }
}
