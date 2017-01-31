package xyz.gnarbot.gnar.commands.executors.music

import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.servers.Host
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("nowplaying", "np"))
class NowPlayingCommand : MusicExecutor() {
    override fun execute(note: Note, args: List<String>, host: Host, manager: MusicManager) {
        val track = manager.player.playingTrack

        if (track == null) {
            note.error("The player is not currently playing anything.")
            return
        }

        val title = track.info.title
        val position = getTimestamp(track.position)
        val duration = getTimestamp(track.duration)

        note.replyMusic("**Playing:** $title\n**Time:** `[$position / $duration]`")
    }
}
