package xyz.gnarbot.gnar.commands.executors.music

import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("nowplaying", "np"),
        description = "Shows what's currently playing.",
        symbol = "♬")
class NowPlayingCommand : MusicExecutor() {

    override fun execute(note: Note, args: List<String>) {
        val track = servlet.musicManager.player.playingTrack

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
