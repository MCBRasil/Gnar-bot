package xyz.gnarbot.gnar.commands.executors.music

import com.google.inject.Inject
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("nowplaying", "np"),
        description = "Shows what's currently playing.",
        symbol = "♬")
class NowPlayingCommand : MusicExecutor() {

    @Inject lateinit private var manager: MusicManager

    override fun execute(note: Note, args: List<String>) {
        val track = manager.player.playingTrack

        if (track == null) {
            note.respond().error("The player is not currently playing anything.")
            return
        }

        note.respond().embed("Now Playing") {
            field("Now Playing", false, "__[${track.info.title}](${track.info.uri})__")

            val position = getTimestamp(track.position)
            val duration = getTimestamp(track.duration)

            field("Time", true, "**[$position / $duration]**")
        }.rest().queue()
    }
}
