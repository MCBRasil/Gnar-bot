package xyz.gnarbot.gnar.commands.executors.music.dj

import net.dv8tion.jda.core.Permission
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("restart"),
        description = "Restart the current song.",
        symbol = "♬",
        voicePermissions = arrayOf(Permission.MANAGE_CHANNEL))
class RestartCommand : MusicExecutor() {
    override fun execute(note: Note, args: List<String>) {
        val manager = servlet.musicManager

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
