package xyz.gnarbot.gnar.commands.executors.music.parent

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.servers.Host
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note
import java.awt.Color
import java.time.Duration

abstract class MusicExecutor : CommandExecutor() {

    protected val color = Color(0, 221, 88)

    init {
        symbol = "**♬**"
    }

    override fun execute(note: Note, args: Array<String>) {
        execute(note, args, note.host, note.host.getMusicManager())
    }

    abstract fun execute(note: Note, args: Array<String>, host: Host, manager: MusicManager)

    protected fun Note.replyMusic(msg: String) {
        this.replyEmbedRaw("Music", msg, color)
    }

    protected fun getTimestamp(milliseconds: Long): String {
        val seconds = (milliseconds / 1000).toInt() % 60
        val minutes = (milliseconds / (1000 * 60) % 60).toInt()
        val hours = (milliseconds / (1000 * 60 * 60) % 24).toInt()

        if (hours > 0)
            return String.format("%02d:%02d:%02d", hours, minutes, seconds)
        else
            return String.format("%02d:%02d", minutes, seconds)
    }

    protected fun loadAndPlay(note: Note, mng: MusicManager, trackUrl: String, addPlaylist: Boolean) {
        mng.manager.loadItemOrdered(mng, trackUrl, object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {

                if (mng.scheduler.queue.size >= 20) {
                    note.error("The queue can not exceed 20 songs.")
                    return
                }

                if (track.duration > Duration.ofHours(1).toMillis()) {
                    note.error("The track can not exceed 1 hour.")
                    return
                }

                var msg = "Adding `${track.info.title}` to queue."

                if (mng.player.playingTrack == null && note.host.audioManager.isConnected) {
                    msg += "\nThe player has started playing."
                }

                mng.scheduler.queue(track)
                note.replyMusic(msg)
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
//                var firstTrack: AudioTrack? = playlist.selectedTrack
//                val tracks = playlist.tracks
//
//
//                if (firstTrack == null) {
//                    firstTrack = playlist.tracks[0]
//                }
//
//                if (addPlaylist) {
//
//                    note.replyEmbedRaw(
//                            msg = "Adding `${playlist.tracks.size}` tracks to queue from playlist: ${playlist.name}",
//                            color = color)
//                    tracks.forEach {
//                        mng.scheduler.queue(it)
//                    }
//                } else {
//                    note.replyEmbedRaw(
//                            msg = "Adding to queue `${firstTrack!!.info.title}` (1st track of playlist ${playlist.name})",
//                            color = color)
//                    mng.scheduler.queue(firstTrack)
//                }
                note.error("I don't know how you got here, but adding playlists are disabled for now.")
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
