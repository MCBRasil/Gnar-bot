package xyz.gnarbot.gnar.commands.executors.music

import com.google.inject.Inject
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("nowplaying", "np"), description = "Shows what's currently playing.")
class NowPlayingCommand : MusicExecutor() {

    @Inject lateinit private var manager: MusicManager

    override fun execute(note: Note, args: List<String>) {
        val track = manager.player.playingTrack

        if (track == null) {
            note.error("The player is not currently playing anything.")
            return
        }

        note.embed("Now Playing") {
            field("Now Playing", false, if (track.sourceManager.sourceName.contains("youtube")) {
                "__[${track.info.title}](https://youtube.com/watch?v=${track.info.identifier})__"
            } else {
                "__[${track.info.title}]()__"
            })

            val position = getTimestamp(track.position)
            val duration = getTimestamp(track.duration)

            field("Time", true, "**[$position / $duration]**")
        }.rest().queue()
    }
}
