package xyz.gnarbot.gnar.servers.music

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.servers.Servlet
import java.awt.Color
import java.time.Duration

class MusicManager(servlet: Servlet, val playerManager: AudioPlayerManager) {

    /**
     * Audio player for the guild.
     */
    val player: AudioPlayer = playerManager.createPlayer()

    /**
     * Track scheduler for the player.
     */
    val scheduler: TrackScheduler = TrackScheduler(servlet, player)

    /**
     * Wrapper around AudioPlayer to use it as an AudioSendHandler.
     */
    val sendHandler: AudioPlayerSendHandler = AudioPlayerSendHandler(player)

    /**
     * Voting Cooldown
     */
    var lastVoteTime: Long = 0L

    /**
     * Boolean to check whether there is a vote to skip the song or not
     */
    var isVotingToSkip = false

    init {
        player.addListener(scheduler)
    }

    fun loadAndPlay(message: Message, trackUrl: String) {
        playerManager.loadItemOrdered(this, trackUrl, object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {
                if (scheduler.queue.size >= TrackScheduler.QUEUE_LIMIT) {
                    message.respond().error("The queue can not exceed 20 songs.")
                    return
                }

                if (track.duration > Duration.ofHours(1).toMillis()) {
                    message.respond().error("The track can not exceed 1 hour.")
                    return
                }

                scheduler.queue(track)

                message.respond().embed("Music Queue") {
                    color = Color(0, 221, 88)
                    description = "Added __**[${track.info.title}](${track.info.uri})**__ to queue."
                }.rest().queue()
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                val tracks = playlist.tracks

                var added = 0
                for (track in tracks) {

                    if (scheduler.queue.size >= TrackScheduler.QUEUE_LIMIT) {
                        message.respond().info("Ignored ${tracks.size - added} songs as the queue can not exceed 20 songs.")
                        break
                    }

                    scheduler.queue(track)
                    added++
                }

                message.respond().embed("Music Queue") {
                    color = Color(0, 221, 88)
                    description = "Added `$added` tracks to queue from playlist `${playlist.name}`."
                }.rest().queue()
            }

            override fun noMatches() {
                message.respond().error("Nothing found by `$trackUrl`.")
            }

            override fun loadFailed(e: FriendlyException) {
                message.respond().error("**Exception**: `${e.message}`")
            }
        })
    }
}
