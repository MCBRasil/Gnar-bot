package xyz.gnarbot.gnar.commands.executors.music.dj

import com.google.inject.Inject
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.members.Level
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("restart"), level = Level.DJ, description = "Restart the current song.")
class RestartCommand : MusicExecutor() {
    @Inject lateinit var manager: MusicManager

    override fun execute(note: Note, args: List<String>) {
        var track = manager.player.playingTrack

        if (track == null) {
            track = manager.scheduler.lastTrack
        }

        if (track != null) {

            note.respond().embed("Restart Song") {
                color = musicColor
                description = "Restarting track: `${track.info.title}`."
            }.rest().queue()

            manager.player.playTrack(track.makeClone())
        } else {
            note.respond().error("No track has been previously started.").queue()
        }
    }
}
